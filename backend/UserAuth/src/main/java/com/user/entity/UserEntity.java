package com.user.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {
    @Id
    private String username;
    private String password;
    private String role;
    private String firstName;
    private String lastName;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date dateOfBirth;
    private String address;
    private long phoneNumber;
    private long salary;
    private String panNumber;
    private String employeeType;
}