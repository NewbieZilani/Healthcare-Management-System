import React from "react";
import { Link, useNavigate, NavLink } from "react-router-dom";
import { Button, Dropdown } from "react-bootstrap";
import "../css/homeNavbar.css";

const HomeNavbar = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  return (
    <div className="HomeNavbar">
      <div className="HomeNavbar-child">
        <Link to="/">Healthcare Management System</Link>
      </div>

      <div className="HomeNavbar-auth">
        {token ? (
          role === "PATIENT" ? (
            <Dropdown className="custom-dropdown">
              <Dropdown.Toggle variant="success" id="dropdown-basic">
                User Account
              </Dropdown.Toggle>
              <Dropdown.Menu>
                <Dropdown.Item as={Link} to="/getProfileById">
                  My Profile
                </Dropdown.Item>
                <Dropdown.Divider />
                <Dropdown.Item
                  onClick={() => {
                    localStorage.removeItem("token");
                    localStorage.removeItem("role");
                    localStorage.removeItem("email");
                    localStorage.removeItem("userId");
                    navigate("/patient/login");
                  }}
                >
                  Logout
                </Dropdown.Item>
              </Dropdown.Menu>
            </Dropdown>
          ) : role === "DOCTOR" ? (
            <Dropdown className="custom-dropdown">
              <Dropdown.Toggle variant="success" id="dropdown-basic">
                Doctor Account
              </Dropdown.Toggle>
              <Dropdown.Menu>
                <Dropdown.Item as={Link} to="/getdoctorProfile">
                  Doctor Profile
                </Dropdown.Item>
                <Dropdown.Divider />
                <Dropdown.Item
                  onClick={() => {
                    localStorage.removeItem("token");
                    localStorage.removeItem("role");
                    localStorage.removeItem("email");
                    localStorage.removeItem("userId");
                    navigate("/doctor/login");
                  }}
                >
                  Logout
                </Dropdown.Item>
              </Dropdown.Menu>
            </Dropdown>
          ) : role === "ADMIN" ? (
            <Dropdown className="custom-dropdown">
              <Dropdown.Toggle variant="success" id="dropdown-basic">
                ADMIN
              </Dropdown.Toggle>
              <Dropdown.Menu>
                <Dropdown.Item as={Link} to="/admin">
                  ADMIN Dashboard
                </Dropdown.Item>
                <Dropdown.Divider />
                <Dropdown.Item
                  onClick={() => {
                    localStorage.removeItem("token");
                    localStorage.removeItem("role");
                    localStorage.removeItem("email");
                    localStorage.removeItem("userId");
                    navigate("/admin/login");
                  }}
                >
                  Logout
                </Dropdown.Item>
              </Dropdown.Menu>
            </Dropdown>
          ) : null
        ) : (
          <>
            <Dropdown className="custom-dropdown">
              <Dropdown.Toggle variant="primary" id="dropdown-basic">
                Login
              </Dropdown.Toggle>
              <Dropdown.Menu>
                <Dropdown.Item as={Link} to="/patient/login">
                  Login as a Patient
                </Dropdown.Item>
                <Dropdown.Item as={Link} to="/doctor/login">
                  Login as a Doctor
                </Dropdown.Item>
                <Dropdown.Item as={Link} to="/admin/login">
                  Login as an Admin
                </Dropdown.Item>
              </Dropdown.Menu>
            </Dropdown>
            <Dropdown className="custom-dropdown">
              <Dropdown.Toggle variant="primary" id="dropdown-basic">
                Register
              </Dropdown.Toggle>
              <Dropdown.Menu>
                <Dropdown.Item as={Link} to="/patient/register">
                  Register as a Patient
                </Dropdown.Item>
                <Dropdown.Item as={Link} to="/doctor/register">
                  Register as a Doctor
                </Dropdown.Item>
                {/* <Dropdown.Item as={Link} to="/admin/register">
                  Register as an Admin
                </Dropdown.Item> */}
              </Dropdown.Menu>
            </Dropdown>
          </>
        )}
      </div>
    </div>
  );
};

export default HomeNavbar;
