package com.example.assignment.controller;

import com.example.assignment.bean.AppointmentRequest;
import com.example.assignment.bean.AppointmentResponse;
import com.example.assignment.bean.ResponseBean;
import com.example.assignment.bean.Status;
import com.example.assignment.constants.Constants;
import com.example.assignment.service.AppointmentService;
import com.example.assignment.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = Constants.V1)
public class AppointmentController {

    private final Logger logger = LoggerFactory.getLogger(AppointmentController.class);

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping("/createAppointment")
    public ResponseEntity<ResponseBean> createAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        ResponseBean responseBean = ResponseBean.builder().build();

        try {
            logger.info("Creating Appointment | Appointment Request Data: {}", appointmentRequest);
            AppointmentResponse createdAppointment = appointmentService.createAppointment(appointmentRequest);
            if (createdAppointment != null) {
                logger.info("Created Appointment | Appointment Id: {}", createdAppointment.getId());
                responseBean.setData(createdAppointment);
            } else {
                logger.error("Failed to create Appointment | Appointment Request Data: {}", appointmentRequest);
                responseBean.setMessage("Something Wrong!!!");
                responseBean.setStatus(Status.FAILED);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            Utils.prepareException(responseBean, ex);
        }

        return new ResponseEntity<>(responseBean, HttpStatus.OK);
    }

    @GetMapping("/getAppointment/{appointmentId}")
    public ResponseEntity<ResponseBean> getAppointment(@PathVariable long appointmentId) {
        ResponseBean responseBean = ResponseBean.builder().build();

        try {
            logger.info("Getting Appointment | Appointment id: {}", appointmentId);
            AppointmentResponse appointment =  appointmentService.getAppointmentById(appointmentId);
            if (appointment != null) {
                logger.info("Found Appointment | Appointment id: {}", appointmentId);
                responseBean.setData(appointment);
            } else {
                logger.error("Appointment Not Found | Appointment id: {}", appointmentId);
                responseBean.setMessage("Appointment Not Found!!!");
                responseBean.setStatus(Status.FAILED);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            Utils.prepareException(responseBean, ex);
        }

        return new ResponseEntity<>(responseBean, HttpStatus.OK);
    }

    @GetMapping("/getAppointmentWithInRange")
    public ResponseEntity<ResponseBean> getAppointmentByIdInRange(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate) {
        ResponseBean responseBean = ResponseBean.builder().build();

        try {
            logger.info("Getting Appointment Within Range | Start Date: {} | End Date: {}", startDate, endDate);
            List<AppointmentResponse> appointments =  appointmentService.getAppointmentByIdInRange(startDate, endDate);
            if (appointments != null) {
                logger.info("Found Appointment Within Range | Appointment id: {}", appointments.toString());
                responseBean.setData(appointments);
            } else {
                logger.error("Appointment Not Found Within Range | Start Date: {} | End Date: {}", startDate, endDate);
                responseBean.setMessage("Appointment Not Found!!!");
                responseBean.setStatus(Status.FAILED);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            Utils.prepareException(responseBean, ex);
        }

        return new ResponseEntity<>(responseBean, HttpStatus.OK);
    }

    @PutMapping("/updateAppointment")
    public ResponseEntity<ResponseBean> getAppointmentByIdInRange(@RequestBody AppointmentRequest appointmentRequest) {
        ResponseBean responseBean = ResponseBean.builder().build();

        try {
            logger.info("Updating Appointment | Appointment id: {}", appointmentRequest.getId());
            if (appointmentService.updateAppointmentById(appointmentRequest) > 0) {
                logger.info("Updated Appointment | Appointment id: {}", appointmentRequest.getId());
                responseBean.setMessage("Appointment Updated Successfully!!!");
            } else {
                logger.error("Failed to Update | Appointment id: {}", appointmentRequest.getId());
                responseBean.setMessage("Failed to Update Appointment!!!");
                responseBean.setStatus(Status.FAILED);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            Utils.prepareException(responseBean, ex);
        }

        return new ResponseEntity<>(responseBean, HttpStatus.OK);
    }

    @DeleteMapping("/deleteAppointment/{appointmentId}")
    public ResponseEntity<ResponseBean> deleteAppointmentById(@PathVariable long appointmentId) {
        ResponseBean responseBean = ResponseBean.builder().build();

        try {
            logger.info("Deleting Appointment | Appointment id: {}", appointmentId);
            if (appointmentService.deleteAppointmentById(appointmentId) > 0) {
                logger.info("Deleted Appointment | Appointment id: {}", appointmentId);
                responseBean.setMessage("Appointment Deleted Successfully!!!");
            } else {
                logger.error("Failed to Delete | Appointment id: {}", appointmentId);
                responseBean.setMessage("Failed to Delete Appointment!!!");
                responseBean.setStatus(Status.FAILED);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            Utils.prepareException(responseBean, ex);
        }

        return new ResponseEntity<>(responseBean, HttpStatus.OK);
    }

    @DeleteMapping("/deleteAppointmentPermanently/{appointmentId}")
    public ResponseEntity<ResponseBean> deleteAppointmentPermanentlyById(@PathVariable long appointmentId) {
        ResponseBean responseBean = ResponseBean.builder().build();

        try {
            logger.info("Permanently Deleting Appointment | Appointment id: {}", appointmentId);
            appointmentService.deleteAppointmentPermanentlyById(appointmentId);
            logger.info("Permanently Deleted Appointment | Appointment id: {}", appointmentId);
            responseBean.setMessage("Permanently Deleted Appointment!!!");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            Utils.prepareException(responseBean, ex);
        }

        return new ResponseEntity<>(responseBean, HttpStatus.OK);
    }

}
