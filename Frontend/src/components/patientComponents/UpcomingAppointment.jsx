import React, { useState, useEffect } from "react";
import { appointmentAxiosInstance } from "../../utils/axiosInstance";
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";

const UpcomingAppointments = () => {
  const [appointments, setAppointments] = useState([]);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    // Fetch upcoming appointments when the component mounts
    fetchUpcomingAppointments();
  }, []); // Empty dependency array ensures the effect runs only once

  const fetchUpcomingAppointments = async () => {
    setIsLoading(true);

    try {
      const response = await appointmentAxiosInstance.get(
        "/getUpcomingAppointments"
      );
      const data = response.data;

      // Assuming the data is an array of upcoming appointments
      setAppointments(data);
    } catch (error) {
      console.error("Error fetching upcoming appointments:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleCancelAppointment = async (appointmentId) => {
    try {
      // Replace "dsfsdf" with the actual endpoint to cancel an appointment
      const response = await appointmentAxiosInstance.post(
        `/cancel/${appointmentId}`
      );
      console.log("Appointment canceled successfully:", response.data);

      // Fetch the updated list of upcoming appointments after cancellation
      fetchUpcomingAppointments();
    } catch (error) {
      console.error("Error canceling appointment:", error);
    }
  };

  return (
    <div className="upcoming-appointments">
      <h1>Upcoming Appointments</h1>

      {/* Display Appointments in Rows with at least 3 Cards */}
      <Row xs={1} md={3} className="g-4">
        {isLoading ? (
          <p>Loading...</p>
        ) : (
          appointments.map((appointment) => (
            <Col key={appointment.appointmentId}>
              <Card className="mb-4">
                <Card.Body>
                  {/* Display appointment details in the card */}
                  <Card.Title>Doctor Name: {appointment.doctorName}</Card.Title>
                  <Card.Text>
                    <p>Patient Name: {appointment.patientName}</p>
                    <p>Appointment Date: {appointment.appointmentDate}</p>
                    <p>Appointment Time: {appointment.appointmentTime}</p>
                    <p>Appointment Type: {appointment.appointmentType}</p>
                  </Card.Text>
                  {/* Cancel Appointment Button */}
                  <Button
                    variant="danger"
                    onClick={() =>
                      handleCancelAppointment(appointment.appointmentId)
                    }
                  >
                    Cancel Appointment
                  </Button>
                </Card.Body>
              </Card>
            </Col>
          ))
        )}
      </Row>
    </div>
  );
};

export default UpcomingAppointments;
