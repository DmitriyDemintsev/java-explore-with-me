package ru.practicum.enums;

import lombok.Getter;

@Getter
public enum EventState {
    /**
     * жизненный цикл события
     */
    PENDING, // ожидает модерации
    PUBLISHED, // опубликовано
    CANCELED; // отменено (модератором или автором)
}
