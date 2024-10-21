package org.example.demo;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class Buttons {

    public static InlineKeyboardMarkup getMainMenuKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton registrationButton = new InlineKeyboardButton("🎮 Регистрация");
        registrationButton.setCallbackData("registration");
        row1.add(registrationButton);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton instructionButton = new InlineKeyboardButton("📖 Инструкция");
        instructionButton.setCallbackData("instruction");
        row2.add(instructionButton);

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton getSignalButton = new InlineKeyboardButton("🖥 Получить сигнал");
        getSignalButton.setCallbackData("get_signal");
        row3.add(getSignalButton);

        buttons.add(row1);
        buttons.add(row2);
        buttons.add(row3);

        return new InlineKeyboardMarkup(buttons);
    }

    public static InlineKeyboardMarkup getRegistrationMenu() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton registrationSiteButton = new InlineKeyboardButton("Сайт для регистрации 🚀");
        registrationSiteButton.setUrl("https://1warlo.top/?open=register&p=e979");
        row1.add(registrationSiteButton);

        List<InlineKeyboardButton> row2 = new ArrayList<>();
        InlineKeyboardButton backButton = new InlineKeyboardButton("Назад ⤴️");
        backButton.setCallbackData("back");
        row2.add(backButton);

        List<InlineKeyboardButton> row3 = new ArrayList<>();
        InlineKeyboardButton helpButton = new InlineKeyboardButton("Помощь \uD83D\uDCD5");
        helpButton.setUrl("https://t.me/kunaesv");
        row3.add(helpButton);

        buttons.add(row1);
        buttons.add(row2);
        buttons.add(row3);

        return new InlineKeyboardMarkup(buttons);
    }

    public static InlineKeyboardMarkup getInstructionKeyboard() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton backButton = new InlineKeyboardButton("Назад ⤴️");
        backButton.setCallbackData("back");
        row1.add(backButton);

        buttons.add(row1);
        return new InlineKeyboardMarkup(buttons);
    }

    public static InlineKeyboardMarkup getInstructionMenu() {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton backButton = new InlineKeyboardButton("Назад ⤴️");
        backButton.setCallbackData("back");
        row1.add(backButton);

        buttons.add(row1);
        return new InlineKeyboardMarkup(buttons);
    }
}
