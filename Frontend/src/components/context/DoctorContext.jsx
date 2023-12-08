import { createContext, useContext, useState } from "react";

const DoctorContext = createContext();

export const DoctorProvider = ({ children }) => {
  const [selectedDoctorId, setSelectedDoctorId] = useState(null);

  const selectDoctor = (doctorId) => {
    setSelectedDoctorId(doctorId);
  };

  return (
    <DoctorContext.Provider value={{ selectedDoctorId, selectDoctor }}>
      {children}
    </DoctorContext.Provider>
  );
};

export const useDoctorContext = () => {
  const context = useContext(DoctorContext);
  if (!context) {
    throw new Error("useDoctorContext must be used within a DoctorProvider");
  }
  return context;
};
