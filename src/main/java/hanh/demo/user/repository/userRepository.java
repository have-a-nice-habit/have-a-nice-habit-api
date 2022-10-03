package hanh.demo.user.repository;

import hanh.demo.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<User, Long> {
}
