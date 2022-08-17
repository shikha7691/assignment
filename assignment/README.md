# Appointment Application

Users can create, read, update and delete their appointments.

### Compiling
       mvn package
   
### How to Run
       java -jar {path_to_jar}

 ## Triggers these CURLS to play with the appointment applications

   *  Create Appointment
   
     curl --location --request POST 'localhost:8080/v1/createAppointment' \
     --header 'Content-Type: application/json' \
     --header 'Cookie: JSESSIONID=53190BAC726796CAC28A90AD8BFADAC7' \
     --data-raw '{
     "name": "Shikha",
     "purpose": "Eye CheckUp",
     "appointmentDate": "17-08-2022",
     "appointmentTime": "14:45",
     "durationInMin": 15
     }'

  * Get Appointment


    curl --location --request GET 'localhost:8080/v1/getAppointment/7' \
    --header 'Cookie: JSESSIONID=53190BAC726796CAC28A90AD8BFADAC7'

    
  * Get Appointments with in range


    curl --location --request GET 'localhost:8080/v1/getAppointmentWithInRange?startDate=17-08-2022&endDate=15-08-2023' \
    --header 'Cookie: JSESSIONID=53190BAC726796CAC28A90AD8BFADAC7'
    

  * Update Appointment

    
    curl --location --request PUT 'localhost:8080/v1/updateAppointment' \
    --header 'Content-Type: application/json' \
    --header 'Cookie: JSESSIONID=53190BAC726796CAC28A90AD8BFADAC7' \
    --data-raw '{
    "id": 11,
    "name": "Shikha",
    "purpose": "Eye CheckUp",
    "appointmentDate": "26-08-2022",
    "appointmentTime": "16:45",
    "durationInMin": 15
    }'

  * Delete Appointment (Just disable the deleted check to false)


    curl --location --request DELETE 'localhost:8080/v1/deleteAppointment/1' \
    --header 'Cookie: JSESSIONID=53190BAC726796CAC28A90AD8BFADAC7'


  * Delete Appointment Permanently

    
    curl --location --request DELETE 'localhost:8080/v1/deleteAppointmentPermanently/2' \
    --header 'Cookie: JSESSIONID=53190BAC726796CAC28A90AD8BFADAC7'
