package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Application;
import ru.practicum.model.Hits;
import ru.practicum.model.Stats;
import ru.practicum.model.UniqueHit;
import ru.practicum.repository.ApplicationRepository;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional(readOnly = true)
@Slf4j
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    @Transactional
    public Stats save(Stats stats, String appName) {
        Application app = applicationRepository.findByApp(appName)
                .orElseGet(() -> applicationRepository.save(new Application(null, appName)));
        stats.setApp(app);
        log.info("Сохраняем статистику по запросам к приложению " + stats);
        return statsRepository.save(stats);
    }

    @Override
    public Map<Hits, Integer> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        List<Stats> stats;
        if (unique) {
            if (uris == null || uris.isEmpty()) {
                stats = statsRepository.findByTimestampIsAfterAndTimestampIsBeforeDistinctByIp(start, end);
                log.info("Статистика по уникальным запросам к приложению без uris " + stats);
            } else {
                stats = statsRepository.findByTimestampIsAfterAndTimestampIsBeforeAndUriInDistinctByIp(start, end, uris);
                log.info("Статистика по уникальным запросам к приложению  с uris " + stats);
            }
        } else {
            if (uris == null || uris.isEmpty()) {
                stats = statsRepository.findByTimestampIsAfterAndTimestampIsBefore(start, end);
                log.info("Статистика по неуникальным запросам к приложению  без uris " + stats);
            } else {
                stats = statsRepository.findByTimestampIsAfterAndTimestampIsBeforeAndUriIn(start, end, uris);
                log.info("Статистика по уникальным запросам к приложению  с uris " + stats);
            }
        }
        Set<UniqueHit> uniqueHits = new HashSet<>();
        Map<Hits, Integer> hits = new HashMap<>();
        for (Stats stat : stats) {
            Hits hit = new Hits(stat.getApp().getApp(), stat.getUri());
            UniqueHit uniqueHit = new UniqueHit(hit, stat.getIp());
            if (unique && uniqueHits.contains(uniqueHit)) {
                break;
            } else {
                uniqueHits.add(uniqueHit);
                hits.put(hit, hits.getOrDefault(hit, 0) + 1);
            }
        }
        return hits;
    }
}

