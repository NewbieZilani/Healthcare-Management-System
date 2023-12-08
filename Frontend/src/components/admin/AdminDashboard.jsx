import { useState, useEffect } from "react";
import axiosInstance from "../../utils/axiosInstance";
import "../../css/patientCss/PatientProfile.css";
import AllPatientsPage from "./AllPatients";
import AllDoctor from "./AllDoctor";
import AdminPanel from "./AdminPanel";

const AdminDashboard = () => {
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
            onClick={() => handleSectionChange("adminDashboard")}
            className={
              selectedSection === "adminDashboard" ? "active-link" : ""
            }
          >
            Admin DashBoard
          </a>
          <a
            onClick={() => handleSectionChange("allPatients")}
            className={selectedSection === "allPatients" ? "active-link" : ""}
          >
            Patients List
          </a>
          <a
            onClick={() => handleSectionChange("allDoctors")}
            className={selectedSection === "allDoctors" ? "active-link" : ""}
          >
            Doctors List
          </a>
          {/* <a
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
          </a> */}
        </div>
      </div>
      <div className="right-section">
        {/* {selectedSection === "details" && (
          <PatientDetailsById patientDetails={patientDetails} />
        )}
        {selectedSection === "createProfile" && <CreateProfileForm />}
        {selectedSection === "createHealthData" && <CreateHealth />} */}
        {selectedSection === "adminDashboard" && <AdminPanel />}
        {selectedSection === "allPatients" && <AllPatientsPage />}
        {selectedSection === "allDoctors" && <AllDoctor />}

        {/* {selectedSection === "myUpcomingAppointments" && (
          <UpcomingAppointments />
        )} */}
      </div>
    </div>
  );
};

export default AdminDashboard;
