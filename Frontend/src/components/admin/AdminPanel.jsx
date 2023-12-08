import { useState, useEffect } from "react";
import axiosInstance, { doctorAxiosInstance } from "../../utils/axiosInstance";
import "../../css/admincss/AdminPanel.css";
import StatisticsChart from "./StatisticsChart";

const AdminPanel = () => {
  const [totalDoctors, setTotalDoctors] = useState(0);
  const [totalPatients, setTotalPatients] = useState(0);

  useEffect(() => {
    const fetchStatistics = async () => {
      try {
        const doctorsResponse = await doctorAxiosInstance.get("/totalDoctor");
        setTotalDoctors(doctorsResponse.data);

        const patientsResponse = await axiosInstance.get("/totalPatient");
        setTotalPatients(patientsResponse.data);
      } catch (error) {
        console.error("Error fetching statistics:", error);
      }
    };

    fetchStatistics();
  }, []);

  return (
    <div className="admin-dashboard">
      <div className="statistics-card">
        <h3>Total Doctors</h3>
        <p>{totalDoctors}</p>
      </div>
      <div className="statistics-card">
        <h3>Total Patients</h3>
        <p>{totalPatients}</p>
      </div>
      <StatisticsChart />
    </div>
  );
};

export default AdminPanel;
