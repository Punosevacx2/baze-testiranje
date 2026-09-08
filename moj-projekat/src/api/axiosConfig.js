import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080", // prilagodi tvom backendu
  headers: { "Content-Type": "application/json" },
});

export default api;
