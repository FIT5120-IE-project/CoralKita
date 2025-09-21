package com.sky.service;

import com.sky.dto.RagQueryDTO;
import com.sky.vo.RagAnswerVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Python脚本调用服务
 */
@Service
@Slf4j
public class PythonScriptService {

    @Value("${python.executable:rag/.venv/Scripts/python.exe}")
    private String pythonExecutable;

    @Value("${rag.script.path:rag_api.py}")
    private String ragScriptPath;

    /**
     * 调用Python RAG脚本进行问答
     * @param queryDTO 查询请求
     * @return RAG回答结果
     */
    public RagAnswerVO queryRag(RagQueryDTO queryDTO) {
        long startTime = System.currentTimeMillis();
        
        try {
            log.info("开始调用Python RAG脚本，问题: {}", queryDTO.getQuestion());
            
            // 构建Python命令
            ProcessBuilder processBuilder;
            if (pythonExecutable.contains("uv run")) {
                // 处理uv run命令
                processBuilder = new ProcessBuilder(
                    "uv", "run", "python",
                    ragScriptPath,
                    queryDTO.getQuestion(),
                    String.valueOf(queryDTO.getTopK()),
                    String.valueOf(queryDTO.getIncludeSources())
                );
            } else {
                // 处理普通python命令
                processBuilder = new ProcessBuilder(
                    pythonExecutable, 
                    ragScriptPath,
                    queryDTO.getQuestion(),
                    String.valueOf(queryDTO.getTopK()),
                    String.valueOf(queryDTO.getIncludeSources())
                );
            }
            
            // 设置工作目录为当前项目的RAG目录
            File ragDir = new File("rag").getAbsoluteFile();
            processBuilder.directory(ragDir);
            processBuilder.redirectErrorStream(true);
            
            // 设置环境变量确保UTF-8编码
            processBuilder.environment().put("PYTHONIOENCODING", "utf-8");
            processBuilder.environment().put("PYTHONUTF8", "1");
            
            // 显式传递GEMINI_API_KEY环境变量
            String geminiApiKey = System.getenv("GEMINI_API_KEY");
            if (geminiApiKey != null && !geminiApiKey.trim().isEmpty()) {
                processBuilder.environment().put("GEMINI_API_KEY", geminiApiKey);
                log.info("已设置GEMINI_API_KEY环境变量到Python进程");
            } else {
                log.warn("系统环境变量GEMINI_API_KEY未设置");
            }
            
            log.info("执行命令: {}", String.join(" ", processBuilder.command()));
            log.info("工作目录: {}", ragDir.getAbsolutePath());
            
            // 启动进程
            Process process = processBuilder.start();
            
            // 读取输出
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            
            // 等待进程完成，最多等待30秒
            boolean finished = process.waitFor(30, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                throw new RuntimeException("Python脚本执行超时");
            }
            
            int exitCode = process.exitValue();
            if (exitCode != 0) {
                String errorMsg = output.toString();
                if (errorMsg.contains("Python was not found")) {
                    throw new RuntimeException("Python环境未配置，请安装Python并添加到系统PATH环境变量中");
                } else {
                    throw new RuntimeException("Python脚本执行失败，退出码: " + exitCode + ", 输出: " + errorMsg);
                }
            }
            
            // 解析输出结果
            RagAnswerVO result = parsePythonOutput(output.toString(), queryDTO.getQuestion());
            result.setProcessingTime(System.currentTimeMillis() - startTime);
            
            log.info("Python RAG脚本执行成功，耗时: {}ms", result.getProcessingTime());
            return result;
            
        } catch (Exception e) {
            log.error("调用Python RAG脚本失败", e);
            throw new RuntimeException("RAG服务调用失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 解析Python脚本输出
     */
    private RagAnswerVO parsePythonOutput(String output, String question) {
        try {
            // 简单的JSON解析（实际项目中建议使用Jackson等JSON库）
            String[] lines = output.split("\n");
            StringBuilder answer = new StringBuilder();
            List<RagAnswerVO.SourceInfo> sources = new ArrayList<>();
            
            boolean inAnswer = false;
            boolean inSources = false;
            
            for (String line : lines) {
                line = line.trim();
                
                if (line.startsWith("ANSWER_START:")) {
                    inAnswer = true;
                    continue;
                } else if (line.startsWith("ANSWER_END")) {
                    inAnswer = false;
                    continue;
                } else if (line.startsWith("SOURCES_START:")) {
                    inSources = true;
                    continue;
                } else if (line.startsWith("SOURCES_END")) {
                    inSources = false;
                    continue;
                }
                
                if (inAnswer && !line.isEmpty()) {
                    answer.append(line).append("\n");
                } else if (inSources && line.contains("|")) {
                    // 解析来源信息: sourceFile|year|score|content
                    String[] parts = line.split("\\|", 4);
                    if (parts.length >= 4) {
                        RagAnswerVO.SourceInfo source = RagAnswerVO.SourceInfo.builder()
                            .sourceFile(parts[0].trim())
                            .year(parts[1].trim())
                            .score(Double.parseDouble(parts[2].trim()))
                            .content(parts[3].trim())
                            .build();
                        sources.add(source);
                    }
                }
            }
            
            return RagAnswerVO.builder()
                .question(question)
                .answer(answer.toString().trim())
                .sources(sources)
                .build();
                
        } catch (Exception e) {
            log.error("解析Python输出失败", e);
            // 返回简单的错误响应
            return RagAnswerVO.builder()
                .question(question)
                .answer("抱歉，解析回答时出现错误。原始输出: " + output)
                .sources(new ArrayList<>())
                .build();
        }
    }
}
