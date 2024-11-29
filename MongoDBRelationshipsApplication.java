package com.example.mongodbrelationships;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
public class MongoDBRelationshipsApplication {
    public static void main(String[] args) {
        SpringApplication.run(MongoDBRelationshipsApplication.class, args);
    }
}

@Document
class User {
    @Id
    private String id;
    private String name;

    @DBRef
    private Address address;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}

@Document
class Address {
    @Id
    private String id;
    private String city;
    private String state;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}

interface UserRepository extends MongoRepository<User, String> {
}

interface AddressRepository extends MongoRepository<Address, String> {
}

@Service
class UserService {

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

@RestController
@RequestMapping("/users")
class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public String createUser(@RequestParam String name, @RequestParam String city, @RequestParam String state) {
        userService.createUserWithAddress(name, city, state);
        return "User created successfully!";
    }
}
