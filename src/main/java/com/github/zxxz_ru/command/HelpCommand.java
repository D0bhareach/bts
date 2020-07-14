package com.github.zxxz_ru.command;

public class HelpCommand {
    private final StringBuilder bld =  new StringBuilder();
    private String composeAdvise() {
        bld.append("[-h; --help] print this help;\n\n")
                .append("<project, task, user> [-a; --all] print all <projects, tasks,users>;\n")
                .append("<project, task, user> [--delete-id id] delete <project, task, user> by id.\n\t")
                .append("If id not exists or id argument missing print message and exit.\n\n")
                .append("user -id {id} print user by id.\n")
                .append("user --assign-task user id task id.\n\t")
                .append("If user id missing or  incorrect and if task id missing or incorrect - print message.\n")
                .append("user --drop-task user id task id.\n\t")
                .append("If user id missing or  incorrect and if task id missing or incorrect - print message.\n\n")
                .append("task [-p {id}; --project id] print all tasks for project with id.\n")
                .append("task [-u {id}; --user-id id] print all tasks for user with id.\n\n")
                .append("Save or update existing user:\n")
                .append("user --put firstname lastname role\n\n")
                .append("Save or update existing task:\n")
                .append("task --put \n\tthema=\"thema/name\" \n\tproject=project id \n\ttype=\"type of task\"\n\t")
                .append("description=\"description of task\" \n\tpriority=[normal, high, done, canceled]\n");
        return bld.substring(0);
    }

    public void printAdvise (){
        System.out.print(composeAdvise());
    }
}