import { useState } from "react";
import axiosInstance from "../../utils/axiosInstance";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "../../css/patientCss/registration.css";

const RegistrationForm = () => {
  const navigate = useNavigate();

  //const [userId, setUserId] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("");
  const [isRegistrationDone, setIsRegistrationDone] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState();
  const [errors, setErrors] = useState({
    email: "",
    password: "",
    role: "",
  });

  const handleRegister = (e) => {
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

    if (!role) {
      setErrors({
        ...errors,
        role: "Role is required",
      });
      return;
    }

    const data = {
      email,
      password,
      role,
    };

    setIsLoading(true);

    axiosInstance
      .post("/register", data)
      .then((resp) => {
        console.log(data);
        console.log("The Response", resp);
        setIsRegistrationDone(true);
        toast.success("Registration Done. Please login for visit this site");
        navigate("/patient/login");
      })
      .catch((error) => {
        console.log("Error is ", error.response.status);
        if (error.response.status == 400) {
          toast.warn("You are already registered. Please login.");
        } else {
          setError(error);
        }
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  return (
    <div className="registration-container">
      <h1>Registration</h1>
      {isRegistrationDone && (
        <h2 style={{ color: "green" }}>Successfully Done Registration</h2>
      )}
      {isLoading && <h1>Loading.....</h1>}
      <form onSubmit={handleRegister}>
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
            type="text"
            onChange={(e) => {
              setPassword(e.target.value);
            }}
          />
          {errors.password && (
            <p className="error-message">{errors.password}</p>
          )}
        </div>

        <div className="form-group">
          <label htmlFor="role">User Role</label>
          <select
            value={role}
            onChange={(e) => {
              setRole(e.target.value);
            }}
          >
            <option value="PATIENT">PATIENT</option>
            <option value="ADMIN">ADMIN</option>
          </select>
          {errors.role && <p className="error-message">{errors.role}</p>}
        </div>

        <button className="registration-button" type="submit">
          Register
        </button>
      </form>
    </div>
  );
};

export default RegistrationForm;
