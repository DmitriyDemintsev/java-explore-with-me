package ru.practicum.enums;

import lombok.Getter;

@Getter
public enum RequestStatus {
    /**
     * статус заявок на участие в событии текущего пользователя
     */
    CONFIRMED, // подтверждено
    PENDING, // на рассмотрении
    REJECTED, // отклонено (модератором)
    CANCELED // отменено (автором)
}
