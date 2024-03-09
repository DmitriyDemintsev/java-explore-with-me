package ru.practicum.enums;

import lombok.Getter;

@Getter
public enum EventState {
    /**
     * жизненный цикл события
     */
    PENDING, // ожидает модерацию
    PUBLISHED, // опубликовано
    CANCELED; // отменено (модератором или автором)
}
