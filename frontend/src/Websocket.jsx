import React, { useEffect, useState } from "react";

const Websocket = ({ serverUrl }) => {
    const [socket, setSocket] = useState(null);
    const [messages, setMessages] = useState([]);
    const [currentSegment, setCurrentSegment] = useState(null);

    useEffect(() => {
        // WebSocket 연결 설정
        const ws = new WebSocket(serverUrl);

        ws.onopen = () => {
            console.log("WebSocket 연결 성공");
        };

        ws.onmessage = (event) => {
            console.log("서버 메시지:", event.data);
            handleServerMessage(event.data);
        };

        ws.onclose = () => {
            console.log("WebSocket 연결 종료");
        };

        ws.onerror = (error) => {
            console.error("WebSocket 오류:", error);
        };

        setSocket(ws);

        // 컴포넌트 언마운트 시 WebSocket 닫기
        return () => {
            if (ws) ws.close();
        };
    }, [serverUrl]);

    const handleServerMessage = (data) => {
        // 서버에서 받은 메시지 처리
        if (data.includes("Next segment")) {
            const segmentNumber = data.split(":")[1].trim();
            setCurrentSegment(segmentNumber);
        } else if (data.includes("You have reached your destination")) {
            setMessages((prev) => [...prev, "목적지에 도착했습니다!"]);
        } else {
            setMessages((prev) => [...prev, data]);
        }
    };

    const sendLocationToServer = (latitude, longitude) => {
        if (socket && socket.readyState === WebSocket.OPEN) {
            const locationData = JSON.stringify({ latitude, longitude });
            socket.send(locationData);
            console.log("위치 전송:", locationData);
        } else {
            console.error("WebSocket이 열려 있지 않습니다.");
        }
    };

    useEffect(() => {
        if ("geolocation" in navigator) {
            const watchId = navigator.geolocation.watchPosition(
                (position) => {
                    const { latitude, longitude } = position.coords;
                    sendLocationToServer(latitude, longitude);
                },
                (error) => {
                    console.error("위치 가져오기 실패:", error.message);
                },
                { enableHighAccuracy: true }
            );

            return () => {
                navigator.geolocation.clearWatch(watchId);
            };
        } else {
            console.error("Geolocation API를 지원하지 않습니다.");
        }
    }, [socket]);

    return (
        <div>
            <h1>WebSocket 실시간 통신</h1>
            <p>현재 세그먼트: {currentSegment ? currentSegment : "출발 대기 중..."}</p>
            <div>
                <h2>서버 메시지</h2>
                <ul>
                    {messages.map((message, index) => (
                        <li key={index}>{message}</li>
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default Websocket;
