import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./Login.module.css";
import { ReactComponent as Logo } from "../assets/icons/logo.svg";
import axios from "axios";
import config from "../config.js"; // baseUrl 불러오기

const baseUrl = config.backendUrl;

const Signup = () => {
  const navigate = useNavigate();

  // 각 인풋값을 저장할 상태
  const [loginID, setLoginID] = useState("");
  const [password, setPassword] = useState("");
  const [passwordConfirm, setPasswordConfirm] = useState("");

  // 회원가입 요청
  const handleSignup = async (e) => {
    e.preventDefault(); // 폼 submit 시 리프레시 방지

    // 비밀번호 일치 여부 체크
    if (password !== passwordConfirm) {
      alert("비밀번호가 일치하지 않습니다.");
      return;
    }

    try {
      console.log("회원가입 요청");

      const response = await axios.post(`${baseUrl}/users`, {
        loginID,
        password,
      });

      console.log(response);
      const { userID } = response.data.userID;
      localStorage.setItem("userID", userID);

      // 회원가입 성공 시 처리 (예: 페이지 이동, 알림 등)
      if (response.status === 201) {
        alert("회원가입이 완료되었습니다!");
        navigate("/login"); // 가입 후 로그인 페이지로 이동
      }
    } catch (error) {
      // 에러 처리
      console.error(error);
      alert("회원가입 중 오류가 발생했습니다. 다시 시도해 주세요.", error);
    }
  };

  return (
    <div className={styles.container}>
      <Logo className={styles.logo} />
      <div className={styles.title}>Sign up</div>
      <form className={styles.inputform} onSubmit={handleSignup}>
        <input
          type="text"
          placeholder="아이디"
          className={styles.input}
          value={loginID}
          onChange={(e) => setLoginID(e.target.value)}
        />
        <input
          type="password"
          placeholder="비밀번호"
          className={styles.input}
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <input
          type="password"
          placeholder="비밀번호 확인"
          className={styles.input}
          value={passwordConfirm}
          onChange={(e) => setPasswordConfirm(e.target.value)}
        />
        <button type="submit" className={styles.loginButton}>
          회원가입
        </button>
      </form>
    </div>
  );
};

export default Signup;
