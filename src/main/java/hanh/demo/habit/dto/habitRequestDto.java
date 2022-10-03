package hanh.demo.habit.dto;

import hanh.demo.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class habitRequestDto {

    private User user;

    private String title;

    private int count;

    @Builder
    public habitRequestDto(
            User user,
            int count,
            String title
    ){
        this.user = user;
        this.count = count;
        this.title = title;
    }

}
