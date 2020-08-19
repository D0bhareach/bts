package com.github.zxxz_ru.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class TaskDeserializer extends JsonDeserializer<Task> {
    @Override
    public Task deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Set<User> userList = new HashSet<>();
        JsonNode node = p.getCodec().readTree(p);
        int id = (node.get("id")).asInt();
        String theme = node.get("thema").asText();
        String priority = node.get("priority").asText();
        String taskType = node.get("taskType").asText();
        String description = node.get("description").asText();
        Task res = new Task(id, theme, priority, taskType, description);
        ArrayNode users = (ArrayNode) node.get("userList");
        Iterator<JsonNode> uiterator = users.elements();
        while (uiterator.hasNext()) {
            JsonNode n = uiterator.next();
            User usr = mapper.treeToValue(n, User.class);
            userList.add(usr);
        }
        res.setUserList(userList);
        return res;
    }
}
