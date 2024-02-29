package ru.practicum.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Hits;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = true)
public interface StatsService {
    @Transactional
    Stats save(Stats stats, String appName);

    Map<Hits, Integer> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
