package hanh.demo.habit.service;

import hanh.demo.habit.domain.Habit;
import hanh.demo.habit.dto.HabitRequestDto;
import hanh.demo.habit.dto.HabitResponseDto;
import hanh.demo.habit.repository.HabitRepository;
import hanh.demo.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HabitService {

    private final HabitRepository habitRepository;

    @Transactional
    public Habit createHabit(HabitRequestDto habitRequestDto) {
        Habit habit = new Habit();

        habit.setTitle(habitRequestDto.getTitle());
        habit.setUser(habitRequestDto.getUser());
        habit.setCount(habitRequestDto.getCount());
        habit.setEmoji(habit.getEmoji());

        habitRepository.save(habit);
        return habit;
    }

    public List<HabitResponseDto> findAllByUser(User user, LocalDate date) {
        List<HabitResponseDto> habitResponseDtoList = habitRepository.findAllByUser(user)
                .stream().map(h -> new HabitResponseDto(h,date)).collect(Collectors.toList());

        return habitResponseDtoList;
    }

}
