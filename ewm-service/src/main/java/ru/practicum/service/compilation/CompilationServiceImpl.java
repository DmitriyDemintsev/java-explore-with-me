package ru.practicum.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.CompilationNotFoundException;
import ru.practicum.model.Compilation;
import ru.practicum.model.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.service.event.EventService;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventService eventService;

    @Override
    public Compilation create(Compilation compilation) {
        return null;
    }

    @Override
    public Compilation update(Compilation compilation) {
        return null;
    }

    @Override
    public void delete(long compId) {
        if (compilationRepository.findById(compId).isPresent()) {
            compilationRepository.deleteById(compId);
        } else throw new CompilationNotFoundException("Нет такой подборки событий");
    }

    @Override
    public Compilation getCompilationById(Long compId) {
        return null;
    }

    @Override
    public List<Compilation> getCompilations(Boolean pinned, Integer from, Integer size) {
        return null;
    }
}
