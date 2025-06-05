package com.reliaquest.api.dto;

import java.util.List;
import lombok.Data;

@Data
public class ServerResponseList {
    private List<Employee> data;
    private String status;
}
