import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

let stompClient = null;
let connected = false;

export const connect = (projectId, onMessage) => {
  stompClient = new Client({
    webSocketFactory: () => new SockJS("http://localhost:8080/ws"),
    reconnectDelay: 5000,
    onConnect: () => {
      connected = true;

      stompClient.subscribe(`/topic/project/${projectId}`, (message) => {
        const receivedMessage = JSON.parse(message.body);
        if (onMessage) {
          onMessage(receivedMessage);
        }
      });
    },
  });

  stompClient.activate();
};

export const disconnect = () => {
  if (stompClient) {
    stompClient.deactivate();
    connected = false;
    stompClient = null;
  }
};

export const sendMessage = (msg) => {
  if (connected && stompClient) {
    stompClient.publish({
      destination: "/chat/chat.sendMessage",
      body: JSON.stringify(msg),
    });
  } else {
    console.error("WebSocket nije konektovan.");
  }
};
