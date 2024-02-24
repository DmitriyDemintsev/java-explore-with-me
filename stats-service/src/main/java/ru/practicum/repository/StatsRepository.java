package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {

    Stats save(Stats stats);

    List<Stats> findByTimestampIsAfterAndTimestampIsBefore(LocalDateTime start, LocalDateTime end); // not unique

    List<Stats> findByTimestampIsAfterAndTimestampIsBeforeAndUriIn(LocalDateTime start, LocalDateTime end,
                                                                   List<String> uris); // not unique
}
