const LOCAL_API_URL = "http://localhost:8080";

const AZURE_API_URL =
    "https://wallet-optimizer-api-cnetf5fmdngdcma8.westus2-01.azurewebsites.net";

const useAzureBackend = false;

const API_URL = useAzureBackend
    ? AZURE_API_URL
    : LOCAL_API_URL;