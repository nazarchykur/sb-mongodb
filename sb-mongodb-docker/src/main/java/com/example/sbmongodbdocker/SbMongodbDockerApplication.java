package com.example.sbmongodbdocker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
@Slf4j
public class SbMongodbDockerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SbMongodbDockerApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(StudentRepository repository, MongoTemplate mongoTemplate) {
        return (args) -> {
            Address address = new Address(
                    "UA",
                    "IF",
                    "76000"
            );

            String email = "mikeyturtles@gmail.com";
            Student student = new Student(
                    "Mikey",
                    "Turtles",
                    email,
                    Gender.MALE,
                    address,
                    List.of("Computer science", "Maths"),
                    BigDecimal.TEN,
                    LocalDateTime.now()
            );

//            usingMongoTemplateAndQuery(repository, mongoTemplate, email, student);
            
            // the same logic is done here but cleaner using method (previously added) from repository
            repository.findStudentByEmail(email)
                    .ifPresentOrElse(student1 -> {
                        log.error("The student already exists: " + student1);
                    }, () -> {
                        log.info("Inserting student: " + student);
                        repository.insert(student);
                    });
        };
    }

    private static void usingMongoTemplateAndQuery(StudentRepository repository, MongoTemplate mongoTemplate, String email, Student student) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(email));

        List<Student> students = mongoTemplate.find(query, Student.class);

        if (students.size() > 1) {
            log.error("Found many students with email: " + email);
            throw new IllegalStateException("Found many students with email: " + email);
        }

        if (students.isEmpty()) {
            log.info("Inserting student: " + student);
            repository.insert(student);
        } else {
            log.error("The student already exists: " + student);
        }
    }
}
