import React, { useRef, useState } from "react";
import styles from "./RecentHistory.module.css";
import RecentHistoryItem from "./RecentHistoryItem";
import PathItem from "./PathItem";
// import axios from "axios";
// import config from "../../config";

// const baseurl = config.backendUrl;

const RecentHistory = ({ recentPath, recentHistory, onPathDelete, onItemDelete }) => {
  const scrollRef = useRef(null);
    const [isDragging, setIsDragging] = useState(false);
    const [startX, setStartX] = useState(0);
    const [scrollLeft, setScrollLeft] = useState(0);
    // const [recentPath, setRecentPath] = useState([]);
    // const [recentHistory, setRecentHistory] = useState([]);
  
    const handleMouseDown = (e) => {
      setIsDragging(true);
      setStartX(e.pageX - scrollRef.current.offsetLeft);
      setScrollLeft(scrollRef.current.scrollLeft);
    };
  
    const handleMouseMove = (e) => {
      if (!isDragging) return;
      const x = e.pageX - scrollRef.current.offsetLeft;
      const walk = (x - startX) * 1; // 드래그 속도 조정
      scrollRef.current.scrollLeft = scrollLeft - walk;
    };
  
    const handleMouseUp = () => {
      setIsDragging(false);
    };

  // // API 호출
  // const callRecentPath = async () => {
  //   try {
  //     const res = await axios.get(`${baseurl}/paths/${userID}/recents`);
  //     setRecentPath(res.data); // API에서 받은 데이터를 state에 저장
  //   } catch (error) {
  //     console.error("Failed to get recent path:", error);
  //   }
  // };

  // // API 호출
  // const callRecentHistory = async () => {
  //   try {
  //     const res = await axios.get(`${baseurl}/recents/${userID}`);
  //     setRecentHistory(res.data); // API에서 받은 데이터를 state에 저장
  //   } catch (error) {
  //     console.error("Failed to get recent path:", error);
  //   }
  // };

  // // 컴포넌트가 처음 렌더링될 때 또는 userID가 변경될 때 데이터 가져오기
  // useEffect(() => {
  //   if (userID) {
  //     callRecentPath();
  //     callRecentHistory();
  //   }
  // }, [userID]);

  return (
    <div className={styles.container}>
      <div className={styles.title}>최근 내역</div>

      {/* 카드 리스트 */}
      <div 
        className={styles.cardContainer}
        ref={scrollRef}
        onMouseDown={handleMouseDown}
        onMouseMove={handleMouseMove}
        onMouseUp={handleMouseUp}
        onMouseLeave={handleMouseUp}
      >
        {recentPath.map((path, index) => (
          <PathItem key={index} path={path} onDelete={() => onPathDelete(path.id)}/>
        ))}
      </div>

      {/* 최근 내역 리스트 */}
      <div className={styles.listContainer}
        ref={scrollRef}
        onMouseDown={handleMouseDown}
        onMouseMove={handleMouseMove}
        onMouseUp={handleMouseUp}
        onMouseLeave={handleMouseUp}
      >
        {recentHistory.map((item, index) => (
          <RecentHistoryItem key={index} item={item} onDelete={() => onItemDelete(item.id)} />
        ))}
      </div>
    </div>
  );
};

export default RecentHistory;
