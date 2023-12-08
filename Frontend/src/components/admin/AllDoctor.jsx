// DoctorsListPage.jsx
import { useState, useEffect } from "react";
import { doctorAxiosInstance } from "../../utils/axiosInstance";
import "../../css/admincss/DoctorList.css";

const DoctorsListPage = () => {
  const [doctors, setDoctors] = useState([]);
  const [selectedDoctor, setSelectedDoctor] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [isFetchingDetails, setIsFetchingDetails] = useState(false);

  useEffect(() => {
    // Fetch doctors when the component mounts
    fetchDoctors();
  }, []); // Empty dependency array ensures the effect runs only once

  const fetchDoctors = async () => {
    setIsLoading(true);

    try {
      const response = await doctorAxiosInstance.get("/getAllDoctor");
      const data = response.data;

      // Assuming the data is an array of doctors
      setDoctors(data);
    } catch (error) {
      console.error("Error fetching doctors:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleShowDetails = async (doctor) => {
    setIsFetchingDetails(true);

    try {
      const response = await doctorAxiosInstance.get(
        `/getById/${doctor.doctor_id}`
      );
      const data = response.data;

      // Set the detailed information for the selected doctor
      setSelectedDoctor(data);
    } catch (error) {
      console.error("Error fetching doctor details:", error);
    } finally {
      setIsFetchingDetails(false);
    }
  };

  const handleCloseDetails = () => {
    // Clear the selected doctor when closing the details modal
    setSelectedDoctor(null);
  };

  const handleDisableDoctor = async (doctorId) => {
    try {
      const response = await doctorAxiosInstance.post(
        `/disableDoctor/${doctorId}`
      );
      console.log(response.data);
      // Refresh the list after disabling a doctor
      fetchDoctors();
    } catch (error) {
      console.error("Error disabling doctor:", error);
    }
  };

  const handleEnableDoctor = async (doctorId) => {
    try {
      const response = await doctorAxiosInstance.post(`/verify/${doctorId}`);
      console.log(response.data);
      // Refresh the list after enabling a doctor
      fetchDoctors();
    } catch (error) {
      console.error("Error enabling doctor:", error);
    }
  };

  return (
    <div className="doctors-list-page">
      <h1>Doctors List</h1>

      {/* Display Doctors in Cards */}
      <div className="doctor-cards">
        {isLoading ? (
          <p>Loading...</p>
        ) : (
          doctors.map((doctor) => (
            <div key={doctor.doctor_id} className="doctor-card">
              {/* Display doctor details in the card */}
              <p>Doctor Name: {doctor.name}</p>
              <p>Qualifications: {doctor.qualifications}</p>
              <p>Department: {doctor.department}</p>
              <button onClick={() => handleShowDetails(doctor)}>
                Show Details
              </button>
              {doctor.isValid ? (
                <button
                  onClick={() => handleDisableDoctor(doctor.doctor_id)}
                  className="disable-btn"
                >
                  Disable
                </button>
              ) : (
                <button onClick={() => handleEnableDoctor(doctor.doctor_id)}>
                  Enable
                </button>
              )}
            </div>
          ))
        )}
      </div>

      {/* Doctor Details Modal */}
      {/* Doctor Details Modal */}
      {selectedDoctor && (
        <div className="modal">
          <div className="modal-content">
            <span className="close" onClick={handleCloseDetails}>
              &times;
            </span>
            <h2>Doctor Details</h2>
            {isFetchingDetails ? (
              <p>Loading details...</p>
            ) : (
              <>
                <p>Name: {selectedDoctor.name}</p>
                <p>Email: {selectedDoctor.email}</p>
                <p>Role: {selectedDoctor.role}</p>
                <p>Gender: {selectedDoctor.gender}</p>
                <p>Department: {selectedDoctor.department}</p>
                {/* Add more details as needed */}
                {/* You can add more fields here similar to the above lines */}
              </>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default DoctorsListPage;
