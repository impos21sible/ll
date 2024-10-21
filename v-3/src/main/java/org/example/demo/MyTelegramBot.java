package org.example.demo;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.Arrays;

public class MyTelegramBot extends TelegramLongPollingBot {

    private String password = "111"; // Задайте ваш пароль здесь
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
                default:
                    handleCoefficientInput(chatId, messageText); // Обработка ввода коэффициентов
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
        // Путь к изображению в ресурсах
        File photo = new File("src/main/resources/trade.jpg");

        // Создание сообщения с фото и подписью
        SendPhoto photoMessage = new SendPhoto();
        photoMessage.setChatId(chatId);
        InputFile inputFile = new InputFile(photo);

        // Установка изображения и текста подписи
        photoMessage.setPhoto(inputFile);
        String caption = "\uD83D\uDE0B\uFEFFДобро пожаловать в \uD83D\uDD38Lucky Jet GPT\uD83D\uDD38!\n" +
                "\n" +
                "\uD83D\uDE80 Lucky Jet — это захватывающая азартная игра, идеально подходящая для тех, кто стремится к быстрой и увлекательной прибыли. Игра построена на основе честной и проверенной системы, которая дарит уверенность каждому участнику.\n" +
                "\n" +
                "Твоя цель — сделать ставку и вовремя вывести выигрыш, пока Счастливчик Джо не улетел!\n" +
                "\n" +
                "Наш бот, основанный на нейросети OpenAI, поможет тебе предугадать лучшие моменты для кэшаута с точностью до 97%.";

        photoMessage.setCaption(caption);  // Установка текста подписи
        photoMessage.setReplyMarkup(Buttons.getMainMenuKeyboard());  // Добавление кнопок

        try {
            execute(photoMessage);  // Отправка фото с подписью и кнопками
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendRegistrationInstructions(long chatId) {
        // Путь к изображению для инструкции регистрации
        File registrationPhoto = new File("src/main/resources/registration.jpg"); // Убедитесь, что путь к изображению корректный

        // Создание сообщения с фото и подписью
        SendPhoto photoMessage = new SendPhoto();
        photoMessage.setChatId(chatId);
        InputFile inputFile = new InputFile(registrationPhoto);

        // Установка изображения и текста подписи
        photoMessage.setPhoto(inputFile);
        String caption = "🌐 Шаг 1 - Зарегистрируйся\n\n" +
                "✦ Для синхронизации с нашим ботом необходимо зарегистрировать НОВЫЙ аккаунт по ранее неиспользованному номеру телефона на сайте.\n" +
                "✦ Если Вы переходите по ссылке и попадаете на старый аккаунт, необходимо выйти с него и заново перейти по ссылке регистрации!\n" +
                "\n" +
                "● После завершения регистрации, нажмите на Помощь и напишите нашему оператору.\n" +
                "\n" +
                "🌟 Шаг 2 - Нажмите Назад и получите сигнал.\n" +
                "\n" +
                "🔍 Шаг 3 - Вставьте данные о прошлых 10 играх, и бот начнет анализировать информацию и отправит вам сигнал.";

        photoMessage.setCaption(caption);  // Установка текста подписи
        photoMessage.setReplyMarkup(Buttons.getRegistrationMenu());  // Добавление кнопок

        try {
            execute(photoMessage);  // Отправка фото с подписью и кнопками
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendInstruction(long chatId) {
        // Путь к изображению для инструкции
        File instructionPhoto = new File("src/main/resources/instruction.jpg"); // Убедитесь, что путь к изображению корректный

        // Создание сообщения с фото и подписью
        SendPhoto photoMessage = new SendPhoto();
        photoMessage.setChatId(chatId);
        InputFile inputFile = new InputFile(instructionPhoto);

        // Установка изображения и текста подписи
        photoMessage.setPhoto(inputFile);
        String caption =
                "Бот основан и обучен на кластере нейросети от OpenAI🖥ChatGPT-v4.\n\n" +
                        "Чтобы достичь максимального выигрыша, придерживайтесь данной инструкции:\n\n" +
                        "1️⃣ Регистрация в 1WIN: Зарегистрируйтесь в букмекерской конторе 1win.\n\n" +
                        "2️⃣ Пополнение баланса: Пополните свой игровой счет. Чем больше сумма, тем больше потенциальная прибыль. 🤑\n\n" +
                        "3️⃣ Выбор игры: Перейдите в раздел 1win games и выберите игру 💣 ‘LuckyJet’.\n\n" +
                        "4️⃣ Настройка игры: Установите сумму.\n\n" +
                        "5️⃣ Получение сигнала: Запросите сигнал у бота.\n\n" +
                        "6️⃣ Неудачные сигналы: Если сигнал оказался неудачным, удвойте свою ставку, чтобы компенсировать потерю и выйти в плюс. ❗️\n" +
                        "И запомните, с каждой вашей новой ставкой шанс последующего сигнала повышается на 50%. ❗️";

        photoMessage.setCaption(caption);  // Установка текста подписи
        photoMessage.setReplyMarkup(Buttons.getInstructionKeyboard());  // Измените на метод, который возвращает клавиатуру с кнопкой "Вернуться назад"

        try {
            execute(photoMessage);  // Отправка фото с подписью и кнопками
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void getSignal(long chatId) {
        if (coefficientIndex == 0) {
            // Запрашиваем ввод коэффициентов, если они еще не введены
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Пожалуйста, введите коэффициент 1: x.xx");
            try {
                execute(message); // Отправка сообщения
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            // Если коэффициенты уже введены, обработаем сигнал
            SendMessage message = new SendMessage();
            message.setChatId(String.valueOf(chatId));
            message.setText("Коэффициенты уже введены. Пожалуйста, ждите получения сигнала.");
            try {
                execute(message); // Отправка сообщения
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
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

    private void checkCoefficients(long chatId) {
        boolean allLessThanTwo = true; // Флаг для проверки всех коэффициентов

        // Проверка, все ли коэффициенты меньше 2
        for (double coefficient : coefficients) {
            if (coefficient >= 2) {
                allLessThanTwo = false; // Если хоть один коэффициент >= 2, устанавливаем флаг в false
                break;
            }
        }

        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));

        if (allLessThanTwo) {
            message.setText("Сигнал !!! - ставьте Авто-Стоп на значение 2x - до проигрыша. " +
                    "Если вы проиграли, удваивайте - ПОКА НЕ ВЫИГРАЕТЕ !.\n\n\n Ждем 3 игры.\nВведите 3 последних коэффициента через Enter.");
        } else {
            message.setText("Ожидаем сигнал...\n\n Ждем 3 игры.\nВведите 3 последних коэффициента через Enter.");
        }

        try {
            execute(message); // Отправка сообщения
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void resetCoefficientInput() {
        coefficientIndex = 0; // Сброс индекса для следующего ввода
        Arrays.fill(coefficients, 0); // Сброс массива коэффициентов
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message); // Отправка сообщения
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
