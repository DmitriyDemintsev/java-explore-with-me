package ru.practicum.service.compilation;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Compilation;

import java.util.List;

@Transactional(readOnly = true)
public interface CompilationService {

    @Transactional
    Compilation create(Compilation compilation);

    @Transactional
    Compilation update(Compilation compilation);

    @Transactional
    void delete(long compId);

    Compilation getCompilationById(Long compId);

    List<Compilation> getCompilations(Boolean pinned, Integer from, Integer size);
}
