package com.example.assignment.service;

import com.example.assignment.bean.AppointmentRequest;
import com.example.assignment.bean.AppointmentResponse;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse createAppointment(AppointmentRequest appointmentRequest);

    AppointmentResponse getAppointmentById(long id);

    List<AppointmentResponse> getAppointmentByIdInRange(String startDate, String endDate);

    long updateAppointmentById(AppointmentRequest appointmentRequest);

    long deleteAppointmentById(long id);

    void deleteAppointmentPermanentlyById(long id);
}
