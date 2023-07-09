package com.example.lendingApp.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response {
    private String responseMessage;
    private Object responsePayload;
}
