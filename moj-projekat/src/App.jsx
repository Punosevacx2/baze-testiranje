import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import Dashboard from "./pages/Dashboard";
import ProjectDetails from "./components/ProjectDetails.jsx";
import ChatPage from "./pages/ChatPage.jsx"; 

export default function App() {
  return (
    <BrowserRouter>
  <Routes>
    <Route path="/" element={<Navigate to="/login" />} />
    <Route path="/login" element={<LoginPage />} />
    <Route path="/register" element={<RegisterPage />} />
    <Route path="/dashboard" element={<Dashboard />} />
    <Route path="/projects/:projectId" element={<ProjectDetails />} /> {/* detalji projekta */}
    <Route path="/projects/:projectId/chat" element={<ChatPage />} /> {/* chat stranica */}
  </Routes>
</BrowserRouter>
  );
}
