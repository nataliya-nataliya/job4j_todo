create table tasks (
   id serial primary key,
   name varchar        not null,
   description varchar not null,
   created timestamp   not null,
   done BOOLEAN        not null
);
