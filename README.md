# Spring Boot MongoDB Relationships

This repository demonstrates the implementation of MongoDB relationships in Spring Boot applications using the `@DBRef` annotation. It includes practical examples, flowcharts, and best practices to help developers seamlessly link collections and manage data efficiently.
---
![Mastering MongoDB Relationships SB](https://github.com/user-attachments/assets/7570deb9-1ea8-496c-b182-2cb2873f4c18)
---
## Features
- Usage of `@DBRef` annotation for linking MongoDB collections.
- Practical examples of one-to-one, one-to-many, and many-to-many relationships.
- Comprehensive flowcharts and diagrams for better understanding.
- Spring Boot configuration and setup for MongoDB integration.


### Configuration
1. Update `application.properties` in `src/main/resources` with your MongoDB connection details:
   ```properties
   spring.data.mongodb.uri=mongodb://localhost:27017/your-database
   ```

### Run the Application
```bash
mvn spring-boot:run
```

## Code Example

### Model Classes
#### User Model
```java
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {
    @Id
    private String id;
    private String name;

    @DBRef
    private Address address;

    // Getters and Setters
}
```

#### Address Model
```java
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Address {
    @Id
    private String id;
    private String city;
    private String state;

    // Getters and Setters
}
```

### Repository Interfaces
#### UserRepository
```java
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
```

#### AddressRepository
```java
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AddressRepository extends MongoRepository<Address, String> {
}
```

### Service Layer
#### UserService
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    public void createUserWithAddress(String userName, String city, String state) {
        Address address = new Address();
        address.setCity(city);
        address.setState(state);
        addressRepository.save(address);

        User user = new User();
        user.setName(userName);
        user.setAddress(address);
        userRepository.save(user);
    }
}
```

### Controller
#### UserController
```java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public String createUser(@RequestParam String name, @RequestParam String city, @RequestParam String state) {
        userService.createUserWithAddress(name, city, state);
        return "User created successfully!";
    }
}
```
