const isLocal= (window.location.hostname ==="localhost" || window.location.hostname ==="127.0.0.1");


const API_URL = isLocal
    ? "http://localhost:8080"
    :"AZURE_BACKEND_URL"; //TODO :Replace after Azure deployment