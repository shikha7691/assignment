package com.example.assignment.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;


@Entity
@Table(name = "appointment")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private long id;

    @Column(columnDefinition = "datetime default now()")
    private Date createdAt;

    @Column(columnDefinition = "datetime default null on update now()")
    private Date updatedAt;

    @Column
    private Date deletedAt;

    @Builder.Default
    @Column(name = "isDeleted", columnDefinition = "int(1) default 0")
    private boolean deleted = false;

    @Column
    private Date appointmentDate;

    @Column
    private String appointmentTime;

    @Column
    private int durationInMin;

    @Column
    private String name;

    @Column
    private String purpose;

    @Transient
    private String appointmentDateTime;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = null;
        deletedAt = null;
    }

    public String getAppointmentDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = formatter.format(appointmentDate);
        String finalTime = appointmentTime;
        String[] arr = appointmentTime.split(":");
        int hour = Integer.parseInt(arr[0]);
        if (hour < 12) {
            finalTime += " AM";
        } else {
            hour = hour - 12;
            finalTime = hour + ":" + arr[1] + " PM";
        }
        return appointmentDateTime = strDate + " | " + finalTime;
    }

}
