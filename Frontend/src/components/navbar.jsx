import { Link, useNavigate, NavLink } from "react-router-dom";
import { Button, Dropdown } from "react-bootstrap";
import "../css/navbar.css";

const Navbar = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  return (
    <div className="Navbar">
      <div className="Navbar-child">
        <Link to="/">Healthcare Management System</Link>
        {token && role === "PATIENT" && (
          <>{/* Add more navigation links for customers */}</>
        )}
        {token && role === "ADMIN" && (
          <>
            <Link to="/books">ADMIN</Link>
            <Link to="/users">User List</Link>
            <Link to="/books/all">Book List</Link>
          </>
        )}
      </div>

      <div className="Navbar-auth">
        {!token ? (
          <>
            <Link to="/register" activeClassName="active-link">
              Register
            </Link>
            <NavLink to="/login" activeClassName="active-link">
              Login
            </NavLink>
          </>
        ) : (
          role === "PATIENT" && (
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
                    navigate("/login");
                  }}
                >
                  Logout
                </Dropdown.Item>
              </Dropdown.Menu>
            </Dropdown>
          )
        )}
        {token && role === "ADMIN" && (
          <Button
            onClick={() => {
              localStorage.removeItem("token");
              navigate("/login");
            }}
          >
            Logout
          </Button>
        )}
      </div>
    </div>
  );
};

export default Navbar;
