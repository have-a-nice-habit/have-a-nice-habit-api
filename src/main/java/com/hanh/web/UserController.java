package com.hanh.web;

import com.hanh.service.UserService;
import com.hanh.web.dto.UserRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/signup")
    public ResponseEntity signup(@Valid @RequestBody UserRequestDto userRequestDto){
        try{
            userService.createUser(userRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
        }
        catch(Exception e){
            logger.info("exception msg in signup : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("회원가입에 실패했습니다.");
        } // 추후 exception handler 로 개선하기
    }
}
