package hanh.demo.user.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nickname;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Habit> habitList = new ArrayList<>();

}
