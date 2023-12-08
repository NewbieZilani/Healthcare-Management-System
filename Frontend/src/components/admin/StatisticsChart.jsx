// StatisticsChart.jsx
import { useEffect, useState } from "react";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { Bar } from "react-chartjs-2";
import axiosInstance, { doctorAxiosInstance } from "../../utils/axiosInstance";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

const StatisticsChart = () => {
  const [chartData, setChartData] = useState({
    datasets: [],
  });

  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: "top",
      },
      title: {
        display: true,
        text: "Number of Doctors and Patients",
      },
    },
  };

  useEffect(() => {
    const fetchData = async () => {
      const doctorsResponse = await doctorAxiosInstance.get("/totalDoctor");
      const patientsResponse = await axiosInstance.get("/totalPatient");

      setChartData({
        labels: ["Doctors", "Patients"],
        datasets: [
          {
            label: "Count",
            data: [doctorsResponse.data, patientsResponse.data],
            backgroundColor: [
              "rgba(75, 192, 192, 0.5)",
              "rgba(255, 99, 132, 0.5)",
            ],
          },
        ],
      });
    };

    fetchData();
  }, []);

  return <Bar options={options} data={chartData} />;
};

export default StatisticsChart;
