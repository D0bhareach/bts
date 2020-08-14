package com.github.zxxz_ru.storage.file;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.zxxz_ru.entity.Project;
import com.github.zxxz_ru.entity.Task;
import com.github.zxxz_ru.entity.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DataDeserializer extends JsonDeserializer<Data> {
    @Override
    public Data deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        Data res = new Data();
        List<User> userList = new ArrayList();
        List<Task> taskList = new ArrayList<>();
        List<Project> projectList = new ArrayList<>();

        JsonNode node = p.getCodec().readTree(p);
        // read users
        ArrayNode users = (ArrayNode) node.get("users");
        Iterator<JsonNode> uiterator = users.elements();
        ObjectMapper mapper = new ObjectMapper();
        while (uiterator.hasNext()) {
            JsonNode n = uiterator.next();
            User usr = mapper.treeToValue(n, User.class);
            userList.add(usr);
        }
        res.setUsers(userList);
        // read tasks
        ArrayNode tasks = (ArrayNode) node.get("tasks");
        Iterator<JsonNode> titerator = tasks.elements();
        while (titerator.hasNext()) {
            JsonNode n = titerator.next();
            Task tsk = mapper.treeToValue(n, Task.class);
            taskList.add(tsk);
        }
        res.setTasks(taskList);
        // read projects
        ArrayNode projects = (ArrayNode) node.get("projects");
        Iterator<JsonNode> piterator = projects.elements();
        while (piterator.hasNext()) {
            JsonNode n = piterator.next();
            Project prj = mapper.treeToValue(n, Project.class);
            projectList.add(prj);
        }
        res.setProjects(projectList);
        res.setUserCounter(new AtomicInteger(node.get("userCounter") != null ? node.get("userCounter").asInt() : 0));
        res.setTaskCounter(new AtomicInteger(node.get("taskCounter") != null ? node.get("taskCounter").asInt() : 0));
        res.setProjectCounter(new AtomicInteger(node.get("projectCounter") != null ? node.get("projectCounter").asInt() : 0));
        return res;
    }
}
