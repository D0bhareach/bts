package com.github.zxxz_ru.command;

import org.springframework.stereotype.Component;

@Component
class HelpCommand {
    private final StringBuilder bld =  new StringBuilder();
    private String composeAdvise() {
        bld.append("[-h; --help] print this help;\n\n")
            .append("<project, task, user> [-a; --all]\t\tprint all <projects, tasks,users>;\n")
            .append("<project, task, user> [-d id; --delete id]\t\tdelete <project, task, user> by id.\n\t")
            .append("If id not exists or id argument missing print message and exit.\n\n")
            .append("user -id id\t\tprint user by id.\n")
            .append("user --assign-task user_id task_id\t\tAssign user new task\n")
            .append("\tIf user id missing or  incorrect and if task id missing or incorrect - print message.\n")
            .append("user --drop-task user id task id.\t\tUser drop task.")
            .append("\tIf user id missing or  incorrect and if task id missing or incorrect - print message.\n\n")
            .append("task [-p id; --project id]\t\tprint all tasks for project with id.\n")
            .append("task [-u id; --user-id id]\t\tprint all tasks for user with id.\n\n")

            .append("Save new or update existing user:\n")
            .append("user --update \n\tid=user id (require parameter)\n\tfirstname=user first name")
            .append("\n\tlastname=user last name\n\trole=user role(default \"Developer\", single word)\n\n")
            .append("user --save \n\tid=user id\n\tfirstname=user first name (require parameter)")
            .append("\n\tlastname=user last name (require parameter)\n\trole=user role(default \"Developer\", single word)\n\n")

            .append("Save new or update existing task:\n")
            .append("task --put \n\tthema=\"thema/name\" \n\tproject=project id \n\ttype=\"type of task\"\n\t")
            .append("description=\"description of task\" \n\tpriority=[normal, high, done, canceled]\n")
            .append("Save new or update existing project:\n")
            .append("project --put project_name description\n\n");
        return bld.substring(0);
    }

    public void printAdvise (){
        System.out.print(composeAdvise());
        System.exit(0);
    }
}