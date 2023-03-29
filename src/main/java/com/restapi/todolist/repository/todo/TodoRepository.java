package com.restapi.todolist.repository.todo;

import com.restapi.todolist.models.todos.Todo;
import com.restapi.todolist.models.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    List<Todo> findTodoByUser_Id(Long id);
    boolean existsTodoByUser_Id(Long id);
    List<Todo> findTodosByUser(User user);

}
