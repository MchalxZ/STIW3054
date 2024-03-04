package uum;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

public class KeyboardFactory {
    public static ReplyKeyboard getRepositoryKeyboard() {
        KeyboardRow row = new KeyboardRow();
        row.add("Repository1");
        row.add("Repository2");
        return new ReplyKeyboardMarkup(List.of(row));
    }

    public static ReplyKeyboard getGithubIssueKeyboard(){
        KeyboardRow row = new KeyboardRow();
        row.add("Github issue");
        return new ReplyKeyboardMarkup(List.of(row));
    }

    public static ReplyKeyboard getYesOrNo() {
        KeyboardRow row = new KeyboardRow();
        row.add("Yes");
        row.add("No");
        return new ReplyKeyboardMarkup(List.of(row));
    }
}