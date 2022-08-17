package com.example.assignment.serviceImpl;

import com.example.assignment.bean.AppointmentRequest;
import com.example.assignment.bean.AppointmentResponse;
import com.example.assignment.dto.Appointment;
import com.example.assignment.exception.ServiceException;
import com.example.assignment.repository.AppointmentRepository;
import com.example.assignment.service.AppointmentService;
import com.example.assignment.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final Logger logger = LoggerFactory.getLogger(AppointmentServiceImpl.class);

    private final AppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public AppointmentResponse createAppointment(AppointmentRequest appointmentRequest) {
        try {
            Appointment appointment = preValidateAppointment(appointmentRequest);
            return Utils.getAppointmentResponse(appointmentRepository.save(appointment));
        } catch (Exception ex) {
            throw new ServiceException("Failed to create Appointment | Reason: " + ex.getMessage());
        }
    }

    @Override
    public AppointmentResponse getAppointmentById(long id) {
        try {
            Appointment appointment = appointmentRepository.findByIdAndDeleted(id, false);
            if (appointment == null) {
                String errorMsz = "Appointment Not Found | Appointment Id: " + id;
                logger.error(errorMsz);
                throw new ServiceException(errorMsz);
            }
            return Utils.getAppointmentResponse(appointment);
        } catch (Exception ex) {
            throw new ServiceException("Failed to get Appointment | Reason: " + ex.getMessage());
        }
    }

    @Override
    public List<AppointmentResponse> getAppointmentByIdInRange(String startDate, String endDate) {
        try {
            Date startDateNew = Utils.convertStringDD_MM_YYYYFormatToDate(startDate);
            Date endDateNew = Utils.convertStringDD_MM_YYYYFormatToDate(endDate + " 59:59:59");
            if (!validateStartAndEndDate(startDate, endDate) || startDateNew == null || endDateNew == null) {
                String errorMsz = "startDate && endDate must be in dd-mm-yyyy format | Ex: 30-09-2022";
                logger.error(errorMsz);
                throw new ServiceException(errorMsz);
            }

            List<Appointment> appointmentList = appointmentRepository.
                    findByAppointmentDateGreaterThanEqualAndAppointmentDateLessThanEqual(startDateNew, endDateNew);
            if (appointmentList == null || appointmentList.isEmpty()) {
                String errorMsz = "Appointments Not Found";
                logger.error(errorMsz);
                throw new ServiceException(errorMsz);
            }
            return Utils.getAppointmentResponse(appointmentList);
        } catch (Exception ex) {
            throw new ServiceException("Failed to get Appointment Within Range | Reason: " + ex.getMessage());
        }
    }

    @Override
    public long updateAppointmentById(AppointmentRequest appointmentRequest) {
        try {
            Appointment appointment = preValidateAppointment(appointmentRequest);
            return appointmentRepository.updateNameDurationPurposeById(
                    appointment.getName(), appointment.getDurationInMin(),
                    appointment.getPurpose(), appointment.getAppointmentDate(),
                    appointment.getAppointmentTime(), appointmentRequest.getId()
            );
        } catch (Exception ex) {
            throw new ServiceException("Failed to Update Appointment | Reason: " + ex.getMessage());
        }
    }

    @Override
    public long deleteAppointmentById(long id) {
        try {
            return appointmentRepository.deleteAppointmentById(id);
        } catch (Exception ex) {
            throw new ServiceException("Failed to Delete Appointment | Reason: " + ex.getMessage());
        }
    }

    @Override
    public void deleteAppointmentPermanentlyById(long id) {
        try {
            appointmentRepository.deleteById(id);
        } catch (Exception ex) {
            throw new ServiceException("Failed to Delete Appointment Permanently | Reason: " + ex.getMessage());
        }
    }

    private Appointment preValidateAppointment(AppointmentRequest appointmentRequest) {
        logger.info("Started Validation of appointment Request Data: {}", appointmentRequest);
        String errorMsz = "";

        if (appointmentRequest.getName() == null || appointmentRequest.getName().isEmpty()) {
            errorMsz += "name can't be empty or null";
            logger.error(errorMsz);
            throw new ServiceException(errorMsz);
        }

        if (appointmentRequest.getPurpose() == null || appointmentRequest.getPurpose().isEmpty()) {
            errorMsz += "purpose can't be empty or null";
            logger.error(errorMsz);
            throw new ServiceException(errorMsz);
        }

        Date appointmentDate = Utils.convertStringDD_MM_YYYYFormatToDate(appointmentRequest.getAppointmentDate());
        if (appointmentDate == null) {
            errorMsz += "appointmentDate must be in dd-mm-yyyy format | Ex: 30-09-2022";
            logger.error(errorMsz);
            throw new ServiceException(errorMsz);
        }

        String appointmentTime = appointmentRequest.getAppointmentTime();
        if (appointmentTime == null || !appointmentTime.contains(":")) {
            errorMsz += "Invalid format. Please provide this format (13:30)";
            logger.error(errorMsz);
            throw new ServiceException(errorMsz);
        }
        String[] arr = appointmentTime.split(":");
        int hour = Integer.parseInt(arr[0].trim());
        int min = Integer.parseInt(arr[1].trim());
        if (hour < 0 || hour > 23) {
            errorMsz += "appointmentTime hour should be in range of [0-23] hour";
            logger.error(errorMsz);
            throw new ServiceException(errorMsz);
        } else if (min > 45 || min % 15 != 0) {
            errorMsz += "appointmentTime minutes should be either [15 / 30 / 45] minutes";
            logger.error(errorMsz);
            throw new ServiceException(errorMsz);
        }

        int duration = appointmentRequest.getDurationInMin();
        if (duration == 0) {
            errorMsz += "durationInMin can't be empty or null";
            logger.error(errorMsz);
            throw new ServiceException(errorMsz);
        }
        if (duration > 60) {
            errorMsz += "durationInMin can't be more than 60 minutes";
            logger.error(errorMsz);
            throw new ServiceException(errorMsz);
        } else if (duration % 15 != 0) {
            errorMsz += "durationInMin should be either [15 / 30 / 45 / 60] minutes";
            logger.error(errorMsz);
            throw new ServiceException(errorMsz);
        }

        Appointment appointment = Appointment.builder()
                .name(appointmentRequest.getName())
                .purpose(appointmentRequest.getPurpose())
                .appointmentDate(appointmentDate)
                .appointmentTime(appointmentTime)
                .durationInMin(duration)
                .createdAt(new Date())
                .build();
        logger.info("Validation Success of appointment Request Data: {}", appointmentRequest);

        return appointment;
    }

    private boolean validateStartAndEndDate(String startDate, String endDate) {
        try {
            String[] arr = startDate.trim().split("-");
            String[] arr2 = endDate.trim().split("-");
            return (arr[0].length() == 2 && arr[1].length() == 2 && arr[2].length() == 4) &&
                    arr2[0].length() == 2 && arr2[1].length() == 2 && arr2[2].length() == 4;
        } catch (Exception ex) {
            return false;
        }
    }

}
