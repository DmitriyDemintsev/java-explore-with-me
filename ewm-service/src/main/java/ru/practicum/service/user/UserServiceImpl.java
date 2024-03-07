package ru.practicum.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.UserAlreadyExistException;
import ru.practicum.exception.UserNotFoundException;
import ru.practicum.exception.UserValidationException;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User create(User user) {
        List<User> users = userRepository.findAll();
        for (User user1 : users) {
            if (user1.getEmail().equals(user.getEmail())) {
                throw new UserAlreadyExistException("Пользователь с таким Email уже сущестует");
            }
        }
        if (user.getName() == null || user.getName().isBlank()) {
            throw new UserValidationException("Отсутствует имя пользователя");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new UserValidationException("Не указан email");
        }
        user = userRepository.save(user);
        return user;
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    @Override
    public List<User> getUsers(List<Long> ids, int from, int size) {
        List<User> users;
        if ((ids == null || ids.isEmpty())) {
            users = userRepository.findAll(getPageableAsc(from, size)).getContent();
        } else {
            users = userRepository.findAllByIdIn(ids, getPageableAsc(from, size)).getContent();
        }
        return users;
    }

    public static Pageable getPageableDesc(int from, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        return PageRequest.of(from > 0 ? from / size : 0, size, sort);
    }

    public static Pageable getPageableAsc(int from, int size) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        return PageRequest.of(from > 0 ? from / size : 0, size, sort);
    }
}
