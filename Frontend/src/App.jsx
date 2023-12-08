import { Route, Routes } from "react-router-dom";
import { ToastContainer, Zoom } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import PatientRegistrationForm from "./components/patientComponents/Register";
import DoctorLoginForm from "./components/doctorComponents/Login.jsx";
import PatientLoginForm from "./components/patientComponents/Login";
//import Navbar from "./components/patientComponents/navbar.jsx";
import CreateProfileForm from "./components/patientComponents/CreateProfile.jsx";
import PatientProfile from "./components/patientComponents/PatientProfile.jsx";
import HomeNavbar from "./components/HomeNavbar.jsx";
import DoctorDashborad from "./components/doctorComponents/DoctorDashboard.jsx";
//import AllDoctor from "./components/doctorComponents/DoctorsList.jsx";
import Authenticate from "./components/Authenticate.jsx";
import Home from "./components/HomePage.jsx";
import AllSlots from "./components/doctorComponents/GetAllAvailableSLotList.jsx";
import AdminDashboard from "./components/admin/AdminDashboard.jsx";
import AdminLoginForm from "./components/admin/Login.jsx";
import DoctorRegistrationForm from "./components/doctorComponents/Register.jsx";
import Telemedicine from "./components/Telemedicine.jsx";
import AdminPanel from "./components/admin/AdminPanel.jsx";
import AllDoctorAdmin from "./components/admin/AllDoctor.jsx";
import AllPatientsAdmin from "./components/admin/AllPatients.jsx";
import StatisticsChart from "./components/admin/StatisticsChart.jsx";
import UpdateProfileForm from "./components/patientComponents/UpdateProfile.jsx";
import PatientDetailsById from "./components/patientComponents/PatientDetailsById.jsx";
import UnAuthenticate from "./components/Unauthenticate.jsx";

function App() {
  return (
    <>
      <div>
        <HomeNavbar />
        <></>
      </div>
      <Routes>
        <Route path="/" exact element={<Home />} />
        <Route
          path="/room/:roomId"
          element={<Telemedicine key={window.location.pathname} />}
        />
        <Route element={<UnAuthenticate />}>
          <Route path="/patient/login" element={<PatientLoginForm />} />
          <Route
            path="/patient/register"
            element={<PatientRegistrationForm />}
          />
          <Route path="/doctor/register" element={<DoctorRegistrationForm />} />
          <Route path="/admin/login" element={<AdminLoginForm />} />
          <Route path="/doctor/login" element={<DoctorLoginForm />} />
        </Route>

        <Route element={<Authenticate expectedRole="PATIENT" />}>
          <Route path="/createProfile" element={<CreateProfileForm />} />
          <Route path="/getProfileById" element={<PatientProfile />} />
          <Route path="/update" element={<UpdateProfileForm />} />
          <Route path="/details" element={<PatientDetailsById />} />
          <Route path="/getAllavailableSlot/:doctorId" element={<AllSlots />} />
        </Route>
        {/* <Route path="/doctor/register" exact element={< />} /> */}
        <Route element={<Authenticate expectedRole="DOCTOR" />}>
          <Route path="/getdoctorProfile" element={<DoctorDashborad />} />
        </Route>
        {/* <Route path="/" exact element={<RegistrationForm />} /> */}
        <Route element={<Authenticate expectedRole="ADMIN" />}>
          <Route path="/admin" element={<AdminDashboard />} />
          <Route path="/adminPanel" element={<AdminPanel />} />
          <Route path="/allDoctorAdmin" element={<AllDoctorAdmin />} />
          <Route path="/allPatientAdmin" element={<AllPatientsAdmin />} />
          <Route path="/statistics" element={<StatisticsChart />} />
        </Route>
      </Routes>
      <ToastContainer
        position="top-center"
        autoClose={2000}
        pauseOnHover={false}
        transition={Zoom}
      />
    </>
  );
}

export default App;
