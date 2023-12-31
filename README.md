# Курсовой проект "Сетевой чат"

## Описание проекта

Вам нужно разработать два приложения для обмена текстовыми сообщениями по сети с помощью консоли (терминала) между двумя и более пользователями.

**Первое приложение - сервер чата**, должно ожидать подключения пользователей.

**Второе приложение - клиент чата**, подключается к серверу чата и осуществляет доставку и получение новых сообщений.

Все сообщения должны записываться в file.log как на сервере, так и на клиентах. File.log должен дополняться при каждом запуске, а также при отправленном или полученном сообщении. Выход из чата должен быть осуществлен по команде exit.

## Требования к серверу

- Установка порта для подключения клиентов через файл настроек (например, settings.txt);
- Возможность подключиться к серверу в любой момент и присоединиться к чату;
- Отправка новых сообщений клиентам;
- Запись всех отправленных через сервер сообщений с указанием имени пользователя и времени отправки.

## Требования к клиенту

- Выбор имени для участия в чате;
- Прочитать настройки приложения из файла настроек - например, номер порта сервера;
- Подключение к указанному в настройках серверу;
- Для выхода из чата нужно набрать команду выхода - “/exit”;
- Каждое сообщение участников должно записываться в текстовый файл - файл логирования. При каждом запуске приложения файл должен дополняться.

## Состав проекта
В проекте использован сборщик пакетов maven. Проект состоит из:

1. Package server - классы серверного приложения;
2. ClientOne, ClientTwo - классы клиентского приложения.


## Сервер:

1. Запуск сервера в классе Server, делаем поток для принятия новых пользователей в serverList.add(new ServerSomthing(socket));
2. ServerSomthing - класс для обработки сообщений;
3. В классе ServerSomthing происходит получение и отправка сообщений 
4. Сообщения записываются в file.log

## Клиент:
1. Считываем настройки settings из src//main//resources//settings.txt - settings.getHost(), settings.getPort() ;
2. Выбираем имя из списка listByNickname, метод choosingNickname();
3. Создаем два потока: для чтения сообщений от сервера Thread readMsg() и для отправки сообщений на сервер Thread writeMsg().