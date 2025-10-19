import streamlit as st
import pandas as pd
import os
import httpx
from PyPDF2 import PdfReader
from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain_core.prompts import ChatPromptTemplate
from langchain_community.vectorstores import FAISS
from langchain.tools.retriever import create_retriever_tool
from langchain.agents import AgentExecutor, create_tool_calling_agent
from langchain_community.embeddings import DashScopeEmbeddings
from langchain.chat_models import init_chat_model
from langchain_experimental.tools import PythonAstREPLTool
from langchain.agents import tool
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import io
import base64
import asyncio

# Configure matplotlib to use English fonts and avoid Chinese encoding issues
plt.rcParams['font.family'] = ['DejaVu Sans', 'Arial', 'Liberation Sans', 'sans-serif']
plt.rcParams['axes.unicode_minus'] = False
plt.rcParams['font.size'] = 12
plt.rcParams['axes.labelsize'] = 12
plt.rcParams['xtick.labelsize'] = 10
plt.rcParams['ytick.labelsize'] = 10
plt.rcParams['legend.fontsize'] = 10
plt.rcParams['figure.titlesize'] = 14
import os
from dotenv import load_dotenv 
load_dotenv(override=True)


DeepSeek_API_KEY = os.getenv("DEEPSEEK_API_KEY")
dashscope_api_key = os.getenv("dashscope_api_key")

# Set environment variables
os.environ["KMP_DUPLICATE_LIB_OK"] = "TRUE"

# Page configuration
st.set_page_config(
    page_title="CoralKita - Ocean Coral AI Assistant",
    page_icon="🌊",
    layout="wide",
    initial_sidebar_state="expanded"
)



# Ocean theme CSS styles
st.markdown("""
<style>
    /* Force dark theme - override Streamlit theme variables */
    :root,
    [data-testid="stAppViewContainer"],
    .stApp {
        --text-color: #ffffff !important;
        --background-color: #0e1117 !important;
        color-scheme: dark !important;
    }
    
    /* Ocean theme colors */
    :root {
        --ocean-blue: #0077be;
        --deep-blue: #003d5c;
        --coral-pink: #ff6b9d;
        --coral-orange: #ffa07a;
        --sea-green: #9bedee;
        --wave-cyan: #00ced1;
        --sand-beige: #f4e4c1;
    }
    
    /* Ocean gradient background */
    .stApp {
         background: linear-gradient(200deg,
        #000814 0%,
        #001d33 25%,
        #00334d 50%,
        #004766 75%,
        #002f36 100%) !important;
        min-height: 100vh !important;
    }
    
    /* Main container background - optimized version */
    .main .block-container {
        background: linear-gradient(135deg, rgba(255, 255, 255, 0.15), rgba(240, 248, 255, 0.1)) !important;
        border-radius: 30px;
        padding: 2.5rem;
        margin: 1.5rem;
        box-shadow: 
            0 20px 70px rgba(0, 119, 190, 0.3),
            0 10px 35px rgba(0, 206, 209, 0.2),
            0 5px 15px rgba(32, 178, 170, 0.15),
            inset 0 2px 1px rgba(255, 255, 255, 0.5),
            inset 0 -2px 1px rgba(0, 0, 0, 0.05);
        backdrop-filter: blur(30px) saturate(180%);
        border: 2px solid;
        border-image: linear-gradient(135deg, rgba(32, 178, 170, 0.6), rgba(0, 206, 209, 0.4), rgba(255, 107, 157, 0.3)) 1;
        border-radius: 30px;
        position: relative;
        overflow: visible;
    }
    
    /* Main container gradient glow */
    .main .block-container::before {
        content: '';
        position: absolute;
        top: -3px;
        left: -3px;
        right: -3px;
        bottom: -3px;
        background: linear-gradient(135deg, #7fffd4, #40e0d0, #ff9fbd, #ffc9a3);
        border-radius: 33px;
        z-index: -1;
        opacity: 0.4;
        filter: blur(15px);
        animation: glow-pulse 4s ease-in-out infinite;
    }
    
    @keyframes glow-pulse {
        0%, 100% { opacity: 0.3; }
        50% { opacity: 0.6; }
    }
    
    /* Hide default Streamlit styles */
    #MainMenu {visibility: hidden;}
    footer {visibility: hidden;}
    header {visibility: hidden;}
    
    /* Force override Streamlit Emotion generated Avatar styles - highest priority!!! */
    div[data-testid="stChatMessageAvatarUser"],
    div[data-testid="stChatMessageAvatarUser"] div,
    [data-testid="stChatMessageAvatarUser"].st-emotion-cache-khw9fs,
    [data-testid="stChatMessageAvatarUser"] .st-emotion-cache-khw9fs,
    .st-emotion-cache-khw9fs[data-testid="stChatMessageAvatarUser"],
    [class*="st-emotion-cache"][data-testid="chatAvatarIcon-user"],
    [class*="st-emotion-cache"][data-testid="chatAvatarIcon-user"] div,
    [data-testid="chatAvatarIcon-user"].st-emotion-cache-khw9fs,
    [data-testid="chatAvatarIcon-user"] .st-emotion-cache-khw9fs,
    [data-testid="chatAvatarIcon-user"] div[class*="st-emotion-cache"] {
        background: #00C853 !important;  /* Pure green */
        background-color: #00C853 !important;
        background-image: none !important;
    }
    
    div[data-testid="stChatMessageAvatarAssistant"],
    div[data-testid="stChatMessageAvatarAssistant"] div,
    [data-testid="stChatMessageAvatarAssistant"].st-emotion-cache-khw9fs,
    [data-testid="stChatMessageAvatarAssistant"] .st-emotion-cache-khw9fs,
    .st-emotion-cache-khw9fs[data-testid="stChatMessageAvatarAssistant"],
    [class*="st-emotion-cache"][data-testid="chatAvatarIcon-assistant"],
    [class*="st-emotion-cache"][data-testid="chatAvatarIcon-assistant"] div,
    [data-testid="chatAvatarIcon-assistant"].st-emotion-cache-khw9fs,
    [data-testid="chatAvatarIcon-assistant"] .st-emotion-cache-khw9fs,
    [data-testid="chatAvatarIcon-assistant"] div[class*="st-emotion-cache"] {
        background: #2196F3 !important;  /* Pure blue */
        background-color: #2196F3 !important;
        background-image: none !important;
    }
    
    /* Global emoji font support - highest priority */
    body, p, div, span, h1, h2, h3, h4, h5, h6 {
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji", sans-serif;
    }
    
    /* Ocean theme title style - elegant art font */
    .main-header {
        background: linear-gradient(120deg,
            #87CEEB 0%,      /* Sky blue */
            #B0E0E6 25%,     /* Powder blue */
            #AFEEEE 50%,     /* Pale turquoise */
            #87CEFA 75%,     /* Light sky blue */
            #87CEEB 100%     /* Back to sky blue */
        ) !important;
        background-size: 300% 300% !important;
        -webkit-background-clip: text !important;
        -webkit-text-fill-color: transparent !important;
        background-clip: text !important;
        font-size: 3.8rem !important;
        font-weight: 700 !important;
        text-align: center !important;
        margin: 2rem auto !important;
        padding: 1rem 0 !important;
        animation: gentle-flow 12s ease-in-out infinite !important;
        display: block !important;
        width: 100% !important;
        letter-spacing: 2px !important;
        position: relative !important;
        filter: drop-shadow(0 2px 8px rgba(135, 206, 235, 0.25)) !important;
    }
    
    /* Gentle gradient flow animation */
    @keyframes gentle-flow {
        0%, 100% { 
            background-position: 0% 50%;
            transform: translateY(0px);
        }
        50% { 
            background-position: 100% 50%;
            transform: translateY(-3px);
        }
    }
    
    /* Subtitle style */
    .subtitle {
        background: linear-gradient(90deg, #a8fff0 0%, #7df6f0 33%, #ffc4d8 66%, #ffe0c0 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
        text-align: center !important;
        margin: 0 auto 2rem auto !important;
        font-size: 1.2rem;
        font-weight: 600;
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
        letter-spacing: 1px;
    }
    
    /* Ocean theme card style (optimized version) */
    .info-card {
        background: linear-gradient(135deg, rgba(255, 255, 255, 0.98), rgba(240, 248, 255, 0.95), rgba(240, 255, 250, 0.92));
        padding: 2rem 2.2rem;
        border-radius: 25px;
        box-shadow: 
            0 15px 50px rgba(0, 119, 190, 0.35),
            0 8px 25px rgba(0, 119, 190, 0.25),
            0 4px 12px rgba(0, 119, 190, 0.15),
            inset 0 2px 1px rgba(255, 255, 255, 0.8),
            inset 0 -1px 1px rgba(0, 0, 0, 0.03);
        margin: 1.5rem 0;
        border: 2.5px solid;
        border-image: linear-gradient(135deg, rgba(0, 119, 190, 0.6), rgba(0, 206, 209, 0.5), rgba(32, 178, 170, 0.4)) 1;
        border-radius: 25px;
        backdrop-filter: blur(25px) saturate(150%);
        position: relative;
        overflow: visible;
        transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
        transform: translateZ(0);
    }
    
    /* Feature card glow effect */
    .info-card::before {
        content: '';
        position: absolute;
        top: -2px;
        left: -2px;
        right: -2px;
        bottom: -2px;
        background: linear-gradient(135deg, #0077be, #00ced1, #20b2aa);
        border-radius: 27px;
        z-index: -1;
        opacity: 0.4;
        filter: blur(12px);
        animation: card-glow 4s ease-in-out infinite;
    }
    
    .info-card:hover {
        transform: translateY(-5px) scale(1.02);
        box-shadow: 
            0 20px 60px rgba(0, 119, 190, 0.45),
            0 10px 30px rgba(0, 119, 190, 0.35),
            0 5px 15px rgba(0, 119, 190, 0.25),
            inset 0 2px 1px rgba(255, 255, 255, 0.9),
            inset 0 -1px 1px rgba(0, 0, 0, 0.05);
    }
    
    .info-card:hover::before {
        opacity: 0.6;
        filter: blur(16px);
    }
    
    @keyframes card-glow {
        0%, 100% { opacity: 0.3; filter: blur(12px); }
        50% { opacity: 0.6; filter: blur(15px); }
    }
    
    .success-card {
        background: linear-gradient(135deg, rgba(32, 178, 170, 0.2), rgba(32, 178, 170, 0.15), rgba(0, 230, 204, 0.1));
        border-image: linear-gradient(135deg, rgba(32, 178, 170, 0.7), rgba(0, 230, 204, 0.6)) 1;
    }
    
    .success-card::before {
        background: linear-gradient(135deg, #20b2aa, #00e6cc);
    }
    
    .warning-card {
        background: linear-gradient(135deg, rgba(255, 160, 122, 0.2), rgba(255, 160, 122, 0.15), rgba(255, 107, 157, 0.1));
        border-image: linear-gradient(135deg, rgba(255, 160, 122, 0.7), rgba(255, 107, 157, 0.6)) 1;
    }
    
    .warning-card::before {
        background: linear-gradient(135deg, #ffa07a, #ff6b9d);
    }
    
    /* Expander area style (soft version - glass border) */
    .streamlit-expanderHeader {
        background: linear-gradient(135deg, rgba(255, 255, 255, 0.85), rgba(240, 248, 255, 0.8), rgba(240, 255, 250, 0.75)) !important;
        border-radius: 22px !important;
        border: 2.5px solid rgba(0, 206, 209, 0.5) !important;
        padding: 1.3rem 1.6rem !important;
        font-weight: 650 !important;
        font-size: 1.05rem !important;
        color: #333 !important;
        box-shadow: 
            0 10px 30px rgba(0, 119, 190, 0.2),
            0 5px 15px rgba(0, 206, 209, 0.15),
            0 2px 8px rgba(32, 178, 170, 0.1),
            inset 0 2px 1px rgba(255, 255, 255, 0.7),
            inset 0 -1px 1px rgba(0, 0, 0, 0.03) !important;
        backdrop-filter: blur(25px) saturate(150%);
        transition: all 0.3s ease !important;
        position: relative;
        overflow: visible;
    }
    
    .streamlit-expanderHeader::before {
        content: '';
        position: absolute;
        top: -3px;
        left: -3px;
        right: -3px;
        bottom: -3px;
        background: linear-gradient(135deg, rgba(0, 119, 190, 0.3), rgba(0, 206, 209, 0.25), rgba(32, 178, 170, 0.3));
        border-radius: 25px;
        z-index: -1;
        opacity: 0;
        filter: blur(12px);
        transition: all 0.3s ease;
    }
    
    .streamlit-expanderHeader:hover {
        background: linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(240, 248, 255, 0.9), rgba(240, 255, 250, 0.85)) !important;
        border-color: rgba(0, 206, 209, 0.7) !important;
        box-shadow: 
            0 15px 45px rgba(0, 119, 190, 0.3),
            0 8px 22px rgba(0, 206, 209, 0.25),
            0 4px 12px rgba(32, 178, 170, 0.15),
            inset 0 2px 1px rgba(255, 255, 255, 0.8) !important;
        transform: translateY(-3px) scale(1.01);
    }
    
    .streamlit-expanderHeader:hover::before {
        opacity: 0.6;
        filter: blur(15px);
    }
    
    .streamlit-expanderContent {
        background: linear-gradient(135deg, rgba(255, 255, 255, 0.92), rgba(240, 248, 255, 0.88), rgba(240, 255, 250, 0.85)) !important;
        border-radius: 0 0 22px 22px !important;
        border: 2.5px solid rgba(0, 206, 209, 0.4) !important;
        border-top: none !important;
        padding: 1.8rem !important;
        backdrop-filter: blur(25px) saturate(150%);
        box-shadow: 
            0 12px 35px rgba(0, 119, 190, 0.18),
            0 6px 18px rgba(0, 206, 209, 0.12),
            0 3px 10px rgba(32, 178, 170, 0.08),
            inset 0 2px 1px rgba(255, 255, 255, 0.7) !important;
        position: relative;
        color: #ffffff !important;
    }
    
    /* All text in AI thinking process is white - but doesn't affect emoji */
    .streamlit-expanderContent {
        color: #ffffff !important;
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji", sans-serif !important;
    }
    
    .streamlit-expanderContent *:not(img):not([role="img"]):not(.emoji) {
        color: inherit !important;
    }
    
    /* Keep emoji original color - AI thinking process - highest priority */
    .streamlit-expanderContent img[data-emoji],
    .streamlit-expanderContent span[role="img"],
    .streamlit-expanderContent [role="img"],
    .streamlit-expanderContent img[alt],
    .streamlit-expanderContent .emoji {
        filter: none !important;
        background: none !important;
        -webkit-background-clip: initial !important;
        -webkit-text-fill-color: initial !important;
        background-clip: initial !important;
    }
    
    /* Column container glass effect style (optimized - more visible border and effects) */
    [data-testid="column"] {
        position: relative;
    }
    
    [data-testid="column"] > div {
        background: linear-gradient(135deg, rgba(255, 255, 255, 0.18), rgba(240, 248, 255, 0.15), rgba(240, 255, 250, 0.12)) !important;
        border-radius: 30px !important;
        padding: 2.5rem 2rem !important;
        box-shadow: 
            0 20px 70px rgba(0, 119, 190, 0.35),
            0 10px 35px rgba(0, 206, 209, 0.25),
            0 5px 18px rgba(32, 178, 170, 0.18),
            inset 0 3px 1px rgba(255, 255, 255, 0.6),
            inset 0 -2px 1px rgba(0, 0, 0, 0.05) !important;
        backdrop-filter: blur(30px) saturate(160%) !important;
        border: 3px solid rgba(0, 206, 209, 0.6) !important;
        position: relative !important;
        overflow: visible !important;
        margin: 0.8rem 0 !important;
        transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
    }
    
    /* Column container enhanced glowing border */
    [data-testid="column"] > div::before {
        content: '';
        position: absolute;
        top: -3px;
        left: -3px;
        right: -3px;
        bottom: -3px;
        background: linear-gradient(135deg, #0077be, #00ced1, #20b2aa, #00e6cc);
        border-radius: 33px;
        z-index: -1;
        opacity: 0.6;
        filter: blur(18px);
        animation: column-glow 4s ease-in-out infinite;
    }
    
    /* Column container hover effect */
    [data-testid="column"] > div:hover {
        border-color: rgba(0, 206, 209, 0.8) !important;
        box-shadow: 
            0 25px 80px rgba(0, 119, 190, 0.45),
            0 12px 40px rgba(0, 206, 209, 0.35),
            0 6px 20px rgba(32, 178, 170, 0.25),
            inset 0 3px 1px rgba(255, 255, 255, 0.7),
            inset 0 -2px 1px rgba(0, 0, 0, 0.05) !important;
        transform: translateY(-3px);
    }
    
    [data-testid="column"] > div:hover::before {
        opacity: 0.8;
        filter: blur(22px);
    }
    
    @keyframes column-glow {
        0%, 100% { opacity: 0.5; filter: blur(18px); }
        50% { opacity: 0.75; filter: blur(22px); }
    }
    
    /* Feature introduction title style */
    [data-testid="column"] h3 {
        color: #ffffff !important;
        font-weight: 800;
        font-size: 1.5rem;
        margin-bottom: 1.5rem;
        padding-bottom: 0.8rem;
        border-bottom: 3px solid;
        border-image: linear-gradient(90deg, rgba(255, 255, 255, 0.6), rgba(255, 255, 255, 0.3), rgba(255, 255, 255, 0)) 1;
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
    }
    
    /* Keep emoji original color in h3 titles */
    [data-testid="column"] h3 .emoji,
    [data-testid="column"] h3 span[role="img"] {
        background: none !important;
        -webkit-background-clip: initial !important;
        -webkit-text-fill-color: initial !important;
    }
    
    /* Markdown content style optimization */
    [data-testid="column"] .stMarkdown {
        background: linear-gradient(135deg, rgba(255, 255, 255, 0.6), rgba(240, 248, 255, 0.5));
        padding: 1.2rem 1.5rem;
        border-radius: 18px;
        margin: 1rem 0;
        border: 2px solid rgba(0, 206, 209, 0.25);
        box-shadow: 
            0 6px 20px rgba(0, 119, 190, 0.12),
            inset 0 1px 0 rgba(255, 255, 255, 0.6);
        backdrop-filter: blur(15px);
        transition: all 0.3s ease;
    }
    
    [data-testid="column"] .stMarkdown:hover {
        background: linear-gradient(135deg, rgba(255, 255, 255, 0.75), rgba(240, 248, 255, 0.65));
        border-color: rgba(0, 206, 209, 0.4);
        box-shadow: 
            0 10px 30px rgba(0, 119, 190, 0.18),
            inset 0 1px 0 rgba(255, 255, 255, 0.7);
        transform: translateY(-2px);
    }
    
    /* Feature introduction info card style (enhanced version) */
    .feature-info-card {
        background: linear-gradient(135deg, rgba(255, 255, 255, 0.92), rgba(240, 248, 255, 0.88), rgba(240, 255, 250, 0.85)) !important;
        padding: 1.8rem 2rem !important;
        border-radius: 22px !important;
        border: 3px solid rgba(0, 206, 209, 0.6) !important;
        box-shadow: 
            0 12px 40px rgba(0, 119, 190, 0.3),
            0 6px 20px rgba(0, 206, 209, 0.22),
            0 3px 10px rgba(32, 178, 170, 0.15),
            inset 0 2px 1px rgba(255, 255, 255, 0.7),
            inset 0 -1px 1px rgba(0, 0, 0, 0.03) !important;
        backdrop-filter: blur(25px) saturate(150%) !important;
        margin: 1.5rem 0 !important;
        position: relative !important;
        overflow: visible !important;
        transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275) !important;
    }
    
    .feature-info-card:hover {
        transform: translateY(-4px) scale(1.01);
        border-color: rgba(0, 206, 209, 0.8) !important;
        box-shadow: 
            0 18px 55px rgba(0, 119, 190, 0.4),
            0 8px 28px rgba(0, 206, 209, 0.3),
            0 4px 14px rgba(32, 178, 170, 0.2),
            inset 0 2px 1px rgba(255, 255, 255, 0.8) !important;
    }
    
    /* Feature card title style */
    .feature-card-title {
        font-weight: 800 !important;
        font-size: 1.15rem !important;
        margin-bottom: 0.8rem !important;
        color: #ffffff !important;
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
    }
    
    /* Feature card content style */
    .feature-card-content {
        color: #ffffff !important;
        font-size: 0.98rem !important;
        line-height: 1.7 !important;
        font-weight: 500;
    }
    
    /* Keep emoji original color in feature cards */
    .feature-card-title .emoji,
    .feature-card-content .emoji,
    .feature-card-title span[role="img"],
    .feature-card-content span[role="img"] {
        background: none !important;
        -webkit-background-clip: initial !important;
        -webkit-text-fill-color: initial !important;
    }
    
    /* Agent info card special style */
    .agent-info-card {
        background: linear-gradient(135deg, rgba(0, 119, 190, 0.12), rgba(0, 206, 209, 0.1), rgba(32, 178, 170, 0.08)) !important;
        border-color: rgba(0, 119, 190, 0.7) !important;
    }
    
    /* Data source card special style */
    .data-source-card {
        background: linear-gradient(135deg, rgba(32, 178, 170, 0.12), rgba(0, 230, 204, 0.1), rgba(32, 178, 170, 0.08)) !important;
        border-color: rgba(32, 178, 170, 0.7) !important;
    }
    
    /* Features card special style */
    .features-card {
        background: linear-gradient(135deg, rgba(0, 206, 209, 0.12), rgba(0, 230, 230, 0.1), rgba(0, 206, 209, 0.08)) !important;
        border-color: rgba(0, 206, 209, 0.7) !important;
    }
    
    /* Disclaimer card special style */
    .disclaimer-card {
        background: linear-gradient(135deg, rgba(255, 160, 122, 0.12), rgba(255, 107, 157, 0.1), rgba(255, 160, 122, 0.08)) !important;
        border-color: rgba(255, 107, 157, 0.7) !important;
    }
    
    /* Travel planning special card style */
    .travel-info-card {
        background: linear-gradient(135deg, rgba(255, 152, 0, 0.12), rgba(255, 183, 77, 0.1), rgba(255, 152, 0, 0.08)) !important;
        border-color: rgba(255, 152, 0, 0.7) !important;
    }
    
 /* Ocean theme button style (modern deep blue version, no green) */
    .stButton > button {
    background: linear-gradient(135deg,
        rgba(10, 25, 60, 0.95) 0%,    /* Deep sea blue */
        rgba(20, 45, 90, 0.92) 50%,   /* Steel blue middle */
        rgba(30, 60, 110, 0.9) 100%) !important;  /* Slightly brighter tech blue */
    color: #f2f6ff !important;  /* Slightly white cold light text */
    border: 1.5px solid rgba(255, 255, 255, 0.1) !important;
        border-radius: 25px !important;
        padding: 0.9rem 2rem !important;
        font-weight: 900 !important;
        font-size: 1.08rem !important;
        letter-spacing: 0.3px !important;
        transition: all 0.3s ease !important;
        box-shadow: 
        0 6px 20px rgba(0, 20, 50, 0.6),
        0 3px 10px rgba(0, 30, 60, 0.4),
        inset 0 1px 1px rgba(255, 255, 255, 0.1),
        inset 0 -1px 1px rgba(0, 0, 0, 0.3) !important;
    text-shadow: 0 1px 3px rgba(0, 0, 0, 0.45) !important;
        position: relative;
        overflow: hidden;
        transform: translateZ(0);
        backdrop-filter: blur(10px);
    font-family: inherit, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji" !important;
    }
    
    /* Button glow halo (soft version) */
    .stButton > button::before {
        content: '';
        position: absolute;
        top: -2px;
        left: -2px;
        right: -2px;
        bottom: -2px;
        background: linear-gradient(135deg, rgba(0, 119, 190, 0.4), rgba(0, 206, 209, 0.3), rgba(32, 178, 170, 0.4));
        border-radius: 27px;
        z-index: -1;
        opacity: 0.4;
        filter: blur(8px);
        transition: all 0.3s ease;
    }
    
    /* Button gloss effect */
    .stButton > button::after {
        content: '';
        position: absolute;
        top: 0;
        left: -100%;
        width: 100%;
        height: 100%;
        background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
        transition: left 0.5s ease;
    }
    
    /* Slightly brighten on hover, no longer greenish */
    .stButton > button:hover {
    background: linear-gradient(135deg,
        rgba(15, 35, 70, 0.95) 0%,
        rgba(30, 60, 120, 0.92) 50%,
        rgba(50, 80, 150, 0.9) 100%) !important;
    transform: translateY(-2px) scale(1.01) !important;
        box-shadow: 
        0 10px 25px rgba(0, 40, 80, 0.6),
        0 4px 12px rgba(0, 60, 120, 0.4) !important;
    }
    
    .stButton > button:hover::before {
        opacity: 0.6;
        filter: blur(10px);
    }
    
    .stButton > button:hover::after {
        left: 100%;
    }
    
    .stButton > button:active {
        transform: translateY(-1px) scale(1.01) !important;
    }
    
    /* Quick question button special style (dark version - no gradient) */
    .stButton > button[data-testid*="button"] {
    background: #1a1a2e !important;
    border: 1.5px solid rgba(255, 255, 255, 0.08) !important;
    box-shadow:
        0 4px 12px rgba(0, 20, 40, 0.7),
        inset 0 1px 1px rgba(255, 255, 255, 0.08) !important;
}

    
    .stButton > button[data-testid*="button"]:hover {
        background: #2d2d44 !important;
        transform: translateY(-2px) !important;
        box-shadow: 0 5px 15px rgba(45, 45, 68, 0.35) !important;
    }
    
    /* Ocean theme Tab style (soft version) */
    .stTabs [data-baseweb="tab-list"] {
        gap: 10px;
        background: linear-gradient(135deg, rgba(255, 255, 255, 0.12), rgba(240, 248, 255, 0.08));
        border-radius: 22px;
        padding: 0.9rem 1rem;
        backdrop-filter: blur(20px) saturate(130%);
        box-shadow: 
            0 6px 20px rgba(0, 119, 190, 0.12),
            inset 0 1px 1px rgba(255, 255, 255, 0.3),
            inset 0 -1px 1px rgba(0, 0, 0, 0.03);
        border: 1.5px solid rgba(255, 255, 255, 0.18);
    }
    
    .stTabs [data-baseweb="tab"] {
        height: 60px;
        background: linear-gradient(135deg, rgba(200, 220, 240, 0.45), rgba(190, 215, 235, 0.4));
        border-radius: 18px;
        padding: 0 28px;
        font-weight: 900 !important;
        font-size: 1.08rem !important;
        letter-spacing: 0.3px !important;
        border: 2px solid transparent;
        transition: all 0.3s ease;
        color: #444 !important;
        position: relative;
        overflow: hidden;
        box-shadow: 0 3px 8px rgba(0, 0, 0, 0.08);
        backdrop-filter: blur(8px);
    }
    
    .stTabs [data-baseweb="tab"]::before {
        content: '';
        position: absolute;
        top: 0;
        left: -100%;
        width: 100%;
        height: 100%;
        background: linear-gradient(90deg, transparent, rgba(0, 206, 209, 0.15), transparent);
        transition: left 0.5s ease;
    }
    
    .stTabs [data-baseweb="tab"]:hover {
        background: linear-gradient(135deg, rgba(255, 255, 255, 0.85), rgba(240, 248, 255, 0.8));
        transform: translateY(-2px) scale(1.02);
        box-shadow: 
            0 6px 18px rgba(0, 206, 209, 0.2),
            0 3px 10px rgba(0, 206, 209, 0.15);
        border-color: rgba(0, 206, 209, 0.25);
        color: #444;
    }
    
    .stTabs [data-baseweb="tab"]:hover::before {
        left: 100%;
    }
    
    .stTabs [aria-selected="true"] {
        background: linear-gradient(135deg, rgba(0, 80, 130, 0.8) 0%, rgba(0, 140, 150, 0.75) 50%, rgba(20, 120, 115, 0.8) 100%) !important;
        border: 2px solid rgba(255, 255, 255, 0.35) !important;
        box-shadow: 
            0 8px 25px rgba(0, 140, 150, 0.4),
            0 4px 15px rgba(0, 140, 150, 0.3),
            0 2px 8px rgba(20, 120, 115, 0.25),
            inset 0 1px 1px rgba(255, 255, 255, 0.3),
            inset 0 -1px 1px rgba(0, 0, 0, 0.08) !important;
        transform: translateY(-3px) scale(1.04);
        color: white !important;
        font-weight: 900 !important;
        font-size: 1.12rem !important;
        letter-spacing: 0.4px !important;
        text-shadow: 0 1px 4px rgba(0, 0, 0, 0.4) !important;
        backdrop-filter: blur(12px);
    }
    
    .stTabs [aria-selected="true"]::after {
        content: '';
        position: absolute;
        top: -2px;
        left: -2px;
        right: -2px;
        bottom: -2px;
        background: linear-gradient(135deg, rgba(0, 119, 190, 0.4), rgba(0, 206, 209, 0.3), rgba(32, 178, 170, 0.4));
        border-radius: 20px;
        z-index: -1;
        opacity: 0.5;
        filter: blur(10px);
    }
    
    /* Force button and tab text to be extra bold */
    button, .stButton button, .stButton > button,
    [data-baseweb="tab"], .stTabs [data-baseweb="tab"],
    .stTabs [aria-selected="true"], .stTabs [aria-selected="false"] {
        font-weight: 900 !important;
    }
    
    button *, .stButton button *, .stButton > button *,
    [data-baseweb="tab"] *, .stTabs [data-baseweb="tab"] *,
    .stTabs [aria-selected="true"] *, .stTabs [aria-selected="false"] * {
        font-weight: 900 !important;
    }
    
    /* Explicitly preserve emoji native color (prevent gradient override) */
    .emoji {
        background: none !important;
        -webkit-background-clip: initial !important;
        -webkit-text-fill-color: initial !important;
        color: inherit !important;
        font-size: 1.2em !important;
        display: inline-block !important;
    }
    
    /* Gradient text class */
    .gradient-text {
        background: linear-gradient(90deg, 
            #7fffd4 0%,    /* Light sea green */
            #40e0d0 33%,   /* Bright cyan */
            #ff9fbd 66%,   /* Soft pink coral */
            #ffc9a3 100%   /* Light coral orange */
        ) !important;
        -webkit-background-clip: text !important;
        -webkit-text-fill-color: transparent !important;
        background-clip: text !important;
    }
    
    /* Emoji protection in buttons - stronger rules */
    .stButton button,
    .stButton > button {
        font-family: inherit, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji" !important;
    }
    
    /* Ensure emoji in buttons are not overridden by button color */
    .stButton button img[data-emoji],
    .stButton button span[role="img"],
    .stButton button .emoji,
    .stButton > button img[data-emoji],
    .stButton > button span[role="img"],
    .stButton > button .emoji {
        filter: none !important;
        color: initial !important;
        background: none !important;
        -webkit-background-clip: initial !important;
        -webkit-text-fill-color: initial !important;
    }
    
    /* Ocean theme sidebar style */
    .css-1d391kg {
        background: linear-gradient(180deg, rgba(255, 255, 255, 0.02), rgba(240, 248, 255, 0.01));
        backdrop-filter: blur(15px);
        border: 1px solid rgba(255, 255, 255, 0.05);
    }
    
    /* Ocean theme file upload area */
    .uploadedFile {
        background: rgba(255, 255, 255, 0.9);
        border: 3px dashed #00ced1;
        border-radius: 15px;
        padding: 1.5rem;
        text-align: center;
        margin: 1rem 0;
        transition: all 0.3s ease;
    }
    
    .uploadedFile:hover {
        border-color: #20b2aa;
        box-shadow: 0 4px 15px rgba(0, 206, 209, 0.3);
    }
    
    /* Ocean theme status indicator */
    .status-indicator {
        display: inline-flex;
        align-items: center;
        gap: 0.5rem;
        padding: 0.8rem 1.5rem;
        border-radius: 25px;
        font-weight: 600;
        font-size: 0.9rem;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }
    
    .status-ready {
        background: linear-gradient(135deg, rgba(32, 178, 170, 0.2), rgba(32, 178, 170, 0.1));
        color: #20b2aa;
        border: 2px solid #20b2aa;
    }
    
    .status-waiting {
        background: linear-gradient(135deg, rgba(255, 160, 122, 0.2), rgba(255, 160, 122, 0.1));
        color: #ffa07a;
        border: 2px solid #ffa07a;
    }
    
    /* Ocean theme chat message - user message (enhanced version with very visible glass border) */
    div.stChatMessage[data-testid="user-message"],
    .stChatMessage[data-testid="user-message"] {
        background: linear-gradient(135deg, rgba(255, 255, 255, 0.92) 0%, rgba(240, 248, 255, 0.88) 50%, rgba(230, 245, 255, 0.85) 100%) !important;
        border-radius: 22px !important;
        border: 6px solid rgba(0, 206, 209, 1) !important;
        backdrop-filter: blur(35px) saturate(200%) !important;
        box-shadow: 
            0 0 0 3px rgba(255, 255, 255, 0.7) inset !important,
            0 0 0 10px rgba(0, 206, 209, 0.35) !important,
            0 25px 70px rgba(0, 119, 190, 0.45) !important,
            0 12px 40px rgba(0, 206, 209, 0.4) !important,
            0 6px 25px rgba(32, 178, 170, 0.35) !important,
            inset 0 4px 8px rgba(255, 255, 255, 1) !important,
            inset 0 -3px 6px rgba(0, 206, 209, 0.25) !important,
            inset 0 0 100px rgba(240, 248, 255, 0.7) !important;
        margin: 1.5rem 0 !important;
        padding: 2rem 2.5rem !important;
        position: relative !important;
        color: #ffffff !important;
        transform: translateZ(0) !important;
        transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275) !important;
        overflow: visible !important;
    }
    
    /* All text in user message is white - but doesn't affect emoji */
    .stChatMessage[data-testid="user-message"] {
        color: #ffffff !important;
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji", sans-serif !important;
    }
    
    .stChatMessage[data-testid="user-message"] *:not(img):not([role="img"]):not(.emoji) {
        color: #ffffff !important;
    }
    
    /* Force all text elements to white - user message - ultra high priority */
    .stChatMessage[data-testid="user-message"] p,
    .stChatMessage[data-testid="user-message"] div,
    .stChatMessage[data-testid="user-message"] span,
    .stChatMessage[data-testid="user-message"] li,
    .stChatMessage[data-testid="user-message"] strong,
    .stChatMessage[data-testid="user-message"] b,
    .stChatMessage[data-testid="user-message"] a,
    .stChatMessage[data-testid="user-message"] code,
    .stChatMessage[data-testid="user-message"] pre,
    .stChatMessage[data-testid="user-message"] h1,
    .stChatMessage[data-testid="user-message"] h2,
    .stChatMessage[data-testid="user-message"] h3,
    .stChatMessage[data-testid="user-message"] h4,
    .stChatMessage[data-testid="user-message"] h5,
    .stChatMessage[data-testid="user-message"] h6,
    .stChatMessage[data-testid="user-message"] td,
    .stChatMessage[data-testid="user-message"] th,
    div[data-testid="user-message"] p,
    div[data-testid="user-message"] div,
    div[data-testid="user-message"] span,
    div[data-testid="user-message"] *:not(img):not([role="img"]):not(.emoji) {
        color: #ffffff !important;
        -webkit-text-fill-color: #ffffff !important;
    }
    
    /* Keep emoji original color - user message - highest priority */
    .stChatMessage[data-testid="user-message"] img[data-emoji],
    .stChatMessage[data-testid="user-message"] span[role="img"],
    .stChatMessage[data-testid="user-message"] [role="img"],
    .stChatMessage[data-testid="user-message"] img[alt],
    .stChatMessage[data-testid="user-message"] .emoji {
        filter: none !important;
        background: none !important;
        -webkit-background-clip: initial !important;
        -webkit-text-fill-color: initial !important;
        background-clip: initial !important;
    }
    
    /* User message glow effect (enhanced version) */
    .stChatMessage[data-testid="user-message"]::before {
        content: '';
        position: absolute;
        top: -4px;
        left: -4px;
        right: -4px;
        bottom: -4px;
        background: linear-gradient(135deg, rgba(0, 119, 190, 0.5), rgba(0, 206, 209, 0.45), rgba(32, 178, 170, 0.5));
        border-radius: 26px;
        z-index: -1;
        opacity: 0.7;
        filter: blur(18px);
        transition: all 0.4s ease;
        animation: user-message-glow 4s ease-in-out infinite;
    }
    
    .stChatMessage[data-testid="user-message"]:hover {
        transform: translateY(-4px) scale(1.01) !important;
        border-color: rgba(0, 206, 209, 1) !important;
        box-shadow: 
            0 0 0 4px rgba(255, 255, 255, 0.8) inset !important,
            0 0 0 12px rgba(0, 206, 209, 0.45) !important,
            0 30px 80px rgba(0, 119, 190, 0.55) !important,
            0 15px 50px rgba(0, 206, 209, 0.5) !important,
            0 8px 30px rgba(32, 178, 170, 0.45) !important,
            inset 0 5px 10px rgba(255, 255, 255, 1) !important,
            inset 0 -4px 8px rgba(0, 206, 209, 0.3) !important,
            inset 0 0 120px rgba(240, 248, 255, 0.85) !important;
    }
    
    .stChatMessage[data-testid="user-message"]:hover::before {
        opacity: 0.9;
        filter: blur(22px);
    }
    
    @keyframes user-message-glow {
        0%, 100% { opacity: 0.6; filter: blur(18px); }
        50% { opacity: 0.85; filter: blur(22px); }
    }
    
    /* Ocean theme chat message - assistant message (enhanced version with very visible glass border) */
    div.stChatMessage[data-testid="assistant-message"],
    .stChatMessage[data-testid="assistant-message"] {
        background: linear-gradient(135deg, rgba(255, 255, 255, 0.92) 0%, rgba(248, 255, 252, 0.88) 50%, rgba(240, 255, 248, 0.85) 100%) !important;
        border-radius: 22px !important;
        border: 6px solid rgba(32, 178, 170, 1) !important;
        backdrop-filter: blur(35px) saturate(200%) !important;
        box-shadow: 
            0 0 0 3px rgba(255, 255, 255, 0.7) inset !important,
            0 0 0 10px rgba(32, 178, 170, 0.35) !important,
            0 25px 70px rgba(32, 178, 170, 0.45) !important,
            0 12px 40px rgba(32, 178, 170, 0.4) !important,
            0 6px 25px rgba(32, 178, 170, 0.35) !important,
            inset 0 4px 8px rgba(255, 255, 255, 1) !important,
            inset 0 -3px 6px rgba(32, 178, 170, 0.25) !important,
            inset 0 0 100px rgba(240, 255, 248, 0.7) !important;
        margin: 1.5rem 0 !important;
        padding: 2rem 2.5rem !important;
        position: relative !important;
        color: #ffffff !important;
        transform: translateZ(0) !important;
        transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275) !important;
        overflow: visible !important;
    }
    
    /* All text in assistant message is white - but doesn't affect emoji */
    .stChatMessage[data-testid="assistant-message"] {
        color: #ffffff !important;
        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol", "Noto Color Emoji", sans-serif !important;
    }
    
    .stChatMessage[data-testid="assistant-message"] *:not(img):not([role="img"]):not(.emoji) {
        color: #ffffff !important;
    }
    
    /* Force all text elements to white - assistant message - ultra high priority */
    .stChatMessage[data-testid="assistant-message"] p,
    .stChatMessage[data-testid="assistant-message"] div,
    .stChatMessage[data-testid="assistant-message"] span,
    .stChatMessage[data-testid="assistant-message"] li,
    .stChatMessage[data-testid="assistant-message"] strong,
    .stChatMessage[data-testid="assistant-message"] b,
    .stChatMessage[data-testid="assistant-message"] a,
    .stChatMessage[data-testid="assistant-message"] code,
    .stChatMessage[data-testid="assistant-message"] pre,
    .stChatMessage[data-testid="assistant-message"] h1,
    .stChatMessage[data-testid="assistant-message"] h2,
    .stChatMessage[data-testid="assistant-message"] h3,
    .stChatMessage[data-testid="assistant-message"] h4,
    .stChatMessage[data-testid="assistant-message"] h5,
    .stChatMessage[data-testid="assistant-message"] h6,
    .stChatMessage[data-testid="assistant-message"] td,
    .stChatMessage[data-testid="assistant-message"] th,
    div[data-testid="assistant-message"] p,
    div[data-testid="assistant-message"] div,
    div[data-testid="assistant-message"] span,
    div[data-testid="assistant-message"] *:not(img):not([role="img"]):not(.emoji) {
        color: #ffffff !important;
        -webkit-text-fill-color: #ffffff !important;
    }
    
    /* Keep emoji original color - assistant message - highest priority */
    .stChatMessage[data-testid="assistant-message"] img[data-emoji],
    .stChatMessage[data-testid="assistant-message"] span[role="img"],
    .stChatMessage[data-testid="assistant-message"] [role="img"],
    .stChatMessage[data-testid="assistant-message"] img[alt],
    .stChatMessage[data-testid="assistant-message"] .emoji {
        filter: none !important;
        background: none !important;
        -webkit-background-clip: initial !important;
        -webkit-text-fill-color: initial !important;
        background-clip: initial !important;
    }
    
    /* Assistant message glow effect (enhanced version) */
    .stChatMessage[data-testid="assistant-message"]::before {
        content: '';
        position: absolute;
        top: -4px;
        left: -4px;
        right: -4px;
        bottom: -4px;
        background: linear-gradient(135deg, rgba(32, 178, 170, 0.5), rgba(0, 230, 204, 0.45), rgba(32, 178, 170, 0.5));
        border-radius: 26px;
        z-index: -1;
        opacity: 0.7;
        filter: blur(18px);
        transition: all 0.4s ease;
        animation: assistant-message-glow 4s ease-in-out infinite;
    }
    
    .stChatMessage[data-testid="assistant-message"]:hover {
        transform: translateY(-4px) scale(1.01) !important;
        border-color: rgba(32, 178, 170, 1) !important;
        box-shadow: 
            0 0 0 4px rgba(255, 255, 255, 0.8) inset !important,
            0 0 0 12px rgba(32, 178, 170, 0.45) !important,
            0 30px 80px rgba(32, 178, 170, 0.55) !important,
            0 15px 50px rgba(32, 178, 170, 0.5) !important,
            0 8px 30px rgba(32, 178, 170, 0.45) !important,
            inset 0 5px 10px rgba(255, 255, 255, 1) !important,
            inset 0 -4px 8px rgba(32, 178, 170, 0.3) !important,
            inset 0 0 120px rgba(240, 255, 248, 0.85) !important;
    }
    
    .stChatMessage[data-testid="assistant-message"]:hover::before {
        opacity: 0.9;
        filter: blur(22px);
    }
    
    @keyframes assistant-message-glow {
        0%, 100% { opacity: 0.6; filter: blur(18px); }
        50% { opacity: 0.85; filter: blur(22px); }
    }
    
    /* Thinking status spinning animation */
    @keyframes spin {
        0% { transform: rotate(0deg); }
        100% { transform: rotate(360deg); }
    }
    
    .thinking-spinner {
        display: inline-block;
        animation: spin 1s linear infinite;
    }
    
    
    /* Ocean theme chat message avatar style */
    .stChatMessage [data-testid="chatAvatarIcon-user"],
    .stChatMessage [data-testid="chatAvatarIcon-user"] > div,
    .stChatMessage [data-testid="chatAvatarIcon-user"] div {
        background: #00C853 !important;  /* Pure green */
        background-color: #00C853 !important;
        background-image: none !important;
        border-radius: 50% !important;
        box-shadow: 0 6px 20px rgba(0, 200, 83, 0.6) !important;
        border: 4px solid #ffffff !important;  /* Pure white border */
        position: relative !important;
        overflow: hidden !important;
        opacity: 1 !important;  /* Ensure opacity */
        width: 40px !important;
        height: 40px !important;
    }
    
    .stChatMessage [data-testid="chatAvatarIcon-user"]::before {
        content: '🌊' !important;
        position: absolute !important;
        top: 50% !important;
        left: 50% !important;
        transform: translate(-50%, -50%) !important;
        font-size: 1.2rem !important;
        color: white !important;
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3) !important;
    }
    
    .stChatMessage [data-testid="chatAvatarIcon-assistant"],
    .stChatMessage [data-testid="chatAvatarIcon-assistant"] > div,
    .stChatMessage [data-testid="chatAvatarIcon-assistant"] div {
        background: #2196F3 !important;  /* Pure blue */
        background-color: #2196F3 !important;
        background-image: none !important;
        border-radius: 50% !important;
        box-shadow: 0 6px 20px rgba(33, 150, 243, 0.6) !important;
        border: 4px solid #ffffff !important;  /* Pure white border */
        position: relative !important;
        overflow: hidden !important;
        opacity: 1 !important;  /* Ensure opacity */
        width: 40px !important;
        height: 40px !important;
    }
    
    .stChatMessage [data-testid="chatAvatarIcon-assistant"]::before {
        content: '🤖' !important;
        position: absolute !important;
        top: 50% !important;
        left: 50% !important;
        transform: translate(-50%, -50%) !important;
        font-size: 1.2rem !important;
        color: white !important;
        text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3) !important;
    }
    
    /* Avatar hover effect */
    .stChatMessage [data-testid="chatAvatarIcon-user"]:hover {
        transform: scale(1.1) !important;
        box-shadow: 0 8px 25px rgba(0, 200, 83, 0.8) !important;  /* Bright green glow */
        border: 5px solid #ffffff !important;  /* Border slightly thicker on hover */
        transition: all 0.3s ease !important;
    }
    
    .stChatMessage [data-testid="chatAvatarIcon-assistant"]:hover {
        transform: scale(1.1) !important;
        box-shadow: 0 8px 25px rgba(33, 150, 243, 0.8) !important;  /* Bright blue glow */
        border: 5px solid #ffffff !important;  /* Border slightly thicker on hover */
        transition: all 0.3s ease !important;
    }
    
 
    
    /* Remove extra margins from paragraphs and lists for more compact content */
    .stChatMessage p,
    .stChatMessage ul,
    .stChatMessage ol {
        margin: 0.5rem 0 !important;
        padding-left: 0 !important;
    }
    
    .stChatMessage ul,
    .stChatMessage ol {
        padding-left: 1.5rem !important;
    }
    
    /* Chat message hover effect */
    .stChatMessage:hover {
        transform: translateY(-2px) !important;
        box-shadow: 0 12px 40px rgba(0, 119, 190, 0.3) !important;
        transition: all 0.3s ease !important;
    }
    
    /* Ocean theme input box */
    .stTextInput > div > div > input,
    .stTextArea > div > div > textarea {
        background: linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(240, 248, 255, 0.9)) !important;
        border: 2px solid rgba(0, 119, 190, 0.3) !important;
        border-radius: 25px !important;
        padding: 1rem 1.5rem !important;
        font-size: 1rem !important;
        transition: all 0.3s ease !important;
        box-shadow: 0 4px 15px rgba(0, 119, 190, 0.1) !important;
        backdrop-filter: blur(10px) !important;
    }
    
    .stTextInput > div > div > input:focus,
    .stTextArea > div > div > textarea:focus {
        border-color: #00ced1 !important;
        box-shadow: 0 0 0 3px rgba(0, 206, 209, 0.2), 0 8px 25px rgba(0, 206, 209, 0.3) !important;
        transform: translateY(-2px) !important;
    }
    
    /* Chat input box container (enhanced version) */
    .stChatInput {
        background: linear-gradient(135deg, rgba(0, 40, 60, 0.65), rgba(0, 70, 90, 0.6)) !important;
        border-radius: 25px !important;
        padding: 1.5rem !important;
        backdrop-filter: blur(30px) saturate(150%) !important;
        border: none !important;  /* Remove border */
        box-shadow: none !important;  /* Remove shadow */
        position: relative;
        margin: 1.5rem 0 !important;
        transition: all 0.3s ease;
    }
    

    
    .stChatInput:focus-within {
        border: none !important;  /* No border on focus */
        box-shadow: none !important;  /* No shadow on focus */
    }
    
    .stChatInput:focus-within::before {
        display: none !important;  /* Hide glowing pseudo-element */
    }
    
    .stChatInput::before {
        display: none !important;  /* Hide glowing pseudo-element */
    }
    
    @keyframes input-glow {
        0%, 100% { opacity: 0.6; filter: blur(15px); }
        50% { opacity: 0.85; filter: blur(18px); }
    }
    
    /* Chat area overall style (very visible glass border version) */
    .stChatMessageContainer {
        background: linear-gradient(135deg, rgba(255, 255, 255, 0.85) 0%, rgba(240, 248, 255, 0.8) 50%, rgba(230, 245, 255, 0.75) 100%) !important;
        border-radius: 25px !important;
        padding: 2rem !important;
        backdrop-filter: blur(30px) saturate(180%) !important;
        border: 5px solid rgba(0, 119, 190, 0.9) !important;
        box-shadow: 
            0 0 0 2px rgba(255, 255, 255, 0.5) inset !important,
            0 0 0 7px rgba(0, 119, 190, 0.3) !important,
            0 20px 60px rgba(0, 119, 190, 0.4) !important,
            0 10px 35px rgba(0, 119, 190, 0.35) !important,
            0 5px 20px rgba(0, 119, 190, 0.3) !important,
            inset 0 3px 5px rgba(255, 255, 255, 0.95) !important,
            inset 0 -2px 5px rgba(0, 119, 190, 0.2) !important,
            inset 0 0 80px rgba(240, 248, 255, 0.6) !important;
        margin: 1.5rem 0 !important;
        position: relative;
        overflow: visible !important;
    }
    
    /* Chat area glowing border (enhanced version) */
    .stChatMessageContainer::before {
        content: '';
        position: absolute;
        top: -3px;
        left: -3px;
        right: -3px;
        bottom: -3px;
        background: linear-gradient(135deg, rgba(0, 119, 190, 0.4), rgba(0, 206, 209, 0.35), rgba(32, 178, 170, 0.4));
        border-radius: 28px;
        z-index: -1;
        opacity: 0.6;
        filter: blur(15px);
        animation: chat-area-glow 4s ease-in-out infinite;
    }
    
    .stChatMessageContainer:hover {
        border-color: rgba(0, 119, 190, 1) !important;
        box-shadow: 
            0 0 0 3px rgba(255, 255, 255, 0.6) inset !important,
            0 0 0 8px rgba(0, 119, 190, 0.4) !important,
            0 25px 70px rgba(0, 119, 190, 0.5) !important,
            0 12px 40px rgba(0, 119, 190, 0.4) !important,
            0 6px 25px rgba(0, 119, 190, 0.35) !important,
            inset 0 3px 5px rgba(255, 255, 255, 1) !important,
            inset 0 -2px 5px rgba(0, 119, 190, 0.25) !important,
            inset 0 0 90px rgba(240, 248, 255, 0.7) !important;
    }
    
    .stChatMessageContainer:hover::before {
        opacity: 0.8;
        filter: blur(18px);
    }
    
    @keyframes chat-area-glow {
        0%, 100% { opacity: 0.5; filter: blur(15px); }
        50% { opacity: 0.75; filter: blur(18px); }
    }
    
    /* Chat message timestamp */
    .stChatMessage .timestamp {
        font-size: 0.8rem !important;
        color: #666 !important;
        opacity: 0.7 !important;
        text-align: right !important;
        margin-top: 0.5rem !important;
    }
    
    /* Chat message status indicator */
    .stChatMessage .status {
        display: inline-flex !important;
        align-items: center !important;
        gap: 0.3rem !important;
        font-size: 0.8rem !important;
        color: #20b2aa !important;
        margin-top: 0.5rem !important;
    }
    
    /* Chat message loading animation */
    .stChatMessage .typing-indicator {
        display: flex !important;
        align-items: center !important;
        gap: 0.5rem !important;
        color: #20b2aa !important;
        font-style: italic !important;
    }
    
    .stChatMessage .typing-indicator::after {
        content: '...' !important;
        animation: typing 1.5s infinite !important;
    }
    
    @keyframes typing {
        0%, 60%, 100% { opacity: 0; }
        30% { opacity: 1; }
    }
    
    /* Ocean theme dataframe */
    .stDataFrame {
        background: rgba(255, 255, 255, 0.95);
        border-radius: 12px;
        overflow: hidden;
    }
    
    /* Ocean theme progress bar */
    .stProgress > div > div > div > div {
        background: linear-gradient(45deg, #0077be, #00ced1, #20b2aa);
    }
    
    /* Ocean gradient text - main container */
    .main .block-container {
        background: linear-gradient(45deg, 
            #7fffd4,    /* Light sea green (original #20b2aa → brighter) */
            #40e0d0,    /* Bright cyan (original #00ced1 → brighter) */
            #0077be     /* Keep ocean blue */
        ) !important;
        -webkit-background-clip: text !important;
        -webkit-text-fill-color: transparent !important;
        background-clip: text !important;
    }
    
    /* Ocean gradient text - normal text */
    .main .block-container p,
    .main .block-container div,
    .main .block-container span,
    .main .block-container li,
    .main .block-container td,
    .main .block-container th {
        background: linear-gradient(45deg, 
            #7fffd4,    /* Light sea green (original #20b2aa → brighter) */
            #40e0d0,    /* Bright cyan (original #00ced1 → brighter) */
            #0077be,    /* Keep ocean blue */
            #ff9fbd     /* Soft pink coral (original #ff6b9d → lighter) */
        ) !important;
        -webkit-background-clip: text !important;
        -webkit-text-fill-color: transparent !important;
        background-clip: text !important;
    }
    
    /* Ocean gradient text - headings */
    .main .block-container h1,
    .main .block-container h2,
    .main .block-container h3,
    .main .block-container h4,
    .main .block-container h5,
    .main .block-container h6 {
        background: linear-gradient(45deg, 
            #7fffd4,    /* Light sea green (original #20b2aa → brighter) */
            #40e0d0,    /* Bright cyan (original #00ced1 → brighter) */
            #0077be,    /* Keep ocean blue */
            #ff9fbd,    /* Soft pink coral (original #ff6b9d → lighter) */
            #ffc9a3     /* Light coral orange (original #ffa07a → brighter) */
        ) !important;
        -webkit-background-clip: text !important;
        -webkit-text-fill-color: transparent !important;
        background-clip: text !important;
    }
    
    /* Ocean gradient text - emphasized text */
    .main .block-container strong,
    .main .block-container b {
        background: linear-gradient(45deg, 
            #0077be,    /* Keep ocean blue */
            #ff9fbd,    /* Soft pink coral (original #ff6b9d → lighter) */
            #ffc9a3     /* Light coral orange (original #ffa07a → brighter) */
        ) !important;
        -webkit-background-clip: text !important;
        -webkit-text-fill-color: transparent !important;
        background-clip: text !important;
    }
    
    /* Ocean gradient text - links */
    .main .block-container a {
        background: linear-gradient(45deg, 
            #0077be,    /* Keep ocean blue */
            #40e0d0     /* Bright cyan (original #00ced1 → brighter) */
        ) !important;
        -webkit-background-clip: text !important;
        -webkit-text-fill-color: transparent !important;
        background-clip: text !important;
    }
    
    /* Ocean gradient text - code */
    .main .block-container code {
        background: linear-gradient(45deg, 
            #7fffd4,    /* Light sea green (original #20b2aa → brighter) */
            #0077be     /* Keep ocean blue */
        ) !important;
        -webkit-background-clip: text !important;
        -webkit-text-fill-color: transparent !important;
        background-clip: text !important;
        background-color: rgba(0, 119, 190, 0.1) !important;
    }
    
    /* Ocean gradient text - table */
    .main .block-container table {
        background: linear-gradient(45deg, 
            #7fffd4,    /* Light sea green (original #20b2aa → brighter) */
            #40e0d0,    /* Bright cyan (original #00ced1 → brighter) */
            #0077be     /* Keep ocean blue */
        ) !important;
        -webkit-background-clip: text !important;
        -webkit-text-fill-color: transparent !important;
        background-clip: text !important;
    }
    
    .main .block-container table th {
        background: linear-gradient(45deg, 
            #0077be,    /* Keep ocean blue */
            #ff9fbd     /* Soft pink coral (original #ff6b9d → lighter) */
        ) !important;
        -webkit-background-clip: text !important;
        -webkit-text-fill-color: transparent !important;
        background-clip: text !important;
        background-color: rgba(0, 119, 190, 0.1) !important;
    }
    
    .main .block-container table td {
        background: linear-gradient(45deg, 
            #7fffd4,    /* Light sea green (original #20b2aa → brighter) */
            #40e0d0,    /* Bright cyan (original #00ced1 → brighter) */
            #0077be     /* Keep ocean blue */
        ) !important;
        -webkit-background-clip: text !important;
        -webkit-text-fill-color: transparent !important;
        background-clip: text !important;
    }

    * {
    text-shadow: none !important;
    }
            
    /* Enlarge Tab button text */
    [data-testid="stTabs"] button p {
        font-size: 1.2rem !important;   /* Originally about 0.9rem, can adjust 1.1~1.4 based on preference */
        font-weight: 700 !important;    /* Bold for more visibility */
        letter-spacing: 0.5px;          /* Optional: make text more spacious */
    }

    /* Adjust activated tab text contrast */
    [data-testid="stTabs"] button[aria-selected="true"] p {
        font-size: 1.25rem !important;  /* Selected tab slightly larger */      

    }


    [data-testid="stTabs"] button {
    padding: 0.8rem 1.6rem !important;
    }

  .stButton > button[data-testid*="button"] {
    background: linear-gradient(135deg, rgba(0, 15, 30, 0.95), rgba(0, 40, 50, 0.9)) !important;
    border: 1.5px solid rgba(255, 255, 255, 0.08) !important;
    box-shadow:
        0 4px 12px rgba(0, 20, 40, 0.7),
        inset 0 1px 1px rgba(255, 255, 255, 0.08) !important;
    }
</style>
""", unsafe_allow_html=True)

# Initialize embeddings
@st.cache_resource
def init_embeddings():
    return DashScopeEmbeddings(
        model="text-embedding-v1", 
        dashscope_api_key=dashscope_api_key
    )

# Force set matplotlib font configuration
def force_english_fonts():
    """Force matplotlib to use English fonts to avoid garbled text"""
    plt.rcParams.update({
        'font.family': ['DejaVu Sans', 'Arial', 'Liberation Sans', 'sans-serif'],
        'axes.unicode_minus': False,
        'font.size': 12,
        'axes.labelsize': 12,
        'xtick.labelsize': 10,
        'ytick.labelsize': 10,
        'legend.fontsize': 10,
        'figure.titlesize': 14
    })

# Save figure to session state (for AI to call)
def save_figure_to_session(fig, title="Chart"):
    """Save matplotlib figure to session state for persistence"""
    try:
        import io
        import re
        buffer = io.BytesIO()
        fig.savefig(buffer, format='png', dpi=150, bbox_inches='tight')
        buffer.seek(0)
        image_data = buffer.getvalue()
        buffer.close()
        
        # Save to session state temporary variable
        if 'temp_figure_data' not in st.session_state:
            st.session_state.temp_figure_data = {}
        
        filename = re.sub(r'[^\w\s-]', '', title.strip()).replace(' ', '_')[:50] + '.png'
        if not filename or filename == '.png':
            filename = 'coral_chart.png'
        
        st.session_state.temp_figure_data = {
            'image_data': image_data,
            'filename': filename,
            'title': title
        }
        return True
    except Exception as e:
        return False

# Create image download link
def create_download_link(fig, filename="plot.png"):
    """Create download link for matplotlib figure"""
    # Save figure to memory buffer
    buffer = io.BytesIO()
    fig.savefig(buffer, format='png', dpi=300, bbox_inches='tight')
    buffer.seek(0)
    
    # Create download link
    b64 = base64.b64encode(buffer.getvalue()).decode()
    href = f'<a href="data:image/png;base64,{b64}" download="{filename}">📥 Download Chart</a>'
    return href

# Initialize LLM
@st.cache_resource
def init_llm():
    # Use lower temperature for faster, more direct responses
    return init_chat_model("deepseek-chat", model_provider="deepseek", temperature=0.3)

# Initialize session state
def init_session_state():
    if 'pdf_messages' not in st.session_state:
        st.session_state.pdf_messages = []
    if 'csv_messages' not in st.session_state:
        st.session_state.csv_messages = []
    if 'df' not in st.session_state:
        # Auto-load coral.csv file
        try:
            if os.path.exists('coral.csv'):
                st.session_state.df = pd.read_csv('coral.csv')
            else:
                st.session_state.df = None
        except Exception as e:
            st.session_state.df = None

# PDF Processing Functions
def pdf_read(pdf_doc):
    text = ""
    for pdf in pdf_doc:
        pdf_reader = PdfReader(pdf)
        for page in pdf_reader.pages:
            text += page.extract_text()
    return text

def get_chunks(text):
    text_splitter = RecursiveCharacterTextSplitter(chunk_size=1000, chunk_overlap=200)
    chunks = text_splitter.split_text(text)
    return chunks

def vector_store(text_chunks):
    embeddings = init_embeddings()
    vector_store = FAISS.from_texts(text_chunks, embedding=embeddings)
    vector_store.save_local("faiss_db")

def check_database_exists():
    return os.path.exists("faiss_db") and os.path.exists("faiss_db/index.faiss")

def get_pdf_response(user_question):
    if not check_database_exists():
        yield {"type": "answer", "content": "❌ Please upload PDF files and click 'Submit & Process' button to process documents first!"}
        return
    
    try:
        embeddings = init_embeddings()
        llm = init_llm()
        
        new_db = FAISS.load_local("faiss_db", embeddings, allow_dangerous_deserialization=True)
        retriever = new_db.as_retriever()
        
        prompt = ChatPromptTemplate.from_messages([
            ("system", """You are an AI assistant. Please answer questions quickly and concisely.
            
            Priority requirements:
            - Respond as quickly as possible, avoid lengthy analysis
            - Answer user questions directly and keep it concise
            - If you can find the answer directly from context, provide it immediately
            - Avoid unnecessary detailed explanations
            
            Answer questions based on the provided context. If the answer is not in the context, say "Answer not found in context". Do not provide incorrect answers."""),
            ("placeholder", "{chat_history}"),
            ("human", "{input}"),
            ("placeholder", "{agent_scratchpad}"),
        ])
        
        retrieval_chain = create_retriever_tool(retriever, "pdf_extractor", "This tool is to give answer to queries from the pdf")
        agent = create_tool_calling_agent(llm, [retrieval_chain], prompt)
        agent_executor = AgentExecutor(agent=agent, tools=[retrieval_chain], verbose=True, max_iterations=10, return_intermediate_steps=True)
        
        # Use stream mode to get response
        thinking_steps = []
        full_response = ""
        
        for chunk in agent_executor.stream({"input": user_question}):
            # Capture intermediate steps (thinking process)
            if 'actions' in chunk:
                for action in chunk['actions']:
                    thinking_step = f"🔍 Tool: {action.tool}\n📝 Input: {action.tool_input}"
                    thinking_steps.append(thinking_step)
                    yield {"type": "thinking", "content": thinking_step}
            
            if 'steps' in chunk:
                for step in chunk['steps']:
                    if hasattr(step, 'observation'):
                        thinking_step = f"💡 Observation: {step.observation[:200]}..."
                        thinking_steps.append(thinking_step)
                        yield {"type": "thinking", "content": thinking_step}
            
            if 'output' in chunk:
                full_response = chunk['output']
        
        # Output final answer word by word
        yield {"type": "answer_start", "content": ""}
        for char in full_response:
            yield {"type": "answer", "content": char}
        
    except Exception as e:
        error_msg = str(e)
        if "max iterations" in error_msg.lower():
            yield {"type": "answer", "content": "❌ Analysis is too complex, please try simpler questions or break down your query"}
        else:
            yield {"type": "answer", "content": f"❌ Error processing question: {error_msg}"}

# CSV Processing Functions
def get_csv_response(query: str):
    if st.session_state.df is None:
        yield {"type": "answer", "content": "Please upload CSV file first"}
        return
    
    llm = init_llm()
    locals_dict = {
        'df': st.session_state.df,
        'st': st,
        'create_download_link': create_download_link,
        'save_figure_to_session': save_figure_to_session,
        'plt': plt,
        'io': io,
        'BytesIO': io.BytesIO,
        'base64': base64
    }
    tools = [PythonAstREPLTool(locals=locals_dict)]
    
    system = f"""Given a pandas dataframe `df` answer user's query.
    Here's the output of `df.head().to_markdown()` for your reference, you have access to full dataframe as `df`:
    ```
    {st.session_state.df.head().to_markdown()}
    ```
    
    AVAILABLE MODULES AND TOOLS:
    - df: pandas DataFrame with coral reef data
    - st: Streamlit module for UI display
    - plt: matplotlib.pyplot for plotting
    - save_figure_to_session(fig, title): REQUIRED function to save figure for persistence
    - create_download_link(fig, filename): function to create download link for figures
    
    ⚠️ IMPORTANT: Do NOT use io, BytesIO, or base64 directly in your code!
    - The create_download_link() function already handles all file operations for you
    - Just call: create_download_link(fig, "filename.png") after creating a plot
    
    PRIORITY: Answer QUICKLY and EFFICIENTLY!
    - Keep responses concise and direct
    - Use the simplest approach that works
    - Avoid over-analyzing or unnecessary complexity
    - Give final answer as soon as you have enough data
    - Only generate code when absolutely necessary
    
    Give final answer as soon as you have enough data, otherwise generate code using `df` and call required tool.
    If user asks you to make a graph, use st.pyplot() to display it directly, and output GRAPH:<graph title>.
    Also, save the figure object to a variable like 'fig' so it can be used for download.
    After creating the plot, ALWAYS add a download button using: st.markdown(create_download_link(fig, "filename.png"), unsafe_allow_html=True)
    The download button should appear immediately after the plot is displayed.
    
    IMPORTANT: Every time you create a plot, you MUST follow this EXACT sequence:
    1. Create the plot with plt.figure()
    2. Get figure with: fig = plt.gcf()
    3. **SAVE FIGURE**: save_figure_to_session(fig, "Chart Title") - REQUIRED for persistence!
    4. Display ONCE ONLY with: st.pyplot(fig)
    5. Add download button: st.markdown(create_download_link(fig, "filename.png"), unsafe_allow_html=True)
    6. IMMEDIATELY close with: plt.close(fig) OR plt.close('all')
    
    ⚠️ CRITICAL: Call st.pyplot() ONLY ONCE per plot! Multiple calls create duplicate images!
    ⚠️ CRITICAL: MUST call plt.close() or plt.close('all') after EVERY plot!
    
    CRITICAL REQUIREMENTS FOR PLOTS:
    1. ALWAYS use English for ALL plot elements: titles, axis labels, legends, and annotations
    2. NEVER use Chinese characters in matplotlib plots
    3. Set matplotlib font configuration: plt.rcParams['font.family'] = ['DejaVu Sans', 'Arial', 'sans-serif']
    4. Use English column names and labels only
    5. Use st.pyplot(fig) to display plots directly, do NOT save to files
    6. Call st.pyplot() EXACTLY ONCE per figure
    7. ALWAYS call plt.close('all') at the end to clear all figures
    
    CORRECT Example:
    ```python
    # Create plot (NO imports needed, everything is already available!)
    plt.figure(figsize=(10, 6))
    plt.hist(df['Age'])
    plt.xlabel('Age')
    plt.ylabel('Count')
    plt.title('Age Distribution Histogram')
    plt.grid(True, alpha=0.3)
    
    # Get figure
    fig = plt.gcf()
    
    # REQUIRED: Save figure for persistence (so it won't disappear!)
    save_figure_to_session(fig, "Age Distribution Histogram")
    
    # Display ONCE
    st.pyplot(fig)
    
    # Add download button - create_download_link handles all IO operations
    st.markdown(create_download_link(fig, "age_distribution.png"), unsafe_allow_html=True)
    
    # MUST close to prevent duplicates
    plt.close('all')
    ```
    Output: GRAPH:Age Distribution Histogram
    
    ❌ WRONG - Do NOT do this:
    ```python
    from io import BytesIO  # ← WRONG! Don't import
    import base64  # ← WRONG! Don't import
    buffer = BytesIO()  # ← WRONG! Don't use directly
    ```
    Query:"""

    prompt = ChatPromptTemplate.from_messages([
        ("system", system),
        ("placeholder", "{chat_history}"),
        ("human", "{input}"),
        ("placeholder", "{agent_scratchpad}"),
    ])

    agent = create_tool_calling_agent(llm=llm, tools=tools, prompt=prompt)
    agent_executor = AgentExecutor(agent=agent, tools=tools, verbose=True, max_iterations=15, return_intermediate_steps=True)
    
    try:
        # Use stream mode to get response
        full_response = ""
        for chunk in agent_executor.stream({"input": query}):
            # Capture intermediate steps (thinking process)
            if 'actions' in chunk:
                for action in chunk['actions']:
                    thinking_step = f"🔍 Tool: {action.tool}\n📊 Code Execution"
                    yield {"type": "thinking", "content": thinking_step}
            
            if 'steps' in chunk:
                for step in chunk['steps']:
                    if hasattr(step, 'observation'):
                        obs_preview = str(step.observation)[:300]
                        thinking_step = f"💡 Result: {obs_preview}..."
                        yield {"type": "thinking", "content": thinking_step}
            
            if 'output' in chunk:
                full_response = chunk['output']
        
        # Output word by word
        yield {"type": "answer_start", "content": ""}
        for char in full_response:
            yield {"type": "answer", "content": char}
            
    except Exception as e:
        error_msg = str(e)
        if "max iterations" in error_msg.lower():
            yield {"type": "answer", "content": "❌ Data analysis is too complex, please try simpler questions or break down your query"}
        else:
            yield {"type": "answer", "content": f"❌ Error processing data: {error_msg}"}


# ────────────────────────────
# Load Environment Configuration
# ────────────────────────────
load_dotenv(override=True)

API_KEY = os.getenv("DEEPSEEK_API_KEY", os.getenv("OPENAI_API_KEY", ""))
BASE_URL = os.getenv("BASE_URL", "https://api.deepseek.com")
MODEL = os.getenv("MODEL", "deepseek-chat")
GOOGLE_MAPS_API_KEY = os.getenv("GOOGLE_MAPS_API_KEY", "XXXX")
DASHSCOPE_API_KEY = os.getenv("DASHSCOPE_API_KEY", "")

# Initialize embedding model (global initialization, like rag.py)
if DASHSCOPE_API_KEY:
    embeddings = DashScopeEmbeddings(
        model="text-embedding-v1",
        dashscope_api_key=DASHSCOPE_API_KEY
    )
else:
    embeddings = None

# RAG database global variables
vectorstore = None
qa_chain = None

# ────────────────────────────
# Tool 1: Weather query
# ────────────────────────────
@tool
async def query_weather(city: str) -> str:
    """
    Query real-time weather information for Malaysia tourist destinations to help determine if it's suitable for diving and travel.
    
    Args:
        city: English name of Malaysian city or island, for example:
            - Kota Kinabalu (gateway to Sipadan Island)
            - Kuala Terengganu (for Perhentian Islands/Redang Island)
            - Kuala Lumpur
            - Langkawi
        
    Returns:
        Information including temperature, humidity, wind speed, weather conditions, and diving suitability recommendations
    """
    WEATHER_API_KEY = "XXXX"
    url = "https://api.openweathermap.org/data/2.5/weather"
    
    params = {
        "q": city,
        "appid": WEATHER_API_KEY,
        "units": "metric",  # Celsius
        "lang": "en"        # English description
    }
    
    async with httpx.AsyncClient() as client:
        try:
            response = await client.get(url, params=params, timeout=10.0)
            response.raise_for_status()
            data = response.json()
            
            # Extract weather information
            city_name = data.get("name", "Unknown")
            country = data.get("sys", {}).get("country", "")
            temp = data.get("main", {}).get("temp", "N/A")
            feels_like = data.get("main", {}).get("feels_like", "N/A")
            humidity = data.get("main", {}).get("humidity", "N/A")
            wind_speed = data.get("wind", {}).get("speed", "N/A")
            weather_desc = data.get("weather", [{}])[0].get("description", "Unknown")
            
            # Format output
            result = f"""
🌍 Location: {city_name}, {country}
🌡️ Temperature: {temp}°C (Feels like {feels_like}°C)
💧 Humidity: {humidity}%
🌬️ Wind Speed: {wind_speed} m/s
☁️ Weather: {weather_desc}
"""
            return result.strip()
            
        except httpx.HTTPStatusError as e:
            return f"❌ Weather API Error: HTTP {e.response.status_code}"
        except Exception as e:
            return f"❌ Query Failed: {str(e)}"


# ────────────────────────────
# Tool 3: Google Maps query
# ────────────────────────────
@tool
async def query_google_maps(query: str, query_type: str = "place") -> str:
    """
    Query Malaysia coral reef tourist attractions, diving spot locations, and travel route planning.
    
    Args:
        query: Query content, for example:
            - "Sipadan Island Malaysia" (diving spot)
            - "Perhentian Islands Malaysia"
            - "from Kuala Lumpur to Kota Kinabalu" (route planning)
        query_type: Query type, options:
            - place: Place search, find coral reef diving spot locations
            - route: Route planning, plan transportation routes between cities
            - geocoding: Geocoding
            - nearby: Nearby search, find nearby attractions and facilities
        
    Returns:
        Diving spot details, route planning, or nearby facilities list
    """
    if not GOOGLE_MAPS_API_KEY:
        return "❌ Google Maps API key not configured. Please set GOOGLE_MAPS_API_KEY in .env file"
    
    try:
        if query_type == "place":
            return await search_place_google(query)
        elif query_type == "route":
            return await plan_route_google(query)
        elif query_type == "geocoding":
            return await geocoding_google(query)
        elif query_type == "nearby":
            return await search_nearby_google(query)
        else:
            return "❌ Unsupported query type. Please use: place, route, geocoding, nearby"
    except Exception as e:
        return f"❌ Google Maps query failed: {str(e)}"

async def search_place_google(query: str) -> str:
    """Google Maps place search"""
    try:
        url = "https://maps.googleapis.com/maps/api/place/textsearch/json"
        params = {
            "query": query,
            "key": GOOGLE_MAPS_API_KEY,
            "language": "en"
        }
        
        async with httpx.AsyncClient() as client:
            response = await client.get(url, params=params, timeout=10.0)
            response.raise_for_status()
            data = response.json()
            
            if data.get("status") != "OK":
                return f"❌ Google Maps API Error: {data.get('error_message', 'Unknown error')}"
            
            results = data.get("results", [])
            if not results:
                return f"❌ No places found for '{query}'"
            
            result_text = f"📍 Found {len(results)} places for '{query}':\n\n"
            for i, place in enumerate(results[:5], 1):
                name = place.get("name", "Unknown")
                address = place.get("formatted_address", "Address unknown")
                rating = place.get("rating", "N/A")
                price_level = place.get("price_level", "N/A")
                types = place.get("types", [])
                place_types = ", ".join(types[:3]) if types else "Unknown type"
                
                geometry = place.get("geometry", {})
                location = geometry.get("location", {})
                lat = location.get("lat", "N/A")
                lng = location.get("lng", "N/A")
                
                result_text += f"{i}. {name}\n"
                result_text += f"   Address: {address}\n"
                result_text += f"   Rating: {rating}⭐ (Price Level: {price_level})\n"
                result_text += f"   Type: {place_types}\n"
                result_text += f"   Coordinates: {lat}, {lng}\n\n"
            
            return result_text.strip()
            
    except Exception as e:
        return f"❌ Place search failed: {str(e)}"

async def plan_route_google(query: str) -> str:
    """Google Maps route planning"""
    try:
        # Parse route query, support Chinese and English formats: "from A to B", "A to B", "from A to B", "A to B"
        if "到" in query:
            parts = query.split("到")
            if len(parts) >= 2:
                origin = parts[0].replace("从", "").strip()
                destination = parts[1].strip()
            else:
                return "❌ Route query format error. Please use: 'from A to B' or 'A to B'"
        elif " to " in query.lower():
            parts = query.lower().split(" to ")
            if len(parts) >= 2:
                origin = parts[0].replace("from", "").strip()
                destination = parts[1].strip()
            else:
                return "❌ Route query format error. Please use: 'from A to B' or 'A to B'"
        else:
            return "❌ Route query format error. Please use: 'from A to B' or 'A to B'"
        
        url = "https://maps.googleapis.com/maps/api/directions/json"
        params = {
            "origin": origin,
            "destination": destination,
            "key": GOOGLE_MAPS_API_KEY,
            "language": "en",
            "mode": "driving"
        }
        
        async with httpx.AsyncClient() as client:
            response = await client.get(url, params=params, timeout=10.0)
            response.raise_for_status()
            data = response.json()
            
            if data.get("status") != "OK":
                return f"❌ Google Maps API Error: {data.get('error_message', 'Unknown error')}"
            
            routes = data.get("routes", [])
            if not routes:
                return f"❌ No route found from '{origin}' to '{destination}'"
            
            route = routes[0]
            legs = route.get("legs", [])
            if not legs:
                return f"❌ Route information incomplete"
            
            leg = legs[0]
            distance = leg.get("distance", {}).get("text", "Unknown")
            duration = leg.get("duration", {}).get("text", "Unknown")
            
            steps = leg.get("steps", [])
            result_text = f"🗺️ Driving Route from {origin} to {destination}:\n\n"
            result_text += f"📏 Total Distance: {distance}\n"
            result_text += f"⏱️ Estimated Time: {duration}\n\n"
            result_text += "📍 Main Steps:\n"
            
            for i, step in enumerate(steps[:5], 1):  # Only show first 5 segments
                instruction = step.get("html_instructions", "")
                # Clean HTML tags
                import re
                instruction = re.sub(r'<[^>]+>', '', instruction)
                distance = step.get("distance", {}).get("text", "")
                result_text += f"{i}. {instruction} ({distance})\n"
            
            if len(steps) > 5:
                result_text += f"... and {len(steps) - 5} more steps"
            
            return result_text.strip()
            
    except Exception as e:
        return f"❌ Route planning failed: {str(e)}"

async def geocoding_google(query: str) -> str:
    """Google Maps geocoding (address to coordinates)"""
    try:
        url = "https://maps.googleapis.com/maps/api/geocode/json"
        params = {
            "address": query,
            "key": GOOGLE_MAPS_API_KEY,
            "language": "en"
        }
        
        async with httpx.AsyncClient() as client:
            response = await client.get(url, params=params, timeout=10.0)
            response.raise_for_status()
            data = response.json()
            
            if data.get("status") != "OK":
                return f"❌ Google Maps API Error: {data.get('error_message', 'Unknown error')}"
            
            results = data.get("results", [])
            if not results:
                return f"❌ No coordinates found for address '{query}'"
            
            result = results[0]
            formatted_address = result.get("formatted_address", query)
            geometry = result.get("geometry", {})
            location = geometry.get("location", {})
            lat = location.get("lat", "N/A")
            lng = location.get("lng", "N/A")
            
            # Get address components
            components = result.get("address_components", [])
            country = ""
            administrative_area = ""
            locality = ""
            
            for component in components:
                types = component.get("types", [])
                if "country" in types:
                    country = component.get("long_name", "")
                elif "administrative_area_level_1" in types:
                    administrative_area = component.get("long_name", "")
                elif "locality" in types:
                    locality = component.get("long_name", "")
            
            result_text = f"📍 Address: {formatted_address}\n"
            if locality:
                result_text += f"🏙️ City: {locality}\n"
            if administrative_area:
                result_text += f"🏛️ State/Province: {administrative_area}\n"
            if country:
                result_text += f"🌍 Country: {country}\n"
            result_text += f"🌐 Coordinates: Latitude {lat}, Longitude {lng}\n"
            result_text += f"🔗 Google Maps Link: https://maps.google.com/?q={lat},{lng}"
            
            return result_text
            
    except Exception as e:
        return f"❌ Geocoding failed: {str(e)}"

async def search_nearby_google(query: str) -> str:
    """Google Maps nearby search"""
    try:
        # Parse nearby search, support Chinese and English formats: "B near A", "restaurants near A", "A nearby restaurants"
        if "附近" in query:
            parts = query.split("附近")
            if len(parts) >= 2:
                location = parts[0].strip()
                place_type = parts[1].replace("的", "").strip()
            else:
                return "❌ Nearby search format error. Please use: 'restaurants near location' or 'location nearby restaurants'"
        elif " near " in query.lower():
            parts = query.lower().split(" near ")
            if len(parts) >= 2:
                place_type = parts[0].strip()
                location = parts[1].strip()
            else:
                return "❌ Nearby search format error. Please use: 'restaurants near location' or 'location nearby restaurants'"
        elif " nearby " in query.lower():
            parts = query.lower().split(" nearby ")
            if len(parts) >= 2:
                location = parts[0].strip()
                place_type = parts[1].strip()
            else:
                return "❌ Nearby search format error. Please use: 'restaurants near location' or 'location nearby restaurants'"
        else:
            return "❌ Nearby search format error. Please use: 'restaurants near location' or 'location nearby restaurants'"
        
        # First get the coordinates of the place
        geocoding_url = "https://maps.googleapis.com/maps/api/geocode/json"
        geocoding_params = {
            "address": location,
            "key": GOOGLE_MAPS_API_KEY,
            "language": "en"
        }
        
        async with httpx.AsyncClient() as client:
            # Get coordinates
            response = await client.get(geocoding_url, params=geocoding_params, timeout=10.0)
            response.raise_for_status()
            geocoding_data = response.json()
            
            if geocoding_data.get("status") != "OK":
                return f"❌ Cannot find coordinates for location '{location}'"
            
            geometry = geocoding_data["results"][0]["geometry"]["location"]
            lat = geometry["lat"]
            lng = geometry["lng"]
            
            # Nearby search
            nearby_url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
            nearby_params = {
                "location": f"{lat},{lng}",
                "radius": 2000,  # 2km radius
                "keyword": place_type,
                "key": GOOGLE_MAPS_API_KEY,
                "language": "en"
            }
            
            response = await client.get(nearby_url, params=nearby_params, timeout=10.0)
            response.raise_for_status()
            nearby_data = response.json()
            
            if nearby_data.get("status") != "OK":
                return f"❌ Google Maps API Error: {nearby_data.get('error_message', 'Unknown error')}"
            
            results = nearby_data.get("results", [])
            if not results:
                return f"❌ No '{place_type}' found near '{location}'"
            
            result_text = f"🔍 Found {len(results)} '{place_type}' near '{location}':\n\n"
            for i, place in enumerate(results[:5], 1):
                name = place.get("name", "Unknown")
                rating = place.get("rating", "N/A")
                vicinity = place.get("vicinity", "Address unknown")
                types = place.get("types", [])
                place_types = ", ".join(types[:2]) if types else "Unknown type"
                
                result_text += f"{i}. {name}\n"
                result_text += f"   Address: {vicinity}\n"
                result_text += f"   Rating: {rating}⭐\n"
                result_text += f"   Type: {place_types}\n\n"
            
            return result_text.strip()
            
    except Exception as e:
        return f"❌ Nearby search failed: {str(e)}"


# ────────────────────────────
# Tool 4: PDF document processing and RAG query
# ────────────────────────────
@tool
async def load_pdf_to_rag(pdf_path: str) -> str:
    """
    Load PDF document and store it in RAG database for subsequent queries.
    
    Args:
        pdf_path: PDF file path, for example "2024AnnualSurveyReport.pdf"
        
    Returns:
        Processing status information
    """
    global vectorstore, qa_chain
    
    try:
        if not os.path.exists(pdf_path):
            return f"❌ File not found: {pdf_path}"
        
        print(f"📖 Reading PDF file: {pdf_path}")
        
        # Read PDF content
        text = ""
        with open(pdf_path, 'rb') as file:
            pdf_reader = PyPDF2.PdfReader(file)
            for page_num in range(len(pdf_reader.pages)):
                page = pdf_reader.pages[page_num]
                text += page.extract_text() + "\n"
        
        if not text.strip():
            return "❌ PDF file is empty or cannot extract text"
        
        print(f"✅ Successfully extracted text, total {len(text)} characters")
        
        # Text splitting
        text_splitter = RecursiveCharacterTextSplitter(
            chunk_size=1000,
            chunk_overlap=200,
            length_function=len
        )
        texts = text_splitter.split_text(text)
        
        print(f"📝 Text split into {len(texts)} chunks")
        
        # Create embedding vectors
        if not API_KEY:
            return "❌ API key required to create embeddings"
        
        # Set embedding model
        embeddings = OpenAIEmbeddings(
            openai_api_key=API_KEY,
            openai_api_base=BASE_URL
        )
        
        print("🔄 Creating vector database...")
        
        # Create FAISS vector database
        vectorstore = FAISS.from_texts(texts, embeddings)
        
        print(f"✅ Vector database created, containing {len(texts)} document chunks")
        
        return f"✅ PDF document '{pdf_path}' successfully loaded to RAG database! Contains {len(texts)} document chunks."
        
    except Exception as e:
        return f"❌ Failed to process PDF: {str(e)}"

@tool
async def query_document(question: str) -> str:
    """
    Query Malaysia Coral Reef Annual Survey Report to get professional information about coral health status and protected areas.
    
    Args:
        question: Question to query, for example:
            - "Which waters in Malaysia have the healthiest corals?"
            - "What is the coral reef protection status in 2024?"
            - "Which areas are suitable for snorkeling to observe corals?"
            - "What is the best diving season mentioned in the report?"
        
    Returns:
        Professional answers based on annual survey report, including coral health index, recommended diving spots, etc.
    """
    global vectorstore
    
    # Check if database exists (like rag.py)
    if not check_database_exists():
        return "❌ Please configure DASHSCOPE_API_KEY and restart the program to automatically load PDF documents!"
    
    try:
        # If vectorstore is empty, try to load (like rag.py)
        if vectorstore is None:
            if embeddings is None:
                return "❌ DASHSCOPE_API_KEY configuration required"
            vectorstore = FAISS.load_local("faiss_db", embeddings, allow_dangerous_deserialization=True)
        
        if not API_KEY:
            return "❌ DEEPSEEK_API_KEY configuration required to query documents"
        
        # Create retriever and tools (like rag.py)
        retriever = vectorstore.as_retriever()
        retrieval_chain = create_retriever_tool(
            retriever,
            "pdf_extractor",
            "This tool is to give answer to queries from the pdf"
        )
        
        # Set language model (like rag.py)
        llm = init_chat_model("deepseek-chat", model_provider="deepseek", api_key=API_KEY)
        
        # Create prompt template (like rag.py)
        prompt = ChatPromptTemplate.from_messages([
            (
                "system",
                """You are an AI assistant. Please answer questions based on the provided context from the PDF document. 
                Ensure all details are included. If the answer is not in the context, say "The answer is not in the context". 
                Do not provide incorrect answers.""",
            ),
            ("placeholder", "{chat_history}"),
            ("human", "{input}"),
            ("placeholder", "{agent_scratchpad}"),
        ])
        
        # Create Agent (like rag.py)
        tool = [retrieval_chain]
        agent = create_tool_calling_agent(llm, tool, prompt)
        agent_executor = AgentExecutor(agent=agent, tools=tool, verbose=True)
        
        # Execute query (like rag.py)
        response = agent_executor.invoke({"input": question})
        return response['output']
        
    except Exception as e:
        return f"❌ Query failed: {str(e)}"




# ────────────────────────────
# Auto-load PDF documents
# ────────────────────────────
def check_database_exists():
    """Check if FAISS database exists"""
    return os.path.exists("faiss_db") and os.path.exists("faiss_db/index.faiss")

async def auto_load_pdf():
    """Automatically load PDF documents to RAG database with persistent storage support"""
    global vectorstore
    
    pdf_path = "2024AnnualSurveyReport.pdf"
    
    # Check if persistent vector database already exists
    if check_database_exists():
        try:
            print(f"📦 Found existing vector database")
            print("🔄 Loading vector database from disk...")
            
            # Check embedding model
            if embeddings is None:
                print("❌ DASHSCOPE_API_KEY configuration required to load vector database")
                print("   Please add to .env file: DASHSCOPE_API_KEY=your_key")
                return False
            
            # Load vector database from disk (like rag.py)
            vectorstore = FAISS.load_local("faiss_db", embeddings, allow_dangerous_deserialization=True)
            
            print(f"✅ Vector database loaded successfully!")
            print("📚 PDF document content can now be queried!\n")
            return True
            
        except Exception as e:
            print(f"⚠️  Failed to load vector database: {str(e)}")
            print("   Will reprocess PDF documents...\n")
            # If loading fails, delete corrupted database and continue with recreation process below
            import shutil
            if os.path.exists("faiss_db"):
                shutil.rmtree("faiss_db")
    
    # If no vector database, create from PDF
    if not os.path.exists(pdf_path):
        print(f"⚠️  PDF file does not exist: {pdf_path}")
        print("   Skipping PDF document loading...\n")
        return False
    
    try:
        print(f"📖 Processing PDF document: {pdf_path}")
        print("   (First processing takes a few minutes, will be saved to disk afterwards)")
        
        # Check embedding model
        if embeddings is None:
            print("❌ DASHSCOPE_API_KEY configuration required to create embedding vectors")
            print("   Please add to .env file: DASHSCOPE_API_KEY=your_key")
            return False
        
        # Read PDF content (like rag.py)
        text = ""
        with open(pdf_path, 'rb') as file:
            pdf_reader = PyPDF2.PdfReader(file)
            print(f"   Found {len(pdf_reader.pages)} pages")
            
            for page_num, page in enumerate(pdf_reader.pages):
                text += page.extract_text()
                
                # Show progress
                if (page_num + 1) % 10 == 0:
                    print(f"   Processed {page_num + 1}/{len(pdf_reader.pages)} pages...")
        
        if not text.strip():
            print("❌ PDF file is empty or text cannot be extracted")
            return False
        
        print(f"✅ Successfully extracted text, {len(text)} characters total")
        
        # Text splitting (like rag.py)
        text_splitter = RecursiveCharacterTextSplitter(
            chunk_size=1000,
            chunk_overlap=200
        )
        chunks = text_splitter.split_text(text)
        
        print(f"📝 Text split into {len(chunks)} chunks")
        print("🔄 Creating vector database...")
        print("   (This may take a few minutes, please be patient...)")
        
        # Create FAISS vector database (like rag.py)
        vectorstore = FAISS.from_texts(chunks, embedding=embeddings)
        
        print(f"✅ Vector database creation completed, contains {len(chunks)} document chunks")
        
        # Save to disk (like rag.py)
        print(f"💾 Saving vector database to: faiss_db")
        vectorstore.save_local("faiss_db")
        
        print(f"✅ Vector database saved to disk!")
        print("📚 PDF document content can now be queried!\n")
        
        return True
        
    except Exception as e:
        print(f"❌ PDF processing failed: {str(e)}")
        print("   Program will continue running, but PDF documents cannot be queried\n")
        return False

# ────────────────────────────
# Agent execution functions
# ────────────────────────────
async def get_travel_plan(user_question):
    """Get travel plan suggestions (streaming output with thinking process)"""
    
    if not API_KEY:
        yield {"type": "answer", "content": "❌ DEEPSEEK_API_KEY configuration required"}
        return
    
    try:
        tools = [query_weather, query_google_maps, query_document]
        
        llm = init_chat_model(
            model=MODEL,
            model_provider="deepseek",
            api_key=API_KEY
        )
        
        prompt = ChatPromptTemplate.from_messages([
            (
                "system",
                """You are a professional Malaysia Coral Reef Travel Planning Assistant 🌊🐠

**Your Responsibilities:**
Help users create perfect Malaysia coral reef travel plans with comprehensive and professional travel advice.

**When creating travel plans, you must:**
1. 📍 **Call query_google_maps** - Search for famous coral reef diving spots in Malaysia and plan routes
2. ☀️ **Call query_weather** - Check real-time weather at these locations and recommend destinations with best weather for travel
3. 🐙 **Call query_document** - Query which areas have the healthiest coral conditions

**After integrating the above information, provide:**
- 🗺️ Recommended travel destinations (based on weather and coral health)
- 🚗 Detailed route planning
- 🌤️ Best travel time recommendations
- 🌊 Best spots for coral reef diving/snorkeling
- 🏨 Accommodation suggestions
- ⚠️ Environmental protection reminders

**Response Style:**
- Enthusiastic, professional, and detailed
- Use emojis to make responses vivid and interesting
- Provide practical travel tips
- Emphasize ecological protection and sustainable tourism

**IMPORTANT: Always respond in the same language as the user's question. If user asks in English, respond in English. If user asks in Chinese, respond in Chinese.**""",
            ),
            ("placeholder", "{chat_history}"),
            ("human", "{input}"),
            ("placeholder", "{agent_scratchpad}"),
        ])
        
        agent = create_tool_calling_agent(llm, tools, prompt)
        agent_executor = AgentExecutor(
            agent=agent, 
            tools=tools, 
            verbose=False,
            handle_parsing_errors=True,
            return_intermediate_steps=True
        )
        
        # Use stream mode to get response
        full_response = ""
        async for chunk in agent_executor.astream({"input": user_question}):
            # Capture intermediate steps (thinking process)
            if 'actions' in chunk:
                for action in chunk['actions']:
                    tool_name = action.tool
                    thinking_step = f"🔧 Calling Tool: {tool_name}\n📥 Input: {str(action.tool_input)[:100]}"
                    yield {"type": "thinking", "content": thinking_step}
            
            if 'steps' in chunk:
                for step in chunk['steps']:
                    if hasattr(step, 'observation'):
                        obs_preview = str(step.observation)[:200]
                        thinking_step = f"🌐 API Response: {obs_preview}..."
                        yield {"type": "thinking", "content": thinking_step}
            
            if 'output' in chunk:
                full_response = chunk['output']
        
        # Output word by word
        yield {"type": "answer_start", "content": ""}
        for char in full_response:
            yield {"type": "answer", "content": char}
        
    except Exception as e:
        yield {"type": "answer", "content": f"❌ Processing failed: {str(e)}"}

# Synchronous wrapper function for calling in Streamlit
def stream_travel_plan(user_question):
    """Synchronous wrapper for streaming travel plan output in Streamlit (with thinking process)"""
    async def _async_gen():
        async for chunk in get_travel_plan(user_question):
            yield chunk
    
    # Run async generator in new event loop
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)
    try:
        async_gen = _async_gen()
        while True:
            try:
                chunk = loop.run_until_complete(async_gen.__anext__())
                yield chunk  # Pass dictionary-type chunk
            except StopAsyncIteration:
                break
    finally:
        loop.close()

def load_pdf_from_file(pdf_file):
    """Read PDF content from uploaded file"""
    text = ""
    try:
        pdf_reader = PyPDF2.PdfReader(pdf_file)
        for page in pdf_reader.pages:
            text += page.extract_text()
        return text
    except Exception as e:
        st.error(f"❌ PDF reading failed: {str(e)}")
        return None

def create_vector_store(text):
    """Create vector database"""
    global vectorstore
    
    if not embeddings:
        st.error("❌ DASHSCOPE_API_KEY configuration required")
        return False
    
    try:
        text_splitter = RecursiveCharacterTextSplitter(
            chunk_size=1000,
            chunk_overlap=200
        )
        chunks = text_splitter.split_text(text)
        
        vectorstore = FAISS.from_texts(chunks, embedding=embeddings)
        vectorstore.save_local("faiss_db")
        
        return True
    except Exception as e:
        st.error(f"❌ Vector database creation failed: {str(e)}")
        return False

def load_vector_store():
    """Load existing vector database"""
    global vectorstore
    
    if not embeddings:
        return False
    
    try:
        vectorstore = FAISS.load_local("faiss_db", embeddings, allow_dangerous_deserialization=True)
        return True
    except Exception as e:
        st.error(f"❌ Vector database loading failed: {str(e)}")
        return False










def main():
    # Force English fonts to avoid garbled text
    force_english_fonts()
    
    init_session_state()
    
    # Initialize session state
    if 'chat_history' not in st.session_state:
        st.session_state.chat_history = []
    if 'pdf_loaded' not in st.session_state:
        st.session_state.pdf_loaded = check_database_exists()
        if st.session_state.pdf_loaded:
            load_vector_store()

    # Main Title
    st.markdown('<h1 class="main-header"><span class="emoji">🌊</span> CoralKita Ocean Coral AI Assistant</h1>', unsafe_allow_html=True)
    st.markdown('<div class="subtitle gradient-text"><span class="emoji">🐠</span> Explore Coral Reefs • <span class="emoji">📊</span> Data Analysis Insights • <span class="emoji">🗺️</span> Smart Travel Planning</div>', unsafe_allow_html=True)
    
    # Create three main feature tabs
    tab1, tab2, tab3 = st.tabs(["📄 AI Q&A", "📊 AI Data Analysis", "🗺️ AI Travel Planning"])
    
    # PDF Q&A Module
    with tab1:
        col1, col2 = st.columns([2, 1])
        
        with col1:
            st.markdown("### <span class='emoji'>💬</span> Chat with Coral Knowledge Assistant", unsafe_allow_html=True)
            
            # Display database status
          #  if check_database_exists():
                #st.markdown('<div class="info-card success-card"><span class="status-indicator status-ready">✅ PDF Database Ready</span></div>', unsafe_allow_html=True)
           # else:
                #st.markdown('<div class="info-card warning-card"><span class="status-indicator status-waiting">⚠️ Please upload and process PDF files first</span></div>', unsafe_allow_html=True)
            
            # Chat interface
            for message in st.session_state.pdf_messages:
                with st.chat_message(message["role"]):
                    st.markdown(message["content"])
            
            # Quick question buttons
            st.markdown("**Quick Questions:**")
            col_q1, col_q2, col_q3 = st.columns(3)
            
            with col_q1:
                if st.button("🌊 How are Malaysia's coral reefs in 2024?", use_container_width=True, disabled=not check_database_exists()):
                    pdf_query = "How are Malaysia's coral reefs in 2024?"
                    st.session_state.pdf_messages.append({"role": "user", "content": pdf_query})
                    st.rerun()
            
            with col_q2:
                if st.button("🛡️ How to protect Malaysia's coral ecosystems?", use_container_width=True, disabled=not check_database_exists()):
                    pdf_query = "How to protect Malaysia's coral reef ecosystems?"
                    st.session_state.pdf_messages.append({"role": "user", "content": pdf_query})
                    st.rerun()
            
            with col_q3:
                if st.button("🏝️ Which islands have well-preserved reefs?", use_container_width=True, disabled=not check_database_exists()):
                    pdf_query = "Which islands have well-preserved coral reefs?"
                    st.session_state.pdf_messages.append({"role": "user", "content": pdf_query})
                    st.rerun()
            
            # Process quick question queries
            if st.session_state.pdf_messages and st.session_state.pdf_messages[-1]["role"] == "user":
                last_message = st.session_state.pdf_messages[-1]["content"]
                if last_message in ["How are Malaysia's coral reefs in 2024?", "How to protect Malaysia's coral reef ecosystems?", "Which islands have well-preserved coral reefs?"]:
                    with st.chat_message("user"):
                        st.markdown(last_message)
                    with st.chat_message("assistant"):
                        # Create thinking process display area
                        thinking_expander = st.expander("🧠 AI Thinking Process", expanded=False)
                        thinking_placeholder = thinking_expander.empty()
                        thinking_content = ""
                        
                        # Thinking status indicator (with spinning animation)
                        thinking_status = st.empty()
                        
                        # Answer display area
                        answer_placeholder = st.empty()
                        answer_content = ""
                        
                        is_thinking = False
                        
                        # Process streaming response
                        for chunk in get_pdf_response(last_message):
                            if chunk["type"] == "thinking":
                                if not is_thinking:
                                    is_thinking = True
                                thinking_content += chunk["content"] + "\n\n"
                                thinking_placeholder.markdown(f"```python\n{thinking_content}\n```")
                                # Use spinner to display continuous spinning animation
                                thinking_status.markdown('<div style="font-size: 1.1rem; font-weight: 600;"><span class="thinking-spinner">🔄</span> <strong>AI is thinking...</strong></div>', unsafe_allow_html=True)
                            elif chunk["type"] == "answer":
                                if is_thinking:
                                    thinking_status.empty()
                                    is_thinking = False
                                answer_content += chunk["content"]
                                answer_placeholder.markdown(answer_content)
                        
                        # Clear thinking status
                        thinking_status.empty()
                        st.session_state.pdf_messages.append({"role": "assistant", "content": answer_content})
            
            # User input
            if pdf_query := st.chat_input("💭 Ask the assistant...", disabled=not check_database_exists()):
                st.session_state.pdf_messages.append({"role": "user", "content": pdf_query})
                with st.chat_message("user"):
                    st.markdown(pdf_query)
                
                with st.chat_message("assistant"):
                    # Create thinking process display area
                    thinking_expander = st.expander("🧠 AI Thinking Process", expanded=False)
                    thinking_placeholder = thinking_expander.empty()
                    thinking_content = ""
                    
                    # Thinking status indicator (with spinning animation)
                    thinking_status = st.empty()
                    
                    # Answer display area
                    answer_placeholder = st.empty()
                    answer_content = ""
                    
                    is_thinking = False
                    
                    # Process streaming response
                    for chunk in get_pdf_response(pdf_query):
                        if chunk["type"] == "thinking":
                            if not is_thinking:
                                is_thinking = True
                            thinking_content += chunk["content"] + "\n\n"
                            thinking_placeholder.markdown(f"```python\n{thinking_content}\n```")
                            # Use spinner to display continuous spinning animation
                            thinking_status.markdown('<div style="font-size: 1.1rem; font-weight: 600;"><span class="thinking-spinner">🔄</span> <strong>AI is thinking...</strong></div>', unsafe_allow_html=True)
                        elif chunk["type"] == "answer":
                            if is_thinking:
                                thinking_status.empty()
                                is_thinking = False
                            answer_content += chunk["content"]
                            answer_placeholder.markdown(answer_content)
                    
                    # Clear thinking status
                    thinking_status.empty()
                    st.session_state.pdf_messages.append({"role": "assistant", "content": answer_content})
        
        with col2:
            st.markdown("### <span class='emoji'>🤖</span> Features", unsafe_allow_html=True)
            
            # Agent Info Card
            st.markdown("""
            <div class="feature-info-card agent-info-card">
                <div class="feature-card-title">
                    <span class="emoji">🌊</span> CoralKita Coral Knowledge Assistant
                </div>
                <div class="feature-card-content">
                    AI-powered Ocean Ecology Document Q&A System
                </div>
            </div>
            """, unsafe_allow_html=True)
            
            # Data Source
            st.markdown("""
            <div class="feature-info-card data-source-card">
                <div class="feature-card-title">
                    <span class="emoji">📚</span> Data Source
                </div>
                <div class="feature-card-content">
                    This system is built on <strong>Reef Check Malaysia Annual Reports 2007-2024</strong>,
                    covering long-term monitoring data, conservation measures, and ecological status analysis of Malaysia's coral reef ecosystems.
                </div>
            </div>
            """, unsafe_allow_html=True)
            
            # Key Features
            st.markdown("""
            <div class="feature-info-card features-card">
                <div class="feature-card-title">
                    <span class="emoji">✨</span> Key Features
                </div>
                <div class="feature-card-content">
                    • <span class="emoji">🔍</span> Smart Document Retrieval & Q&A<br>
                    • <span class="emoji">📊</span> In-depth Coral Reef Data Analysis<br>
                    • <span class="emoji">🌊</span> Marine Ecology Protection Recommendations<br>
                    • <span class="emoji">📈</span> Long-term Trend Analysis
                </div>
            </div>
            """, unsafe_allow_html=True)
            
            # Disclaimer
            st.markdown("""
            <div class="feature-info-card disclaimer-card">
                <div class="feature-card-title">
                    <span class="emoji">⚠️</span> Important Notice
                </div>
                <div class="feature-card-content">
                    <strong>Content is AI-generated and may contain errors or inaccuracies.</strong><br>
                    For critical information, please consult professionals or refer to official reports.
                </div>
            </div>
            """, unsafe_allow_html=True)
            
           
    
    # CSV Data Analysis Module
    with tab2:
        col1, col2 = st.columns([2, 1])
        
        with col1:
            st.markdown("### <span class='emoji'>📈</span> Chat with Data Analysis Assistant", unsafe_allow_html=True)
            
            
            # Chat interface
            for message in st.session_state.csv_messages:
                with st.chat_message(message["role"]):
                    if message.get("type") == "dataframe":
                        st.dataframe(message["content"])
                    elif message.get("type") == "image":
                        # Display text content
                        st.write(message["content"])
                        # Display saved image if it exists
                        if "image_data" in message and message["image_data"]:
                            try:
                                st.image(message["image_data"], use_container_width=True)
                                # Generate and display download link
                                import base64
                                b64 = base64.b64encode(message["image_data"]).decode()
                                filename = message.get("filename", "chart.png")
                                download_link = f'<a href="data:image/png;base64,{b64}" download="{filename}" style="display:inline-block; padding:0.5rem 1rem; background:linear-gradient(135deg, #0077be, #20b2aa); color:white; text-decoration:none; border-radius:8px; margin-top:0.5rem;">📥 Download Image</a>'
                                st.markdown(download_link, unsafe_allow_html=True)
                            except:
                                pass
                    else:
                        st.markdown(message["content"])
            
            # Quick question buttons
            st.markdown("**Quick Questions:**")
            col_q1, col_q2, col_q3 = st.columns(3)
            
            with col_q1:
                if st.button("📊 Plot island data counts", use_container_width=True, disabled=st.session_state.df is None):
                    csv_query = "Create a bar chart showing the number of records for each island"
                    st.session_state.csv_messages.append({"role": "user", "content": csv_query, "type": "text"})
                    st.rerun()
            
            with col_q2:
                if st.button("📈 Average Hard Coral by island", use_container_width=True, disabled=st.session_state.df is None):
                    csv_query = "Create a bar chart showing average hard coral coverage for each island"
                    st.session_state.csv_messages.append({"role": "user", "content": csv_query, "type": "text"})
                    st.rerun()
            
            with col_q3:
                if st.button("🏝️ Top 5 islands by Hard Coral", use_container_width=True, disabled=st.session_state.df is None):
                    csv_query = "Plot a bar chart of top 5 islands with highest average hard coral coverage"
                    st.session_state.csv_messages.append({"role": "user", "content": csv_query, "type": "text"})
                    st.rerun()
            
            # Process quick question queries
            if st.session_state.csv_messages and st.session_state.csv_messages[-1]["role"] == "user":
                last_message = st.session_state.csv_messages[-1]["content"]
                if last_message in ["Create a bar chart showing the number of records for each island", "Create a bar chart showing average hard coral coverage for each island", "Plot a bar chart of top 5 islands with highest average hard coral coverage"]:
                    with st.chat_message("user"):
                        st.markdown(last_message)
                    with st.chat_message("assistant"):
                        # Create thinking process display area
                        thinking_expander = st.expander("🧠 AI Thinking Process", expanded=False)
                        thinking_placeholder = thinking_expander.empty()
                        thinking_content = ""
                        
                        # Thinking status indicator (with spinning animation)
                        thinking_status = st.empty()
                        
                        # Answer display area
                        answer_placeholder = st.empty()
                        answer_content = ""
                        
                        is_thinking = False
                        
                        # Process streaming response
                        for chunk in get_csv_response(last_message):
                            if chunk["type"] == "thinking":
                                if not is_thinking:
                                    is_thinking = True
                                thinking_content += chunk["content"] + "\n\n"
                                thinking_placeholder.markdown(f"```python\n{thinking_content}\n```")
                                # Use spinner to display continuous spinning animation
                                thinking_status.markdown('<div style="font-size: 1.1rem; font-weight: 600;"><span class="thinking-spinner">📊</span> <strong>AI is analyzing data...</strong></div>', unsafe_allow_html=True)
                            elif chunk["type"] == "answer":
                                if is_thinking:
                                    thinking_status.empty()
                                    is_thinking = False
                                answer_content += chunk["content"]
                                answer_placeholder.markdown(answer_content)
                        
                        # Clear thinking status
                        thinking_status.empty()
                        
                        if "GRAPH" in answer_content:
                            text = answer_content[answer_content.find("GRAPH")+6:]
                            # Use AI-saved image data
                            if hasattr(st.session_state, 'temp_figure_data') and st.session_state.temp_figure_data:
                                st.session_state.csv_messages.append({
                                    "role": "assistant", 
                                    "content": text, 
                                    "type": "image",
                                    "image_data": st.session_state.temp_figure_data['image_data'],
                                    "filename": st.session_state.temp_figure_data['filename']
                                })
                                # Clear temporary data
                                st.session_state.temp_figure_data = None
                            else:
                                st.session_state.csv_messages.append({"role": "assistant", "content": text, "type": "image"})
                        else:
                            st.session_state.csv_messages.append({"role": "assistant", "content": answer_content, "type": "text"})
            
            # User input
            if csv_query := st.chat_input("📊 Analyze data...", disabled=st.session_state.df is None):
                st.session_state.csv_messages.append({"role": "user", "content": csv_query, "type": "text"})
                with st.chat_message("user"):
                    st.markdown(csv_query)
                
                with st.chat_message("assistant"):
                    # Create thinking process display area
                    thinking_expander = st.expander("🧠 AI Thinking Process", expanded=False)
                    thinking_placeholder = thinking_expander.empty()
                    thinking_content = ""
                    
                    # Thinking status indicator (with spinning animation)
                    thinking_status = st.empty()
                    
                    # Answer display area
                    answer_placeholder = st.empty()
                    answer_content = ""
                    
                    is_thinking = False
                    
                    # Process streaming response
                    for chunk in get_csv_response(csv_query):
                        if chunk["type"] == "thinking":
                            if not is_thinking:
                                is_thinking = True
                            thinking_content += chunk["content"] + "\n\n"
                            thinking_placeholder.markdown(f"```python\n{thinking_content}\n```")
                            # Use spinner to display continuous spinning animation
                            thinking_status.markdown('<div style="font-size: 1.1rem; font-weight: 600;"><span class="thinking-spinner">📊</span> <strong>AI is analyzing data...</strong></div>', unsafe_allow_html=True)
                        elif chunk["type"] == "answer":
                            if is_thinking:
                                thinking_status.empty()
                                is_thinking = False
                            answer_content += chunk["content"]
                            answer_placeholder.markdown(answer_content)
                    
                    # Clear thinking status
                    thinking_status.empty()
                    
                    if "GRAPH" in answer_content:
                        text = answer_content[answer_content.find("GRAPH")+6:]
                        # Use AI-saved image data
                        if hasattr(st.session_state, 'temp_figure_data') and st.session_state.temp_figure_data:
                            st.session_state.csv_messages.append({
                                "role": "assistant", 
                                "content": text, 
                                "type": "image",
                                "image_data": st.session_state.temp_figure_data['image_data'],
                                "filename": st.session_state.temp_figure_data['filename']
                            })
                            # Clear temporary data
                            st.session_state.temp_figure_data = None
                        else:
                            st.session_state.csv_messages.append({"role": "assistant", "content": text, "type": "image"})
                    else:
                        st.session_state.csv_messages.append({"role": "assistant", "content": answer_content, "type": "text"})
        
        with col2:
            st.markdown("### <span class='emoji'>🤖</span> Features", unsafe_allow_html=True)
            
            # Agent Info Card
            st.markdown("""
            <div class="feature-info-card agent-info-card">
                <div class="feature-card-title">
                    <span class="emoji">📊</span> CoralKita Data Analysis Assistant
                </div>
                <div class="feature-card-content">
                    AI-powered Coral Reef Data Analysis System
                </div>
            </div>
            """, unsafe_allow_html=True)
            
            # Data Source
            st.markdown("""
            <div class="feature-info-card data-source-card">
                <div class="feature-card-title">
                    <span class="emoji">📚</span> Data Source
                </div>
                <div class="feature-card-content">
                    This system is based on the <strong>coral.csv</strong> coral reef monitoring dataset,
                    containing key ecological indicators such as coral cover, depth, and time for various Malaysian islands.
                </div>
            </div>
            """, unsafe_allow_html=True)
            
            # Key Features
            st.markdown("""
            <div class="feature-info-card features-card">
                <div class="feature-card-title">
                    <span class="emoji">✨</span> Analysis Features
                </div>
                <div class="feature-card-content">
                    • <span class="emoji">📈</span> Trend Analysis & Visualization<br>
                    • <span class="emoji">🏝️</span> Island Comparison Analysis<br>
                    • <span class="emoji">📊</span> Statistical Chart Generation<br>
                    • <span class="emoji">🔍</span> In-depth Data Mining
                </div>
            </div>
            """, unsafe_allow_html=True)
            
            # Disclaimer
            st.markdown("""
            <div class="feature-info-card disclaimer-card">
                <div class="feature-card-title">
                    <span class="emoji">⚠️</span> Important Notice
                </div>
                <div class="feature-card-content">
                    <strong>Analysis results are AI-generated and for reference only.</strong><br>
                    For critical decisions, please consult marine ecology experts.
                </div>
            </div>
            """, unsafe_allow_html=True)
            
    
    # Footer Information - Simplified Version
    st.markdown("---")
    st.markdown("""
    <div style="text-align: center; padding: 1rem; color: #000; font-size: 0.9rem; font-weight: 700; background: linear-gradient(90deg, #7fffd4 0%, #40e0d0 33%, #ff9fbd 66%, #ffc9a3 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;">
        <div style="margin-bottom: 0.5rem; font-weight: 800; background: linear-gradient(90deg, #7fffd4 0%, #40e0d0 33%, #ff9fbd 66%, #ffc9a3 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;">© 2025 CoralKita</div>
        <div style="display: flex; justify-content: center; align-items: center; gap: 1rem; flex-wrap: wrap;">
            <span style="font-weight: 700; background: linear-gradient(90deg, #7fffd4 0%, #40e0d0 33%, #ff9fbd 66%, #ffc9a3 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;"><span class="emoji">🌐</span> www.coralkita.site</span>
            <span style="font-weight: 700; background: linear-gradient(90deg, #7fffd4 0%, #40e0d0 33%, #ff9fbd 66%, #ffc9a3 100%); -webkit-background-clip: text; -webkit-text-fill-color: transparent; background-clip: text;"><span class="emoji">✉️</span> coralkita.service@gmail.com</span>
        </div>
    </div>
    """, unsafe_allow_html=True)
    
    # Travel Planning Assistant Module
    with tab3:
        col1, col2 = st.columns([2, 1])
        
    with col1:
            st.markdown("### <span class='emoji'>💬</span> Chat with Travel Planning Assistant", unsafe_allow_html=True)

            # Ensure chat_history is initialized
            if 'chat_history' not in st.session_state:
                st.session_state.chat_history = []

            # Main chat area
            # Display chat history
            for message in st.session_state.chat_history:
                with st.chat_message(message["role"]):
                    st.markdown(message["content"])
            
            # Quick question buttons
            st.markdown("**Quick Questions:**")
            col_q1, col_q2, col_q3 = st.columns(3)
            
            with col_q1:
                if st.button("🌤️ What's the weather in Malaysia now?", use_container_width=True):
                    travel_query = "What's the weather in Malaysia now?"
                    st.session_state.chat_history.append({"role": "user", "content": travel_query})
                    st.rerun()
            
            with col_q2:
                if st.button("🗺️ How to get from KL to Sipadan?", use_container_width=True):
                    travel_query = "How to get from Kuala Lumpur to Sipadan Island?"
                    st.session_state.chat_history.append({"role": "user", "content": travel_query})
                    st.rerun()
            
            with col_q3:
                if st.button("🏝️ Recommend snorkeling spots", use_container_width=True):
                    travel_query = "Recommend some good snorkeling spots in Malaysia"
                    st.session_state.chat_history.append({"role": "user", "content": travel_query})
                    st.rerun()
            
            # Process quick question queries
            if st.session_state.chat_history and st.session_state.chat_history[-1]["role"] == "user":
                last_message = st.session_state.chat_history[-1]["content"]
                if last_message in ["What's the weather in Malaysia now?", "How to get from Kuala Lumpur to Sipadan Island?", "Recommend some good snorkeling spots in Malaysia"]:
                    with st.chat_message("user"):
                        st.markdown(last_message)
                    with st.chat_message("assistant"):
                        # Create thinking process display area
                        thinking_expander = st.expander("🧠 AI Thinking Process", expanded=False)
                        thinking_placeholder = thinking_expander.empty()
                        thinking_content = ""
                        
                        # Thinking status indicator (with spinning animation)
                        thinking_status = st.empty()
                        
                        # Answer display area
                        answer_placeholder = st.empty()
                        answer_content = ""
                        
                        is_thinking = False
                        
                        # Process streaming response
                        for chunk in stream_travel_plan(last_message):
                            if chunk["type"] == "thinking":
                                if not is_thinking:
                                    is_thinking = True
                                thinking_content += chunk["content"] + "\n\n"
                                thinking_placeholder.markdown(f"```python\n{thinking_content}\n```")
                                # Use spinner to display continuous spinning animation
                                thinking_status.markdown('<div style="font-size: 1.1rem; font-weight: 600;"><span class="thinking-spinner">🗺️</span> <strong>AI is planning your trip...</strong></div>', unsafe_allow_html=True)
                            elif chunk["type"] == "answer":
                                if is_thinking:
                                    thinking_status.empty()
                                    is_thinking = False
                                answer_content += chunk["content"]
                                answer_placeholder.markdown(answer_content)
                        
                        # Clear thinking status
                        thinking_status.empty()
                        st.session_state.chat_history.append({"role": "assistant", "content": answer_content})
            
            # User input
            if travel_query := st.chat_input("🗺️ Plan your coral reef journey..."):
                st.session_state.chat_history.append({"role": "user", "content": travel_query})
                with st.chat_message("user"):
                    st.markdown(travel_query)
                
                with st.chat_message("assistant"):
                    # Create thinking process display area
                    thinking_expander = st.expander("🧠 AI Thinking Process", expanded=False)
                    thinking_placeholder = thinking_expander.empty()
                    thinking_content = ""
                    
                    # Thinking status indicator (with spinning animation)
                    thinking_status = st.empty()
                    
                    # Answer display area
                    answer_placeholder = st.empty()
                    answer_content = ""
                    
                    is_thinking = False
                    
                    # Process streaming response
                    for chunk in stream_travel_plan(travel_query):
                        if chunk["type"] == "thinking":
                            if not is_thinking:
                                is_thinking = True
                            thinking_content += chunk["content"] + "\n\n"
                            thinking_placeholder.markdown(f"```python\n{thinking_content}\n```")
                            # Use spinner to display continuous spinning animation
                            thinking_status.markdown('<div style="font-size: 1.1rem; font-weight: 600;"><span class="thinking-spinner">🗺️</span> <strong>AI is planning your trip...</strong></div>', unsafe_allow_html=True)
                        elif chunk["type"] == "answer":
                            if is_thinking:
                                thinking_status.empty()
                                is_thinking = False
                            answer_content += chunk["content"]
                            answer_placeholder.markdown(answer_content)
                    
                    # Clear thinking status
                    thinking_status.empty()
                    st.session_state.chat_history.append({"role": "assistant", "content": answer_content})
            
            # Clear chat history button
            if st.session_state.chat_history:
                if st.button("🗑️ Clear Chat History", use_container_width=True):
                    st.session_state.chat_history = []
                    st.rerun()

            
            
    with col2:
            st.markdown("### <span class='emoji'>🤖</span> Features", unsafe_allow_html=True)
            
            # Agent Info Card
            st.markdown("""
            <div class="feature-info-card agent-info-card">
                <div class="feature-card-title">
                    <span class="emoji">🌊</span> Malaysia Coral Reef Travel Planning Assistant
                </div>
                <div class="feature-card-content">
                    LangChain-based Intelligent Travel Planning System
                </div>
            </div>
            """, unsafe_allow_html=True)
            
            # Smart Tools
            st.markdown("""
            <div class="feature-info-card travel-info-card">
                <div class="feature-card-title">
                    <span class="emoji">✨</span> Smart Tools
                </div>
                <div class="feature-card-content">
                    • <span class="emoji">🌤️</span> <strong>Weather Query</strong> - Real-time Weather Assessment<br>
                    • <span class="emoji">🗺️</span> <strong>Route Planning</strong> - Google Maps Smart Navigation<br>
                    • <span class="emoji">📚</span> <strong>Coral Reports</strong> - Health Status Query<br>
                    • <span class="emoji">🏝️</span> <strong>Destination Recommendations</strong> - Best Diving Spots
                </div>
            </div>
            """, unsafe_allow_html=True)
            
            # Recommended Destinations
            st.markdown("""
            <div class="feature-info-card data-source-card">
                <div class="feature-card-title">
                    <span class="emoji">🏝️</span> Recommended Destinations
                </div>
                <div class="feature-card-content">
                    • <strong>Sipadan Island</strong> - World-class Diving Paradise<br>
                    • <strong>Redang Island</strong> - Crystal Clear Waters & Rich Coral<br>
                    • <strong>Tioman Island</strong> - Pristine Coral Reef Ecosystem<br>
                    • <strong>Perhentian Islands</strong> - Diving Spots for Beginners
                </div>
            </div>
            """, unsafe_allow_html=True)
            
            # Disclaimer
            st.markdown("""
            <div class="feature-info-card disclaimer-card">
                <div class="feature-card-title">
                    <span class="emoji">⚠️</span> Important Notice
                </div>
                <div class="feature-card-content">
                    <strong>Content is AI-generated and may contain errors or inaccuracies.</strong><br>
                    For critical information, please consult professionals or refer to official reports.
                </div>
            </div>
            """, unsafe_allow_html=True)
            
        

if __name__ == "__main__":
    main() 