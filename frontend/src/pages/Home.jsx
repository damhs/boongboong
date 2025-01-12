import React from 'react';
import { useNavigate } from 'react-router-dom';
import Map from '../components/Map';
import TrafficInfo from '../components/TrafficInfo';
import { ReactComponent as NavigationIcon } from "../assets/icons/navigation_icon.svg";
import styles from "./Home.module.css"


function Home() {
  const navigate = useNavigate();

  const handleButtonClick = () => {
    navigate('/about');
  };

  return (
    <div style={{ textAlign: 'center'}}>
      <Map />
      <TrafficInfo />
      <button className={styles.navigationButton}>
        <NavigationIcon onClick={handleButtonClick}/>
      </button>
    </div>
  );
}

export default Home;
