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

public class MyTelegramBot extends TelegramLongPollingBot {

    private static final String USER_DATA_FILE = "src/main/resources/users.csv"; // Путь к файлу с данными пользователей
    private boolean isAuthorized = false; // Флаг для проверки успешной авторизации
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
                    break;
                case "/login":
                    // Запрос ввода логина и пароля
                    sendMessage(chatId, "Введите ваше имя пользователя и пароль через пробел: /login <имя_пользователя> <пароль>");
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

    private void sendWelcomeMessage(long chatId) {
        File photo = new File("src/main/resources/trade.jpg");
        SendPhoto photoMessage = new SendPhoto();
        photoMessage.setChatId(chatId);
        InputFile inputFile = new InputFile(photo);
        photoMessage.setPhoto(inputFile);

        String caption = "\uD83D\uDE0B\uFEFFДобро пожаловать в \uD83D\uDD38Lucky Jet GPT\uD83D\uDD38!\n" +
                "\n" +
                "\uD83D\uDE80 Lucky Jet — это захватывающая азартная игра, идеально подходящая для тех, кто стремится к быстрой и увлекательной прибыли.\n" +
                "Твоя цель — сделать ставку и вовремя вывести выигрыш!\n" +
                "Наш бот, основанный на нейросети OpenAI, поможет тебе предугадать лучшие моменты для кэшаута с точностью до 97%.";

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
                "✦ Для синхронизации с нашим ботом необходимо зарегистрировать НОВЫЙ аккаунт...\n" +
                "✦ Если Вы переходите по ссылке и попадаете на старый аккаунт, необходимо выйти с него.\n" +
                "● После завершения регистрации, нажмите на Помощь и напишите нашему оператору.\n" +
                "🌟 Шаг 2 - Нажмите Назад и получите сигнал.\n" +
                "🔍 Шаг 3 - Вставьте данные о прошлых 10 играх.";

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
                "Чтобы достичь максимального выигрыша, придерживайтесь данной инструкции:\n" +
                "1️⃣ Регистрация в 1WIN: Зарегистрируйтесь.\n" +
                "2️⃣ Пополнение баланса: Пополните свой игровой счет.\n" +
                "3️⃣ Выбор игры: Перейдите в раздел 1win games и выберите игру 💣 ‘LuckyJet’.\n" +
                "4️⃣ Настройка игры: Установите сумму.\n" +
                "5️⃣ Получение сигнала: Запросите сигнал у бота.\n" +
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
        if (!isAuthorized) { // Проверка, авторизован ли пользователь
            sendMessage(chatId, "Пожалуйста, сначала авторизуйтесь с помощью команды /login.");
            return; // Завершение выполнения метода, если пользователь не авторизован
        }

        if (coefficientIndex == 0) {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Пожалуйста, введите коэффициент 1: x.xx");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Коэффициенты уже введены. Пожалуйста, ждите получения сигнала.");
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }


    private void handleCoefficientInput(long chatId, String userInput) {
        if (coefficientIndex < 3) {
            try {
                coefficients[coefficientIndex] = Double.parseDouble(userInput);
                coefficientIndex++;

                if (coefficientIndex < 3) {
                    SendMessage message = new SendMessage();
                    message.setChatId(String.valueOf(chatId));
                    message.setText("Коэффициент " + coefficientIndex + " принят. Введите следующий коэффициент: x.xx");
                    execute(message);
                } else {
                    SendMessage finalMessage = new SendMessage();
                    finalMessage.setChatId(String.valueOf(chatId));
                    finalMessage.setText("Коэффициенты успешно получены: " + Arrays.toString(coefficients));
                    execute(finalMessage);

                    checkCoefficients(chatId);
                    resetCoefficientInput();
                }
            } catch (NumberFormatException | TelegramApiException e) {
                SendMessage errorMessage = new SendMessage();
                errorMessage.setChatId(String.valueOf(chatId));
                errorMessage.setText("Пожалуйста, введите корректный коэффициент в формате x.xx.");
                try {
                    execute(errorMessage);
                } catch (TelegramApiException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void checkCoefficients(long chatId) {
        boolean allLessThanTwo = true;

        for (double coefficient : coefficients) {
            if (coefficient >= 2) {
                allLessThanTwo = false;
                break;
            }
        }

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));

        if (allLessThanTwo) {
            message.setText("Сигнал !!! - ставьте Авто-Стоп на значение 2x - до проигрыша. Желаем удачи! 🎰");
        } else {
            message.setText("Обратите внимание! Один из коэффициентов превышает 2. Будьте осторожны!");
        }

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void resetCoefficientInput() {
        Arrays.fill(coefficients, 0);
        coefficientIndex = 0;
    }

    private void handleLogin(String messageText, long chatId) {
        String[] parts = messageText.split(" ");
        if (parts.length != 3) {
            sendMessage(chatId, "Неправильный формат. Используйте: /login <имя_пользователя> <пароль>");
            return;
        }

        String username = parts[1];
        String password = parts[2];

        if (authenticateUser(username, password)) {
            isAuthorized = true; // Установка флага авторизации
            sendMessage(chatId, "Вы успешно авторизованы!");
            // Здесь можно добавить дополнительные действия после успешной авторизации
        } else {
            sendMessage(chatId, "Неправильное имя пользователя или пароль.");
        }
    }

    private boolean authenticateUser(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] credentials = line.split(",");
                if (credentials.length == 2 && credentials[0].equals(username) && credentials[1].equals(password)) {
                    return true; // Успешная авторизация
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Неудачная авторизация
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
