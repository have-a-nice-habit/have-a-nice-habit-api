package hanh.demo.habit.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import hanh.demo.user.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@DynamicInsert
public class Habit {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;

    @NotNull(message = "제목은 필수 값입니다.")
    private String title;

    private String emoji;

    @Column(name="is_display")
    @ColumnDefault("true")
    private Boolean isDisplay;

    @ElementCollection
    @Column(name="display_date_list")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private List<LocalDate> displayDateList = new ArrayList<LocalDate>();

    // 한 주에 몇번 수행했는지
    @ColumnDefault("0")
    private Integer weekCnt;

    // 일주일에 몇번 수행할것인지
    @Max(value = 7, message = "횟수는 최대 7번까지 설정 가능합니다.")
    @Min(value = 1, message = "횟수는 최소 1번 이상이어야 합니다.")
    @ColumnDefault("1")
    private Integer count ;

    @ElementCollection
    @Column(name="date_list")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private List<LocalDate> dateList = new ArrayList<LocalDate>();

    @PrePersist
    public void prePersist() {
        this.isDisplay = this.isDisplay == null ? true : this.isDisplay;
    }

    public Habit changeStatus(LocalDate date){

        this.isDisplay = !this.isDisplay;
        if (this.isDisplay == true && !(this.displayDateList.contains(date))) {
            this.displayDateList.add(date);
        }
        return this;
    }

    public Habit addDate(LocalDate date){
        this.dateList.add(date);
        return this;
    }

    public Habit removeDate(LocalDate date){
        this.dateList.remove(date);
        return this;
    }

    public Habit addDisplayDate(LocalDate date){
        this.displayDateList.add(date);
        return this;
    }

}
