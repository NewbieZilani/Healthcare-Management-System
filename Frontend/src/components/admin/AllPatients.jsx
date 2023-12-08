// AllPatientsPage.jsx
import { useState, useEffect } from "react";
import "../../css/admincss/Allpatient.css";
import axiosInstance from "../../utils/axiosInstance";

const AllPatientsPage = () => {
  const [patients, setPatients] = useState([]);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    // Fetch all patients when the component mounts
    fetchAllPatients();
  }, []); // Empty dependency array ensures the effect runs only once

  const fetchAllPatients = async () => {
    setIsLoading(true);

    try {
      const response = await axiosInstance.get("/getAll");
      const data = response.data;

      // Assuming the data is an array of patients
      setPatients(data);
    } catch (error) {
      console.error("Error fetching patients:", error);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="all-patients-page">
      <h1>All Patients</h1>

      {/* Display Patients in Cards */}
      <div className="patient-cards">
        {isLoading ? (
          <p>Loading...</p>
        ) : (
          patients.map((patient) => (
            <div key={patient.userID} className="patient-card">
              {/* Display patient details in the card */}
              <p>Name: {patient.name}</p>
              <p>Gender: {patient.gender}</p>
              <p>Age: {patient.age}</p>
              <p>Contact Info: {patient.contactInfo}</p>
              <p>Address: {patient.address}</p>
              {/* Add more details as needed */}
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default AllPatientsPage;
