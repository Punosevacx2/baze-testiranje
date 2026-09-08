import { useEffect, useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import api from "../api/axiosConfig";

export default function Dashboard() {
  const [currentUser, setCurrentUser] = useState(null);
  const [projects, setProjects] = useState([]);
  const [allUsers, setAllUsers] = useState([]);
  const [allProjects, setAllProjects] = useState([]);
  const [adminTab, setAdminTab] = useState("users");
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [newProject, setNewProject] = useState({ name: "", description: "" });
  const navigate = useNavigate();

  useEffect(() => {
    api
      .get("/users/me")
      .then((res) => setCurrentUser(res.data.user))
      .catch(() => navigate("/login"));
  }, [navigate]);

  const role = currentUser?.roles?.name;

  const handleLogout = () => {
    sessionStorage.removeItem("token");
    navigate("/login");
  };

  // USER / MANAGER - učitaj svoje projekte
  const handleShowProjects = async () => {
    if (!currentUser) return;
    try {
      const res = await api.get(`/usersnode/${currentUser.id}/projects`);
      setProjects(res.data);
    } catch (err) {
      alert("Greška prilikom učitavanja projekata");
    }
  };

  // ADMIN - učitaj sve korisnike
  const loadAllUsers = async () => {
    try {
      const res = await api.get("/users");
      setAllUsers(res.data);
    } catch (err) {
      alert("Greška pri učitavanju korisnika");
    }
  };

  // ADMIN - učitaj sve projekte
  const loadAllProjects = async () => {
    try {
      const res = await api.get("/projects");
      setAllProjects(res.data);
    } catch (err) {
      alert("Greška pri učitavanju projekata");
    }
  };

  // MANAGER - kreiraj projekat
  const handleCreateProject = async (e) => {
    e.preventDefault();
    try {
      await api.post("/projects", newProject);
      setShowCreateModal(false);
      setNewProject({ name: "", description: "" });
      handleShowProjects();
    } catch (err) {
      alert("Greška pri kreiranju projekta");
    }
  };

  // ADMIN - obrisi korisnika
  const handleDeleteUser = async (userId) => {
    if (!window.confirm("Sigurno želiš obrisati ovog korisnika?")) return;
    try {
      await api.delete(`/users/${userId}`);
      setAllUsers((prev) => prev.filter((u) => u.id !== userId));
    } catch (err) {
      alert("Greška pri brisanju korisnika");
    }
  };

  const roleBadge = {
    ADMIN: "bg-red-100 text-red-700",
    MANAGER: "bg-yellow-100 text-yellow-700",
    USER: "bg-green-100 text-green-700",
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Navbar */}
      <header className="bg-indigo-700 text-white px-8 py-4 flex items-center justify-between shadow-md">
        <h1 className="text-xl font-bold tracking-wide">Scrum Board</h1>
        <div className="flex items-center gap-4">
          {currentUser && (
            <>
              <span className={`text-xs font-semibold px-2.5 py-1 rounded-full ${roleBadge[role] || "bg-gray-100 text-gray-700"}`}>
                {role}
              </span>
              <span className="text-indigo-200 text-sm">Dobrodošao, {currentUser.username}</span>
            </>
          )}
          <button
            onClick={handleLogout}
            className="bg-white text-indigo-700 text-sm font-semibold px-4 py-1.5 rounded-lg hover:bg-indigo-100 transition"
          >
            Odjavi se
          </button>
        </div>
      </header>

      <main className="max-w-6xl mx-auto px-8 py-10">

        {/* ===== ADMIN PANEL ===== */}
        {role === "ADMIN" && (
          <div>
            <h2 className="text-2xl font-bold text-gray-800 mb-6">Admin Panel</h2>

            {/* Tabovi */}
            <div className="flex gap-2 mb-6">
              <button
                onClick={() => { setAdminTab("users"); loadAllUsers(); }}
                className={`px-5 py-2 rounded-lg text-sm font-semibold transition ${adminTab === "users" ? "bg-indigo-600 text-white" : "bg-white text-gray-600 border hover:bg-gray-50"}`}
              >
                Svi korisnici
              </button>
              <button
                onClick={() => { setAdminTab("projects"); loadAllProjects(); }}
                className={`px-5 py-2 rounded-lg text-sm font-semibold transition ${adminTab === "projects" ? "bg-indigo-600 text-white" : "bg-white text-gray-600 border hover:bg-gray-50"}`}
              >
                Svi projekti
              </button>
            </div>

            {/* Tab: Korisnici */}
            {adminTab === "users" && (
              <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                <table className="w-full text-sm">
                  <thead className="bg-gray-50 text-gray-500 uppercase text-xs">
                    <tr>
                      <th className="px-6 py-3 text-left">Korisnik</th>
                      <th className="px-6 py-3 text-left">Email</th>
                      <th className="px-6 py-3 text-left">Rola</th>
                      <th className="px-6 py-3 text-left">Akcije</th>
                    </tr>
                  </thead>
                  <tbody className="divide-y divide-gray-100">
                    {allUsers.map((u) => (
                      <tr key={u.id} className="hover:bg-gray-50">
                        <td className="px-6 py-4 font-medium text-gray-800 flex items-center gap-3">
                          <div className="w-8 h-8 rounded-full bg-indigo-100 text-indigo-600 flex items-center justify-center font-bold">
                            {u.username?.charAt(0).toUpperCase()}
                          </div>
                          {u.username}
                        </td>
                        <td className="px-6 py-4 text-gray-500">{u.email}</td>
                        <td className="px-6 py-4">
                          <span className={`text-xs font-semibold px-2.5 py-1 rounded-full ${roleBadge[u.roles?.name] || "bg-gray-100 text-gray-600"}`}>
                            {u.roles?.name || "N/A"}
                          </span>
                        </td>
                        <td className="px-6 py-4">
                          {u.email !== "admin@admin.rs" && (
                            <button
                              onClick={() => handleDeleteUser(u.id)}
                              className="text-red-500 hover:text-red-700 text-xs font-semibold"
                            >
                              Obriši
                            </button>
                          )}
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
                {allUsers.length === 0 && (
                  <p className="text-center text-gray-400 py-10">Klikni "Svi korisnici" da učitaš listu.</p>
                )}
              </div>
            )}

            {/* Tab: Projekti */}
            {adminTab === "projects" && (
              <div>
                {allProjects.length === 0 ? (
                  <p className="text-center text-gray-400 py-10">Nema projekata ili klikni "Svi projekti" da učitaš.</p>
                ) : (
                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {allProjects.map((project) => (
                      <Link key={project.id} to={`/projects/${project.id}`}>
                        <div className="bg-white rounded-xl p-6 shadow hover:shadow-lg transition border border-gray-100 cursor-pointer group">
                          <div className="flex items-center justify-between mb-3">
                            <div className="w-10 h-10 rounded-lg bg-indigo-100 flex items-center justify-center text-indigo-600 font-bold text-lg">
                              {project.name?.charAt(0).toUpperCase()}
                            </div>
                            <span className="text-indigo-500 text-sm font-medium group-hover:underline">Otvori &rarr;</span>
                          </div>
                          <h3 className="text-lg font-bold text-gray-800">{project.name}</h3>
                          <p className="text-gray-500 text-sm mt-1">{project.description}</p>
                        </div>
                      </Link>
                    ))}
                  </div>
                )}
              </div>
            )}
          </div>
        )}

        {/* ===== MANAGER / USER PANEL ===== */}
        {(role === "MANAGER" || role === "USER") && (
          <div>
            <div className="flex items-center justify-between mb-8">
              <h2 className="text-2xl font-bold text-gray-800">Moji projekti</h2>
              <div className="flex gap-3">
                {role === "MANAGER" && (
                  <button
                    onClick={() => setShowCreateModal(true)}
                    className="bg-green-600 text-white px-5 py-2 rounded-lg text-sm font-semibold hover:bg-green-700 transition"
                  >
                    + Novi projekat
                  </button>
                )}
                <button
                  onClick={handleShowProjects}
                  className="bg-indigo-600 text-white px-5 py-2 rounded-lg text-sm font-semibold hover:bg-indigo-700 transition"
                >
                  Učitaj projekte
                </button>
              </div>
            </div>

            {projects.length === 0 ? (
              <div className="text-center text-gray-400 mt-20">
                <p className="text-lg">Nema projekata za prikaz.</p>
                <p className="text-sm mt-1">Klikni "Učitaj projekte" da vidiš svoje projekte.</p>
              </div>
            ) : (
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {projects.map((project) => (
                  <Link key={project.id} to={`/projects/${project.id}`}>
                    <div className="bg-white rounded-xl p-6 shadow hover:shadow-lg transition border border-gray-100 cursor-pointer group">
                      <div className="flex items-center justify-between mb-3">
                        <div className="w-10 h-10 rounded-lg bg-indigo-100 flex items-center justify-center text-indigo-600 font-bold text-lg">
                          {project.name?.charAt(0).toUpperCase()}
                        </div>
                        <span className="text-indigo-500 text-sm font-medium group-hover:underline">Otvori &rarr;</span>
                      </div>
                      <h3 className="text-lg font-bold text-gray-800">{project.name}</h3>
                      <p className="text-gray-500 text-sm mt-1 line-clamp-2">{project.description}</p>
                    </div>
                  </Link>
                ))}
              </div>
            )}
          </div>
        )}
      </main>

      {/* Modal za kreiranje projekta */}
      {showCreateModal && (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center z-50">
          <div className="bg-white rounded-2xl shadow-2xl w-full max-w-md p-8">
            <h2 className="text-xl font-bold text-gray-800 mb-6">Novi projekat</h2>
            <form onSubmit={handleCreateProject} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Naziv projekta</label>
                <input
                  type="text"
                  value={newProject.name}
                  onChange={(e) => setNewProject({ ...newProject, name: e.target.value })}
                  placeholder="Unesi naziv projekta"
                  className="w-full px-4 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-400"
                  required
                />
              </div>
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">Opis</label>
                <textarea
                  value={newProject.description}
                  onChange={(e) => setNewProject({ ...newProject, description: e.target.value })}
                  placeholder="Kratki opis projekta"
                  rows={3}
                  className="w-full px-4 py-2.5 border border-gray-200 rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-indigo-400 resize-none"
                />
              </div>
              <div className="flex gap-3 pt-2">
                <button
                  type="submit"
                  className="flex-1 bg-green-600 text-white py-2.5 rounded-lg font-semibold hover:bg-green-700 transition"
                >
                  Kreiraj
                </button>
                <button
                  type="button"
                  onClick={() => { setShowCreateModal(false); setNewProject({ name: "", description: "" }); }}
                  className="flex-1 bg-gray-100 text-gray-700 py-2.5 rounded-lg font-semibold hover:bg-gray-200 transition"
                >
                  Otkaži
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
