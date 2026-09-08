import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/axiosConfig";

export default function Dashboard() {
  const [message, setMessage] = useState("");
  const [userId, setUserId] = useState(null);
  const [projects, setProjects] = useState([]);
  const navigate = useNavigate();

  // Učitavanje info o korisniku
  useEffect(() => {
    const token = sessionStorage.getItem("token");
    if (!token) {
      navigate("/login");
      return;
    }

    api
      .get("/users/me", { headers: { Authorization: `Bearer ${token}` } })
      .then((res) => {
        setMessage(`Dobrodošao ${res.data.user.username}`);
        setUserId(res.data.user.id);
      })
      .catch(() => navigate("/login"));
  }, [navigate]);

  const handleLogout = () => {
    sessionStorage.removeItem("token");
    navigate("/login");
  };

  const handleShowProjects = async () => {
    if (!userId) return;
    const token = sessionStorage.getItem("token");

    try {
      const res = await api.get(`/usersnode/${userId}/projects`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setProjects(res.data); // niz projekata
    } catch (err) {
      console.error(err);
      alert("Greška prilikom učitavanja projekata");
    }
  };

  return (
    <div className="p-8 bg-gray-50 min-h-screen">
      <h1 className="text-2xl font-bold mb-4">{message}</h1>
      <button
        onClick={handleShowProjects}
        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 mb-6"
      >
        Prikaži moje projekte
      </button>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {projects.map((project) => (
          <Link key={project.id} to={`/projects/${project.id}`}>
            <div className="bg-white p-4 rounded shadow hover:shadow-lg transition cursor-pointer">
              <h2 className="text-xl font-bold">{project.name}</h2>
              <p className="text-gray-600 mt-2">{project.description}</p>
            </div>
          </Link>
        ))}
      </div>

      <button
        onClick={handleLogout}
        className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600 mt-6"
      >
        Logout
      </button>
    </div>
  );
}
