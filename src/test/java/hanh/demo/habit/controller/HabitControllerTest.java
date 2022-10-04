package hanh.demo.habit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanh.demo.DemoApplication;
import hanh.demo.habit.domain.Habit;
import hanh.demo.habit.dto.HabitRequestDto;
import hanh.demo.habit.dto.HabitResponseDto;
import hanh.demo.habit.repository.HabitRepository;
import hanh.demo.habit.service.HabitService;
import hanh.demo.user.domain.User;
import hanh.demo.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;
import java.util.stream.Collectors;

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

        //when
        Habit habit = habitRepository.findById(1L).get();

        Boolean temp = habit.getIsDisplay();

        //then
        mockMvc.perform(post("/habit/1/change-status")
                )
                .andExpect(status().isOk());

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

        info.add("date",dateToStr);

        // 추가 로직

        //when
        mockMvc.perform(post("/habit/"+habitId+"/add-date")
                .params(info));

        assertEquals(1, savedHabit.getDateList().size());
        //then
        assertThat(savedHabit.getDateList()
                .contains(dateToStr));



        // 취소 로직
        mockMvc.perform(post("/habit/"+habitId+"/add-date")
                .params(info));

        assertEquals(0, savedHabit.getDateList().size());

    }

    @Test
    void readAllHabitTest() throws Exception {
        //given
        MultiValueMap<String, String> info = new LinkedMultiValueMap<>();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
        MvcResult result = mockMvc.perform(get("/habit")
                .params(info))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println("content = " + content);
    }

}
