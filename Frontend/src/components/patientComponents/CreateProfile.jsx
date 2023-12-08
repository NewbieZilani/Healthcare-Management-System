import { useState } from "react";
import axiosInstance from "../../utils/axiosInstance";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "../../css/patientCss/createProfile.css";

const CreateProfileForm = () => {
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [gender, setGender] = useState("Male"); // Default value is "Male"
  const [age, setAge] = useState("");
  const [contactInfo, setContactInfo] = useState("");
  const [address, setAddress] = useState("");
  const [isProfileCreated, setIsProfileCreated] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState();
  const [errors, setErrors] = useState({
    name: "",
    age: "",
    contactInfo: "",
    address: "",
  });

  const handleCreateProfile = (e) => {
    e.preventDefault();

    // Validation logic (you can customize this as needed)
    if (!name || !age || !contactInfo || !address) {
      setErrors({
        name: !name ? "Name is required" : "",
        age: !age ? "Age is required" : "",
        contactInfo: !contactInfo ? "Contact Info is required" : "",
        address: !address ? "Address is required" : "",
      });
      return;
    }

    const data = {
      name,
      gender,
      age,
      contactInfo,
      address,
    };

    setIsLoading(true);

    axiosInstance
      .post("/createProfile", data) // Adjust the endpoint accordingly
      .then((resp) => {
        console.log(data);
        console.log("The Response", resp);
        setIsProfileCreated(true);
        toast.success("Profile Created Successfully");
        // You might want to redirect to a different page after profile creation
        navigate("/");
      })
      .catch((error) => {
        toast.warn(error.response.data.message);
        setError(error);
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  return (
    <div className="create-profile-container">
      <h1>Create Profile</h1>
      {isProfileCreated && (
        <h2 style={{ color: "green" }}>Profile Created Successfully</h2>
      )}
      {isLoading && <h1>Loading.....</h1>}
      <form onSubmit={handleCreateProfile}>
        <div className="form-group">
          <label htmlFor="name">Name</label>
          <input
            value={name}
            id="name"
            placeholder="Enter Name"
            type="text"
            onChange={(e) => setName(e.target.value)}
          />
          {errors.name && <p className="error-message">{errors.name}</p>}
        </div>

        <div className="form-group">
          <label>Gender</label>
          <div className="radio-group">
            <label>
              <input
                type="radio"
                value="Male"
                checked={gender === "Male"}
                onChange={() => setGender("Male")}
              />
              Male
            </label>
            <label>
              <input
                type="radio"
                value="Female"
                checked={gender === "Female"}
                onChange={() => setGender("Female")}
              />
              Female
            </label>
          </div>
          {errors.gender && <p className="error-message">{errors.gender}</p>}
        </div>
        <div className="form-group">
          <label htmlFor="age">Age</label>
          <input
            value={age}
            id="age"
            placeholder="Enter Age"
            type="number"
            onChange={(e) => setAge(e.target.value)}
          />
          {errors.age && <p className="error-message">{errors.age}</p>}
        </div>

        <div className="form-group">
          <label htmlFor="contactInfo">Contact Info</label>
          <input
            value={contactInfo}
            id="contactInfo"
            placeholder="Enter Contact Info"
            type="text"
            onChange={(e) => setContactInfo(e.target.value)}
          />
          {errors.contactInfo && (
            <p className="error-message">{errors.contactInfo}</p>
          )}
        </div>

        <div className="form-group">
          <label htmlFor="address">Address</label>
          <input
            value={address}
            id="address"
            placeholder="Enter Address"
            type="text"
            onChange={(e) => setAddress(e.target.value)}
          />
          {errors.address && <p className="error-message">{errors.address}</p>}
        </div>

        <button className="create-profile-button" type="submit">
          Create Profile
        </button>
      </form>
    </div>
  );
};

export default CreateProfileForm;
