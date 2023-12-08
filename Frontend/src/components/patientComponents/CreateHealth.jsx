// CreateHealthDataForm.jsx
import React, { useState } from "react";
import axiosInstance from "../../utils/axiosInstance";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "../../css/patientCss/createHealthData.css"; // Adjust the path based on your project structure

const CreateHealthDataForm = () => {
  const navigate = useNavigate();

  const [heightCm, setHeightCm] = useState("");
  const [weightKg, setWeightKg] = useState("");
  const [bloodSugarLevel, setBloodSugarLevel] = useState("");
  const [bloodPressure, setBloodPressure] = useState("LOW");
  const [bloodGroup, setBloodGroup] = useState("A_NEGATIVE");
  const [heartRate, setHeartRate] = useState("");
  const [sleepHour, setSleepHour] = useState("");
  const [smoke, setSmoke] = useState(false);
  const [isHealthDataCreated, setIsHealthDataCreated] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState();
  const [errors, setErrors] = useState({
    heightCm: "",
    weightKg: "",
    bloodSugarLevel: "",
    heartRate: "",
    sleepHour: "",
  });

  const handleCreateHealthData = (e) => {
    e.preventDefault();

    // Validation logic (customize as needed)
    if (
      !heightCm ||
      !weightKg ||
      !bloodSugarLevel ||
      !heartRate ||
      !sleepHour
    ) {
      setErrors({
        heightCm: !heightCm ? "Height is required" : "",
        weightKg: !weightKg ? "Weight is required" : "",
        bloodSugarLevel: !bloodSugarLevel
          ? "Blood Sugar Level is required"
          : "",
        heartRate: !heartRate ? "Heart Rate is required" : "",
        sleepHour: !sleepHour ? "Sleep Hour is required" : "",
      });
      return;
    }

    const data = {
      heightCm,
      weightKg,
      bloodSugarLevel,
      bloodPressure,
      bloodGroup,
      heartRate,
      sleepHour,
      smoke,
    };

    setIsLoading(true);

    axiosInstance
      .post("/createHealthDAta", data) // Adjust the endpoint accordingly
      .then((resp) => {
        console.log(data);
        console.log("The Response", resp);
        setIsHealthDataCreated(true);
        toast.success("Health Data Created Successfully");
        // You might want to redirect to a different page after health data creation
        navigate("/");
      })
      .catch((error) => {
        toast.warn(error.response.data.message);
        setError(error);
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  return (
    <div className="create-health-data-container">
      <h1>Create Health Data</h1>
      {isHealthDataCreated && (
        <h2 style={{ color: "green" }}>Health Data Created Successfully</h2>
      )}
      {isLoading && <h1>Loading.....</h1>}
      <form onSubmit={handleCreateHealthData}>
        <div className="form-group double form-group-height">
          <div className="form-group-item">
            <label htmlFor="heightCm">Height (cm)</label>
            <input
              value={heightCm}
              id="heightCm"
              placeholder="Enter Height"
              type="number"
              onChange={(e) => setHeightCm(e.target.value)}
            />
            {errors.heightCm && (
              <p className="error-message">{errors.heightCm}</p>
            )}
          </div>
          <div className="form-group-item">
            <label htmlFor="weightKg">Weight (kg)</label>
            <input
              value={weightKg}
              id="weightKg"
              placeholder="Enter Weight"
              type="number"
              onChange={(e) => setWeightKg(e.target.value)}
            />
            {errors.weightKg && (
              <p className="error-message">{errors.weightKg}</p>
            )}
          </div>
        </div>

        <div className="form-group double">
          <div className="form-group-item">
            <label htmlFor="bloodSugarLevel">Blood Sugar Level</label>
            <input
              value={bloodSugarLevel}
              id="bloodSugarLevel"
              placeholder="Enter Blood Sugar Level"
              type="number"
              onChange={(e) => setBloodSugarLevel(e.target.value)}
            />
            {errors.bloodSugarLevel && (
              <p className="error-message">{errors.bloodSugarLevel}</p>
            )}
          </div>
          <div className="form-group-item">
            <label htmlFor="bloodPressure">Blood Pressure</label>
            <select
              value={bloodPressure}
              onChange={(e) => setBloodPressure(e.target.value)}
            >
              <option value="LOW">Low</option>
              <option value="NORMAL">Normal</option>
              <option value="HIGH">High</option>
            </select>
          </div>
        </div>

        <div className="form-group double">
          <div className="form-group-item">
            <label htmlFor="bloodGroup">Blood Group</label>
            <select
              value={bloodGroup}
              onChange={(e) => setBloodGroup(e.target.value)}
            >
              <option value="A_NEGATIVE">A Negative</option>
              <option value="A_POSITIVE">A Positive</option>
              <option value="B_NEGATIVE">B Negative</option>
              {/* Add other blood groups as needed */}
            </select>
          </div>
          <div className="form-group-item">
            <label htmlFor="heartRate">Heart Rate</label>
            <input
              value={heartRate}
              id="heartRate"
              placeholder="Enter Heart Rate"
              type="number"
              onChange={(e) => setHeartRate(e.target.value)}
            />
            {errors.heartRate && (
              <p className="error-message">{errors.heartRate}</p>
            )}
          </div>
        </div>

        <div className="form-group double">
          <div className="form-group-item">
            <label htmlFor="sleepHour">Sleep Hour</label>
            <input
              value={sleepHour}
              id="sleepHour"
              placeholder="Enter Sleep Hour"
              type="number"
              onChange={(e) => setSleepHour(e.target.value)}
            />
            {errors.sleepHour && (
              <p className="error-message">{errors.sleepHour}</p>
            )}
          </div>
          <div className="form-group-item">
            <label htmlFor="smoke">Smoke</label>
            <input
              type="checkbox"
              id="smoke"
              checked={smoke}
              onChange={(e) => setSmoke(e.target.checked)}
            />
          </div>
        </div>

        <button className="create-health-data-button" type="submit">
          Create Health Data
        </button>
      </form>
    </div>
  );
};

export default CreateHealthDataForm;
