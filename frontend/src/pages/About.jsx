import React from "react";
import Header from "../components/Header/Header";
import SearchBar from "../components/SearchBar/SearchBar";
import ArrivalTime from "../components/ArrivalTime/ArrivalTime";
import Favorites from "../components/Favorites/Favorites";
import RecentHistory from "../components/RecentHistory/RecentHistory";

function About() {
  // const favorites = ["집", "기숙사", "회사", "집", "기숙사", "회사", "집", "기숙사", "회사"];
  // const recentPath = [
  //   {start: "대전역", end: "세종관"},
  //   {start: "카이스트 택시 승강장", end: "대전역"},
  //   {start: "대전역", end: "세종관"},
  //   {start: "카이스트 택시 승강장", end: "대전역"}
  // ]
  // const recentHistory = [
  //   { name: "집", address: "대전광역시 유성구 대학로 291" },
  //   { name: "대전역", address: "대전 동구 중앙로 215" },
  //   { name: "카이스트 택시 승강장", address: "대전 동구 중앙로 215" },
  //   { name: "집", address: "대전광역시 유성구 대학로 291" },
  //   { name: "대전역", address: "대전 동구 중앙로 215" },
  //   { name: "카이스트 택시 승강장", address: "대전 동구 중앙로 215" },
  //       { name: "집", address: "대전광역시 유성구 대학로 291" },
  //   { name: "대전역", address: "대전 동구 중앙로 215" },
  //   { name: "카이스트 택시 승강장", address: "대전 동구 중앙로 215" }
  // ];

  const userID = "0000";

  const handlePathDelete = (path) => {
    console.log(`${path.start} → ${path.end} 경로 삭제됨`);
  };

  const handleItemDelete = (item) => {
    console.log(`${item.title} 삭제됨`);
  };

  return (
    <div style={{ padding: "16px", fontFamily: "Arial, sans-serif" }}>
      <Header />
      <SearchBar />
      <ArrivalTime />
      <Favorites userID={userID} />
      <RecentHistory
        userID={userID}
        onPathDelete={handlePathDelete}
        onItemDelete={handleItemDelete}/>
    </div>
  );
};

export default About;