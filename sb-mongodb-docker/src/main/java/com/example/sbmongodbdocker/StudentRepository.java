package com.example.sbmongodbdocker;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {
    
    Optional<Student> findStudentByEmail(String email);
    
    
    /*
    In Spring Data MongoDB, you can use @Query annotation to create custom queries in your repository methods.

    Here's an example:
    
    Suppose you have a User class with fields id, name, age, and email. You want to retrieve all users who are over a 
    certain age.
    
    Define a repository interface for the User class, extending the MongoRepository interface:
        public interface UserRepository extends MongoRepository<User, String> {
        }
        
    Add a method to the UserRepository interface with the @Query annotation:
        @Query("{ age : { $gt: ?0 } }")
        List<User> findByAgeGreaterThan(int age);
        
    This method uses the @Query annotation to define a custom query that retrieves all users with an age greater than 
    the given age parameter. The query uses MongoDB's $gt operator to compare the age field with the age parameter.
    
    You can now call this method from your application code:
        List<User> users = userRepository.findByAgeGreaterThan(30);
        
    This code retrieves all users with an age greater than 30 using the custom query defined in the 
    findByAgeGreaterThan method.
    
    You can use other MongoDB query operators and functions in your @Query annotations to create more complex queries. 
    The syntax of the queries follows MongoDB's query syntax, with the addition of parameter placeholders (e.g. ?0) 
    to indicate method parameters.

     */
}
