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
 * Python Script Calling Service
 */
@Service
@Slf4j
public class PythonScriptService {

    @Value("${python.executable:rag/.venv/Scripts/python.exe}")
    private String pythonExecutable;

    @Value("${rag.script.path:rag_api.py}")
    private String ragScriptPath;

    /**
     * Call Python RAG script for Q&A
     * @param queryDTO Query request
     * @return RAG answer result
     */
    public RagAnswerVO queryRag(RagQueryDTO queryDTO) {
        long startTime = System.currentTimeMillis();
        
        try {
            log.info("Starting to call Python RAG script, question: {}", queryDTO.getQuestion());
            
            // Build Python command
            ProcessBuilder processBuilder;
            if (pythonExecutable.contains("uv run")) {
                // Handle uv run command
                processBuilder = new ProcessBuilder(
                    "uv", "run", "python",
                    ragScriptPath,
                    queryDTO.getQuestion(),
                    String.valueOf(queryDTO.getTopK()),
                    String.valueOf(queryDTO.getIncludeSources())
                );
            } else {
                // Handle regular python command
                processBuilder = new ProcessBuilder(
                    pythonExecutable, 
                    ragScriptPath,
                    queryDTO.getQuestion(),
                    String.valueOf(queryDTO.getTopK()),
                    String.valueOf(queryDTO.getIncludeSources())
                );
            }
            
            // Set working directory to current project's RAG directory
            File ragDir = new File("rag").getAbsoluteFile();
            processBuilder.directory(ragDir);
            processBuilder.redirectErrorStream(true);
            
            // Set environment variables to ensure UTF-8 encoding
            processBuilder.environment().put("PYTHONIOENCODING", "utf-8");
            processBuilder.environment().put("PYTHONUTF8", "1");
            
            // Explicitly pass GEMINI_API_KEY environment variable
            String geminiApiKey = System.getenv("GEMINI_API_KEY");
            if (geminiApiKey != null && !geminiApiKey.trim().isEmpty()) {
                processBuilder.environment().put("GEMINI_API_KEY", geminiApiKey);
                log.info("GEMINI_API_KEY environment variable set to Python process");
            } else {
                log.warn("System environment variable GEMINI_API_KEY not set");
            }
            
            log.info("Executing command: {}", String.join(" ", processBuilder.command()));
            log.info("Working directory: {}", ragDir.getAbsolutePath());
            
            // Start process
            Process process = processBuilder.start();
            
            // Read output
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            
            // Wait for process to complete, maximum 30 seconds
            boolean finished = process.waitFor(30, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                throw new RuntimeException("Python script execution timeout");
            }
            
            int exitCode = process.exitValue();
            if (exitCode != 0) {
                String errorMsg = output.toString();
                if (errorMsg.contains("Python was not found")) {
                    throw new RuntimeException("Python environment not configured, please install Python and add to system PATH");
                } else {
                    throw new RuntimeException("Python script execution failed, exit code: " + exitCode + ", output: " + errorMsg);
                }
            }
            
            // Parse output result
            RagAnswerVO result = parsePythonOutput(output.toString(), queryDTO.getQuestion());
            result.setProcessingTime(System.currentTimeMillis() - startTime);
            
            log.info("Python RAG script execution successful, time: {}ms", result.getProcessingTime());
            return result;
            
        } catch (Exception e) {
            log.error("Failed to call Python RAG script", e);
            throw new RuntimeException("RAG service call failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Parse Python script output
     */
    private RagAnswerVO parsePythonOutput(String output, String question) {
        try {
            // Simple JSON parsing (recommended to use Jackson or similar JSON library in actual project)
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
                    // Parse source information: sourceFile|year|score|content
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
            log.error("Failed to parse Python output", e);
            // Return simple error response
            return RagAnswerVO.builder()
                .question(question)
                .answer("Sorry, error occurred while parsing answer. Raw output: " + output)
                .sources(new ArrayList<>())
                .build();
        }
    }
}
