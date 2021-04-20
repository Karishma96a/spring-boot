# spring-boot

This project uses the H2 database.

To launch this application: Run DemoApplication.java

To access the database after launching the application:
1. http://localhost:8080/h2-console
2. Driver class - org.h2.Driver
3. JDBC URL - jdbc:h2:mem:testdb
4. User Name - sa


A REST service that supports these endpoints:

1. POST Mapping - URL - "http://localhost:8080/employee/create" - creates employee and the training period

JSON structure Example:
{
    "employeeId": "xyz@gmail.com",
    "trainingPeriod": [
        {
            "startDate": "2021-04-02",
            "endDate": "2021-04-03",
            "time": "02:00",
            "duration": "60",
            "repeat": true,
            "frequency": "WEEKLY"
        },
        {
            "startDate": "2021-04-06",
            "endDate": "2021-04-08",
            "time": "03:00",
            "duration": "40",
            "repeat": true,
            "frequency": "DAILY"
        }
    ]
}


2. GET Mapping - URL - "http://localhost:8080/schedule/{employeeId}" - returns the training period schedule of the given employee ID

eg: "http://localhost:8080/schedule/xyz@gmail.com"

3. DELETE Mapping - URL - "http://localhost:8080/cancel/schedule/{employeeId}/{scheduleId}" - deletes corresponding schedule (found using the scheduleId) from training period of given employee id

eg: "http://localhost:8080/cancel/schedule/xyz@gmail.com/1"

5. DELETE Mapping - URL - "http://localhost:8080/cancel/schedule/{employeeId}" - deletes the training period for the given employee id
eg: "http://localhost:8080/cancel/schedule/xyz@gmail.com"


7. PUT Mapping - URL - "http://localhost:8080/update/schedule/{employeeId}" - updates the training period for the given employee id

eg: "http://localhost:8080/update/schedule/xyz@gmail.com"

Sample JSON:
[{
    "startDate": "10 April 2021",
    "endDate": "11 April 2021",
    "time": "02:00",
    "duration": "60",
    "repeat": true,
    "frequency": "WEEKLY"
}]

9. GET Mapping - URL - "http://localhost:8080/schedule/date/{date}" - returns all the schedules that fall on the given date

Eg: http://localhost:8080/schedule/date/2021-04-12

