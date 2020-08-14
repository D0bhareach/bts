package com.github.zxxz_ru.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProjectDeserializer extends JsonDeserializer<Project> {
    @Override
    public Project deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        List<Task> taskList = new ArrayList<>();
        // ObjectNode node = ctxt.getNodeFactory().objectNode();
        JsonNode node = p.getCodec().readTree(p);
        int id = (node.get("id")).asInt();
        String name = node.get("projectName").asText();
        String description = node.get("description").asText();
        Project res = new Project(id, name, description);
        ArrayNode tasks = (ArrayNode) node.get("taskList");
        p.setCurrentValue(tasks);
        Iterator<Task> iter = p.readValuesAs(Task.class);
        if (iter == null) {
            return res;
        }
        while (iter.hasNext()) {
            taskList.add(iter.next());
        }
        res.setTaskList(taskList);
        return res;
    }
}
