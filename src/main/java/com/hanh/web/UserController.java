package com.hanh.web;

import com.hanh.service.UserSecurityService;
import com.hanh.web.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/auth")
public class UserController {
    private final UserSecurityService userSecurityService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody UserRequestDto userRequestDto){
        try{
            userSecurityService.createUser(userRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
        }
        catch(Exception e){
            logger.info("exception msg in signup : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입에 실패했습니다.");
        } // 추후 exception handler 로 개선하기
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserRequestDto requestDto, HttpServletRequest request) throws Exception {

        final UserDetails userDetails = userSecurityService
                .loadUserByUsername(requestDto.getEmail());

        System.out.println("userDetails = " + userDetails);

        authenticate(requestDto.getEmail(), requestDto.getPassword());

        System.out.println("-------------------------");
        HttpSession session = request.getSession();
        System.out.println("session = " + session);
        System.out.println("-------------------------");

        return ResponseEntity.status(HttpStatus.OK).body(userDetails);
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
