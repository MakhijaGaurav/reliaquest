package com.reliaquest.api.dto;

import lombok.Data;

@Data
public class ServerResponseEmployee {
    private Employee data;
    private String status;
}
