package ru.practicum.mapper;

import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.model.Hits;
import ru.practicum.model.Stats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatsMapper {

    public static Stats toStats(StatsDto saveDto) {
        Stats stats = new Stats(
                saveDto.getId(),
                null,
                saveDto.getUri(),
                saveDto.getIp(),
                saveDto.getTimestamp()
        );
        return stats;
    }

    public static StatsDto toStatsDto(Stats stats) {
        StatsDto statsDto = new StatsDto(
                stats.getId(),
                stats.getApp().getApp(),
                stats.getUri(),
                stats.getIp(),
                stats.getTimestamp()
        );
        return statsDto;
    }

    public static List<HitDto> toHitDto(Map<Hits, Integer> hits) {
        List<HitDto> result = new ArrayList<>();
        for (Hits hit : hits.keySet()) {
            int count = hits.get(hit);
            result.add(new HitDto(hit.getAppName(), hit.getUri(), count));
        }
        return result;
    }
}
