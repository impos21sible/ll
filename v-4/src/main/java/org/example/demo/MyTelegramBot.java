package org.example.demo;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static javafx.application.Application.launch;

public class MyTelegramBot extends TelegramLongPollingBot {



    private static final String USER_DATA_FILE = "src/main/resources/users.csv"; // Путь к файлу с данными пользователей
    private Set<Long> authorizedUsers = new HashSet<>(); // Множество для хранения авторизованных пользователей
    private double[] coefficients = new double[3]; // Массив для хранения коэффициентов
    private int coefficientIndex = 0; // Индекс для отслеживания, какой коэффициент вводится

    @Override
    public String getBotUsername() {
        return "LuckyGet_AssistantBot";  // Укажите имя вашего бота
    }

    @Override
    public String getBotToken() {
        return "7609275319:AAENhldLJvTSqM9SiY6NGzxgRRjDGwdZFPE";  // Укажите токен вашего бота
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    sendWelcomeMessage(chatId);
                    break;
                case "🎮 Регистрация":
                    sendRegistrationInstructions(chatId);
                    break;
                case "🖥 Получить сигнал":
                    getSignal(chatId); // Обработка запроса сигнала
                case "/take_signals":
                    getSignal(chatId); // Обработка запроса сигнала
                    break;
                case "/login":
                    // Запрос ввода логина и пароля
                    sendMessage(chatId, "🔑 Пожалуйста, введите ваше имя пользователя и пароль через пробел в формате:\n" +
                            "/login <имя_пользователя> <пароль>");

                    break;
                case "/logout":
                    handleLogout(chatId); // Обработка выхода
                    break;
                default:
                    if (messageText.startsWith("/login ")) {
                        handleLogin(messageText, chatId); // Обработка авторизации
                    } else {
                        handleCoefficientInput(chatId, messageText); // Обработка ввода коэффициентов
                    }
                    break;
            }
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            switch (callbackData) {
                case "instruction":
                    sendInstruction(chatId);
                    break;
                case "registration":
                    sendRegistrationInstructions(chatId);
                    break;
                case "back":
                    sendWelcomeMessage(chatId);
                    break;
                case "get_signal":
                    getSignal(chatId); // Обработка запроса сигнала через кнопку
                    break;
                default:
                    sendMessage(chatId, "Неизвестный запрос.");
            }
        }
    }

    private void handleLogout(long chatId) {
        if (authorizedUsers.remove(chatId)) { // Удаляем пользователя из списка авторизованных
            sendMessage(chatId, "Вы успешно вышли из системы. Вам необходимо авторизоваться снова.");
        } else {
            sendMessage(chatId, "Вы не авторизованы.");
        }
    }

    private void sendWelcomeMessage(long chatId) {
        File photo = new File("src/main/resources/trade.jpg");
        SendPhoto photoMessage = new SendPhoto();
        photoMessage.setChatId(chatId);
        InputFile inputFile = new InputFile(photo);
        photoMessage.setPhoto(inputFile);

        String caption = "\uD83D\uDE0B Добро пожаловать в \uD83D\uDD38 Lucky Jet GPT \uD83D\uDD38!\n" +
                "\n" +
                "\uD83D\uDE80 Присоединяйтесь к захватывающему миру Lucky Jet — идеальному месту для быстрой и увлекательной прибыли!\n" +
                "✨ Ваша задача — ставить и вовремя выводить выигрыши!\n" +
                "🧠 Наш бот, использующий мощь нейросети OpenAI, поможет вам выбирать лучшие моменты для кэшаута с точностью до 97%.";


        photoMessage.setCaption(caption);
        photoMessage.setReplyMarkup(Buttons.getMainMenuKeyboard());

        try {
            execute(photoMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendRegistrationInstructions(long chatId) {
        File registrationPhoto = new File("src/main/resources/registration.jpg");
        SendPhoto photoMessage = new SendPhoto();
        photoMessage.setChatId(chatId);
        InputFile inputFile = new InputFile(registrationPhoto);
        photoMessage.setPhoto(inputFile);

        String caption = "🌐 Шаг 1 - Зарегистрируйся\n\n" +
                "✦ 🔑 Для синхронизации с нашим ботом необходимо зарегистрировать НОВЫЙ аккаунт!\n" +
                "✦ Если Вы переходите по ссылке и попадаете на старый аккаунт, 🚫 необходимо выйти с него.\n" +
                "● ✉️ После завершения регистрации, нажмите на Помощь и напишите нашему оператору!\n" +
                "🌟 Шаг 2 - Нажмите Назад и получите сигнал.\n" +
                "🔍 Шаг 3 - Вставьте данные о прошлых 3 - ех играх.";



        photoMessage.setCaption(caption);
        photoMessage.setReplyMarkup(Buttons.getRegistrationMenu());

        try {
            execute(photoMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendInstruction(long chatId) {
        File instructionPhoto = new File("src/main/resources/instruction.jpg");
        SendPhoto photoMessage = new SendPhoto();
        photoMessage.setChatId(chatId);
        InputFile inputFile = new InputFile(instructionPhoto);
        photoMessage.setPhoto(inputFile);

        String caption = "Бот основан на нейросети от OpenAI.\n\n" +
                "Чтобы достичь максимального выигрыша, придерживайтесь данной инструкции:\n\n" +
                "1️⃣ Регистрация в 1WIN: Зарегистрируйтесь.\n\n" +
                "2️⃣ Пополнение баланса: Пополните свой игровой счет.\n\n" +
                "3️⃣ Выбор игры: Перейдите в раздел 1win games и выберите игру 💣 ‘LuckyJet’.\n\n" +
                "4️⃣ Настройка игры: Установите сумму.\n\n" +
                "5️⃣ Получение сигнала: Запросите сигнал у бота.\n\n" +
                "6️⃣ Неудачные сигналы: Если сигнал оказался неудачным, удвойте свою ставку.";

        photoMessage.setCaption(caption);
        photoMessage.setReplyMarkup(Buttons.getInstructionKeyboard());

        try {
            execute(photoMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void getSignal(long chatId) {
        if (!authorizedUsers.contains(chatId)) { // Проверка, авторизован ли пользователь
            sendMessage(chatId, "🚫 *Доступ запрещен!* 🚫\n\n" +
                    "✅ Сначала авторизуйтесь с помощью команды \uD83C\uDF1F /login.\n\n" +
                    "Если у вас возникли трудности с авторизацией, пожалуйста, напишите нашему оператору в разделе 'Регистрация' ✉️.\n\n" +
                    "Или напишите напрямую: [написать оператору](https://t.me/kunaesv)");


        }




        if (coefficientIndex == 0) {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("💡 **Коэффициент 1**:\n" +
                    "Пожалуйста, введите значение в формате x.xx:");

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("✅ **Коэффициенты уже введены!**\n\n" +
                    "Пожалуйста, подождите получения сигнала...");

            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkCoefficients(long chatId) {
        boolean allLessThanTwo = true; // Флаг для проверки всех коэффициентов
        boolean oneZero = true; // Флаг для проверки всех коэффициентов


        for (double coefficient : coefficients) {
            if (coefficient >= 2) {
                allLessThanTwo = false; // Если хоть один коэффициент >= 2, устанавливаем флаг в false
                break;
            }
        }



        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));

        if (allLessThanTwo) {
            message.setText("🚨 **Сигнал !!!** 🚨\n\n" +
                    "Ставьте **Авто-Стоп** на значение **2x** - до проигрыша.\n" +
                    "Если вы проиграли, **удваивайте** - **ПОКА НЕ ВЫИГРАЕТЕ!**\n\n" +
                    "⏳ Ждем 3 игры...\n" +
                    "Введите 3 последних коэффициента через **Enter**.");

        }
        else
        {
            message.setText("⏳ Ожидаем сигнал...\n\n" +
                    "🔍 Ждем 3 игры.\n" +
                    "📊 Введите 3 последних коэффициента через Enter:");

        }

        try {
            execute(message); // Отправка сообщения
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void handleCoefficientInput(long chatId, String userInput) {
        // Проверка, если мы ожидаем ввод коэффициентов
        if (coefficientIndex < 3) {
            try {
                // Преобразование ввода в число
                coefficients[coefficientIndex] = Double.parseDouble(userInput);
                coefficientIndex++;

                if (coefficientIndex < 3) {
                    // Если еще нужно вводить коэффициенты, отправьте следующий запрос
                    SendMessage message = new SendMessage();
                    message.setChatId(String.valueOf(chatId));
                    message.setText("Коэффициент " + coefficientIndex + " принят. Введите следующий коэффициент: x.xx");
                    execute(message); // Отправка сообщения
                } else {
                    // Если все коэффициенты введены, выполните нужные действия
                    SendMessage finalMessage = new SendMessage();
                    finalMessage.setChatId(String.valueOf(chatId));
                    finalMessage.setText("Коэффициенты успешно получены: " + Arrays.toString(coefficients));
                    execute(finalMessage); // Отправка финального сообщения

                    // Логика проверки коэффициентов
                    checkCoefficients(chatId);

                    // Сброс индекса для следующего ввода
                    resetCoefficientInput();
                }
            } catch (NumberFormatException | TelegramApiException e) {
                // Обработка ошибки, если ввод некорректен
                SendMessage errorMessage = new SendMessage();
                errorMessage.setChatId(String.valueOf(chatId));
                errorMessage.setText("Пожалуйста, введите корректный коэффициент в формате x.xx.");
                try {
                    execute(errorMessage); // Отправка сообщения об ошибке
                } catch (TelegramApiException ex) {
                    ex.printStackTrace(); // Обработка исключений
                }
            }
        }

    }
    private void resetCoefficientInput() {
        coefficientIndex = 0; // Сброс индекса для следующего ввода
        Arrays.fill(coefficients, 0); // Сброс массива коэффициентов
    }

    private void handleLogin(String messageText, long chatId) {
        String[] parts = messageText.split(" ");
        if (parts.length != 3) {
            sendMessage(chatId, "Неправильный формат. Используйте: /login <имя_пользователя> <пароль>");
            return;
        }

        String username = parts[1];
        String password = parts[2];

        // Проверка учетных данных
        if (checkUserCredentials(username, password)) {
            authorizedUsers.add(chatId); // Добавление chatId в список авторизованных пользователей
            sendMessage(chatId, "Вы успешно авторизованы.\n\n/take_signals ");
        } else {
            sendMessage(chatId, "Неверные имя пользователя или пароль.");
        }
    }

    private boolean checkUserCredentials(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length == 2 && credentials[0].equals(username) && credentials[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void sendMessage(long chatId, String messageText) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageText);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
