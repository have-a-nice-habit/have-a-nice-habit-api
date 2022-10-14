package com.hanh.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanh.domain.habit.Habit;
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
