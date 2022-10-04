package hanh.demo.habit.repository;

import hanh.demo.habit.domain.Habit;
import hanh.demo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HabitRepository extends JpaRepository<Habit, Long> {

    List<Habit> findAllByUser(User user);

}
