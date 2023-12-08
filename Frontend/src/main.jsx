import ReactDOM from "react-dom/client";
import App from "./App.jsx";
import { BrowserRouter } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import { DoctorProvider } from "./components/context/DoctorContext.jsx";
import "@popperjs/core/dist/umd/popper.min.js";
import "bootstrap/dist/js/bootstrap.min.js";

ReactDOM.createRoot(document.getElementById("root")).render(
  <BrowserRouter>
    <DoctorProvider>
      <App />
    </DoctorProvider>
  </BrowserRouter>
);
