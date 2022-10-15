package com.hanh.web.dto;

import com.hanh.domain.habit.Habit;
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

    private Integer weekCount;

    @Builder
    public HabitResponseDto(
            Habit habit,
            LocalDate date
    ){
        List<LocalDate> displayDateList = habit.getDisplayDateList();

        this.id = habit.getId();
        this.isDisplay = habit.getIsDisplay();
        this.name = habit.getTitle();
        this.todayDisplay = getIsDisplay(displayDateList, date);
        this.count = habit.getCount();
        this.emoji = habit.getEmoji();
        this.isAchieved = getIsAchieved(displayDateList, date);
        this.weekCount = habit.getWeekCount();
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
