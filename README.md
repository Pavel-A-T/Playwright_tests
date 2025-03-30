# Автотесты на playwright java
## Инструкция по запуску:
1. Перейдите в корень проекта.
2. Для полного прохождения тестов необходимо иметь<br>
   учетную запись на хосте https://otus.ru<br>
   #### Пример команды для запуска тестов: <br>
   mvn clean test -Demail="your real email" -Dpass="your password" (вводится без **"** кавычек)<br>
   #### Для выполнения тестов без авторизации:
   mvn clean test -Dmaven.test.failure.ignore=true <br>
   У вас "упадет" только один тест при таком запуске. <br>
  #### Для просмотра трассировки: <br>
    mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="show-trace trace.zip"<br>

#### 3. Enjoy!<br>
   **PS.** У вас должен быть локально установлен Maven и JDK 17.<br>