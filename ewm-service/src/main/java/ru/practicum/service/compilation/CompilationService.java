package ru.practicum.service.compilation;

import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Compilation;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface CompilationService {

    @Transactional
    Compilation create(Compilation compilation, Set<Long> eventIds);

    @Transactional
    Compilation update(Compilation compilation, Set<Long> eventIds, Long compId);

    @Transactional
    void delete(long compId);

    Compilation getCompilationById(Long compId);

    List<Compilation> getCompilations(boolean pinned, PageRequest page);
}
