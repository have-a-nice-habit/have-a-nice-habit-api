package com.hanh.habit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanh.domain.habit.Habit;
import com.hanh.domain.habit.HabitRepository;
import com.hanh.domain.user.User;
import com.hanh.domain.user.UserRepository;
import com.hanh.service.HabitService;
import com.hanh.web.dto.HabitRequestDto;
import com.hanh.DemoApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.not;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class HabitControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    HabitRequestDto habitRequestDto;

    @Autowired
    HabitService habitService;

    @Autowired
    HabitRepository habitRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void createHabit() throws Exception {

        //given
        User testUser = new User();
        userRepository.save(testUser);

        String title = "물 마시기";
        int count = 3;
        User user = testUser;

        //when
        String body = mapper.writeValueAsString(
                habitRequestDto.builder()
                        .title(title)
                        .count(count)
                        .user(testUser)
                        .build()
        );

        //then
        mockMvc.perform(post("http://localhost:8080/habit")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }

    @Test
    void changeStatus() throws Exception {

        long beforeTime = System.currentTimeMillis();
        //when
        Habit habit = habitRepository.findById(1L).get();

        Boolean temp = habit.getIsDisplay();
        //then
        mockMvc.perform(post("/habit/1/change-status")
                )
                .andExpect(status().isOk());

        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime)/1000; //두 시간에 차 계산
        System.out.println("시간차이(m) : "+secDiffTime);

        Assertions.assertNotEquals(temp, habit.getIsDisplay());

    }

    @Test
    void addDateTest() throws Exception{

        //given
        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String dateToStr = dateFormat.format(date);

        User testUser = new User();
        testUser.setNickname("HELLO");
        userRepository.save(testUser);

        Habit habit = new Habit();
        habit.setTitle("테스트용입니다");
        habit.setCount(1);
        habit.setUser(testUser);

        Habit savedHabit = habitRepository.save(habit);

        Long habitId = savedHabit.getId();


        // 추가 로직

        //when
        mockMvc.perform(post("/habit/"+habitId+"/add-date?date=2022-10-04"));

        mockMvc.perform(post("/habit/"+habitId+"/add-date?date=2022-09-04"));

        assertEquals(2, savedHabit.getDateList().size());
        //then
        assertThat(savedHabit.getDateList()
                .contains(dateToStr));


        // 취소 로직
        mockMvc.perform(post("/habit/"+habitId+"/add-date?date=2022-10-04"));

        assertEquals(1, savedHabit.getDateList().size());

    }

    @Test
    @Transactional
    void readAllHabitTest() throws Exception {
        //given
        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateToStr = dateFormat.format(date);

        User testUser = userRepository.findById(1L).get();

        Habit habit = new Habit();
        habit.setTitle("테스트용입니다");
        habit.setCount(1);
        habit.setUser(testUser);

        Habit savedHabit = habitRepository.save(habit);

        info.add("nickname",testUser.getNickname());

        // isAchieved 를 확인하기 위해서 add-date 도 호출
        mockMvc.perform(post("/habit/"+3+"/add-date")
                .param("date",dateToStr));

        System.out.println("dateTostr = " + dateToStr);

        //when, then
        MvcResult result = mockMvc.perform(get("/habit?date=2022-09-10")
                .params(info))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println("content = " + content);
    }

}
