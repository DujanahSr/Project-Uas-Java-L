package com.uas.java1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uas.java1.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

}
