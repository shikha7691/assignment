package com.example.assignment.bean;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AppointmentResponse implements Serializable {

    private long id;
    private String name;
    private String purpose;
    private String appointmentDateTime;
    private String duration;

}
