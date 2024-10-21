package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class HelloController {
    @FXML
    private Label statusLabel;

    @FXML
    protected void onHelloButtonClick() {
        statusLabel.setText("Запуск Telegram бота...");

        // Запуск Telegram бота
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new MyTelegramBot());  // Запускаем бота
            statusLabel.setText("Telegram бот запущен!");
        } catch (TelegramApiException e) {
            statusLabel.setText("Ошибка запуска Telegram бота!");
            e.printStackTrace();
        }
    }
}
