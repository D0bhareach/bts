package com.github.zxxz_ru.command;

import org.springframework.stereotype.Component;

@Component
class HelpCommandBuilder {
    private final StringBuilder bld =  new StringBuilder();
    public String composeHelpMessage() {
        bld.append("[-h; --help] print this help and exit if passed during application startup.\n")
            .append("[-h; help] print help to console and wait for future user input.\n\n")
            .append("Common Operations:\n")
            .append("<project, task, user> [-a; --all] - print all <projects, tasks,users>;\n")
            .append("<project, task, user> [-d id; --delete id] - delete <project, task, user> by id.\n")
            .append("<project, task, user> [-id id] - print <project, task, user> by id.\n")
            .append("If Entity with id not exists or id argument missing Application will print message and exit.\n\n")
            .append("Specific Entity's Operations:\n")
            .append("user --assign-task user_id task_id - Assign user new task\n")
            .append("If user's id missing or incorrect and if task's id missing or incorrect - print message.\n")
            .append("user --drop-task user id task id. - User drop task.")
            .append("If user id missing or  incorrect and if task id missing or incorrect - print message.\n")
            .append("task [-p id; --project id] - print all tasks for project with given id.\n")
            .append("task [-u id; --user id] - print all tasks for user with given id.\n\n")
            .append("Entity's Saving Operations:\n")
            .append("To save new or update existing Entity use Entity's name with [--update] command,\n")
            .append("along with Entity's parameters separated by equality sign.\n")
            .append("Multi words text values must be enclosed in single brackets. Examples:\n")
            .append("user --update \tid=user id (required parameter)\tfirstname='user first name'\\ \n")
            .append("lastname='user last name'\trole=user role(default \"Developer\", must be single word.)\n\n")

            .append("task --update \n\tthema=\"thema/name\" \n\tproject=project id \n\ttype=\"type of task\"\n\t")
            .append("description=\"description of task\" \n\tpriority=[normal, high, done, canceled]\n")
            .append("Save new or update existing project:\n")
            .append("project --put project_name description\n\n");
        return bld.substring(0);
    }
}