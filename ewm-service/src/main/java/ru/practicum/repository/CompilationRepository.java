package ru.practicum.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.Compilation;

import java.util.List;


@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Transactional
    Compilation save(Compilation compilation);

    Compilation getCompilationById(Long id);

    List<Compilation> findAllByPinnedIs(boolean pinned, PageRequest page);
}
