package hanh.demo.habit.service;

import hanh.demo.habit.domain.Habit;
import hanh.demo.habit.dto.habitRequestDto;
import org.springframework.stereotype.Service;

@Service
public class HabitService {

    public void createHabit(habitRequestDto habitRequestDto){
        Habit habit = new Habit();
        habit.setTitle(habitRequestDto.getTitle());
        habit.setUser(habitRequestDto.getUser());
        habit.setCount(habitRequestDto.getCount());
    }

}
