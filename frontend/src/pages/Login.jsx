import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./Login.module.css";
import { ReactComponent as Logo } from "../assets/icons/logo.svg";
import axios from "axios";
import config from "../config";

const baseUrl = config.backendUrl;

const Login = () => {
  const [id, setid] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault(); // 폼 제출 시 페이지 새로고침 방지
    try {
      const response = await axios.post(`${baseUrl}/users/login`, {
        id,
        password,
      });

      // 서버에서 받은 인증 토큰을 로컬 스토리지에 저장
      // localStorage.setItem("authToken", response.data.token);

      // 로그인 성공 시 홈 화면으로 이동
      if (response.status === 200) {
        navigate("/");
      }
    } catch (err) {
      // 에러 처리 (예: 잘못된 로그인 정보)
      alert(err.response?.data?.message || "로그인 실패");
      setError(err.response?.data?.message || "로그인 실패");
    }
  };

  const handleSignupNavigation = () => {
    navigate("/signup");
  };

  return (
    <div className={styles.container}>
      <Logo className={styles.logo} />
      <div className={styles.title}>Login</div>
      <form className={styles.inputform} onSubmit={handleLogin}>
        <input
          type="text"
          placeholder="아이디"
          className={styles.input}
          value={id}
          onChange={(e) => setid(e.target.value)}
        />
        <input
          type="password"
          placeholder="비밀번호"
          className={styles.input}
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit" className={styles.loginButton}>
          로그인
        </button>
      </form>
      <form className={styles.form}>
        <button
          type="button"
          className={styles.signupButton}
          onClick={handleSignupNavigation}
        >
          회원가입
        </button>
      </form>
    </div>
  );
};

export default Login;
