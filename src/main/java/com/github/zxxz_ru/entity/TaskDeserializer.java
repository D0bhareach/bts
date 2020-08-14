package com.github.zxxz_ru.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TaskDeserializer extends JsonDeserializer<Task> {
    @Override
    public Task deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        int id = (node.get("id")).asInt();
        String theme = node.get("thema").asText();
        String priority = node.get("priority").asText();
        String taskType = node.get("taskType").asText();
        String description = node.get("description").asText();
        Task res = new Task(id, theme, priority, taskType, description);
        String users = node.get("userList").asText();
        List<User> ul = ctxt.readValue(p, List.class);
        if (ul != null && ul.size() > 0)
            res.setUserList(ul);
        return res;
    }
}
