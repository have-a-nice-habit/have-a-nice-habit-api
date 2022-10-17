package com.hanh.service;

import com.hanh.domain.user.User;
import com.hanh.domain.user.UserRepository;
import com.hanh.web.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(UserRequestDto userRequestDto){
        User newUser = new User();
        newUser.setEmail(userRequestDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        newUser.setNickname(userRequestDto.getNickname());
        userRepository.save(newUser);
    }
}
