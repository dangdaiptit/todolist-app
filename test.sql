select users.id, users.username, user_roles.role_id, roles.name
from (users inner join user_roles on users.id = user_roles.user_id)
         inner join roles on role_id = roles.id;

insert into user_roles value (1, 1);

select * from users;