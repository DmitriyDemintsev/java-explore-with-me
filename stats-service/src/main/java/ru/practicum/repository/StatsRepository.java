package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("select s " +
            "from Stats s where s.timestamp between ?1 and ?2 " +
            "group by s.id, s.app " +
            "order by count (distinct s.ip) desc")
    List<Stats> findByTimestampIsAfterAndTimestampIsBeforeDistinctByIp(LocalDateTime start, LocalDateTime end); // unique


    @Query("select s " +
            "from Stats s " +
            "where s.timestamp between ?1 and ?2 and s.uri in (?3) " +
            "group by s.id, s.app, s.uri " +
            "order by count (distinct s.ip) desc")
    List<Stats> findByTimestampIsAfterAndTimestampIsBeforeAndUriInDistinctByIp(LocalDateTime start, LocalDateTime end,
                                                                               List<String> uris); // unique
}
