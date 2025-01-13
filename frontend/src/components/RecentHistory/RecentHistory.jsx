import React, { useRef, useState, useEffect } from "react";
import axios from "axios";
import styles from "./RecentHistory.module.css";
import RecentHistoryItem from "./RecentHistoryItem";
import PathItem from "./PathItem";
import config from "../../config";

const baseurl = config.backendUrl;


const RecentHistory = ({ recentPath, recentHistory, onPathDelete, onItemDelete, onItemSelect }) => {
  const scrollRef = useRef(null);
  const [isDragging, setIsDragging] = useState(false);
  const [startX, setStartX] = useState(0);
  const [scrollLeft, setScrollLeft] = useState(0);


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

  return (
    <div className={styles.container}>
      <div className={styles.title}>최근 내역</div>

      {/* 카드 리스트 */}
      {recentPath && recentPath.length > 0 && (
        <div 
          className={styles.cardContainer}
          ref={scrollRef}
          onMouseDown={handleMouseDown}
          onMouseMove={handleMouseMove}
          onMouseUp={handleMouseUp}
          onMouseLeave={handleMouseUp}
        >
          {recentPath.map((path) => (
            <PathItem 
              key={path.id} 
              path={path} 
              onDelete={() => onPathDelete(path.id)}/>
          ))}
        </div>
      )}

      {/* 최근 내역 리스트 */}
      {recentHistory && recentHistory.length > 0 ? (
        <div className={styles.listContainer}
          ref={scrollRef}
          onMouseDown={handleMouseDown}
          onMouseMove={handleMouseMove}
          onMouseUp={handleMouseUp}
          onMouseLeave={handleMouseUp}
        >
          {recentHistory.map((item) => (
            <RecentHistoryItem 
              key={item.recentID} 
              item={item} 
              onDelete={() => onItemDelete(item.recentID)}
              onSelect={() => onItemSelect(item)} />
          ))}
        </div>
      ) : (
        <p className={styles.noDataMessage}>최근 내역이 없습니다.</p>
      )}
    </div>
  );
};

export default RecentHistory;
