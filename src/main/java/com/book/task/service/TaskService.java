package com.book.task.service;


import com.book.task.dto.TaskDTO;
import com.book.task.entity.Task;
import com.book.task.repositories.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class TaskService {

    private TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public Page<Task> getTasks(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Task getTask(long taskId) {
        Optional<Task> taskOptional = repository.findById(taskId);
        return taskOptional.get();
    }

    public Task saveTask(TaskDTO taskDTO){
        ModelMapper modelMapper = new ModelMapper();
        Task task= modelMapper.map(taskDTO, Task.class);
        return repository.save(task);
    }
}
