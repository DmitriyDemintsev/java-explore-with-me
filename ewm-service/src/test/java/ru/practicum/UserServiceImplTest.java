package ru.practicum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.practicum.exception.UserValidationException;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.user.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void create_whenUserNameValid_thenSavedUser() {
        User savedUser = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        when(userRepository.save(savedUser)).thenReturn(savedUser);

        User actualUser = userService.create(savedUser);

        assertEquals(savedUser, actualUser);
        verify(userRepository).save(savedUser);
    }

    @Test
    void create_whenUserNameNotValid_thenUserValidationException() {
        User savedUser = new User(0L, "", "ivan@ivanov.ru");

        assertThrows(UserValidationException.class, () -> userService.create(savedUser));
        verify(userRepository, never()).save(savedUser);
    }

    @Test
    void create_whenUserEmailNotValid_thenUserValidationException() {
        User savedUser = new User(0L, "Иван Иванов", "");

        assertThrows(UserValidationException.class, () -> userService.create(savedUser));
        verify(userRepository, never()).save(savedUser);
    }

    @Test
    void deleteUser() {
        User user = new User(0L, "Иван Иванов", "ivai@ivanov.ru");

        userRepository.deleteById(user.getId());
        assertNull(userRepository.getById(user.getId()));
    }

    @Test
    void getUsers_whenIsIds_thenListUsersReturn() {
        User fistUser = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        User fourthUser = new User(3L, "Иван Иванов", "ivai@ivanov.ru");
        User fifthUser = new User(4L, "Петр Петров", "petr@petrov.ru");
        User sixthUser = new User(5L, "Степан Степанов", "stepan@stepanov.ru");
        User ninthUser = new User(8L, "Степан Степанов", "stepan@stepanov.ru");

        List<Long> ids = List.of(0L, 3L, 4L, 5L, 8L);

        List<User> expectedUsers = List.of(fistUser, fourthUser, fifthUser, sixthUser, ninthUser);
        when((userRepository.findAllByIdIn(anyList(), any()))).thenReturn(new PageImpl<>(expectedUsers));

        List<User> actualUsers = userService.getUsers(ids, 0, 10);
        assertEquals(expectedUsers, actualUsers);
    }

    @Test
    void getUsers_whenNotIds_thenListUsersReturn() {
        User fistUser = new User(0L, "Иван Иванов", "ivai@ivanov.ru");
        User secondUser = new User(1L, "Петр Петров", "petr@petrov.ru");
        User thirdUser = new User(2L, "Степан Степанов", "stepan@stepanov.ru");
        User fourthUser = new User(3L, "Сергей Сергеев", "sergey@sergeev.ru");
        User fifthUser = new User(4L, "Николай Николаев", "nikolay@nikolaev.ru");

        List<Long> ids = new ArrayList<>();

        List<User> expectedUsers = List.of(fistUser, secondUser, thirdUser, fourthUser, fifthUser);

        when((userRepository.findAll((Pageable) any()))).thenReturn(new PageImpl<>(expectedUsers));

        List<User> actualUsers = userService.getUsers(ids, 0, 10);
        assertEquals(expectedUsers, actualUsers);
    }
}
