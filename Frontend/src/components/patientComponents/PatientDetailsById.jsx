// PatientDetailsById.jsx
import "../../css/patientCss/PatientDetailsById.css";

const PatientDetailsById = ({ patientDetails }) => {
  if (!patientDetails) {
    return <div>Loading...</div>;
  }

  return (
    <div className="patient-details-card">
      <div className="patient-header">
        <img
          src={patientDetails.image}
          alt="Patient"
          className="patient-image"
        />
        <div className="patient-info">
          <h1>{patientDetails.name}</h1>
          <p className="patient-id">User ID: {patientDetails.userID}</p>
          <p className="patient-contact">
            Contact Info: {patientDetails.contactInfo}
          </p>
          <p className="patient-address">Address: {patientDetails.address}</p>
        </div>
      </div>
      <div className="patient-body">
        <div className="detail-field">
          <label>Email</label>
          <p className="detail-value">{patientDetails.email}</p>
        </div>
        <div className="detail-field">
          <label>Gender</label>
          <p className="detail-value">{patientDetails.gender}</p>
        </div>
        <div className="detail-field">
          <label>Age</label>
          <p className="detail-value">{patientDetails.age}</p>
        </div>
      </div>
    </div>
  );
};

export default PatientDetailsById;
