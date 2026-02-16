import type { ApiResponse } from "./api";
import type { User } from "./authApi";
import apiClient from "./axiosConfig";

export type TaskPriority = "LOW" | "MEDIUM" | "HIGH";
export const taskPriorities: TaskPriority[] = ["LOW", "MEDIUM", "HIGH"] as const;
export type TaskStatus = "TODO" | "IN_PROGRESS" | "DONE";
export const taskStatuses: TaskStatus[] = ["TODO", "IN_PROGRESS", "DONE"] as const;

export type CreateTaskRequest = {
  title: string;
  description: string | null;
  priority: TaskPriority;
  status: TaskStatus;
  dueDate: Date;
};

export type UpdateTaskRequest = {
  title: string;
  description: string | null;
  priority: TaskPriority;
  status: TaskStatus;
  dueDate: Date;
};

export type TaskResponse = {
  id: number;
  user: User;
  title: number;
  description: string | null;
  priority: TaskPriority;
  status: TaskStatus;
  dueDate: Date;
  createdAt: Date;
  updatedAt: Date;
};

export const taskApi = {
  // Get all tasks
  getAllTasks: async (
    status: TaskStatus | null = null,
  ): Promise<ApiResponse<Array<TaskResponse>>> => {
    try {
      const params = status ? { status } : {};
      const response = await apiClient.get("/tasks", { params });
      const res: ApiResponse<Array<TaskResponse>> = response.data;
      return res;
    } catch (error: any) {
      return {
        success: false,
        message: error.response?.data?.message || "Failed to fetch tasks",
      };
    }
  },

  // Get task by ID
  getTaskById: async (id: number): Promise<ApiResponse<TaskResponse>> => {
    try {
      const response = await apiClient.get(`/tasks/${id}`);
      const res: ApiResponse<TaskResponse> = await response.data;
      return res;
    } catch (error: any) {
      return {
        success: false,
        message: error.response?.data?.message || "Failed to fetch task",
      };
    }
  },

  // Create task
  createTask: async (
    request: CreateTaskRequest,
  ): Promise<ApiResponse<TaskResponse>> => {
    try {
      const response = await apiClient.post("/tasks", request);
      const res: ApiResponse<TaskResponse> = await response.data;
      return res;
    } catch (error: any) {
      return {
        success: false,
        message: error.response?.data?.message || "Failed to create task",
      };
    }
  },

  // Update task
  updateTask: async (
    id: number,
    request: UpdateTaskRequest,
  ): Promise<ApiResponse<TaskResponse>> => {
    try {
      const response = await apiClient.put(`/tasks/${id}`, request);
      const res: ApiResponse<TaskResponse> = await response.data;
      return res;
    } catch (error: any) {
      return {
        success: false,
        message: error.response?.data?.message || "Failed to update task",
      };
    }
  },

  // Delete task
  deleteTask: async (id: number): Promise<ApiResponse<boolean>> => {
    try {
      const response = await apiClient.delete(`/tasks/${id}`);
      const res: ApiResponse<boolean> = response.data;
      return res;
    } catch (error: any) {
      return {
        success: false,
        message: error.response?.data?.message || "Failed to delete task",
      };
    }
  },

  // Get overdue tasks
  getOverdueTasks: async (): Promise<ApiResponse<Array<TaskResponse>>> => {
    try {
      const response = await apiClient.get("/tasks/overdue");
      const res: ApiResponse<Array<TaskResponse>> = response.data;
      return res;
    } catch (error: any) {
      return {
        success: false,
        message:
          error.response?.data?.message || "Failed to fetch overdue tasks",
      };
    }
  },
};
