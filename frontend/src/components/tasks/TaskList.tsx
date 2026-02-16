import { useState, useEffect } from "react";
import {
  taskApi,
  taskStatuses,
  type CreateTaskRequest,
  type TaskResponse,
  type TaskStatus,
  type UpdateTaskRequest,
} from "../../api/taskApi";
import TaskItem from "./TaskItem";
import TaskForm from "./TaskForm";
import TaskDetail from "./TaskDetail";

const TaskList = () => {
  const [tasks, setTasks] = useState<Array<TaskResponse> | undefined>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>("");
  const [showForm, setShowForm] = useState(false);
  const [editingTask, setEditingTask] = useState<TaskResponse | null>(null);
  const [selectedTask, setSelectedTask] = useState<TaskResponse | null>(null);
  const [filterStatus, setFilterStatus] = useState<TaskStatus | null>(null);

  useEffect(() => {
    fetchTasks();
  }, [filterStatus]);

  const fetchTasks = async () => {
    setLoading(true);
    setError("");

    const result = await taskApi.getAllTasks(filterStatus);

    if (result.success) {
      setTasks(result.data);
    } else {
      setError(result.message ?? "");
    }

    setLoading(false);
  };

  const handleCreateTask = async (taskData: CreateTaskRequest) => {
    const result = await taskApi.createTask(taskData);

    if (result.success) {
      setShowForm(false);
      fetchTasks();
    } else {
      setError(result.message ?? "");
    }
  };

  const handleUpdateTask = async (taskData: UpdateTaskRequest) => {
    const result = await taskApi.updateTask(editingTask!.id, taskData);

    if (result.success) {
      setEditingTask(null);
      fetchTasks();
    } else {
      setError(result.message ?? "");
    }
  };

  const handleDeleteTask = async (taskId: number) => {
    if (!window.confirm("Are you sure you want to delete this task?")) {
      return;
    }

    const result = await taskApi.deleteTask(taskId);

    if (result.success) {
      fetchTasks();
    } else {
      setError(result.message ?? "");
    }
  };

  const handleEdit = (task: TaskResponse) => {
    setEditingTask(task);
    setShowForm(false);
  };

  const handleView = (task: TaskResponse) => {
    setSelectedTask(task);
  };

  if (loading) {
    return <div>Loading tasks...</div>;
  }

  return (
    <div>
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          marginBottom: "20px",
        }}
      >
        <h2>My Tasks</h2>

        <div style={{ display: "flex", gap: "10px" }}>
          <select
            value={filterStatus || ""}
            onChange={(e) => setFilterStatus(e.target.value as TaskStatus)}
            style={{ padding: "8px" }}
          >
            <option value="">All Tasks</option>
            {taskStatuses.map((status) => (
              <option key={status} value={status}>
                {status}
              </option>
            ))}
          </select>

          <button
            onClick={() => {
              setShowForm(true);
              setEditingTask(null);
            }}
            style={{
              padding: "8px 20px",
              backgroundColor: "#28a745",
              color: "white",
              border: "none",
              cursor: "pointer",
              borderRadius: "3px",
            }}
          >
            New Task
          </button>
        </div>
      </div>

      {error && (
        <div
          style={{
            color: "red",
            marginBottom: "10px",
            padding: "10px",
            border: "1px solid red",
            borderRadius: "3px",
          }}
        >
          {error}
        </div>
      )}

      {showForm && (
        <div
          style={{
            border: "2px solid #007bff",
            padding: "20px",
            marginBottom: "20px",
            borderRadius: "5px",
            backgroundColor: "#f8f9fa",
          }}
        >
          <h3>Create New Task</h3>
          <TaskForm
            task={null}
            onSubmit={handleCreateTask}
            onCancel={() => setShowForm(false)}
          />
        </div>
      )}

      {editingTask && (
        <div
          style={{
            border: "2px solid #ffc107",
            padding: "20px",
            marginBottom: "20px",
            borderRadius: "5px",
            backgroundColor: "#fff3cd",
          }}
        >
          <h3>Edit Task</h3>
          <TaskForm
            task={editingTask}
            onSubmit={handleUpdateTask}
            onCancel={() => setEditingTask(null)}
          />
        </div>
      )}

      {tasks?.length === 0 ? (
        <p>No tasks found. Create your first task!</p>
      ) : (
        <div>
          {tasks!.map((task) => (
            <TaskItem
              key={task.id}
              task={task}
              onEdit={handleEdit}
              onDelete={handleDeleteTask}
              onView={handleView}
            />
          ))}
        </div>
      )}

      {selectedTask && (
        <TaskDetail
          task={selectedTask}
          onClose={() => setSelectedTask(null)}
          onEdit={handleEdit}
        />
      )}
    </div>
  );
};

export default TaskList;
