package hanh.demo.habit.service;

import hanh.demo.habit.domain.Habit;
import hanh.demo.habit.dto.HabitRequestDto;
import hanh.demo.habit.repository.HabitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitRepository habitRepository;

    @Transactional
    public Habit createHabit(HabitRequestDto habitRequestDto){
        Habit habit = new Habit();

        habit.setTitle(habitRequestDto.getTitle());
        habit.setUser(habitRequestDto.getUser());
        habit.setCount(habitRequestDto.getCount());

        habitRepository.save(habit);
        return habit;
    }

    public List<Habit> readAllHabit(){
        return habitRepository.findAll();
    }
}
