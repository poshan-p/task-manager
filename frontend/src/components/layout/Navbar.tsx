import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../../contexts/AuthContext";

const Navbar = () => {
  const { user, isAuthenticated, logout, logoutAll } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate("/login");
  };

  const handleLogoutAll = async () => {
    if (window.confirm("Logout from all devices?")) {
      await logoutAll();
      navigate("/login");
    }
  };

  return (
    <nav
      style={{
        backgroundColor: "#333",
        color: "white",
        padding: "10px 20px",
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
      }}
    >
      <div style={{ display: "flex", gap: "20px", alignItems: "center" }}>
        <h1 style={{ margin: 0 }}>Task Manager</h1>

        {isAuthenticated && (
          <>
            <Link
              to="/dashboard"
              style={{ color: "white", textDecoration: "none" }}
            >
              Dashboard
            </Link>
            <Link
              to="/tasks"
              style={{ color: "white", textDecoration: "none" }}
            >
              Tasks
            </Link>
          </>
        )}
      </div>

      <div style={{ display: "flex", gap: "15px", alignItems: "center" }}>
        {isAuthenticated ? (
          <>
            <span>Hello, {user?.username}</span>
            <button
              onClick={handleLogout}
              style={{
                padding: "5px 15px",
                backgroundColor: "#dc3545",
                color: "white",
                border: "none",
                cursor: "pointer",
              }}
            >
              Logout
            </button>
            <button
              onClick={handleLogoutAll}
              style={{
                padding: "5px 15px",
                backgroundColor: "#6c757d",
                color: "white",
                border: "none",
                cursor: "pointer",
              }}
            >
              Logout All
            </button>
          </>
        ) : (
          <>
            <Link
              to="/login"
              style={{ color: "white", textDecoration: "none" }}
            >
              Login
            </Link>
            <Link
              to="/register"
              style={{ color: "white", textDecoration: "none" }}
            >
              Register
            </Link>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
