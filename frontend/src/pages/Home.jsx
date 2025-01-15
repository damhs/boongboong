import React from 'react';
import { useNavigate } from 'react-router-dom';
import Map from '../components/Map';
import TrafficInfo from '../components/TrafficInfo';
import { ReactComponent as NavigationIcon } from "../assets/icons/navigation_icon.svg";
import styles from "./Home.module.css"


function Home() {
  const navigate = useNavigate();

  const handleButtonClick = () => {
    navigate('/search');
  };

  return (
    <div style={{ textAlign: 'center' }}>
      <div style={{ position: 'relative', height: '100vh' }}> {/* 부모 요소에 상대 위치 설정 */}
        <Map />
        <button 
          className={styles.navigationButton} 
          onClick={handleButtonClick}
        >
          <NavigationIcon />
        </button>
      </div>
      {/* <TrafficInfo /> */}
    </div>
  );
}

export default Home;
