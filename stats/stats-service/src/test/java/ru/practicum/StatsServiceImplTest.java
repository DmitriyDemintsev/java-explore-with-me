package ru.practicum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.model.Application;
import ru.practicum.model.Hits;
import ru.practicum.model.Stats;
import ru.practicum.model.UniqueHit;
import ru.practicum.repository.StatsRepository;
import ru.practicum.service.StatsServiceImpl;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatsServiceImplTest {
    @Mock
    private StatsRepository statsRepository;
    @InjectMocks
    private StatsServiceImpl statsService;

    @Test
    void getStats_whenNoUriAndTimestampIsAfterAndTimestampIsBefore_thenStatsReturn() {
        Application application = new Application(1, "ewm-main-service");

        Stats festStat = new Stats(null, application, "/events/1",
                "192.163.0.1", LocalDateTime.now().minusYears(3));
        Stats secondStat = new Stats(null, application, "/events/2",
                "192.163.0.1", LocalDateTime.now());
        Stats thirdStat = new Stats(null, application, "/events/2",
                "192.163.0.1", LocalDateTime.now());
        Stats fourthStat = new Stats(null, application, "/events/1",
                "192.163.0.1", LocalDateTime.now());
        Stats fifthStat = new Stats(null, application, "/events/2",
                "192.163.0.1", LocalDateTime.now().plusYears(5));

        List<Stats> expectedStats = List.of(festStat, secondStat, thirdStat, fourthStat, fifthStat);

        when(statsRepository.findByTimestampIsAfterAndTimestampIsBefore(
                LocalDateTime.of(2023, 05, 05, 00, 00, 00),
                LocalDateTime.of(2025, 05, 05, 00, 00, 00)))
                .thenReturn(expectedStats);

        Set<UniqueHit> uniqueHits = new HashSet<>();
        Map<Hits, Integer> hits = new HashMap<>();
        for (Stats stat : expectedStats) {
            Hits hit = new Hits(stat.getApp().getApp(), stat.getUri());
            UniqueHit uniqueHit = new UniqueHit(hit, stat.getIp());
            if (false && uniqueHits.contains(uniqueHit)) {
                break;
            } else {
                uniqueHits.add(uniqueHit);
                hits.put(hit, hits.getOrDefault(hit, 0) + 1);
            }
        }

        Map<Hits, Integer> actualStats = statsService.getStats(
                LocalDateTime.of(2023, 05, 05, 00, 00, 00),
                LocalDateTime.of(2025, 05, 05, 00, 00, 00),
                null, false);
        assertEquals(hits, actualStats);
    }

    @Test
    void getStats_whenIsUriAndTimestampIsAfterAndTimestampIsBeforeAndUriIn_thenStatsReturn() {
        Application application = new Application(1, "ewm-main-service");

        List<String> uris = List.of("/events/1", "/events/2");

        Stats festStat = new Stats(null, application, "/events/1",
                "192.163.0.1", LocalDateTime.now());
        Stats secondStat = new Stats(null, application, "/events/2",
                "192.163.0.1", LocalDateTime.now());
        Stats thirdStat = new Stats(null, application, "/events/2",
                "192.163.0.1", LocalDateTime.now());
        Stats fourthStat = new Stats(null, application, "/events/1",
                "192.163.0.1", LocalDateTime.now());
        Stats fifthStat = new Stats(null, application, "/events/2",
                "192.163.0.1", LocalDateTime.now());

        List<Stats> expectedStats = List.of(festStat, secondStat, thirdStat, fourthStat, fifthStat);

        when(statsRepository.findByTimestampIsAfterAndTimestampIsBeforeAndUriIn(
                LocalDateTime.of(2020, 05, 05, 00, 00, 00),
                LocalDateTime.of(2035, 05, 05, 00, 00, 00), uris))
                .thenReturn(expectedStats);

        Set<UniqueHit> uniqueHits = new HashSet<>();
        Map<Hits, Integer> hits = new HashMap<>();
        for (Stats stat : expectedStats) {
            Hits hit = new Hits(stat.getApp().getApp(), stat.getUri());
            UniqueHit uniqueHit = new UniqueHit(hit, stat.getIp());
            if (false && uniqueHits.contains(uniqueHit)) {
                break;
            } else {
                uniqueHits.add(uniqueHit);
                hits.put(hit, hits.getOrDefault(hit, 0) + 1);
            }
        }
        Map<Hits, Integer> actualStats = statsService.getStats(
                LocalDateTime.of(2020, 05, 05, 00, 00, 00),
                LocalDateTime.of(2035, 05, 05, 00, 00, 00),
                uris, false);
        assertEquals(hits, actualStats);
    }

    @Test
    void getStats_whenTimestampIsAfterAndTimestampIsBeforeAndUriInAndIsUnique_thenStatsReturn() {
        Application application = new Application(1, "ewm-main-service");

        List<String> uris = List.of("/events");

        Stats festStat = new Stats(null, application, "/events",
                "192.163.0.1", LocalDateTime.now());
        Stats secondStat = new Stats(null, application, "/events",
                "192.163.0.1", LocalDateTime.now());
        Stats thirdStat = new Stats(null, application, "/events",
                "192.163.0.1", LocalDateTime.now());

        List<Stats> expectedStats = List.of(festStat, secondStat, thirdStat);

        when(statsRepository.findByTimestampIsAfterAndTimestampIsBeforeAndUriIn(
                LocalDateTime.of(2020, 05, 05, 00, 00, 00),
                LocalDateTime.of(2035, 05, 05, 00, 00, 00), uris))
                .thenReturn(expectedStats);

        Set<UniqueHit> uniqueHits = new HashSet<>();
        Map<Hits, Integer> hits = new HashMap<>();
        for (Stats stat : expectedStats) {
            Hits hit = new Hits(stat.getApp().getApp(), stat.getUri());
            UniqueHit uniqueHit = new UniqueHit(hit, stat.getIp());
            if (false && uniqueHits.contains(uniqueHit)) {
                break;
            } else {
                uniqueHits.add(uniqueHit);
                hits.put(hit, hits.getOrDefault(hit, 0) + 1);
            }
        }
        Map<Hits, Integer> actualStats = statsService.getStats(
                LocalDateTime.of(2020, 05, 05, 00, 00, 00),
                LocalDateTime.of(2035, 05, 05, 00, 00, 00),
                uris, true);
        assertEquals(actualStats.size(), 1);
    }
}
