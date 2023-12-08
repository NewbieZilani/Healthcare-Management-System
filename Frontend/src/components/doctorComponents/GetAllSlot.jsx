import React, { useState, useEffect } from "react";
import Card from "react-bootstrap/Card";
import Row from "react-bootstrap/Row";
import Container from "react-bootstrap/Container";
import Form from "react-bootstrap/Form";
import Button from "react-bootstrap/Button";
import { useNavigate, useParams } from "react-router-dom";
import { doctorAxiosInstance } from "../../utils/axiosInstance";
import { useDoctorContext } from "../context/DoctorContext";
import { toast } from "react-toastify";
import "../../css/doctorcss/allSlot.css";

const SlotCard = ({ slot }) => (
  <Card key={slot.slotId} className="slot-card">
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
        <br />
        {slot.isAvailable ? (
          <Button variant="success" disabled>
            Available
          </Button>
        ) : (
          <Button variant="danger" disabled>
            Booked
          </Button>
        )}
      </Card.Text>
    </Card.Body>
  </Card>
);

const SlotPage = ({ slotData }) => (
  <Container>
    <h1 className="text-center mt-4">All Slots</h1>
    <Row xs={1} md={2} lg={3} className="g-4">
      {slotData.map((slot) => (
        <SlotCard key={slot.slotId} slot={slot} />
      ))}
    </Row>
  </Container>
);

const AllSlots = () => {
  const [slotData, setSlotData] = useState([]);
  const [selectedDate, setSelectedDate] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const { doctorId } = useParams();
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
      const userId = localStorage.getItem("userId");
      const response = await doctorAxiosInstance.get(
        `/getAllSlots/${userId}/${selectedDate}`
      ); // Replace with your actual endpoint
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
      <SlotPage slotData={slotData} />
    </div>
  );
};

export default AllSlots;
