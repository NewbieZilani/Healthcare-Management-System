import { useState, useEffect } from "react";
import axiosInstance from "../../utils/axiosInstance";
import PatientDetailsById from "./PatientDetailsById.jsx";
import "../../css/patientCss/PatientProfile.css";
import CreateProfileForm from "./CreateProfile.jsx";
import CreateHealth from "./CreateHealth.jsx";
import MyBookedAppointments from "./MyBookedAppointment.jsx";
import UpcomingAppointments from "./UpcomingAppointment.jsx";
import UpdateProfileForm from "./UpdateProfile.jsx";

const PatientProfile = () => {
  const [selectedSection, setSelectedSection] = useState("details");
  const [patientDetails, setPatientDetails] = useState(null);

  useEffect(() => {
    const fetchUsers = async () => {
      //const userId = localStorage.getItem("userId");
      try {
        const response = await axiosInstance.get("/getProfileById");
        console.log(response);
        setPatientDetails(response.data);
      } catch (error) {
        console.log(error);
      }
    };
    fetchUsers();
  }, []);

  console.log(patientDetails);
  const handleSectionChange = (section) => {
    setSelectedSection(section);
  };

  return (
    <div className="user-profile">
      <div className="left-section">
        <div className="links">
          <a
            onClick={() => handleSectionChange("details")}
            className={selectedSection === "details" ? "active-link" : ""}
          >
            User Details
          </a>
          <a
            onClick={() => handleSectionChange("createProfile")}
            className={selectedSection === "createProfile" ? "active-link" : ""}
          >
            Create Profile
          </a>
          <a
            onClick={() => handleSectionChange("updateProfile")}
            className={selectedSection === "updateProfile" ? "active-link" : ""}
          >
            Update Profile
          </a>
          <a
            onClick={() => handleSectionChange("createHealthData")}
            className={
              selectedSection === "createHealthData" ? "active-link" : ""
            }
          >
            Create HealthData
          </a>
          <a
            onClick={() => handleSectionChange("myBookedAppointments")}
            className={
              selectedSection === "myBookedAppointments" ? "active-link" : ""
            }
          >
            My Booked Appointment
          </a>
          <a
            onClick={() => handleSectionChange("myUpcomingAppointments")}
            className={
              selectedSection === "myUpcomingAppointments" ? "active-link" : ""
            }
          >
            Upcoming Appointment
          </a>
        </div>
      </div>
      <div className="right-section">
        {selectedSection === "details" && (
          <PatientDetailsById patientDetails={patientDetails} />
        )}
        {selectedSection === "createProfile" && <CreateProfileForm />}
        {selectedSection === "updateProfile" && <UpdateProfileForm />}
        {selectedSection === "createHealthData" && <CreateHealth />}
        {selectedSection === "myBookedAppointments" && <MyBookedAppointments />}
        {selectedSection === "myUpcomingAppointments" && (
          <UpcomingAppointments />
        )}
      </div>
    </div>
  );
};

export default PatientProfile;
