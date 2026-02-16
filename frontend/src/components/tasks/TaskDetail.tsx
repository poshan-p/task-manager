import type { TaskResponse } from "../../api/taskApi";

const TaskDetail = ({
  task,
  onClose,
  onEdit,
}: {
  task: TaskResponse;
  onClose: () => void;
  onEdit: (task: TaskResponse) => void;
}) => {
  if (!task) return null;

  return (
    <div
      style={{
        position: "fixed",
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
        backgroundColor: "rgba(0,0,0,0.5)",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        zIndex: 1000,
      }}
    >
      <div
        style={{
          backgroundColor: "white",
          padding: "30px",
          borderRadius: "8px",
          maxWidth: "600px",
          width: "90%",
          maxHeight: "80vh",
          overflow: "auto",
        }}
      >
        <h2>{task.title}</h2>

        <div style={{ marginBottom: "15px" }}>
          <strong>Description:</strong>
          <p>{task.description || "No description"}</p>
        </div>

        <div style={{ marginBottom: "15px" }}>
          <strong>Status:</strong> {task.status}
        </div>

        <div style={{ marginBottom: "15px" }}>
          <strong>Priority:</strong> {task.priority}
        </div>

        {task.dueDate && (
          <div style={{ marginBottom: "15px" }}>
            <strong>Due Date:</strong>{" "}
            {new Date(task.dueDate).toLocaleDateString()}
          </div>
        )}

        <div style={{ marginBottom: "15px" }}>
          <strong>Created:</strong> {new Date(task.createdAt).toLocaleString()}
        </div>

        {task.updatedAt && (
          <div style={{ marginBottom: "15px" }}>
            <strong>Updated:</strong>{" "}
            {new Date(task.updatedAt).toLocaleString()}
          </div>
        )}

        <div style={{ display: "flex", gap: "10px", marginTop: "20px" }}>
          <button
            onClick={() => {
              onEdit(task);
              onClose();
            }}
            style={{
              padding: "10px 20px",
              backgroundColor: "#ffc107",
              color: "white",
              border: "none",
              cursor: "pointer",
              borderRadius: "3px",
            }}
          >
            Edit
          </button>

          <button
            onClick={onClose}
            style={{
              padding: "10px 20px",
              backgroundColor: "#6c757d",
              color: "white",
              border: "none",
              cursor: "pointer",
              borderRadius: "3px",
            }}
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
};

export default TaskDetail;
