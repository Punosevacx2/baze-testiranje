import { useState } from "react";
import LoginForm from "../components/LoginForm.jsx";
import RegisterForm from "../components/RegisterForm";

export default function AuthPage() {
  const [mode, setMode] = useState("login"); // login | register

  return (
    <div className="flex flex-col items-center justify-center h-screen">
      <h1 className="text-2xl font-bold mb-4">
        {mode === "login" ? "Login" : "Register"}
      </h1>
      {mode === "login" ? <LoginForm /> : <RegisterForm />}
      <button
        onClick={() => setMode(mode === "login" ? "register" : "login")}
        className="mt-4 text-blue-500 underline"
      >
        {mode === "login"
          ? "Nemate nalog? Registrujte se"
          : "Već imate nalog? Login"}
      </button>
    </div>
  );
}
