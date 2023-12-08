import { useState, useEffect } from "react";
import axiosInstance from "../../utils/axiosInstance";
import { useNavigate, useParams } from "react-router-dom";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "../../css/patientCss/updateProfile.css";

const UpdateProfileForm = () => {
  const navigate = useNavigate();
  const { id } = useParams(); // Assuming you have a route parameter for the profile ID

  const [name, setName] = useState("");
  const [gender, setGender] = useState("Male");
  const [age, setAge] = useState("");
  const [contactInfo, setContactInfo] = useState("");
  const [address, setAddress] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState();
  const [errors, setErrors] = useState({
    name: "",
    age: "",
    contactInfo: "",
    address: "",
  });

  useEffect(() => {
    // Fetch the existing profile data using the profile ID
    setIsLoading(true);

    axiosInstance
      .get("/getProfileById") // Adjust the endpoint accordingly
      .then((resp) => {
        const profileData = resp.data; // Assuming the response contains profile data
        setName(profileData.name);
        setGender(profileData.gender);
        setAge(profileData.age);
        setContactInfo(profileData.contactInfo);
        setAddress(profileData.address);
      })
      .catch((error) => {
        toast.error("Error fetching profile data");
        setError(error);
      })
      .finally(() => {
        setIsLoading(false);
      });
  }, [id]);

  const handleUpdateProfile = (e) => {
    e.preventDefault();

    // Validation logic (similar to create profile)
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
      .put(`/updateUserProfile`, data) // Adjust the endpoint accordingly
      .then(() => {
        toast.success("Profile Updated Successfully");
        navigate("/getProfileById");
      })
      .catch((error) => {
        toast.error("Error updating profile");
        setError(error);
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  return (
    <div className="update-profile-container">
      <h1>Update Profile</h1>
      {isLoading && <h1>Loading...</h1>}
      <form onSubmit={handleUpdateProfile}>
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
        <button className="update-profile-button" type="submit">
          Update Profile
        </button>
      </form>
    </div>
  );
};

export default UpdateProfileForm;
