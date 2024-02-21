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
    void findByTimestampIsAfterAndTimestampIsBefore() {

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
                LocalDateTime.of(2020, 05, 05, 00, 00, 00),
                LocalDateTime.of(2035, 05, 05, 00, 00, 00));

        assertEquals(statsList, actualStats);
    }

    @Test
    void findByTimestampIsAfterAndTimestampIsBeforeAndUriIn() {

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
                LocalDateTime.of(2020, 05, 05, 00, 00, 00),
                LocalDateTime.of(2035, 05, 05, 00, 00, 00), uris);

        assertEquals(statsList, actualStats);
    }

    @Test
    void findByTimestampIsAfterAndTimestampIsBeforeDistinctByIp() {

        Application application = new Application(1, "ewm-main-service");
        application = applicationRepository.save(application);

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
        List<Stats> actualStats = statsRepository.findByTimestampIsAfterAndTimestampIsBeforeDistinctByIp(
                LocalDateTime.of(2020, 05, 05, 00, 00, 00),
                LocalDateTime.of(2035, 05, 05, 00, 00, 00));

        assertEquals(statsList, actualStats);
    }

    @Test
    void findByTimestampIsAfterAndTimestampIsBeforeAndUriInDistinctByIp() {
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
        List<Stats> actualStats = statsRepository.findByTimestampIsAfterAndTimestampIsBeforeAndUriInDistinctByIp(
                LocalDateTime.of(2020, 05, 05, 00, 00, 00),
                LocalDateTime.of(2035, 05, 05, 00, 00, 00), uris);

        assertEquals(statsList, actualStats);
    }
}
