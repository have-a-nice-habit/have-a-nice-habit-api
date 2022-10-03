package hanh.demo.user.domain;

import hanh.demo.habit.domain.Habit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class User {

    @Id @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "user")
    private List<Habit> habitList = new ArrayList<>();

}
