import actions.TextActions;
import functions.Secret;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVoice;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;

public class NormaloideBot extends TelegramLongPollingBot {

    private Message msg;
    private SendPhoto sp;
    private TextActions txtAction;

    public void onUpdateReceived(Update update) {
        this.msg = update.getMessage();
        if (!msg.getFrom().getUserName().toLowerCase().contains("bot")) {
            try {
                if (msg.hasText()) {
                    doText();
                }
            } catch (TelegramApiException | IOException tae) {
                tae.printStackTrace();
            }
        }
    }

    private void doText() throws TelegramApiException, IOException {
        String[] words = msg
                .getText()
                .toLowerCase()
                .split("\\s+");

        txtAction = new TextActions(msg, words);
        switch (words[0]) {
            case "/chatid":
                execute(txtAction.getChatIdAction());
                break;

            case "/help":
            case "/help@normaloide_bot":
                execute(txtAction.helpAction());
                break;

            case "/id":
                execute(txtAction.getIDAction());
                break;

            case "/getmalus":
                execute(new SendDocument()
                        .setChatId(msg.getChatId())
                        .setReplyToMessageId(msg.getMessageId())
                        .setDocument(new File("malus.txt"))
                        .setCaption("These are my maluses"));
                break;

            case "/msgid":
                execute(txtAction.getMsgIDAction());
                break;

            case "average":
                manipulatePicture(0);
                break;

            case "gradient":
                manipulatePicture(1);
                break;

            case "horoscope":
                execute(txtAction.horoscopeAction());
                break;

            case "isprime":
                execute(txtAction.primeAction());
                break;

            case "malus":
                execute(txtAction.malusAction());
                break;

            case "mandel":
                execute(txtAction.mandelAction());
                break;

            case "negative":
                manipulatePicture(2);
                break;

            case "nextprime":
                execute(txtAction.nextPrimeAction());
                break;

            case "pixelate":
                manipulatePicture(3);
                break;

            case "rate":
                execute(txtAction.rateAction());
                break;

            case "replace":
                execute(txtAction.replaceAction());
                break;

            case "road":
                execute(txtAction.roadAction());
                break;

            case "roll":
                execute(txtAction.rollAction());
                break;

            case "send":
                if (msg.getFrom().getId().toString().equals(new Secret().getId())) {
                    execute(txtAction.consoleMessageAction());
                }

                break;

            case "simplify":
                manipulatePicture(4);
                break;

            case "solve":
                execute(txtAction.solveAction());
                break;

            case "urban":
                execute(txtAction.urbanAction());
                break;

            case "weather":
                execute(txtAction.weatherAction());
                break;

            default:
                break;
        }

        routines();
    }

    private void manipulatePicture(int n) throws TelegramApiException {
        try {
            switch (n) {
                case 0:
                    sp = txtAction.averageAction();
                    if (sp != null) {
                        execute(txtAction.averageAction());
                    }

                    break;

                case 1:
                    sp = txtAction.gradientAction();
                    if (sp != null) {
                        execute(txtAction.gradientAction());
                    }

                    break;

                case 2:
                    sp = txtAction.negativeAction();
                    if (sp != null) {
                        execute(txtAction.negativeAction());
                    }

                    break;

                case 3:
                    sp = txtAction.pixelatedAction();
                    if (sp != null) {
                        execute(txtAction.pixelatedAction());
                    }

                    break;

                case 4:
                    sp = txtAction.simplifyAction();
                    if (sp != null) {
                        execute(txtAction.simplifyAction());
                    }

                    break;

                default:
                    break;
            }
        } catch (IOException e) {
            execute(txtAction.problemNotifier());
        }
    }

    private void routines() throws TelegramApiException {
        try {
            SendAnimation sa = txtAction.animationRoutine();
            if (sa != null) {
                execute(sa);
            }

            sp = txtAction.photoRoutine();
            if (sp != null) {
                execute(sp);
            }

            SendVoice sv = txtAction.voiceRoutine();
            if (sv != null) {
                execute(sv);
            }
        } catch (Exception e) {
            execute(txtAction.problemNotifier());
        }
    }

    @Override
    public String getBotToken() {
        return new Secret().getToken();
    }

    @Override
    public String getBotUsername() {
        return "normaloide's bot";
    }
}
