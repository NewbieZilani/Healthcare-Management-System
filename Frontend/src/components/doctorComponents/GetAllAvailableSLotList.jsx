import React, { useState, useEffect } from "react";
import Card from "react-bootstrap/Card";
import Row from "react-bootstrap/Row";
import Container from "react-bootstrap/Container";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import Dropdown from "react-bootstrap/Dropdown";
import { useNavigate, useParams } from "react-router-dom";
import {
  appointmentAxiosInstance,
  doctorAxiosInstance,
} from "../../utils/axiosInstance";
import { useDoctorContext } from "../context/DoctorContext";
import { toast } from "react-toastify";

const SlotCard = ({ slot, handleMakeAppointment }) => (
  <Card key={slot.slotId} className="mb-4">
    <Card.Body>
      <Card.Title>Slot ID: {slot.slotId}</Card.Title>
      <Card.Text>
        Doctor ID: {slot.doctorId}
        <br />
        Start Time: {slot.startTime}
        <br />
        End Time: {slot.endTime}
        <br />
        Available: {slot.isAvailable.toString()}
        <br />
        Slot Date: {slot.slotDate}
      </Card.Text>
      <Form.Group controlId="appointmentType">
        <Form.Label>Appointment Type</Form.Label>
        <Dropdown>
          <Dropdown.Toggle variant="success" id="dropdown-basic">
            Select Type
          </Dropdown.Toggle>
          <Dropdown.Menu>
            <Dropdown.Item eventKey="OFFLINE">OFFLINE</Dropdown.Item>
            <Dropdown.Item eventKey="ONLINE">ONLINE</Dropdown.Item>
          </Dropdown.Menu>
        </Dropdown>
      </Form.Group>
      <Button
        onClick={() => handleMakeAppointment(slot.slotId)}
        className="mt-2"
      >
        Make Appointment
      </Button>
    </Card.Body>
  </Card>
);

const SlotPage = ({ slotData, handleMakeAppointment }) => (
  <Container>
    <h1 className="text-center mt-4">All Slots</h1>
    <Row xs={1} md={2} lg={3} className="g-4">
      {slotData.map((slot) => (
        <SlotCard
          key={slot.slotId}
          slot={slot}
          handleMakeAppointment={handleMakeAppointment}
        />
      ))}
    </Row>
  </Container>
);

const AllSlots = () => {
  const { doctorId } = useParams();
  const [slotData, setSlotData] = useState([]);
  const [selectedDate, setSelectedDate] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [appointmentType, setAppointmentType] = useState("OFFLINE");
  const navigate = useNavigate();
  const { selectedDoctorId } = useDoctorContext();

  useEffect(() => {
    if (selectedDoctorId) {
      // Fetch slots for the selected doctor
      fetchSlots();
    }
  }, [selectedDoctorId, doctorId]);

  const fetchSlots = async () => {
    try {
      setIsLoading(true);
      const response = await doctorAxiosInstance.get(
        `/getAllAvailableSlotDoctor/${doctorId}/${selectedDate}`
      );
      setSlotData(response.data);
    } catch (error) {
      toast.error(error.response.data);
    } finally {
      setIsLoading(false);
    }
  };

  const handleDateChange = (e) => {
    setSelectedDate(e.target.value);
  };

  const handleFetchSlots = () => {
    fetchSlots();
  };

  const handleMakeAppointment = async (slotId) => {
    try {
      const response = await appointmentAxiosInstance.post(
        `/book/${slotId}/${appointmentType}`
      );
      toast.success("Appointment booked successfully!");
    } catch (error) {
      toast.error(error.response.data);
    }
  };

  return (
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
        <Button variant="primary" onClick={handleFetchSlots}>
          Fetch Available Slots
        </Button>
      </Form>
      {isLoading && <p>Loading...</p>}
      <SlotPage
        slotData={slotData}
        handleMakeAppointment={handleMakeAppointment}
      />
    </div>
  );
};

export default AllSlots;
