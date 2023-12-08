// axiosInstance.js

import axios from "axios";

export const axiosInstance = axios.create({
  //baseURL: 'http://localhost:8084/user-service/users',
  baseURL: 'http://localhost:8091/users',
  headers: {
    'Content-Type': 'application/json',
  },
});

axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");

  if (token) {
    config.headers.Authorization = `${token}`;
  }

  return config;
});

export const doctorAxiosInstance = axios.create({
  baseURL: 'http://localhost:8082/doctor',
  headers: {
    'Content-Type': 'application/json',
  },
});
doctorAxiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");

  if (token) {
    config.headers.Authorization = `${token}`;
  }

  return config;
});

export const appointmentAxiosInstance = axios.create({
  baseURL: 'http://localhost:8082/appointment',
  headers: {
    'Content-Type': 'application/json',
  },
});
appointmentAxiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  console.log("before token");
  console.log(token);

  if (token) {
    config.headers.Authorization = `${token}`;
  }

  return config;
});


export default axiosInstance;
