package ru.practicum.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);

    void deleteById(long id);

    Page<User> findAllUsers(Pageable pageable);

    Page<User> findAllByIds(List<Long> ids, Pageable pageable);
    List<Long> findAllById(List<Long> ids);
}