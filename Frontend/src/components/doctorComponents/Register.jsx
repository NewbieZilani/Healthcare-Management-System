// DoctorRegistrationForm.jsx
import { useState } from "react";
import axiosInstance, { doctorAxiosInstance } from "../../utils/axiosInstance";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "../../css/doctorcss/register.css";

const DoctorRegistrationForm = () => {
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [gender, setGender] = useState("");
  const [department, setDepartment] = useState("");
  const [registrationNumberBDMC, setRegistrationNumberBDMC] = useState("");
  const [allocatedRoom, setAllocatedRoom] = useState("");
  const [qualifications, setQualifications] = useState("");
  const [isRegistrationDone, setIsRegistrationDone] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState();
  const [errors, setErrors] = useState({
    name: "",
    email: "",
    password: "",
    gender: "",
    department: "",
    registrationNumberBDMC: "",
    allocatedRoom: "",
    qualifications: "",
  });

  const handleRegister = (e) => {
    e.preventDefault();

    // Validate the form fields
    const validationErrors = validateForm();
    if (Object.values(validationErrors).some((err) => err !== "")) {
      setErrors(validationErrors);
      return;
    }

    const data = {
      name,
      email,
      password,
      gender,
      department,
      registration_number_BDMC: registrationNumberBDMC,
      allocated_room: allocatedRoom,
      qualifications,
    };

    setIsLoading(true);

    doctorAxiosInstance
      .post("/register", data)
      .then((resp) => {
        console.log(data);
        console.log("The Response", resp);
        setIsRegistrationDone(true);
        toast.success("Registration Done. Please login to visit this site");
        navigate("/login");
      })
      .catch((error) => {
        console.log("Error is ", error.response.status);
        if (error.response.status === 400) {
          toast.warn("You are already registered. Please login.");
        } else {
          setError(error);
        }
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  const validateForm = () => {
    const errors = {
      name: "",
      email: "",
      password: "",
      gender: "",
      department: "",
      registrationNumberBDMC: "",
      allocatedRoom: "",
      qualifications: "",
    };

    // Add your validation logic here
    // Example: Check if the fields are not empty, etc.

    return errors;
  };

  return (
    <div className="registration-container">
      <h1>Doctor Registration</h1>
      {isRegistrationDone && (
        <h2 style={{ color: "green" }}>Successfully Done Registration</h2>
      )}
      {isLoading && <h1>Loading.....</h1>}
      <form onSubmit={handleRegister}>
        {/* Row 1: Name and Email */}
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="name">Doctor Name</label>
            <input
              value={name}
              id="name"
              placeholder="Enter Doctor Name"
              type="text"
              onChange={(e) => {
                setName(e.target.value);
              }}
            />
            {errors.name && <p className="error-message">{errors.name}</p>}
          </div>
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              value={email}
              id="email"
              placeholder="Enter Email"
              type="email"
              onChange={(e) => {
                setEmail(e.target.value);
              }}
            />
            {errors.email && <p className="error-message">{errors.email}</p>}
          </div>
        </div>

        {/* Row 2: Gender and Department */}
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="gender">Gender</label>
            <select
              value={gender}
              onChange={(e) => {
                setGender(e.target.value);
              }}
            >
              <option value="male">Male</option>
              <option value="female">Female</option>
            </select>
            {errors.gender && <p className="error-message">{errors.gender}</p>}
          </div>
          <div className="form-group">
            <label htmlFor="department">Department</label>
            <input
              value={department}
              id="department"
              placeholder="Enter Department"
              type="department"
              onChange={(e) => {
                setDepartment(e.target.value);
              }}
            />
            {errors.department && (
              <p className="error-message">{errors.department}</p>
            )}
          </div>
        </div>

        {/* Row 3: Registration Number and Allocated Room */}
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="registrationNumberBDMC">
              BDMC Registration Number
            </label>
            <input
              value={registrationNumberBDMC}
              id="registrationNumberBDMC"
              placeholder="Enter BDMC Registration Number"
              type="registrationNumberBDMC"
              onChange={(e) => {
                setRegistrationNumberBDMC(e.target.value);
              }}
            />
            {errors.registrationNumberBDMC && (
              <p className="error-message">{errors.registrationNumberBDMC}</p>
            )}
          </div>
          <div className="form-group">
            <label htmlFor="allocatedRoom">Allocated Room</label>
            <input
              value={allocatedRoom}
              id="allocatedRoom"
              placeholder="Enter Allocated Room"
              type="allocatedRoom"
              onChange={(e) => {
                setAllocatedRoom(e.target.value);
              }}
            />
            {errors.allocatedRoom && (
              <p className="error-message">{errors.allocatedRoom}</p>
            )}
          </div>
        </div>

        {/* Row 4: Qualifications and Password */}
        <div className="form-row">
          <div className="form-group">
            <label htmlFor="qualifications">Qualifications</label>
            <input
              value={qualifications}
              id="qualifications"
              placeholder="Enter Qualifications"
              type="qualifications"
              onChange={(e) => {
                setQualifications(e.target.value);
              }}
            />
            {errors.qualifications && (
              <p className="error-message">{errors.qualifications}</p>
            )}
          </div>
          <div className="form-group">
            <label htmlFor="password">Password</label>
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
        </div>
        <button className="registration-button" type="submit">
          Register
        </button>
      </form>
    </div>
  );
};

export default DoctorRegistrationForm;
