package ru.practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatsClient;
import ru.practicum.dto.HitDto;
import ru.practicum.exception.CategoryValidationException;
import ru.practicum.exception.CompilationNotFoundException;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.event.EventService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EventService eventService;
    private final StatsClient statsClient;

    @Override
    public Compilation create(Compilation compilation, Set<Long> eventIds) {
        List<Event> events = eventIds != null
                ? eventRepository.findAllByIdIn(eventIds)
                : List.of();
        if (compilation.getTitle() == null || compilation.getTitle().isBlank()) {
            throw new CategoryValidationException("Отсутствует Title");
        }
        compilation.setEvents(events);
        return compilationRepository.save(compilation);
    }

    @Override
    public Compilation update(Compilation compilation, Set<Long> eventIds, Long compId) {
        Compilation old = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Подборка событий не найдена"));
        List<Event> events = eventIds != null
                ? eventRepository.findAllByIdIn(eventIds)
                : List.of();
        if (compilation.getPinned() == null) {
            compilation.setPinned(old.getPinned());
        }
        if (compilation.getTitle() == null) {
            compilation.setTitle(old.getTitle());
        }
        compilation.setId(compId);
        compilation.setEvents(events);
        return compilationRepository.save(compilation);
    }

    @Override
    public void delete(long compId) {
        if (compilationRepository.findById(compId).isPresent()) {
            compilationRepository.deleteById(compId);
        } else {
            throw new CompilationNotFoundException("Нет такой подборки событий");
        }
    }

    @Override
    public Compilation getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new CompilationNotFoundException("Подборка событий не найдена"));
        List<Event> events = compilation.getEvents();
        for (Event event : events) {
            eventService.enrichEvent(event);
        }
        return compilation;
    }

    @Override
    public List<Compilation> getCompilations(boolean pinned, PageRequest page) {
        List<Compilation> compilations = compilationRepository.findAllByPinnedIs(pinned, page);
        for (Compilation compilation : compilations) {
            List<Event> events = compilation.getEvents();
            for (Event event : events) {
                List<String> uris = List.of("/event/" + event.getId());
                List<HitDto> hits = statsClient.getStats(LocalDateTime.now().minusYears(25),
                        LocalDateTime.now().plusYears(80),
                        uris, false);
                Integer views = hits.stream().mapToInt(HitDto::getHits).sum();
                event.setViews(views);
            }
        }
        return compilations;
    }
}
