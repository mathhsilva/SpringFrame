package com.example.Product.Errors;

import java.util.ArrayList;
import java.util.List;

public class JsonParseException extends Exception {
    private List<String> messageList;
    public JsonParseException(String message) {
        super(message);
        this.messageList = new ArrayList<>();
    }

    public List<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<String> messageList) {
        this.messageList = messageList;
    }
}
