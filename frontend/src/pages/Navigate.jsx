import React, { useEffect, useState } from "react";
import MapEdit from '../components/MapEdit';
import styles from "./Home.module.css";
import { ReactComponent as NavigationIcon } from "../assets/icons/navigation_icon.svg";

function Navigate() {
  const [status, setStatus] = useState(""); // 초록불까지 남은 시간
  const [speed, setSpeed] = useState(""); // 권장 속도

  

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
