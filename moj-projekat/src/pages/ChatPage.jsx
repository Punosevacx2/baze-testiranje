import { useEffect, useState } from "react";
import { useParams ,useLocation} from "react-router-dom";
import { connect, sendMessage as stompSendMessage } from "../services/websocket";
import axios from "axios";
import api from "../api/axiosConfig";

export default function ChatPage({  }) {
  const { projectId } = useParams(); // uzmi ID direktno iz URL-a
  const [messages, setMessages] = useState([]);
  const [text, setText] = useState("");
  const [currentUser, setCurrentUser] = useState(null);
 
  useEffect(() => {
    // 🚀 Callback koji prima poruke iz websocket.js
    window.onMessageReceived = (msg) => {
      console.log("📩 stigla poruka (ChatPage):", msg);
      setMessages((prev) => [...prev, msg]);
    };

    connect(projectId); // prosleđujemo projectId

    const token = sessionStorage.getItem("token");
    if (!token) {
      return;
    }

    api.get("/users/me", { headers: { Authorization: `Bearer ${token}` } })
      .then((ress) => {
        setCurrentUser(ress.data.user);
      });

    // učitaj stare poruke
    const fetchMessages = async () => {
      try {
        console.log("ProjecId: " + projectId);
        const res = await api.get(`/chat/projects/${projectId}/messages`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        console.log("📥 Stare poruke:", res.data);
        setMessages(res.data);
      } catch (err) {
        console.error(err);
      }
    };
    fetchMessages();

    return () => {
      // očisti callback kad se izađe sa stranice
      window.onMessageReceived = null;
    };
  }, [projectId]);

  const sendMessage = () => {
    if (!text.trim() || !currentUser) return;

    const message = {
      projectId: projectId,
      senderUserId: currentUser.id,
      senderName: currentUser.username,
      content: text,
      timestamp: new Date().toISOString(),
    };

    stompSendMessage(message);
    // 👇 OVO možeš ostaviti ili zakomentarisati
    // setMessages((prev) => [...prev, message]);
    setText("");
  };

  return (
    <div>
      <div style={{ height: "300px", overflowY: "auto" }}>
        {messages.map((m, i) => (
          <div key={i}>
            <b>{m.senderName}</b>: {m.content}
          </div>
        ))}
      </div>
      <input
        value={text}
        onChange={(e) => setText(e.target.value)}
        placeholder="Type a message"
      />
      <button onClick={sendMessage}>Send</button>
    </div>
  );
}
