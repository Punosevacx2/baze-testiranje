import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";

export default function ProjectDetails() {
  const { projectId } = useParams(); // ID projekta iz URL
  const navigate = useNavigate();
  console.log(projectId);

  const [project, setProject] = useState(null);
  const [users, setUsers] = useState([]);
  const [tasks, setTasks] = useState([]);

  useEffect(() => {
    const token = sessionStorage.getItem("token");
    if (!token) {
      navigate("/login");
      return;
    }
 
    // 1. Učitaj osnovni projekat
    api
      .get(`/projects/${projectId}`, { headers: { Authorization: `Bearer ${token}` } })
      .then((res) => {setProject(res.data)
      }) 

      // JSON je niz sa jednim objektom
      .catch((err) => console.error(err));

    // 2. Učitaj korisnike projekta
   
    api
  .get(`/relations/projects/${projectId}/users`, { headers: { Authorization: `Bearer ${token}` } })
  .then((res) =>{
    console.log(res.data)
    setUsers(Array.from(res.data));
})
  .catch((err) => console.error("Greška pri učitavanju korisnika:", err));

    // 3. Učitaj taskove projekta
    api
      .get(`/relations/${projectId}/tasks`, { headers: { Authorization: `Bearer ${token}` } })
      .then((res) => {
        console.log(res.data);
        setTasks(Array.from(res.data));

      }) // Set pretvorimo u niz
      .catch((err) => console.error(err));
  }, [projectId, navigate]);

  if (!project) return <p>Učitavanje projekta...</p>;

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">{project.name}</h1>

      {/* Taskovi */}
      <h2 className="text-2xl font-semibold mb-3">Zadaci</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4 mb-6">
        {tasks.length === 0 ? (
          <p>Nema zadataka za ovaj projekat.</p>
        ) : (
          tasks.map((task) => (
            <div
              key={task.id}
              className="bg-white p-4 rounded shadow hover:shadow-lg transition cursor-pointer"
            >
              <h3 className="font-bold">{task.name}</h3>
              <p>Title: {task.title}</p>
              <p>Description: {task.description}</p>
              <p>Status: {task.status}</p>
              <p>Deadline: {task.deadline}</p>
            </div>
          ))
        )}
      </div>

      {/* Korisnici */}
      <h2 className="text-2xl font-semibold mb-3">Članovi projekta</h2>
      <div className="flex flex-wrap gap-4">
        {users.length === 0 ? (
          <p>Nema članova za ovaj projekat.</p>
        ) : (
          users.map((user) => (
            <div
              key={user.id}
              className="bg-blue-100 text-blue-800 px-4 py-2 rounded-full"
            >
              {user.username} ({user.email})
            </div>
          ))
        )}
      </div>
      <button
  onClick={() => navigate(`/projects/${projectId}/chat` // popuni po svom
  )}
>
  Otvori chat
</button>
    </div>
  );
}
