package com.uas.java1.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uas.java1.model.Task;
import com.uas.java1.model.TaskFile;
import com.uas.java1.repository.TaskFileRepository;
import com.uas.java1.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskFileService {

    private final TaskRepository taskRepository;
    private final TaskFileRepository taskFileRepository;

    public TaskFile uploadFile(Long taskId, MultipartFile file) throws IOException {

        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task tidak ditemukan"));

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path path = Paths.get("uploads/" + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        TaskFile taskFile = TaskFile.builder()
            .task(task)
            .fileName(fileName)
            .filePath(path.toString())
            .uploadedAt(LocalDateTime.now())
            .build();

        taskFileRepository.save(taskFile);

        // Cek deadline & update status
        if (task.getDeadline() != null) {
            LocalDate today = LocalDate.now();
            if (!today.isAfter(task.getDeadline())) {
                task.setStatus("Completed");
            } else {
                task.setStatus("Overdue");
            }
            taskRepository.save(task);
        }

        return taskFile;
    }

    public List<TaskFile> getFilesByTaskId(Long taskId) {
        return taskFileRepository.findByTaskId(taskId);
    }
}


