import React, { useEffect, useState } from "react";
import MapEdit from '../components/MapEdit';
import styles from "./Home.module.css";
import { ReactComponent as NavigationIcon } from "../assets/icons/navigation_icon.svg";

function Navigate() {
  const [status, setStatus] = useState(""); // 초록불까지 남은 시간
  const [speed, setSpeed] = useState(""); // 권장 속도

  useEffect(() => {
    // WebSocket 연결
    const socket = new WebSocket("ws://localhost:8080/ws");

    socket.onopen = () => {
      console.log("WebSocket connected");
    };

    socket.onmessage = (event) => {
      const data = JSON.parse(event.data);
      setStatus(`초록불까지 남은 시간: ${data.remainingTime}초`);
      setSpeed(`권장 속도: ${data.recommendedSpeed}km/h`);
    };

    socket.onerror = (error) => {
      console.error("WebSocket error:", error);
    };

    socket.onclose = () => {
      console.log("WebSocket disconnected");
    };

    // Clean up WebSocket
    return () => {
      socket.close();
    };
  }, []);

  return (
    <div style={{ textAlign: "center" }}>
      <div style={{ position: "relative", height: "80vh" }}>
        <MapEdit />
        <span>{status}</span>
        <span>{speed}</span>
      </div>
    </div>
  );
}

export default Navigate;
