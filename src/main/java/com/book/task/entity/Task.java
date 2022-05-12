package com.book.task.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Task {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    @NotNull(message = "{NotNull.Task.name}")
    private String name;

    @Column
    @NotNull(message = "{NotNull.Task.description}")
    private String description;

}
