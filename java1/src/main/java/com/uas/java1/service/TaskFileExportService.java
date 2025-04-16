package com.uas.java1.service;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.uas.java1.model.TaskFile;
import com.uas.java1.repository.TaskFileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskFileExportService {

    private final TaskFileRepository taskFileRepository;

    public ByteArrayInputStream exportToExcel() throws IOException {
        List<TaskFile> files = taskFileRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Task Files");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("ID");
            header.createCell(1).setCellValue("Task ID");
            header.createCell(2).setCellValue("File Name");
            header.createCell(3).setCellValue("Upload Time");

            int rowIdx = 1;
            for (TaskFile tf : files) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(tf.getId());
                row.createCell(1).setCellValue(tf.getTask().getId());
                row.createCell(2).setCellValue(tf.getFileName());
                row.createCell(3).setCellValue(tf.getUploadedAt().toString());
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}


