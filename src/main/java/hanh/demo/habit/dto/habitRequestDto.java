package hanh.demo.habit.dto;

import hanh.demo.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabitRequestDto {

    private User user;

    private String title;

    private int count;

    @Builder
    public HabitRequestDto(
            User user,
            int count,
            String title
    ){
        this.user = user;
        this.count = count;
        this.title = title;
    }

}
