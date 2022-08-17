package com.example.assignment.repository;

import com.example.assignment.dto.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    Appointment findByIdAndDeleted(long id, boolean deletedFlag);

    List<Appointment> findByAppointmentDateGreaterThanEqualAndAppointmentDateLessThanEqual(Date startDate, Date endDate);

    @Query(value = "update Appointment set name = ?1, durationInMin = ?2, purpose = ?3, appointmentDate = ?4, " +
            "appointmentTime = ?5, updatedAt = now() where deleted = false and id = ?6")
    @Transactional
    @Modifying
    int updateNameDurationPurposeById(String name, int durationInMin, String purpose, Date appointmentDate,
                                      String appointmentTime, long id);

    @Query(value = "update Appointment set deleted = true, deletedAt = now() where deleted = false and id = ?1")
    @Transactional
    @Modifying
    int deleteAppointmentById(long id);

    void deleteById(long id);

}
