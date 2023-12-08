import "../../css/doctorcss/doctorDetails.css";

const DoctorDetails = ({ doctorDetails }) => {
  if (!doctorDetails) {
    return <p>Loading...</p>;
  }
  const centerTitleStyle = {
    textAlign: "center",
    fontSize: "24px", // You can adjust the font size as needed
    fontWeight: "bold",
    color: "#2c3e50",
    marginBottom: "40px",
  };
  return (
    <>
      <h1 style={centerTitleStyle}>Doctor Details</h1>
      <div className="doctor-details-card">
        <div className="details-container">
          <div className="left-side">
            {/* Display the image if imageUrl is available */}
            {doctorDetails.image && (
              <img
                src={doctorDetails.image}
                alt={`${doctorDetails.name}'s image`}
                className="doctor-image"
              />
            )}
          </div>
          <div className="right-side">
            <p>
              <strong>Name:</strong> {doctorDetails.name}
            </p>
            <p>
              <strong>Email:</strong> {doctorDetails.email}
            </p>
            <p>
              <strong>Role:</strong> {doctorDetails.role}
            </p>
            <p>
              <strong>Gender:</strong> {doctorDetails.gender}
            </p>
            <p>
              <strong>Registration Number (BDMC):</strong>{" "}
              {doctorDetails.registration_number_BDMC}
            </p>
            <p>
              <strong>Allocated Room:</strong> {doctorDetails.allocated_room}
            </p>
            <p>
              <strong>Qualifications:</strong> {doctorDetails.qualifications}
            </p>
            <p>
              <strong>Availability:</strong>{" "}
              {doctorDetails.isAvailable ? "Yes" : "No"}
            </p>
            <p>
              <strong>Validity:</strong> {doctorDetails.isValid ? "Yes" : "No"}
            </p>
          </div>
        </div>
      </div>
    </>
  );
};

export default DoctorDetails;
