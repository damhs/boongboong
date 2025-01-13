import React, { useEffect, useState } from "react";
import axios from "axios";
import { ReactComponent as Clock } from "../../assets/icons/clock.svg";
import { ReactComponent as Delete } from "../../assets/icons/delete.svg";
import styles from "./RecentHistoryItem.module.css";

const RecentHistoryItem = ({ item, onDelete, onSelect }) => {
  const [placeDetails, setPlaceDetails] = useState({
    placeName: "로딩 중...",
    etc: "로딩 중...",
  });

  const fetchPlaceDetails = async () => {
    try {
      const response = await axios.get(`/api/places/${item.placeID}`);
      const { placeName, etc } = response.data;
      setPlaceDetails({ placeName, etc });
    } catch (error) {
      console.error("장소 정보를 가져오는 중 오류 발생:", error);
      setPlaceDetails({
        placeName: "정보 없음",
        etc: "정보 없음",
      });
    }
  };

  useEffect(() => {
    if (item.placeID) {
      fetchPlaceDetails();
    }
  }, [item.placeID]);

  return (
    <div className={styles.item}>
      <Clock className={styles.clockIcon} />
      <div className={styles.textContainer} onClick={onSelect}>
        <div className={styles.title}>{placeDetails.placeName}</div>
        <div className={styles.subtitle}>{placeDetails.etc}</div>
      </div>
      <Delete className={styles.deleteButton} onClick={onDelete}/>
    </div>
  );
};

export default RecentHistoryItem;
