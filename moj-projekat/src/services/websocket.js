import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

let stompClient = null;
let connected = false;

export const connect = (projectId) => {
  stompClient = new Client({
    webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
    reconnectDelay: 5000,
    debug: (str) => console.log(str),
    onConnect: () => {
      console.log("✅ Connected to WebSocket");
      connected = true;

      stompClient.subscribe(`/topic/project/${projectId}`, (message) => {
        const receivedMessage = JSON.parse(message.body);
        console.log("📩 stigla poruka:", receivedMessage);

        // napravi globalnu callback funkciju
        if (window.onMessageReceived) {
          window.onMessageReceived(receivedMessage);
        }
      });
    },
  });

  stompClient.activate();
};

export const sendMessage = (msg) => {
  console.log(connected);
  console.log("d");
  console.log(stompClient);
  if (connected && stompClient) {
    stompClient.publish({
      destination: "/chat/chat.sendMessage", // backend endpoint
      body: JSON.stringify(msg),
    });
    console.log("📤 Sent:", msg);
  } else {
    console.error("❌ Not connected yet!");
  }
};
