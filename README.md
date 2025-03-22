# Green API Test Project

Проект для тестирования функциональности Green API с использованием REST Assured.

## Описание

Этот проект содержит автоматизированные тесты для проверки основных функций Green API:
- Отправка сообщений
- Получение истории чата
- Проверка состояния авторизации

## Структура проекта

```
src/
├── test/
│   ├── java/
│   │   ├── com/greenapi/tests/
│   │   │   ├── api/
│   │   │   │   └── GreenApi.java
│   │   │   └── config/
│   │   │       └── GreenApiConfig.java
│   │   └── GreenApiTest.java
│   └── resources/
│       └── config.properties
```

## Настройка

1. Склонируйте репозиторий:
```bash
git clone https://github.com/RePlaze/green-api-test.git
```

2. Создайте файл `src/test/resources/config.properties` со следующим содержимым:
```properties
base.url=https://api.green-api.com/waInstance%s
instance.id=YOUR_INSTANCE_ID
api.token=YOUR_API_TOKEN
chat.id=YOUR_CHAT_ID
test.message=Test message
chat.history.limit=100
```

3. Замените значения в config.properties на ваши:
- `YOUR_INSTANCE_ID` - ID вашего инстанса
- `YOUR_API_TOKEN` - токен доступа к API
- `YOUR_CHAT_ID` - ID чата для тестирования

## Запуск тестов

Для запуска тестов используйте команду:
```bash
mvn test
```

## Технологии

- Java 11
- JUnit 4
- REST Assured
- Maven

## Автор

RePlaze 