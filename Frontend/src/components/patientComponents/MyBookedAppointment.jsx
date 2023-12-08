import { useState } from "react";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { appointmentAxiosInstance } from "../../utils/axiosInstance";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Card from "react-bootstrap/Card";
import Row from "react-bootstrap/Row";
import Col from "react-bootstrap/Col";

const AppointmentsPage = () => {
  const [selectedDate, setSelectedDate] = useState(null);
  const [appointments, setAppointments] = useState([]);
  const [isLoading, setIsLoading] = useState(false);

  const handleDateChange = (e) => {
    setSelectedDate(e.target.value);
  };

  const handleBookAppointments = async () => {
    if (!selectedDate) {
      console.error("Please select a date.");
      return;
    }

    setIsLoading(true);

    try {
      const response = await appointmentAxiosInstance.get(
        `/getCurrentAppointments/${selectedDate}`
      );
      const data = response.data;

      // Assuming the data is an array of appointments
      setAppointments(data);
    } catch (error) {
      console.error("Error fetching appointments:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleJoinAppointment = (appointmentId) => {
    // Handle logic when joining an online appointment
    window.open(`/room/${appointmentId}`, "_blank");
    // Add your API call or other logic here
  };

  return (
    <div className="appointments-page">
      <h1>My booked Appointments</h1>

      {/* Date Picker */}
      <div>
        <Form className="mb-4">
          <Form.Group controlId="datePicker">
            <Form.Label>Select Date</Form.Label>
            <Form.Control
              type="date"
              value={selectedDate}
              onChange={handleDateChange}
            />
          </Form.Group>
          <Button
            variant="primary"
            onClick={handleBookAppointments}
            disabled={isLoading}
          >
            {isLoading ? "Loading..." : "Fetch Available Slots"}
          </Button>
        </Form>
      </div>

      {/* Display Appointments in Cards */}
      <Row xs={1} md={3} className="g-4 justify-content-center">
        {appointments.map((appointment) => (
          <Col key={appointment.appointmentId}>
            <Card className="mb-3">
              <Card.Body>
                <Card.Title>
                  Appointment ID: {appointment.appointmentId}
                </Card.Title>
                {/* Display appointment details in the card */}
                <Card.Text>Doctor Name: {appointment.doctorName}</Card.Text>
                <Card.Text>Patient Name: {appointment.patientName}</Card.Text>
                <Card.Text>
                  Appointment Type: {appointment.appointmentType}
                </Card.Text>
                <Card.Text>
                  Appointment Time: {appointment.appointmentTime}
                </Card.Text>

                {/* Add "Join" button for online appointments */}
                {appointment.appointmentType === "ONLINE" && (
                  <Button
                    variant="success"
                    onClick={() => handleJoinAppointment(5)}
                  >
                    Join Telemedicine
                  </Button>
                )}
              </Card.Body>
            </Card>
          </Col>
        ))}
      </Row>
    </div>
  );
};

export default AppointmentsPage;
