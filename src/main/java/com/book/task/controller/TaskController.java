package com.book.task.controller;

import com.book.task.dto.TaskDTO;
import com.book.task.entity.Task;
import com.book.task.service.TaskService;
import com.book.task.uri.TaskUri;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RepositoryRestController
@RequestMapping("/todo/")
@RequiredArgsConstructor
public class TaskController {

    private final PagedResourcesAssembler pagedResourcesAssembler;
    private final TaskService taskService;

    @GetMapping(path = TaskUri.TASKS)
    public ResponseEntity<?> getTasks(TaskDTO taskDTO, Pageable pageable,
                                      PersistentEntityResourceAssembler resourceAssembler) {
        log.info("TasksController: " + taskDTO);
        Page<Task> events = taskService.getTasks(pageable);

        PagedModel<?> resource = pagedResourcesAssembler.toModel(events, resourceAssembler);
        return ResponseEntity.ok(resource);
    }

    @GetMapping(path = TaskUri.TASK)
    public ResponseEntity<?> getTask(@PathVariable("id") int taskId,
                                     Pageable pageable, PersistentEntityResourceAssembler resourceAssembler) {

        try {
            log.info("TasksController::: " + taskId);
            Task task = taskService.getTask(taskId);
            Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(this.getClass())
                            .getTask(taskId, pageable, resourceAssembler))
                    .withSelfRel();
            Link allTasksLink = WebMvcLinkBuilder.linkTo(this.getClass()).slash("/tasks").withRel("allTasksLink");

            EntityModel<Task> entityModel = EntityModel.of(task);
            entityModel.add(selfLink, allTasksLink);

            return ResponseEntity.ok(entityModel);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task Not Found", e);
        }

    }

    @PostMapping(path = TaskUri.CREATE_TASK)
    public ResponseEntity<?> createdTask(@RequestBody TaskDTO taskDTO,
                                         Pageable pageable, PersistentEntityResourceAssembler resourceAssembler) {

        log.info("TaskController: " + taskDTO);
        Task events = taskService.saveTask(taskDTO);

        Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(this.getClass())
                .createdTask(taskDTO, pageable, resourceAssembler)).withSelfRel();

        Link allTasksLink = WebMvcLinkBuilder.linkTo(this.getClass()).slash("/tasks").withRel("/allTasks");

        EntityModel<Task> taskEntityModel = EntityModel.of(events);
        taskEntityModel.add(selfLink, allTasksLink);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("CustomResponseHeaders", "CustomValue");

        return new ResponseEntity<EntityModel<Task>>(taskEntityModel, responseHeaders, HttpStatus.CREATED);

    }

}
