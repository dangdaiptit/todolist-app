select users.id, users.username, user_roles.role_id, roles.name
from (users inner join user_roles on users.id = user_roles.user_id)
         inner join roles on role_id = roles.id;

insert into user_roles value (1, 1);

select * from users;

select users.id, users.username, user_roles.role_id, roles.name
from (users inner join user_roles on users.id = user_roles.user_id)
         inner join roles on role_id = roles.id;

select users.id, users.username, users.email, roles.name as roleName
from (users inner join user_roles on users.id = user_roles.user_id)
         inner join roles on role_id = roles.id;

SELECT users.id, users.username, users.email, GROUP_CONCAT(roles.name) as role_names
FROM (users INNER JOIN user_roles ON users.id = user_roles.user_id)
         INNER JOIN roles ON role_id = roles.id
GROUP BY users.id, users.username, users.email;

select users.id, users.username, users.email, group_concat(roles.name) as roles_names
from users
         inner join user_roles on users.id = user_roles.user_id
         inner join roles on roles.id = user_roles.role_id
where username = 'dangdaiptit'
group by users.id, users.username, users.email;