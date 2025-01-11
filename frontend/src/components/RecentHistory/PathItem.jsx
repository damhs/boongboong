import React from "react";
import styles from "./PathItem.module.css";
import { ReactComponent as DotsIcon } from "../../assets/icons/dots.svg";
import { ReactComponent as Delete } from "../../assets/icons/delete.svg";


const PathItem = ({ path, onDelete }) => {
  return (
    <div className={styles.card}>
        <button className={styles.deleteButton} onClick={() => onDelete(path)}>
          <Delete />
        </button>
        <div className={styles.cardContent}>
            <div className={styles.cardItem}>
                <span className={styles.bulletStart} />
                <span className={styles.text}>{path.start}</span>
            </div>
            <DotsIcon className={styles.dots}/>
            <div className={styles.cardItem}>
                <span className={styles.bulletEnd} />
                <span className={styles.text}>{path.end}</span>
            </div>
        </div>
    </div>
  );
};

export default PathItem;
