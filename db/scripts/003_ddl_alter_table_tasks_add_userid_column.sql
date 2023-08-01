alter table tasks
add column user_id integer not null references todo_users(id);
