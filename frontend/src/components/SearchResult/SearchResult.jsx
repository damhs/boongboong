import React, { useRef, useState } from "react";

function SearchResult({ searchResults }) {
  if (!searchResults || searchResults.length === 0) {
    return <p>검색 결과가 없습니다.</p>;
  }

  return (
    <div>
      <h2>검색 결과</h2>
      <ul>
        {searchResults.map((result, index) => (
          <li key={index}>
            <a href={result.address} target="_blank" rel="noopener noreferrer">
              {result.title}
            </a>
            <p>{result.description}</p>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default SearchResult;
