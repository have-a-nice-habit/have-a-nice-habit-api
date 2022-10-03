package hanh.demo.habit.controller;

import hanh.demo.exception.DataNotFoundException;
import hanh.demo.habit.domain.Habit;
import hanh.demo.habit.dto.HabitRequestDto;
import hanh.demo.habit.repository.HabitRepository;
import hanh.demo.habit.service.HabitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/habit")
public class HabitController {

    private final HabitService habitService;
    private final HabitRepository habitRepository;

    @PostMapping("{habitId}/change-status")
    public ResponseEntity changeStatus(@PathVariable Long habitId){
        Optional<Habit> findHabit = habitRepository.findById(habitId);

        if (findHabit.isPresent()){
            findHabit.get().changeStatus();
        }else{
            throw new DataNotFoundException();
        }

        return ResponseEntity.status(HttpStatus.OK).body("상태를 변경했습니다.");

    }

    @PostMapping
    public ResponseEntity createHabit(@RequestBody @Valid HabitRequestDto habitRequestDto, BindingResult result){

        Habit savedHabit = habitService.createHabit(habitRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedHabit);

    }

    @GetMapping
    public ResponseEntity readAllHabit() {

        List<Habit> habitList = habitService.readAllHabit();

        return ResponseEntity.status(HttpStatus.OK).body(habitList);

    }
}
