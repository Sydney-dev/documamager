# Document Management System

## Run the Application

To run the application, run the following command in a terminal window

`./gradlew bootRun`

Then goto http://localhost:8181/swagger-ui/index.html for swagger docomentation of all the end-points

## Testing

Run the following command in a terminal window

`./gradlew test`

## Coverage Report

Run `./gradlew build` 

JaCoCo now automatically creates a file build/jacoco/test.exec which contains the coverage statistics in binary form.

Then Run `./gradlew build jacocoTestReport` to generate htm report

Results are in to build/reports/jacoco/test/html/index.html
