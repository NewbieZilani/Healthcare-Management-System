import { Navigate, Outlet } from "react-router-dom";

const UnAuthenticate = () => {
  const token = localStorage.getItem("token");

  return <div>{token ? <Navigate to="/" /> : <Outlet />}</div>;
};

export default UnAuthenticate;
