package com.uas.java1.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uas.java1.dto.TaskDto;
import com.uas.java1.model.Task;
import com.uas.java1.model.User;
import com.uas.java1.repository.TaskRepository;
import com.uas.java1.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public Task createTask(TaskDto taskDto) {

        User assignedUser = userRepository.findById(taskDto.getAssignedTo())
                .orElseThrow(() -> new RuntimeException("User dengan ID " + taskDto.getAssignedTo() + " tidak ditemukan"));

        Task newTask = Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .assignedTo(assignedUser)  
                .deadline(taskDto.getDeadline())
                .status("In-Progress")     
                .build();

        return taskRepository.save(newTask);
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task existingTask = getTaskById(id);
        existingTask.setTitle(updatedTask.getTitle());
        existingTask.setDescription(updatedTask.getDescription());
        existingTask.setAssignedTo(updatedTask.getAssignedTo());
        existingTask.setDeadline(updatedTask.getDeadline());
        existingTask.setStatus(updatedTask.getStatus());
        return taskRepository.save(existingTask);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
