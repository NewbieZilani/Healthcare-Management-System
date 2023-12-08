import { useState } from "react";
import { doctorAxiosInstance } from "../../utils/axiosInstance";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "../../css/doctorcss/createAppointmentSlot.css";

const CreateAppointmentSlot = () => {
  const [startTime, setStartTime] = useState("");
  const [duration, setDuration] = useState("");
  const [slotDate, setSlotDate] = useState("");
  const [isSlotCreated, setIsSlotCreated] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [errors, setErrors] = useState({
    startTime: "",
    duration: "",
    slotDate: "",
  });

  const handleCreateSlot = (e) => {
    e.preventDefault();

    // Validation logic (you can customize this as needed)
    if (!startTime || !duration || !slotDate) {
      setErrors({
        startTime: !startTime ? "Start Time is required" : "",
        duration: !duration ? "Duration is required" : "",
        slotDate: !slotDate ? "Slot Date is required" : "",
      });
      return;
    }

    // Format date and time
    const formattedDate = new Date(slotDate).toISOString().split("T")[0]; // Format: YYYY-MM-DD
    const formattedTime = startTime + ":00"; // Add ':00' for seconds

    const data = {
      startTime: formattedTime,
      duration,
      slotDate: formattedDate,
    };

    setIsLoading(true);

    doctorAxiosInstance
      .post("/createSlots", data) // Adjust the endpoint accordingly
      .then((resp) => {
        console.log(data);
        console.log("The Response", resp);
        setIsSlotCreated(true);
        toast.success("Appointment Slot Created Successfully");
        // You might want to reset the form or redirect to a different page
      })
      .catch((error) => {
        console.log(error.response.data.message);
        // Handle error, show a message, etc.
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  return (
    <div className="create-appointment-slot-container">
      <h1>Create Appointment Slot</h1>
      {isSlotCreated && (
        <h2 style={{ color: "green" }}>
          Appointment Slot Created Successfully
        </h2>
      )}
      {isLoading && <h1>Loading.....</h1>}
      <form onSubmit={handleCreateSlot}>
        <div className="form-group">
          <label htmlFor="startTime">Start Time</label>
          <input
            value={startTime}
            id="startTime"
            placeholder="Enter Start Time"
            type="time"
            onChange={(e) => setStartTime(e.target.value)}
          />
          {errors.startTime && (
            <p className="error-message">{errors.startTime}</p>
          )}
        </div>

        <div className="form-group">
          <label htmlFor="duration">Duration (in minutes)</label>
          <input
            value={duration}
            id="duration"
            placeholder="Enter Duration"
            type="number"
            onChange={(e) => setDuration(e.target.value)}
          />
          {errors.duration && (
            <p className="error-message">{errors.duration}</p>
          )}
        </div>

        <div className="form-group">
          <label htmlFor="slotDate">Slot Date</label>
          <input
            value={slotDate}
            id="slotDate"
            placeholder="Enter Slot Date"
            type="date"
            onChange={(e) => setSlotDate(e.target.value)}
          />
          {errors.slotDate && (
            <p className="error-message">{errors.slotDate}</p>
          )}
        </div>

        <button className="create-slot-button" type="submit">
          Create Appointment Slot
        </button>
      </form>
    </div>
  );
};

export default CreateAppointmentSlot;
