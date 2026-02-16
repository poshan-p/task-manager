import type { TaskPriority, TaskResponse, TaskStatus } from "../../api/taskApi";

const TaskItem = ({
  task,
  onEdit,
  onDelete,
  onView,
}: {
  task: TaskResponse;
  onEdit: (task: TaskResponse) => void;
  onDelete: (taskId: number) => {};
  onView: (task: TaskResponse) => void;
}) => {
  const getStatusColor = (status: TaskStatus) => {
    switch (status) {
      case "TODO":
        return "#ffc107";
      case "IN_PROGRESS":
        return "#17a2b8";
      case "DONE":
        return "#28a745";
      default:
        return "#6c757d";
    }
  };

  const getPriorityColor = (priority: TaskPriority) => {
    switch (priority) {
      case "LOW":
        return "#28a745";
      case "MEDIUM":
        return "#ffc107";
      case "HIGH":
        return "#dc3545";
      default:
        return "#6c757d";
    }
  };

  return (
    <div
      style={{
        border: "1px solid #ddd",
        padding: "15px",
        marginBottom: "10px",
        borderRadius: "5px",
      }}
    >
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "start",
        }}
      >
        <div style={{ flex: 1 }}>
          <h3 style={{ margin: "0 0 10px 0" }}>{task.title}</h3>

          <p style={{ margin: "5px 0", color: "#666" }}>
            {task.description || "No description"}
          </p>

          <div style={{ display: "flex", gap: "10px", marginTop: "10px" }}>
            <span
              style={{
                padding: "3px 10px",
                backgroundColor: getStatusColor(task.status),
                color: "white",
                borderRadius: "3px",
                fontSize: "12px",
              }}
            >
              {task.status}
            </span>

            <span
              style={{
                padding: "3px 10px",
                backgroundColor: getPriorityColor(task.priority),
                color: "white",
                borderRadius: "3px",
                fontSize: "12px",
              }}
            >
              {task.priority}
            </span>
          </div>

          {task.dueDate && (
            <p
              style={{ margin: "10px 0 0 0", fontSize: "14px", color: "#666" }}
            >
              Due: {new Date(task.dueDate).toLocaleDateString()}
            </p>
          )}
        </div>

        <div style={{ display: "flex", gap: "5px", flexDirection: "column" }}>
          <button
            onClick={() => onView(task)}
            style={{
              padding: "5px 15px",
              backgroundColor: "#17a2b8",
              color: "white",
              border: "none",
              cursor: "pointer",
              borderRadius: "3px",
            }}
          >
            View
          </button>

          <button
            onClick={() => onEdit(task)}
            style={{
              padding: "5px 15px",
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
            onClick={() => onDelete(task.id)}
            style={{
              padding: "5px 15px",
              backgroundColor: "#dc3545",
              color: "white",
              border: "none",
              cursor: "pointer",
              borderRadius: "3px",
            }}
          >
            Delete
          </button>
        </div>
      </div>
    </div>
  );
};

export default TaskItem;
