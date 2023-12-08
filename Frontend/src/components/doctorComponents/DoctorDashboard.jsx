import { useState, useEffect } from "react";
import { doctorAxiosInstance } from "../../utils/axiosInstance";
import DoctorDetails from "./DoctorDetails";
import CreateAppointment from "./CreateAppointmentSlot";
import AllSlots from "./GetAllSlot";
import BookedSLots from "./GetBookedSlots";

const DoctorDashboard = () => {
  const [selectedSection, setSelectedSection] = useState("details");
  const [doctorDetails, setDoctorDetails] = useState(null);

  useEffect(() => {
    const fetchUsers = async () => {
      const email = localStorage.getItem("email");
      try {
        const response = await doctorAxiosInstance.get(`/getByEmail/${email}`);
        console.log(response);
        setDoctorDetails(response.data);
      } catch (error) {
        console.log(error);
      }
    };
    fetchUsers();
  }, []);

  console.log(doctorDetails);
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
            Doctor Details
          </a>
          <a
            onClick={() => handleSectionChange("createAppointmentSlots")}
            className={
              selectedSection === "createAppointmentSlots" ? "active-link" : ""
            }
          >
            Create Apointment
          </a>
          <a
            onClick={() => handleSectionChange("allBookedSlots")}
            className={
              selectedSection === "allBookedSlots" ? "active-link" : ""
            }
          >
            Booked Slots
          </a>
          <a
            onClick={() => handleSectionChange("allSlots")}
            className={selectedSection === "allSlots" ? "active-link" : ""}
          >
            Get All slots
          </a>
        </div>
      </div>
      <div className="right-section">
        {selectedSection === "details" && (
          <DoctorDetails doctorDetails={doctorDetails} />
        )}
        {selectedSection === "createAppointmentSlots" && <CreateAppointment />}
        {selectedSection === "allBookedSlots" && <BookedSLots />}
        {selectedSection === "allSlots" && <AllSlots />}
      </div>
    </div>
  );
};

export default DoctorDashboard;
