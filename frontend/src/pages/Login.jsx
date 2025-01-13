import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import styles from "./Login.module.css";
import { ReactComponent as Logo } from "../assets/icons/logo.svg";
import axios from "axios";
import config from "../config";

const baseUrl = config.backendUrl;

const Login = () => {
  const [id, setId] = useState("");
  const [password, setPassword] = useState("");
  const [rememberMe, setRememberMe] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault(); // 폼 제출 시 페이지 새로고침 방지

    const formData = new URLSearchParams();
    formData.append('id', id);                   // <-- SecurityConfig에서 .usernameParameter("id")
    formData.append('password', password);       // <-- .passwordParameter("password")
    formData.append('remember-me', rememberMe ? "on" : "");

    try {
      console.log("로그인 시도");
      const response = await axios.post(`${baseUrl}/users/login`, formData, {
        withCredentials: true,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
      });

      // 로그인 성공 시 홈 화면으로 이동
      if (response.status === 200 || response.status === 302) {
        navigate("/");
      }
    } catch (err) {
      // 에러 처리 (예: 잘못된 로그인 정보)
      alert(err.response?.data?.message || "로그인 실패");
      console.error(err);
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
          onChange={(e) => setId(e.target.value)}
        />
        <input
          type="password"
          placeholder="비밀번호"
          className={styles.input}
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <label>
          <input
            type="checkbox"
            name="remember-me" // SecurityConfig의 .rememberMeParameter("remember-me")와 일치
            checked={rememberMe}
            onChange={(e) => setRememberMe(e.target.checked)}
          />
          자동 로그인
        </label>
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
