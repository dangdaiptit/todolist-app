package com.restapi.todolist.validation.todos;

import com.restapi.todolist.models.todos.Todo;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class TodoAccessValidator {
    public void validateTodoAccess(Todo todo) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!todo.getUser().getUsername().equals(authentication.getName()))
            throw new AccessDeniedException("You are not authorized to access this todo");
    }
}
