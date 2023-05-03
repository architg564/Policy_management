package com.user.controller;

import com.user.entity.PolicyEntity;
import com.user.entity.PolicyProxy;
import com.user.entity.UserEntity;
import com.user.jwt.JwtResponse;
import com.user.jwt.JwtUtil;
import com.user.repository.UserRepository;
import com.user.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@Slf4j
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PolicyProxy policyProxy;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/policy")
    public ResponseEntity<?> hello(@RequestBody PolicyEntity policyEntity){
        log.info("creating policy");
        return new ResponseEntity<>(policyProxy.savePolicy(policyEntity),HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable String id)throws Exception{

        try{
            log.info("deleting policy");
            return new ResponseEntity<>(policyProxy.delete(id),HttpStatus.CREATED);
        }catch (Exception e){
            throw new Exception("Invalid id or id not exist");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable String id,@RequestBody PolicyEntity policyEntity)throws Exception{
        try{
            log.info("updating policy");
            return policyProxy.update(id,policyEntity);
        }catch (Exception e){
            throw new Exception("Invalid id or id not exist");
        }
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> get(@PathVariable String id) throws Exception{
        try{
            log.info("reading policy by id");
            return new ResponseEntity<>(policyProxy.getPolicy(id),HttpStatus.CREATED);
        }catch (Exception e){
            throw new Exception("Invalid id or id not exist");
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<?> getall(){
        log.info("reading policies");
        return policyProxy.getPolicy();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserEntity userEntity){
        log.info("register user");
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRole("ROLE_USER");
        return new ResponseEntity<>(userRepository.save(userEntity), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserEntity userEntity) throws Exception {
        try{
            log.info("login user");
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEntity.getUsername(),userEntity.getPassword()));
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEntity.getUsername());
            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setToken(jwtUtil.generateToken(userDetails));
            jwtResponse.setRole(userRepository.findByUsername(userEntity.getUsername()).getRole());
            return new ResponseEntity<>(jwtResponse,HttpStatus.OK);
        }catch(Exception e){
            throw new Exception("Invalid user");
        }

    }

}

