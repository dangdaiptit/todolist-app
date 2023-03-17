package com.restapi.todolist.repository.users;

import com.restapi.todolist.models.users.ERole;
import com.restapi.todolist.models.users.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
