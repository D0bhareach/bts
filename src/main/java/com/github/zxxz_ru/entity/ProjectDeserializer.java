package com.github.zxxz_ru.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.*;

public class ProjectDeserializer extends JsonDeserializer<Project> {
    @Override
    public Project deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Set<Task> taskList = new HashSet<>();
        JsonNode node = p.getCodec().readTree(p);
        int id = (node.get("id")).asInt();
        String name = node.get("projectName").asText();
        String description = node.get("description").asText();
        Project res = new Project(id, name, description);
        ArrayNode tasks = (ArrayNode) node.get("taskList");
        Iterator<JsonNode> titerator = tasks.elements();
        while (titerator.hasNext()) {
            JsonNode n = titerator.next();
            Task tsk = mapper.treeToValue(n, Task.class);
            taskList.add(tsk);
        }

        res.setTaskList(taskList);
        return res;
    }
}
