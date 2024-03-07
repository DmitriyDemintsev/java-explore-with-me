package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.exception.InvalidParametersException;
import ru.practicum.mapper.StatsMapper;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;
    private static Comparator<HitDto> comparator = Comparator.comparing(HitDto::getHits).reversed();


    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public StatsDto createStats(@RequestBody @Valid StatsDto statsDto) {
        return StatsMapper.toStatsDto(statsService.create(StatsMapper.toStats(statsDto), statsDto.getApp()));
    }

    @GetMapping("/stats")
    public List<HitDto> getStats(@RequestParam(value = "start") LocalDateTime start,
                                 @RequestParam(value = "end") LocalDateTime end,
                                 @RequestParam(value = "uris", required = false) List<String> uris,
                                 @RequestParam(value = "unique", defaultValue = "false") boolean unique) {
        if (!end.isAfter(start)) {
            throw new InvalidParametersException("end должен быть больше, чем start");
        }
        List<HitDto> hits = StatsMapper.toHitDto(statsService.getStats(start, end, uris, unique));
        ArrayList<HitDto> sortedHits = hits.stream().sorted(comparator)
                .collect(Collectors.toCollection(ArrayList::new));
        log.info("Статистика для метода getStats " + hits);
        return sortedHits;
    }
}
