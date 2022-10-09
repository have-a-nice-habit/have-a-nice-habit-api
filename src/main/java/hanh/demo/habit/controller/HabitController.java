package hanh.demo.habit.controller;

import hanh.demo.exception.DataNotFoundException;
import hanh.demo.habit.domain.Habit;
import hanh.demo.habit.dto.HabitRequestDto;
import hanh.demo.habit.dto.HabitResponseDto;
import hanh.demo.habit.repository.HabitRepository;
import hanh.demo.habit.service.HabitService;
import hanh.demo.user.domain.User;
import hanh.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/habit")
public class HabitController {

    private final HabitService habitService;
    private final HabitRepository habitRepository;

    private final UserRepository userRepository;

    @PostMapping("{habitId}/change-status")
    public ResponseEntity changeStatus(@PathVariable Long habitId,
                                        @RequestParam("date")
                                        @DateTimeFormat(pattern = "yyyy-MM-dd")
                                        LocalDate date
    ){
        Optional<Habit> findHabit = habitRepository.findById(habitId);

        if (findHabit.isPresent()){
            habitRepository.save(findHabit.get().changeStatus(date));
        }else{
            throw new DataNotFoundException(); // exception controller 로 빼기
        }

        return ResponseEntity.status(HttpStatus.OK).body("상태를 변경했습니다.");

    }

    @PostMapping
    public ResponseEntity createHabit(@RequestBody @Valid HabitRequestDto habitRequestDto, BindingResult result){

        Habit savedHabit = habitService.createHabit(habitRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedHabit);

    }

    @GetMapping
    public ResponseEntity readAllHabit(String nickname,
                                       @RequestParam("date")
                                       @DateTimeFormat(pattern = "yyyy-MM-dd")
                                       LocalDate date) {

        Optional<User> user = userRepository.findByNickname(nickname);

        List<HabitResponseDto> habitList = habitService.findAllByUser(user.get(), date);

        return ResponseEntity.status(HttpStatus.OK).body(habitList);

    }

    @PostMapping("{habitId}/add-date")
    public ResponseEntity addDate(@PathVariable Long habitId,
                                  @RequestParam("date")
                                  @DateTimeFormat(pattern = "yyyy-MM-dd")
                                  LocalDate date){

        Optional<Habit> findHabit = habitRepository.findById(habitId);

        if (findHabit.isPresent()){
            List<LocalDate> dateList = findHabit.get().getDateList();
            if (!dateList.contains(date)) {
                habitRepository.save(findHabit.get().addDate(date));
            }
            else{
                habitRepository.save(findHabit.get().removeDate(date));}
        }else{
            throw new DataNotFoundException();
        }

        return ResponseEntity.status(HttpStatus.OK).body(date+"의 달성 여부가 변경되었습니다");
    }

}
