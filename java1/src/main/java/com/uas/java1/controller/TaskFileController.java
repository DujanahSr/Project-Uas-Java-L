package com.uas.java1.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uas.java1.model.TaskFile;
import com.uas.java1.service.TaskFileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user/task-files")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class TaskFileController {

    private final TaskFileService taskFileService;

    @PostMapping("/{taskId}/upload")
    public ResponseEntity<TaskFile> uploadFile(@PathVariable Long taskId, @RequestParam("file") MultipartFile file) throws IOException {
        TaskFile savedFile = taskFileService.uploadFile(taskId, file);
        return ResponseEntity.ok(savedFile);
    }

    @GetMapping("/{taskId}")
    public List<TaskFile> getTaskFiles(@PathVariable Long taskId) {
        return taskFileService.getFilesByTaskId(taskId);
    }
}
