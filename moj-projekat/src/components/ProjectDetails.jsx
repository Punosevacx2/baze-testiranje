import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../api/axiosConfig";

export default function ProjectDetails() {
  const { projectId } = useParams();
  const navigate = useNavigate();

  const [project, setProject] = useState(null);
  const [users, setUsers] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [currentUser, setCurrentUser] = useState(null);
  const [allUsers, setAllUsers] = useState([]);
  const [selectedUserId, setSelectedUserId] = useState("");
  const [addUserMsg, setAddUserMsg] = useState("");

  useEffect(() => {
    api.get("/users/me").then((res) => setCurrentUser(res.data.user)).catch(() => navigate("/login"));

    api
      .get(`/projects/${projectId}`)
      .then((res) => setProject(res.data))
      .catch(() => navigate("/login"));

    api
      .get(`/relations/projects/${projectId}/users`)
      .then((res) => setUsers(Array.from(res.data)))
      .catch((err) => console.error("Greška pri učitavanju korisnika:", err));

    api
      .get(`/relations/${projectId}/tasks`)
      .then((res) => setTasks(Array.from(res.data)))
      .catch((err) => console.error("Greška pri učitavanju taskova:", err));
  }, [projectId, navigate]);

  const role = currentUser?.roles?.name;

  const loadAllUsers = () => {
    api.get("/users").then((res) => setAllUsers(res.data));
  };

  const handleAddUser = async () => {
    if (!selectedUserId) return;
    try {
      await api.post(`/relations/${projectId}/add-user/${selectedUserId}`);
      setAddUserMsg("Korisnik uspješno dodat!");
      setSelectedUserId("");
      const res = await api.get(`/relations/projects/${projectId}/users`);
      setUsers(Array.from(res.data));
    } catch (err) {
      setAddUserMsg("Greška pri dodavanju korisnika.");
    }
  };

  const statusColor = (status) => {
    switch (status?.toUpperCase()) {
      case "DONE": return "bg-green-100 text-green-700";
      case "IN_PROGRESS": return "bg-yellow-100 text-yellow-700";
      case "TODO": return "bg-gray-100 text-gray-600";
      default: return "bg-blue-100 text-blue-700";
    }
  };

  if (!project) return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <p className="text-gray-400 text-lg">Učitavanje projekta...</p>
    </div>
  );

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Navbar */}
      <header className="bg-indigo-700 text-white px-8 py-4 flex items-center gap-4 shadow-md">
        <button
          onClick={() => navigate("/dashboard")}
          className="text-indigo-200 hover:text-white text-sm"
        >
          &larr; Dashboard
        </button>
        <h1 className="text-xl font-bold">{project.name}</h1>
      </header>

      <main className="max-w-6xl mx-auto px-8 py-10">
        {/* Chat dugme */}
        <div className="flex justify-end mb-8">
          <button
            onClick={() => navigate(`/projects/${projectId}/chat`)}
            className="bg-indigo-600 text-white px-5 py-2 rounded-lg text-sm font-semibold hover:bg-indigo-700 transition"
          >
            Otvori chat
          </button>
        </div>

        {/* Zadaci */}
        <section className="mb-10">
          <h2 className="text-xl font-bold text-gray-800 mb-4">Zadaci</h2>
          {tasks.length === 0 ? (
            <p className="text-gray-400 text-sm">Nema zadataka za ovaj projekat.</p>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {tasks.map((task) => (
                <div
                  key={task.id}
                  className="bg-white rounded-xl p-5 shadow-sm border border-gray-100 hover:shadow-md transition"
                >
                  <div className="flex items-center justify-between mb-3">
                    <h3 className="font-semibold text-gray-800">{task.title}</h3>
                    <span className={`text-xs font-medium px-2.5 py-1 rounded-full ${statusColor(task.status)}`}>
                      {task.status}
                    </span>
                  </div>
                  <p className="text-gray-500 text-sm">{task.description}</p>
                  {task.deadline && (
                    <p className="text-gray-400 text-xs mt-3">
                      Rok: {new Date(task.deadline).toLocaleDateString("sr-RS")}
                    </p>
                  )}
                </div>
              ))}
            </div>
          )}
        </section>

        {/* Članovi */}
        <section>
          <h2 className="text-xl font-bold text-gray-800 mb-4">Članovi projekta</h2>
          {users.length === 0 ? (
            <p className="text-gray-400 text-sm">Nema članova za ovaj projekat.</p>
          ) : (
            <div className="flex flex-wrap gap-3">
              {users.map((user) => (
                <div
                  key={user.id}
                  className="flex items-center gap-2 bg-white border border-gray-200 rounded-full px-4 py-2 shadow-sm"
                >
                  <div className="w-7 h-7 rounded-full bg-indigo-100 text-indigo-600 flex items-center justify-center text-sm font-bold">
                    {user.username?.charAt(0).toUpperCase()}
                  </div>
                  <div>
                    <p className="text-sm font-medium text-gray-800">{user.username}</p>
                    <p className="text-xs text-gray-400">{user.email}</p>
                  </div>
                </div>
              ))}
            </div>
          )}

          {/* Dodaj korisnika - samo za MANAGER i ADMIN */}
          {(role === "MANAGER" || role === "ADMIN") && (
            <div className="mt-6 bg-white border border-gray-200 rounded-xl p-5 shadow-sm">
              <h3 className="text-sm font-bold text-gray-700 mb-3">Dodaj korisnika na projekat</h3>
              <div className="flex gap-3">
                <select
                  value={selectedUserId}
                  onChange={(e) => setSelectedUserId(e.target.value)}
                  onFocus={loadAllUsers}
                  className="flex-1 px-4 py-2 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-400 bg-white"
                >
                  <option value="">-- Izaberi korisnika --</option>
                  {allUsers
                    .filter((u) => u.roles?.name === "USER" && !users.some((m) => m.id === u.id))
                    .map((u) => (
                      <option key={u.id} value={u.id}>
                        {u.username} ({u.email})
                      </option>
                    ))}
                </select>
                <button
                  onClick={handleAddUser}
                  disabled={!selectedUserId}
                  className="bg-indigo-600 text-white px-5 py-2 rounded-lg text-sm font-semibold hover:bg-indigo-700 transition disabled:opacity-40"
                >
                  Dodaj
                </button>
              </div>
              {addUserMsg && (
                <p className="text-sm mt-2 text-green-600">{addUserMsg}</p>
              )}
            </div>
          )}
        </section>
      </main>
    </div>
  );
}
