package hanh.demo.habit.dto;

import hanh.demo.habit.domain.Habit;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class HabitResponseDto {

    private Long id;

    private boolean isDisplay;

    private Integer count;

    @Builder
    public HabitResponseDto(
            Habit habit
    ){
        this.id = habit.getId();
        this.isDisplay = habit.getIsDisplay();
        this.count = habit.getCount();
    }

}
