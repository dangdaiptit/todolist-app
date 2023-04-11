package com.restapi.todolist.payload.request.admin;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ChangeRoleRequest {
    private Set<String> role;
}
