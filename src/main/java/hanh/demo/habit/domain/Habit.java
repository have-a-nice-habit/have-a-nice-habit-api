package hanh.demo.habit.domain;

import hanh.demo.user.domain.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Data
public class Habit {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

    @NotNull(message = "제목은 필수 값입니다.")
    private String title;

    @Column(name="is_display")
    @ColumnDefault("true")
    private Boolean isDisplay;

    // 일주일에 몇번 수행할것인지
    @Max(7)
    @Min(1)
    @ColumnDefault("1")
    private Integer count ;

    @ElementCollection
    @Column(name="date_list")
    private List<Date> dateList = new ArrayList<>();

    // 해빗 수행 시 날짜 추가
    public void addDate(Date date){
        this.dateList.add(date);
    }

    public void changeStatus(){
        this.isDisplay = !(this.isDisplay);
    }

}
