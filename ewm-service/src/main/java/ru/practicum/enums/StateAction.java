package ru.practicum.enums;

public enum StateAction {
    /**
     * новое состояние - результат модерации или действия user'а
     */

    PUBLISH_EVENT, // отправлено на публикацию
    REJECT_EVENT, // публикация отклонена
    SEND_TO_REVIEW, // отправлено на модерацию
    CANCEL_REVIEW // отозвать с модерации
}
