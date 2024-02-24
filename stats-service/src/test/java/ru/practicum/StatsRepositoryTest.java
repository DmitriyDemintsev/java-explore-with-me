package ru.practicum;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.model.Application;
import ru.practicum.model.Stats;
import ru.practicum.repository.ApplicationRepository;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class StatsRepositoryTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private StatsRepository statsRepository;
    @Autowired
    private ApplicationRepository applicationRepository;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    void findByTimestampIsAfterAndTimestampIsBefore_GetAll() {

        Application application = new Application(1, "ewm-main-service");
        application = applicationRepository.save(application);

        Stats firstStat = new Stats(null, application, "/events/1",
                "192.163.0.1", LocalDateTime.now());
        firstStat = statsRepository.save(firstStat);
        Stats secondStat = new Stats(null, application, "/events/1",
                "192.163.0.2", LocalDateTime.now());
        secondStat = statsRepository.save(secondStat);
        Stats thirdStat = new Stats(null, application, "/events/1",
                "192.163.0.3", LocalDateTime.now());
        thirdStat = statsRepository.save(thirdStat);

        List<Stats> statsList = List.of(firstStat, secondStat, thirdStat);
        List<Stats> actualStats = statsRepository.findByTimestampIsAfterAndTimestampIsBefore(
                LocalDateTime.of(2023, 05, 05, 00, 00, 00),
                LocalDateTime.of(2025, 05, 05, 00, 00, 00));

        assertEquals(statsList, actualStats);
    }

    @Test
    void findByTimestampIsAfterAndTimestampIsBeforeAndUriIn_GetAll() {

        Application application = new Application(1, "ewm-main-service");
        application = applicationRepository.save(application);

        List<String> uris = List.of("/events/1", "/events/2", "/events/3");

        Stats firstStat = new Stats(null, application, "/events/1",
                "192.163.0.1", LocalDateTime.now());
        firstStat = statsRepository.save(firstStat);
        Stats secondStat = new Stats(null, application, "/events/2",
                "192.163.0.2", LocalDateTime.now());
        secondStat = statsRepository.save(secondStat);
        Stats thirdStat = new Stats(null, application, "/events/3",
                "192.163.0.2", LocalDateTime.now());
        thirdStat = statsRepository.save(thirdStat);

        List<Stats> statsList = List.of(firstStat, secondStat, thirdStat);
        List<Stats> actualStats = statsRepository.findByTimestampIsAfterAndTimestampIsBeforeAndUriIn(
                LocalDateTime.of(2023, 05, 05, 00, 00, 00),
                LocalDateTime.of(2024, 05, 05, 00, 00, 00), uris);

        assertEquals(statsList, actualStats);
    }

    @Test
    void findByTimestampIsAfterAndTimestampIsBefore_GetByTime() {

        Application application = new Application(1, "ewm-main-service");
        application = applicationRepository.save(application);

        Stats firstStat = new Stats(null, application, "/events/1",
                "192.163.0.1", LocalDateTime.now().minusYears(3));
        statsRepository.save(firstStat);
        Stats secondStat = new Stats(null, application, "/events/2",
                "192.163.0.2", LocalDateTime.now());
        statsRepository.save(secondStat);
        Stats thirdStat = new Stats(null, application, "/events/3",
                "192.163.0.2", LocalDateTime.now());
        statsRepository.save(thirdStat);
        Stats fourthStat = new Stats(null, application, "/events/3",
                "192.163.0.2", LocalDateTime.now().plusYears(5));
        statsRepository.save(fourthStat);

        List<Stats> expectedStats = List.of(secondStat, thirdStat);

        List<Stats> actualStats = statsRepository.findByTimestampIsAfterAndTimestampIsBefore(
                LocalDateTime.of(2023, 05, 05, 00, 00, 00),
                LocalDateTime.of(2024, 05, 05, 00, 00, 00));

        assertEquals(actualStats, expectedStats);
    }

    @Test
    void findByTimestampIsAfterAndTimestampIsBeforeAndUriIn_GetByUri() {

        Application application = new Application(1, "ewm-main-service");
        application = applicationRepository.save(application);

        List<String> uris = List.of("/events/1");

        Stats firstStat = new Stats(null, application, "/events/1",
                "192.163.0.1", LocalDateTime.now());
        statsRepository.save(firstStat);
        Stats secondStat = new Stats(null, application, "/events/2",
                "192.163.0.2", LocalDateTime.now());
        statsRepository.save(secondStat);
        Stats thirdStat = new Stats(null, application, "/events/3",
                "192.163.0.2", LocalDateTime.now());
        statsRepository.save(thirdStat);
        Stats fourthStat = new Stats(null, application, "/events/3",
                "192.163.0.2", LocalDateTime.now());
        statsRepository.save(fourthStat);

        List<Stats> expectedStats = List.of(firstStat);

        List<Stats> actualStats = statsRepository.findByTimestampIsAfterAndTimestampIsBeforeAndUriIn(
                LocalDateTime.of(2023, 05, 05, 00, 00, 00),
                LocalDateTime.of(2024, 05, 05, 00, 00, 00), uris);

        assertEquals(actualStats, expectedStats);
    }
}
