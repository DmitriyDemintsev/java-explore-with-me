package ru.practicum.service.user;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.User;

import java.util.List;

@Transactional(readOnly = true)
public interface UserService {

    @Transactional
    User create(User user);

    @Transactional
    void delete(long id);

    User getUserById(long id);

    List<User> getUsers(List<Long> ids, int from, int size);
}
