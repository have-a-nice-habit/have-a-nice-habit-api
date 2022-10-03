package hanh.demo.habit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hanh.demo.DemoApplication;
import hanh.demo.habit.dto.HabitRequestDto;
import hanh.demo.user.domain.User;
import hanh.demo.user.repository.UserRepository;
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
        /**
         * Object를 JSON으로 변환
         * */
        String body = mapper.writeValueAsString(
                habitRequestDto.builder()
                        .title(title)
                        .count(count)
                        .user(testUser)
                        .build()
        );

        //then
        mockMvc.perform(post("http://localhost:8080/habit")
                        .content(body) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isCreated());
    }

    @Test
    void readHabit() throws Exception {

        mockMvc.perform(get("/habit")
                )
                .andExpect(status().isOk());
    }
}
