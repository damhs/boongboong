import React from "react";
import { ReactComponent as Clock } from "../../assets/icons/clock.svg";
import { ReactComponent as Delete } from "../../assets/icons/delete.svg";
import styles from "./RecentHistoryItem.module.css";

const RecentHistoryItem = ({ item, onDelete, onSelect }) => {
  return (
    <div className={styles.item}>
      <Clock className={styles.clockIcon} />
      <div className={styles.textContainer} onClick={onSelect}>
        <div className={styles.title}>{item.name}</div>
        <div className={styles.subtitle}>{item.address}</div>
      </div>
      <Delete className={styles.deleteButton} onClick={onDelete}/>
    </div>
  );
};

export default RecentHistoryItem;
