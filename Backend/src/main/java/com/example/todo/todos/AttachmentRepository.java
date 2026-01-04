package com.example.todo.todos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface AttachmentRepository extends CrudRepository<Attachment, Integer> {
    List<Attachment> findByTodoId(Integer todoId);
}
