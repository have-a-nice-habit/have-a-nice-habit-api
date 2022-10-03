package hanh.demo.habit.controller;

import hanh.demo.exception.DataNotFoundException;
import hanh.demo.habit.domain.Habit;
import hanh.demo.habit.repository.HabitRepository;
import hanh.demo.habit.service.HabitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/habit")
public class habitController {

    private HabitService habitService;
    private HabitRepository habitRepository;

    @PostMapping("/change-status")
    public ResponseEntity changeStatus(@PathVariable Long habitId){
        Optional<Habit> findHabit = habitRepository.findById(habitId);

        if (findHabit.isPresent()){
            findHabit.get().changeStatus();
        }else{
            throw new DataNotFoundException();
        }

        return ResponseEntity.status(HttpStatus.OK).body("습관의 상태를 변경했습니다.");

    }
}
