package hanh.demo.habit.dto;

import hanh.demo.habit.domain.Habit;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class HabitResponseDto {

    private Long id;

    private String name;

    //그 날 띄우는지 여부
    private boolean todayDisplay;

    //오늘 띄우는지 여부
    private boolean isDisplay;

    // 달성 여부
    private boolean isAchieved;

    private String emoji;

    private Integer count;

    @Builder
    public HabitResponseDto(
            Habit habit,
            LocalDate date
    ){
        this.id = habit.getId();
        this.isDisplay = habit.getIsDisplay();
        this.name = habit.getTitle();
        this.todayDisplay = getIsDisplay(habit.getDisplayDateList(), date);
        this.count = habit.getCount();
        this.emoji = habit.getEmoji();
        this.isAchieved = getIsAchieved(habit.getDateList(), date);
    }

    private boolean getIsDisplay(
            List<LocalDate> displayDateList,
            LocalDate date
    ){
        return displayDateList.contains(date);
    }

    private boolean getIsAchieved(
            List<LocalDate> dateList,
            LocalDate date
    ){
        return dateList.contains(date);
    }



}
