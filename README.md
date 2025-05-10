# Учебный проект для Яндекс.Практикум "Intershop"

## Сборка и запуск

1. Указать настройки подключения к пустой БД Postgres в файле application.yml
2. В корневой папке проекта создать docker-образ приложения командой
    ```
    docker build -t intershop .
    ```
3. Запустить контейнер командой 
    ```
   docker run -d -p 8040:8040 --name intershop intershop
   ```
