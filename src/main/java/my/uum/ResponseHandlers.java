package uum;

import org.telegram.abilitybots.api.db.DBContext;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;

import java.util.Map;

import static my.uum.BotState.*;
import static my.uum.Constants.START_TEXT;


public class ResponseHandlers {
    public static long chatID;
   public static SilentSender sender;
    public static Map<Long, BotState> chatStates;
    public ResponseHandlers(SilentSender sender, DBContext db) {
        ResponseHandlers.sender = sender;
        chatStates = db.getMap(Constants.CHAT_STATES);
    }

    public void replyToStart(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(START_TEXT);
        sender.execute(message);
        chatStates.put(chatId, AWAITING_NAME);
    }

    public void replyToButtons(long chatId, Message message) {
        if (message.getText().equalsIgnoreCase("/stop")) {
            stopChat(chatId);
        }

        switch (chatStates.get(chatId)) {
            case AWAITING_NAME -> replyToName(chatId, message);
            case GITHUB_ISSUE_SELECTION -> replyToGithubIssue(chatId, message);
            case GITHUB_REPO_SELECTION -> replyRepo(chatId, message);
            case AWAITING_CONFIRMATION -> replyToCommand(chatId, message);
            default -> unexpectedMessage(chatId);
        }
    }

    private void unexpectedMessage(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Error!");
        sender.execute(sendMessage);
    }

    private void stopChat(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Thank you for using it. \nPress /start to start again");
        chatStates.remove(chatId);
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
        sender.execute(sendMessage);
    }

    private void replyToCommand(long chatId, Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if ("yes".equalsIgnoreCase(message.getText())) {
            sendMessage.setText("Click Repository!");
            sendMessage.setReplyMarkup(KeyboardFactory.getGithubIssueKeyboard());
            sender.execute(sendMessage);
            chatStates.put(chatId, GITHUB_ISSUE_SELECTION);
        } else if ("no".equalsIgnoreCase(message.getText())) {
            stopChat(chatId);
        } else {
            sendMessage.setText("Please select yes or no");
            sendMessage.setReplyMarkup(KeyboardFactory.getYesOrNo());
            sender.execute(sendMessage);
        }
    }

    private void replyRepo(long chatId, Message message) {
        chatID = chatId;
        if ("Repository1".equalsIgnoreCase(message.getText())) {
            String replyM = JsonExample.extractAndParseJsonArray(GitHubIssueController1.k());
            promptWithKeyboardForState(chatId, "Github Issues array: ["+replyM+"]\nRead again?",
                    KeyboardFactory.getYesOrNo(), AWAITING_CONFIRMATION);
        } else if("Repository2".equalsIgnoreCase(message.getText())){
            String replyM = JsonExample.extractAndParseJsonArray(GitHubIssueController2.k());
            promptWithKeyboardForState(chatId, "Github Issues array: ["+replyM+"]\nRead again?",
                    KeyboardFactory.getYesOrNo(), AWAITING_CONFIRMATION);
        }else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Only Repository1 or Repository2 at the same time");
            sendMessage.setReplyMarkup(KeyboardFactory.getRepositoryKeyboard());
            sender.execute(sendMessage);
        }
    }
    private void promptWithKeyboardForState(long chatId, String text, ReplyKeyboard YesOrNo, BotState awaitingReorder) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(YesOrNo);
        sender.execute(sendMessage);
        chatStates.put(chatId, awaitingReorder);
    }
    private void replyToGithubIssue(long chatId, Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
       if ("Github issue".equalsIgnoreCase(message.getText())) {
            sendMessage.setText("Choose Repository 1 or Repository 2");
            sendMessage.setReplyMarkup(KeyboardFactory.getRepositoryKeyboard());
            sender.execute(sendMessage);
            chatStates.put(chatId, GITHUB_REPO_SELECTION);
        } else {
            sendMessage.setText("Do not key in " + message.getText() + " as we don't provide that function");
            sendMessage.setReplyMarkup(KeyboardFactory.getGithubIssueKeyboard());
            sender.execute(sendMessage);
        }
    }

    private void replyToName(long chatId, Message message) {
        promptWithKeyboardForState(chatId, "Hello " + message.getText() + ". Anything?",
                KeyboardFactory.getGithubIssueKeyboard(),
                GITHUB_ISSUE_SELECTION);
    }

    public boolean userIsActive(Long chatId) {
        return chatStates.containsKey(chatId);
    }
}
