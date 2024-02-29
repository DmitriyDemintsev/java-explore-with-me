package ru.practicum.enums;

import lombok.Getter;


@Getter
public enum RequestStatus {
    /**
     * статус запроса на участие в событии текущего пользователя
     */
    CONFIRMED, // подтверждено
    PENDING, // на рассмотрении
    REJECTED, // отклонено (модератором)
    CANCELED // отменено (автором)
}
