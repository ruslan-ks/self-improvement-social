package rkostiuk.selfimprovement.repository;

import rkostiuk.selfimprovement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Page<User> findByStatus(User.Status status, Pageable pageable);

    Page<User> findByFollowingsContaining(User user, Pageable pageable);

    Page<User> findByFollowersContaining(User user, Pageable pageable);

}
