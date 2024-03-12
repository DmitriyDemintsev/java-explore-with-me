package ru.practicum.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.springframework.context.annotation.Lazy;
import ru.practicum.enums.EventState;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id")
    private Category category;
    @Column(name = "created_on")
    private LocalDateTime createdOn; // когда создано событие
    private String description;
    @Column(name = "event_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate; // дата проведения события
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "initiator_id")
    private User initiator; // кто создает событие
    @Embedded
    private Location location; // где проходит (координаты места)
    @Column(nullable = false)
    private Boolean paid; // платно или нет
    @Column(name = "participant_limit", nullable = false)
    private Integer participantLimit; // лимит на количество участников
    @Column(name = "published_on")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn; // когда опубликовано
    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;
    @Column(name = "state", nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonAlias({"state"})
    private EventState eventState;
    @Column(nullable = false)
    private String title;
    @Lazy
    @Formula("(select count(*) from Requests r where r.event_id = event_id and r.status = 'CONFIRMED')")
    private Integer confirmedRequests; // подтвержденные заявки на участие
    @Transient
    private Integer views; // количество просмотров
    @Transient
    private List<Comment> comments; // для фичи
}

