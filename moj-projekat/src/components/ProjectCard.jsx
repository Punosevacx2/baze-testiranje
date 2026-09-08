const { id } = useParams();
const [project, setProject] = useState(null);

useEffect(() => {
  const token = sessionStorage.getItem("token");
  api.get(`/projects/${id}`, { headers: { Authorization: `Bearer ${token}` } })
    .then(res => setProject(res.data))
    .catch(err => console.error(err));
}, [id]);
