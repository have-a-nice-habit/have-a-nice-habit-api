package com.hanh.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanh.domain.habit.Habit;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 닉네임
    @Column(unique = true)
    private String nickname;

    // 로그인에 사용되는 이메일
    @Column(unique = true)
    private String email;

    // 로그인에 사용되는 비밀번호 -> oauth 적용시 수정 예정
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Habit> habitList = new ArrayList<>();
}
