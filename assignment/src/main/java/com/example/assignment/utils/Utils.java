package com.example.assignment.utils;

import com.example.assignment.bean.AppointmentResponse;
import com.example.assignment.bean.ResponseBean;
import com.example.assignment.bean.Status;
import com.example.assignment.dto.Appointment;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class Utils {

    public static final String DATE_FORMAT_DD_MM_YYYY_ISO = "dd-MM-yyyy";

    public static void prepareException(ResponseBean responseBean, Exception ex) {
        responseBean.setMessage(ex.getMessage());
        responseBean.setStatus(Status.FAILED);
    }

    public static Date convertStringDD_MM_YYYYFormatToDate(String date) {
        try {
            return new SimpleDateFormat(DATE_FORMAT_DD_MM_YYYY_ISO).parse(date);
        } catch (Exception ex) {
            return null;
        }
    }

    public static AppointmentResponse getAppointmentResponse(Appointment appointment) {
        return  AppointmentResponse.builder()
                .id(appointment.getId())
                .name(appointment.getName())
                .purpose(appointment.getPurpose())
                .appointmentDateTime(appointment.getAppointmentDateTime())
                .duration(appointment.getDurationInMin() + " minutes")
                .build();
    }

    public static List<AppointmentResponse> getAppointmentResponse(List<Appointment> appointments) {
        List<AppointmentResponse> appointmentResponseList = new ArrayList<>();
        appointments.forEach(appointment -> appointmentResponseList.add(getAppointmentResponse(appointment)));
        return appointmentResponseList;
    }

}
