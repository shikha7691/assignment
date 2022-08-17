package com.example.assignment.bean;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBean {

    @Builder.Default
    private Status status = Status.SUCCESS;
    private String message;
    private Object data;
}
