import { useEffect, useState } from "react";
import Card from "react-bootstrap/Card";
import Image from "react-bootstrap/Image";
import Row from "react-bootstrap/Row";
import Container from "react-bootstrap/Container";
import Button from "react-bootstrap/Button";
import { Link, useNavigate } from "react-router-dom";
import { doctorAxiosInstance } from "../../utils/axiosInstance";
import { useDoctorContext } from "../context/DoctorContext";

const AllDoctor = () => {
  const { selectDoctor } = useDoctorContext();
  const navigate = useNavigate();
  const [doctors, setDoctors] = useState([]);

  const getAllDoctors = async () => {
    try {
      const response = await doctorAxiosInstance.get("/getAllAvailableDoctor");
      setDoctors(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  const handleMakeAppointment = (doctorId) => {
    const userRole = localStorage.getItem("role");
    if (userRole === "PATIENT") {
      navigate(`/getAllavailableSlot/${doctorId}`);
    } else navigate("/patient/login");
  };

  useEffect(() => {
    getAllDoctors();
  }, []);

  return (
    <div>
      <Container>
        <h1 className="text-center mt-4">Doctor List</h1>
        <Row xs={1} md={2} lg={4} className="g-4">
          {doctors.map((doctor) => (
            <Card
              key={doctor.doctor_id}
              className="mb-4"
              style={{ width: "15rem" }}
            >
              <div className="image-container">
                <Image
                  src={doctor.image}
                  alt={doctor.name}
                  fluid
                  style={{ width: "14rem", height: "15rem" }}
                />
                <Card.Body>
                  <Card.Title>{doctor.name}</Card.Title>
                  <Card.Text>Email: {doctor.email}</Card.Text>
                </Card.Body>
                <div className="make-appointment-button">
                  {/* <Button
                    onClick={() => handleMakeAppointment(doctor.doctor_id)}
                  >
                    Make an Appointment
                  </Button> */}

                  {/* <Link to={`/getAllavailableSlot/${doctor.doctor_id}`}> */}
                  <Button
                    onClick={() => handleMakeAppointment(doctor.doctor_id)}
                  >
                    Make an Appointmnent
                  </Button>
                  {/* </Link> */}
                </div>
              </div>
            </Card>
          ))}
        </Row>
      </Container>
    </div>
  );
};

export default AllDoctor;
