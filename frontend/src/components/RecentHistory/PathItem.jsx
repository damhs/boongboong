import React, { useEffect, useState } from "react";
import axios from "axios";
import styles from "./PathItem.module.css";
import { ReactComponent as DotsIcon } from "../../assets/icons/dots.svg";
import { ReactComponent as Delete } from "../../assets/icons/delete.svg";


const PathItem = ({ path, onDelete, onSelect }) => {
  const [origin, setOrigin] = useState({ placeName: "로딩 중..." });
  const [destination, setDestination] = useState({ placeName: "로딩 중..." });

  const fetchPlaceDetails = async (placeID, setPlace) => {
    try {
      const response = await axios.get(`/api/places/${placeID}`);
      const { placeName } = response.data;
      setPlace({ placeName });
    } catch (error) {
      console.error("장소 정보를 가져오는 중 오류 발생:", error);
      setPlace({ placeName: "정보 없음" });
    }
  };

  useEffect(() => {
    if (path.originID) {
      fetchPlaceDetails(path.originID, setOrigin);
    }
    if (path.destinationID) {
      fetchPlaceDetails(path.destinationID, setDestination);
    }
  }, [path.originID, path.destinationID]);

  return (
    <div className={styles.card}>
        <button className={styles.deleteButton} onClick={() => onDelete(path)}>
          <Delete />
        </button>
        <div className={styles.cardContent} onClick={() => onSelect(path)}>
            <div className={styles.cardItem}>
                <span className={styles.bulletStart} />
                <span className={styles.text}>{origin.placeName}</span>
            </div>
            <DotsIcon className={styles.dots}/>
            <div className={styles.cardItem}>
                <span className={styles.bulletEnd} />
                <span className={styles.text}>{destination.placeName}</span>
            </div>
        </div>
    </div>
  );
};

export default PathItem;
