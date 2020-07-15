CREATE TABLE IF NOT EXISTS user (
user_id INT AUTO_INCREMENT PRIMARY KEY,
first_name VARCHAR(50) NOT NULL,
last_name VARCHAR(50) NOT NULL,
user_role VARCHAR(50) DEFAULT 'Developer'
);

INSERT INTO user(user_id, first_name, last_name, user_role) VALUES
(1,'Александр', 'Иванов', 'Developer'),
(2,'Василий', 'Петров', 'Developer'),
(3, 'Роман', 'Володин', 'Developer'),
(4, 'Владимир', 'Сердюк', 'Project Manager'),
(5, 'Олег', 'Торшин', 'CTO');

CREATE TABLE IF NOT EXISTS project (
project_id INT AUTO_INCREMENT PRIMARY KEY,
project_name VARCHAR(50) NOT NULL,
description VARCHAR(250) DEFAULT ''
);

INSERT INTO project(project_id, project_name, description) VALUES
(1,'BTS', 'Bug tracking System' ),
(2,'Project_1', 'Good Idea Project'),
(3,'Enigma', ''),
(4, 'Project_2', 'Very important Project');


CREATE TABLE IF NOT EXISTS task (
task_id INT AUTO_INCREMENT PRIMARY KEY,
thema VARCHAR(100) NOT NULL,
priority ENUM('normal', 'high', 'done', 'canceled'),
task_type VARCHAR(50) DEFAULT '',
project_id INT DEFAULT 0,
description VARCHAR(250) DEFAULT '',
FOREIGN KEY(project_id) REFERENCES project(project_id)
);

INSERT INTO task(task_id,thema, priority, task_type, project_id, description) VALUES
(1, 'Bug 233 in BTS', 'high', '', 1, 'Nasty Bug in JPA somewhere.' ),
(2,'Bug 235 in BTS', 'normal', '', 1, 'Null Pointer in Spring Boot.' ),
(3,'Bug 239 in BTS', 'done', '', 1, 'Simple typos in HelpCommand.' ),
(4,'Project_1 start', 'normal', '', 2,  'Good Idea Project Start and Git init.' ),
(5,'Enigma', 'normal', 'secret', 3,  'VIP for FBI.' );
