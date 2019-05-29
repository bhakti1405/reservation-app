To start REST micro service 

$ mvn spring-boot:run


Example REST commands:
GET http://localhost:8080/numSeatsAvailable
POST http://localhost:8080/hold?numSeats=10&customerEmail=mal@sdf.com
POST http://localhost:8080/reserve?seatHoldId=21&customerEmail=mal@asd.com




To run unit tests
$mvn test




