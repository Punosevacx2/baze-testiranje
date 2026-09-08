import { useEffect, useRef, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { connect, disconnect, sendMessage as stompSendMessage } from "../services/websocket";
import api from "../api/axiosConfig";

export default function ChatPage() {
  const { projectId } = useParams();
  const navigate = useNavigate();
  const [messages, setMessages] = useState([]);
  const [text, setText] = useState("");
  const [currentUser, setCurrentUser] = useState(null);
  const bottomRef = useRef(null);

  useEffect(() => {
    connect(projectId, (msg) => {
      setMessages((prev) => [...prev, msg]);
    });

    api.get("/users/me")
      .then((res) => setCurrentUser(res.data.user))
      .catch(() => navigate("/login"));

    api.get(`/chat/projects/${projectId}/messages`)
      .then((res) => setMessages(res.data))
      .catch((err) => console.error("Greška pri učitavanju poruka:", err));

    return () => {
      disconnect();
    };
  }, [projectId, navigate]);

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const sendMessage = () => {
    if (!text.trim() || !currentUser) return;

    const message = {
      projectId,
      senderUserId: currentUser.id,
      senderName: currentUser.username,
      content: text,
    };

    stompSendMessage(message);
    setText("");
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      sendMessage();
    }
  };

  return (
    <div className="flex flex-col h-screen bg-gray-100">
      {/* Header */}
      <div className="bg-white shadow px-6 py-4 flex items-center gap-4">
        <button
          onClick={() => navigate(-1)}
          className="text-gray-500 hover:text-gray-800 text-sm"
        >
          &larr; Nazad
        </button>
        <h1 className="text-xl font-bold text-gray-800">Projektni chat</h1>
      </div>

      {/* Poruke */}
      <div className="flex-1 overflow-y-auto px-6 py-4 space-y-3">
        {messages.map((m, i) => {
          const senderId = m.senderUserId || m.senderId;
          const isMe = currentUser && senderId === currentUser.id;
          return (
            <div
              key={i}
              className={`flex ${isMe ? "justify-end" : "justify-start"}`}
            >
              <div
                className={`max-w-xs lg:max-w-md px-4 py-2 rounded-2xl shadow-sm ${
                  isMe
                    ? "bg-blue-500 text-white rounded-br-none"
                    : "bg-white text-gray-800 rounded-bl-none"
                }`}
              >
                {!isMe && (
                  <p className="text-xs font-semibold text-blue-500 mb-1">
                    {m.senderName}
                  </p>
                )}
                <p className="text-sm">{m.content}</p>
              </div>
            </div>
          );
        })}
        <div ref={bottomRef} />
      </div>

      {/* Input */}
      <div className="bg-white border-t px-6 py-4 flex gap-3">
        <input
          value={text}
          onChange={(e) => setText(e.target.value)}
          onKeyDown={handleKeyDown}
          placeholder="Napiši poruku..."
          className="flex-1 border rounded-full px-4 py-2 text-sm outline-none focus:ring-2 focus:ring-blue-300"
        />
        <button
          onClick={sendMessage}
          className="bg-blue-500 text-white px-5 py-2 rounded-full hover:bg-blue-600 text-sm font-medium"
        >
          Pošalji
        </button>
      </div>
    </div>
  );
}
