import React from "react";
import MapEdit from '../components/MapEdit';
import styles from "./Home.module.css"
import { ReactComponent as NavigationIcon } from "../assets/icons/navigation_icon.svg";


function Navigate() {
  return (
    <div style={{ textAlign: 'center' }}>
      <div style={{ position: 'relative', height: '80vh' }}> {/* 부모 요소에 상대 위치 설정 */}
        <MapEdit />
        <span>status</span>
        <span>speed</span>
      </div>
      {/* <TrafficInfo /> */}
    </div>
  );
}

export default Navigate;