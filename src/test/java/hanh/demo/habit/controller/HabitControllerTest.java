package hanh.demo.habit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanh.demo.DemoApplication;
import hanh.demo.habit.domain.Habit;
import hanh.demo.habit.dto.HabitRequestDto;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @BeforeEach
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
    void readHabit() throws Exception {

        mockMvc.perform(get("/habit")
                )
                .andExpect(status().isOk());
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
}
