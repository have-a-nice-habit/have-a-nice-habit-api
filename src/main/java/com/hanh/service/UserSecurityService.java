package com.hanh.service;

import com.hanh.domain.user.User;
import com.hanh.domain.user.UserRepository;
import com.hanh.domain.user.UserRole;
import com.hanh.exception.DataNotFoundException;
import com.hanh.web.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void createUser(UserRequestDto userRequestDto){
        User newUser = new User();
        newUser.setEmail(userRequestDto.getEmail());
        newUser.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        newUser.setNickname(userRequestDto.getNickname());
        userRepository.save(newUser);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> loginUser = userRepository.findByEmail(email);
        if (loginUser.isEmpty()) {
            throw new DataNotFoundException();
        }
        User user = loginUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(user.getNickname())) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }


}
