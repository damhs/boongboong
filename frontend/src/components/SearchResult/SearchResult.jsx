import React from "react";
import styles from "./SearchResult.module.css";

function SearchResult({ searchResults, onSelect }) {
  if (!searchResults || searchResults.length === 0) {
    return <p>검색 결과가 없습니다.</p>;
  }

  const stripHtmlTags = (html) => {
    return html.replace(/<\/?[^>]+(>|$)/g, ""); // HTML 태그 제거
  };

  return (
    <div className={styles.container}>
      <div className={styles.pageTitle}>검색 결과</div>
      <div className={styles.listContainer}>
        {searchResults.map((result, index) => (
          <div 
            key={index} 
            className={styles.item}
            onClick={() => onSelect(result)}>
            <p className={styles.title}>{stripHtmlTags(result.title)}</p>
            <p className={styles.subtitle}>{stripHtmlTags(result.roadAddress)}</p>
          </div>
        ))}
      </div>
    </div>
  );
}

export default SearchResult;
