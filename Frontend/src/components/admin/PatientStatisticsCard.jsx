// // DoctorStatisticsCard.jsx
// import { useState, useEffect } from "react";
// import axiosInstance from "../../utils/axiosInstance";
// import "../../css/admincss/PatientStatisticCard.css";

// const PatientStatisticsCard = () => {
//   const [totalPatients, setTotalPatients] = useState(0);

//   useEffect(() => {
//     const fetchTotalDoctors = async () => {
//       try {
//         const response = await axiosInstance.get("/totalPatient");
//         setTotalPatients(response.data);
//       } catch (error) {
//         console.error("Error fetching total doctors:", error);
//       }
//     };

//     fetchTotalDoctors();
//   }, []);

//   return (
//     <div className="statistics-card">
//       <h3>Total Patients</h3>
//       <p>{totalPatients}</p>
//     </div>
//   );
// };

// export default PatientStatisticsCard;
