package com.github.zxxz_ru.command;

import org.springframework.stereotype.Component;

@Component
class HelpCommandBuilder {
    private final StringBuilder bld = new StringBuilder();

    public String composeHelpMessage() {
        bld.append("[-h; --help] print this help and exit if passed during application startup.\n")
                .append("[-h; help] print help to console and wait for future user input.\n")
                .append("To exit from application type <quit> or use Ctrl - C.\n\n")
                .append("Entities Common Operations:\n")
                .append("<project, task, user> [-a; --all] - print all <projects, tasks,users>;\n")
                .append("<project, task, user> [-d id; --delete id] - delete <project, task, user> by id.\n")
                .append("<project, task, user> [-id id] - print <project, task, user> by id.\n")
                .append("If Entity with id not exists continue without Indication. Else if id argument missing\n")
                .append("when user search for Entity by Id Application will print message.\n\n")
                .append("Examples:\n")
                .append("<task -a> will print all tasks to screen.\n")
                .append("<user --delete 3> will delete user without any indication.\n")
                .append("<project -id 1> will print project with id = 1, if no project with id = 1 exists will\n")
                .append("continue without any indication.\n\n")
                .append("Specific Entity's Commands:\n")
                .append("<user -id 1 --assign-task 2> assign user with id = 1 to task with id = 2. Print Task with id = 2.\n")
                .append("<user -id 1 --drop-task 2> user with id=1 will be removed from task's with id=2. Print Task with id = 2.\n")
                .append("<task -id 4 --add-user 1> add user with id = 1 to task with id = 4. Print Task with id = 4.\n")
                .append("<task -id 3 --remove-user 1> remove user with id=1 from task with id = 3. Print Task with id = 3.\n")
                .append("<task -uid 3> print all tasks to the screen of they have user with id= 3 assigned to them.\n")
                .append("<project -id 5 --add-task 1> add task with id=1 to project with id=5. Print Project with id = 5.\n")
                .append("<project -id 1 --remove-task 2> remove task with id=2 from project with id = 1. Print Project with id = 1.\n\n")
                .append("Entity's Update / Save Commands:\n")
                .append("To save new or update existing Entity use Entity's name with [--update] command,\n")
                .append("along with Entity's parameters separated by equality sign.\n")
                .append("Text values must be enclosed in single brackets. Id parameter is optional.\n")
                .append("When no id parameter is provided Application will set Entity's id from internal Counter and will\n")
                .append("create new Entity from parameters that provided.\n")
                .append("When id parameter is provided Application try to find Entity with Id, if found update Entity\n")
                .append("with parameters that provided. If Application can not find Entity with given Id it will create\n")
                .append("new Entity with provided Id and reset internal Entity's counter to Id parameter.\n")
                .append("This command print new or updated Entity to the screen.\n")
                .append("Examples:\n")
                .append("<user --update id=1 firstname='New Name' lastname='Last Name' role='User Role'>\n")
                .append("<task --update id=1 theme='Theme of Task' priority='Task's Priority'\n")
                .append("type='Type of Task' description='Task's Description' users={1, 2, 3}>(user ids assigned to this Task).\n")
                .append("<project --update id=1 name='Project Name'\n")
                .append("description='Project's Description' tasks={1,2,3}>(task ids assigned to this project).");
        return bld.substring(0);
    }
}