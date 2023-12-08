// // DoctorStatisticsCard.jsx
// import { useState, useEffect } from "react";
// import { doctorAxiosInstance } from "../../utils/axiosInstance";

// const DoctorStatisticsCard = () => {
//   const [totalDoctors, setTotalDoctors] = useState(0);

//   useEffect(() => {
//     const fetchTotalDoctors = async () => {
//       try {
//         const response = await doctorAxiosInstance.get("/totalDoctor");
//         setTotalDoctors(response.data);
//       } catch (error) {
//         console.error("Error fetching total doctors:", error);
//       }
//     };

//     fetchTotalDoctors();
//   }, []);

//   return (
//     <div className="statistics-card">
//       <h3>Total Doctors</h3>
//       <p>{totalDoctors}</p>
//     </div>
//   );
// };

// export default DoctorStatisticsCard;
