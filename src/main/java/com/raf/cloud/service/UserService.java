package com.raf.cloud.service;

import com.raf.cloud.model.User;
import com.raf.cloud.model.UserInfo;
import com.raf.cloud.repository.UserRepository;
import com.raf.cloud.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User addUser(RegisterRequest request){
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(request.getRoles())
                .build();

        User newUser = userRepository.save(user);
        return newUser;
    }


    public User updateUser(UserInfo userInfo){
        Optional<User> optionalUser = userRepository.findById(userInfo.getId());
        System.out.println(userInfo.getEmail());
        if (optionalUser.isEmpty()) {
            return null;
        }

        User user = optionalUser.get();
        if (userInfo.getFirstname() != null) {
            user.setFirstname(userInfo.getFirstname());
        }
        if (userInfo.getLastname() != null) {
            user.setLastname(userInfo.getLastname());
        }
        if (userInfo.getEmail() != null) {
            user.setEmail(userInfo.getEmail());
        }

        return userRepository.save(user);
    }

    public void deleteUser(Integer id){
        userRepository.deleteById(id);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User getById(Integer id){
        Optional<User> user = userRepository.findById(id);
        if(!user.isPresent()){
            return null;
        }
        return user.get();
    }



}
