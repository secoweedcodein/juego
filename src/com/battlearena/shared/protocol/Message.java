package com.battlearena.shared.protocol;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private MessageType type;
    private Map<String, String> data;
    private Object payload;

    public Message(MessageType type) {
        this.type = type;
        this.data = new HashMap<>();
    }

    public MessageType getType() {
        return type;
    }

    public void put(String key, String value) {
        if (key != null) {
            data.put(key, value);
        }
    }

    public String get(String key) {
        return data.get(key);
    }

    public Map<String, String> getAllData() {
        return data;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public Object getPayload() {
        return payload;
    }
}