# City Infrastructure Platform: Modular Monolith

**Учебный проект** «Умный город» — реализация **модульного монолита** (лабораторная работа 2).

Этот репозиторий содержит объединённое приложение `smart-city-monolith`, в котором два микросервиса (`utility-service` и `notification-service`) перенесены в один процесс как модули. Остальные сервисы (`auth`, `transport`, `environment`, `billing`, `frontend`) остаются микросервисами и запускаются вместе с монолитом.

> **Исходный микросервисный проект:** [city-infrastructure](https://github.com/Darya-Rusanova/city-infrastructure)
>
> Общая информация о проекте, стеке, команде и сквозном сценарии — в README микросервисного репозитория. Здесь описано только то, что касается модульного монолита.

## Команда

| Участник | Зона ответственности |
|---|---|
| Русанова Дарья | Модуль utility, Docker |
| Бойченко Ксения | Модуль notification |
| Яманова Ульяна | Замеры, эксперимент с отказом |
| Агеева Александра | SQL-скрипты |

## Что изменилось по сравнению с микросервисами

| Было (микросервисы) | Стало (модульный монолит) |
|---|---|
| `utility-service` (отдельный контейнер) | Модуль `modules/utility` внутри монолита |
| `notification-service` (отдельный контейнер) | Модуль `modules/notification` внутри монолита |
| HTTP-вызов `POST /notifications` между ними | Внутреннее событие Spring (`ApplicationEventPublisher`) |
| Две БД (`utility_db`, `notification_db`) | Одна БД `monolith_db` с префиксами таблиц |
| 13 контейнеров | 11 контейнеров |
| Порт `utility-service`: 8083 | Порт монолита: 8090 |

**Внешний REST API не изменился.** Фронтенд и Billing работают с монолитом по тем же адресам (`/issues`, `/notifications/my`, `POST /notifications`).

### Способ взаимодействия: вариант Б (события)

Модуль utility публикует событие при создании заявки и смене статуса. Модуль notification ловит его и сохраняет уведомление.

Почему вариант Б:
- utility не знает о notification — слабая связность.
- При выделении notification обратно в микросервис достаточно заменить ApplicationEventPublisher на брокер сообщений.
- Ошибка уведомления не ломает основную операцию: AFTER_COMMIT + try/catch.

### Границы модулей

- utility не импортирует ничего из modules.notification.
- notification импортирует только modules.utility.event (DTO событий).

## Единая база данных

Одна БД monolith_db. Таблицы с префиксами модулей, без внешних ключей между модулями:

| Модуль | Таблица |
|---|---|
| utility | utility_issue |
| notification | notification_message |

SQL-скрипты (sql/01-utility.sql, sql/02-notification.sql) создают структуру и тестовые данные. Скрипты выполняются только при первом запуске (пустой том БД). 

## Быстрый старт

### Требования

- **Docker Desktop** (WSL 2)
- **Git**
- **Java 17** (для локальной разработки)

## Быстрый старт

### Требования

- **Docker Desktop** (WSL 2)
- **Git**
- **Java 17** (для локальной разработки)

### Установка

**1. Клонировать репозиторий:**
 
```bash
git clone https://github.com/Darya-Rusanova/city-infrastructure.git
cd city-infrastructure
```
 
**2. Создать файл `.env`:**
 
```bash
cp .env.example .env
```
 
Задать в `.env` два секрета:
 
```
JWT_SECRET=ваш-секрет-минимум-32-символа
WEBHOOK_SECRET=ваш-секрет-для-webhook-платежей
```

**3. Запустить всё:**

```bash
docker-compose up --build -d
```

**4. Открыть в браузере:**

```
http://localhost:8080
```

**Первый запуск** — 5–10 минут.

### Сквозной сценарий

1. Открыть http://localhost:8080.
2. Войти как user1 → создать заявку.
3. Проверить /notifications — появилось «Заявка создана».
4. Войти как admin → сменить статус заявки.
5. Войти как user1 — появилось «Статус заявки был изменен».
6. Войти как billing → оплатить счёт (кнопка «Подтвердить оплату (демо)»).
7. Проверить /notifications — появилось уведомление от Billing (через POST /notifications в монолит).

## Тестовые учётные данные

Создаются автоматически в auth-service при первом запуске.

| Роль | Логин | Пароль |
|---|---|---|
| ADMIN | admin | admin123 |
| USER | user1 | user123 |
| USER | user2 | user234 |

ADMIN — видит все заявки, меняет статусы, управляет пользователями и датчиками.
USER — видит только свои заявки, брони, счета.

## Основные эндпоинты без изменений

| Метод | URL | Роли | Сервис |
|---|---|---|---|
| POST | /auth/register | все | auth |
| POST | /auth/login | все | auth |
| GET | /users/me | USER, ADMIN | auth |
| GET | /vehicles | USER, ADMIN | transport |
| GET | /parking | USER, ADMIN | transport |
| POST | /parking/{id}/reserve | USER, ADMIN | transport |
| POST | /issues | USER, ADMIN | monolith (utility) |
| GET | /issues | USER, ADMIN | monolith (utility) |
| PUT | /issues/{id} | ADMIN | monolith (utility) |
| GET | /sensors | USER, ADMIN | environment |
| GET | /accounts/{userId}/invoices | USER, ADMIN | billing |
| POST | /payments | USER, ADMIN | billing |
| POST | /payments/webhook/success | все (с X-Webhook-Secret) | billing |
| POST | /notifications | внутренний | monolith (notification) |
| GET | /notifications/my | USER, ADMIN | monolith (notification) |
