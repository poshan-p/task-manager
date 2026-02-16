import { useState, useEffect } from "react";
import { useAuth } from "../contexts/AuthContext";
import { taskApi } from "../api/taskApi";
import { Link } from "react-router-dom";

const DashboardPage = () => {
  const { user } = useAuth();
  const [stats, setStats] = useState({
    total: 0,
    pending: 0,
    inProgress: 0,
    completed: 0,
    overdue: 0,
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    setLoading(true);

    const [allTasks, overdueTasks] = await Promise.all([
      taskApi.getAllTasks(),
      taskApi.getOverdueTasks(),
    ]);

    if (allTasks.success) {
      const tasks = allTasks.data!;
      setStats({
        total: tasks.length,
        pending: tasks.filter((t) => t.status === "TODO").length,
        inProgress: tasks.filter((t) => t.status === "IN_PROGRESS").length,
        completed: tasks.filter((t) => t.status === "DONE").length,
        overdue: overdueTasks.success ? overdueTasks.data!.length : 0,
      });
    }

    setLoading(false);
  };

  if (loading) {
    return <div>Loading dashboard...</div>;
  }

  return (
    <div style={{ maxWidth: "1200px", margin: "0 auto" }}>
      <h1>Dashboard</h1>
      <h2>Welcome, {user?.username}!</h2>

      <div
        style={{
          display: "grid",
          gridTemplateColumns: "repeat(auto-fit, minmax(200px, 1fr))",
          gap: "20px",
          marginTop: "30px",
        }}
      >
        <div
          style={{
            padding: "20px",
            backgroundColor: "#17a2b8",
            color: "white",
            borderRadius: "8px",
            textAlign: "center",
          }}
        >
          <h3 style={{ margin: "0 0 10px 0" }}>Total Tasks</h3>
          <p style={{ fontSize: "36px", margin: 0 }}>{stats.total}</p>
        </div>

        <div
          style={{
            padding: "20px",
            backgroundColor: "#ffc107",
            color: "white",
            borderRadius: "8px",
            textAlign: "center",
          }}
        >
          <h3 style={{ margin: "0 0 10px 0" }}>Pending</h3>
          <p style={{ fontSize: "36px", margin: 0 }}>{stats.pending}</p>
        </div>

        <div
          style={{
            padding: "20px",
            backgroundColor: "#007bff",
            color: "white",
            borderRadius: "8px",
            textAlign: "center",
          }}
        >
          <h3 style={{ margin: "0 0 10px 0" }}>In Progress</h3>
          <p style={{ fontSize: "36px", margin: 0 }}>{stats.inProgress}</p>
        </div>

        <div
          style={{
            padding: "20px",
            backgroundColor: "#28a745",
            color: "white",
            borderRadius: "8px",
            textAlign: "center",
          }}
        >
          <h3 style={{ margin: "0 0 10px 0" }}>Completed</h3>
          <p style={{ fontSize: "36px", margin: 0 }}>{stats.completed}</p>
        </div>

        {stats.overdue > 0 && (
          <div
            style={{
              padding: "20px",
              backgroundColor: "#dc3545",
              color: "white",
              borderRadius: "8px",
              textAlign: "center",
            }}
          >
            <h3 style={{ margin: "0 0 10px 0" }}>Overdue</h3>
            <p style={{ fontSize: "36px", margin: 0 }}>{stats.overdue}</p>
          </div>
        )}
      </div>

      <div style={{ marginTop: "40px", textAlign: "center" }}>
        <Link to="/tasks">
          <button
            style={{
              padding: "15px 30px",
              backgroundColor: "#007bff",
              color: "white",
              border: "none",
              cursor: "pointer",
              fontSize: "16px",
              borderRadius: "5px",
            }}
          >
            View All Tasks
          </button>
        </Link>
      </div>
    </div>
  );
};

export default DashboardPage;
