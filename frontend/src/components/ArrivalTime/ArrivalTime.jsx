import React from "react";
import styles from "./ArrivalTime.module.css";

const ArrivalTime = () => (
  <div className={styles.container}>
    <label className={styles.label}>도착 시간</label>
    <div className={styles.timePickerContainer}>
      <input type="date" className={styles.dateBox}/>
      <input type="time" className={styles.timeBox}/>
    </div>
  </div>
);

export default ArrivalTime;
