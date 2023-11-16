package com.example.Product.Errors;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends Exception {
    private List<String> messageList;

    public ValidationException(String message, List<String> messageList) {
        super(message);
        this.messageList = messageList;
    }

    public ValidationException(String message) {
        super(message);
        this.messageList = new ArrayList<>();
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.messageList = new ArrayList<>();
    }

    public List<String> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<String> messageList) {
        this.messageList = messageList;
    }
}
