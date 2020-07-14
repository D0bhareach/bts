DROP TABLE IF EXISTS user ;
CREATE TABLE user (
id INT AUTO_INCREMENT PRIMARY KEY,
first_name VARCHAR(50) NOT NULL,
last_name VARCHAR(50) NOT NULL,
user_role VARCHAR(50) DEFAULT 'Developer'
);

INSERT INTO user(first_name, last_name, user_role) VALUES
('Александр', 'Иванов', 'Developer'),
('Василий', 'Петров', ''),
('Роман', 'Володин', 'Developer'),
('Владимир', 'Сердюк', 'Project Manager'),
('Олег', 'Торшин', 'CTO');

CREATE TABLE IF NOT EXISTS project (
id INT AUTO_INCREMENT PRIMARY KEY,
project_name VARCHAR(50) NOT NULL,
description VARCHAR(250) DEFAULT ''
);

INSERT INTO project(project_name, description) VALUES
('BTS', 'Bug tracking System' ),
('Project_1', 'Good Idea Project'),
('Enigma', ''),
('Project_2', 'Very important Project');


DROP TABLE IF EXISTS task ;
CREATE TABLE task (
id INT AUTO_INCREMENT PRIMARY KEY,
thema VARCHAR(100) NOT NULL,
priority ENUM('normal', 'high', 'done', 'canceled'),
task_type VARCHAR(50) DEFAULT '',
project_id INT DEFAULT 0,
description VARCHAR(250) DEFAULT '',
FOREIGN KEY(project_id) REFERENCES project(id)
);

INSERT INTO task(thema, priority, task_type, project_id, description) VALUES
('Bug 233 in BTS', 'high', '', 1, 'Nasty Bug in JPA somewhere.' ),
('Bug 235 in BTS', 'normal', '', 1, 'Null Pointer in Spring Boot.' ),
('Bug 239 in BTS', 'done', '', 1, 'Simple typos in HelpCommand.' ),
('Project_1 start', 'normal', '', 2,  'Good Idea Project Start and Git init.' ),
('Enigma', 'normal', 'secret', 3,  'VIP for FBI.' );
