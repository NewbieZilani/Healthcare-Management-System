import { useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../../utils/axiosInstance";
import "../../css/patientCss/login.css";
import { toast } from "react-toastify";

const LoginPage = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState({
    email: "",
    password: "",
  });

  const handleLogin = (e) => {
    e.preventDefault();

    if (!email) {
      setErrors({
        ...errors,
        email: "Email is required",
      });
      return;
    }

    if (!password) {
      setErrors({
        ...errors,
        password: "Password is required",
      });
      return;
    }

    const userCredential = {
      email: email,
      password: password,
    };

    axiosInstance.post("/login", userCredential).then((resp) => {
      const data = resp.data;
      localStorage.setItem("token", data.Authorization);
      localStorage.setItem("role", data.role);
      localStorage.setItem("userId", data.userId);
      localStorage.setItem("email", data.email);
      toast.success("Successfully logged In ");
      navigate("/");
    });
  };

  return (
    <div className="login-container">
      <h1>Login Page</h1>
      <form onSubmit={handleLogin}>
        <div className="form-group">
          <label htmlFor="email">Email</label>
          <input
            value={email}
            id="email"
            placeholder="Enter Email"
            type="text"
            onChange={(e) => {
              setEmail(e.target.value);
            }}
          />
          {errors.email && <p className="error-message">{errors.email}</p>}
        </div>

        <div className="form-group">
          <label htmlFor="password">Enter Password</label>
          <input
            value={password}
            id="password"
            placeholder="Enter Password"
            type="password"
            onChange={(e) => {
              setPassword(e.target.value);
            }}
          />
          {errors.password && (
            <p className="error-message">{errors.password}</p>
          )}
        </div>

        <button type="submit" className="login-button">
          Login
        </button>
      </form>
    </div>
  );
};

export default LoginPage;
