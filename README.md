# Афиша мероприятий


Микросервисная архитектура:
- основной сервис содержит всё необходимое для работы продукта;
- сервис статистики хранит количество просмотров и позволяет делать различные выборки для анализа работы приложения.

API основного сервиса состоит из трех частей:
- публичная доступна без регистрации любому пользователю сети;
- закрытая доступна только авторизованным пользователям;
- административная доступна для администраторов сервиса.

Реализован функционал для комментариев, подачи заявок для участия в событиях

Стеки: Spring Framework, PostgreSQL, Docker, REST API, JSON API, Intellij IDEA, Mockito, Spring Data, Spring Boot
