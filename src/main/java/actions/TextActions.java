package actions;

import functions.*;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TextActions {

    private final Integer msgID;
    private Integer targetID;
    private final long chatID;
    private final Message msg;
    private final Message reply;
    private final Random rand;
    private SendMessage msgToSend;
    private String targetName;
    private String text;
    private String[] words;
    private URLManipulator urlm;
    private final User author;

    private final ArrayList<String> oofVoices = new ArrayList<>(Arrays.asList("minecraft", "roblox"));
    private final ArrayList<String> starWarsVoices = new ArrayList<>(Arrays.asList("ackbar", "chewbacca", "droid", "ewok",
            "gamorrean_guard", "jar_jar", "R2-D2", "yoda"));

    public TextActions(Message msg, String[] words) {
        rand = new Random(System.currentTimeMillis());

        this.msg = msg;
        this.author = msg.getFrom();
        this.chatID = msg.getChatId();
        this.msgID = msg.getMessageId();
        this.reply = msg.getReplyToMessage();
        this.text = msg.getText().toLowerCase();
        this.words = words;
        this.urlm = null;

        try {
            targetName = reply
                    .getFrom()
                    .getFirstName();
            targetID = reply
                    .getFrom()
                    .getId();
        } catch (Exception e) {
            targetName = null;
            targetID = null;
        }
    }

    public SendMessage problemNotifier() {
        return new SendMessage()
                .setChatId(chatID)
                .setReplyToMessageId(msgID)
                .setText("Something went wrong, sorry");
    }

    public SendAnimation animationRoutine() {
        SendAnimation sa = new SendAnimation()
                .setChatId(chatID)
                .setReplyToMessageId(msgID);

        if (text.contains("coom")) {
            return sa
                    .setAnimation(new File("coom.gif"));
        }

        return null;
    }

    public SendPhoto averageAction() throws IOException {
        if (targetID != null && reply.hasPhoto()) {
            urlm = new URLManipulator(new URL("https://api.telegram.org/bot"
                    + new Secret()
                    .getToken()
                    + "/getFile?file_id="
                    + reply
                    .getPhoto()
                    .get(0)
                    .getFileId()));

            urlm.urlGET();
            PictureManipulator pm = new PictureManipulator(urlm.getURL("photos/").openStream());
            pm.average();

            return new SendPhoto()
                    .setChatId(chatID)
                    .setReplyToMessageId(msgID)
                    .setPhoto(new File("average.png"));
        }

        return null;
    }

    public SendMessage getChatIdAction() {
        return new SendMessage()
                .setChatId(chatID)
                .setReplyToMessageId(msgID)
                .setText(String.valueOf(chatID));
    }

    public SendDocument helpAction() {
        return new SendDocument()
                .setChatId(chatID)
                .setReplyToMessageId(msgID)
                .setDocument(new File("help.txt"))
                .setCaption("This is my command list");
    }

    public SendMessage getIDAction() {
        msgToSend = new SendMessage()
                .setChatId(chatID)
                .setReplyToMessageId(msgID);

        if (targetID != null) {
            msgToSend
                    .setText("The target's ID is " + targetID
                            .toString());
        } else {
            msgToSend
                    .setText("Your ID is " + author
                            .getId()
                            .toString());
        }

        return msgToSend;
    }

    public SendMessage getMsgIDAction() {
        msgToSend = new SendMessage()
                .setChatId(chatID)
                .setReplyToMessageId(msgID);

        if (targetID != null) {
            msgToSend
                    .setText("The target's message ID is " + reply
                            .getMessageId()
                            .toString());
        } else {
            msgToSend
                    .setText("Your message ID is " + msgID.toString());
        }

        return msgToSend;
    }

    public SendPhoto gradientAction() throws IOException {
        if (targetID != null && reply.hasPhoto()) {
            urlm = new URLManipulator(new URL("https://api.telegram.org/bot"
                    + new Secret()
                    .getToken()
                    + "/getFile?file_id="
                    + reply
                    .getPhoto()
                    .get(0)
                    .getFileId()));

            urlm.urlGET();
            PictureManipulator pm = new PictureManipulator(urlm.getURL("photos/").openStream());
            pm.gradient();

            return new SendPhoto()
                    .setChatId(chatID)
                    .setReplyToMessageId(msgID)
                    .setPhoto(new File("gradient.png"));
        }

        return null;
    }

    public SendMessage horoscopeAction() {
        return new SendMessage()
                .setChatId(chatID)
                .setReplyToMessageId(msgID)
                .setParseMode("MarkdownV2")
                .setText(new Horoscope(text).toString());
    }

    public SendMessage malusAction() {
        msgToSend = new SendMessage()
                .setChatId(chatID);

        text = msg.getText();

        try {
            if (words.length > 1) {
                return malusSwitch();
            } else if (words.length == 1 && targetID != null) {
                return msgToSend
                        .setText(targetName + " " + new Malus(rand)
                                .toString());
            } else {
                return msgToSend
                        .setText(author.getFirstName() + " " + new Malus(rand)
                                .toString());
            }
        } catch (IOException e) {
            return problemNotifier();
        }
    }

    private SendMessage malusSwitch() throws IOException {
        boolean b;
        switch (words[1]) {
            case "add":
                words = text.split("\\s+", 3);
                b = new Malus()
                        .add(words[2]);

                if (b) {
                    return msgToSend
                            .setText(words[2] + " added to mali");
                } else {
                    return msgToSend
                            .setText(words[2] + " already exists");
                }

            case "remove":
                words = text.split("\\s+", 3);
                b = new Malus()
                        .remove(words[2]);

                if (b) {
                    return msgToSend
                            .setText(words[2] + " removed from mali");
                } else {
                    return msgToSend
                            .setText(words[2] + " can't be removed since it can't be found");
                }

            default:
                words = text.split("\\s+", 2);
                return msgToSend
                        .setText(words[1] + " " + new Malus(rand)
                                .toString());
        }
    }

    public SendPhoto mandelAction() throws IOException {
        final int height = 600;
        final int width = 800;

        int l = words.length;

        int maxit = Integer.parseInt(words[1]);
        if (maxit < 1 || maxit > 10000) {
            maxit = 1000;
        }

        double centering = Double.parseDouble(words[2]);

        double zoom = Double.parseDouble(words[3]);
        if (zoom < 0.01 || zoom > 1000) {
            zoom = 5;
        }

        Mandel mandel;
        if (l == 5) {
            mandel = new Mandel(maxit, centering, zoom, new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR), words[4]);
        } else {
            mandel = new Mandel(maxit, centering, zoom, new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR));
        }

        BufferedImage bi = mandel.getPlot();
        ImageIO.write(bi, "png", new File("mandelbrot.png"));

        return new SendPhoto()
                    .setChatId(chatID)
                    .setReplyToMessageId(msgID)
                    .setPhoto(new File("mandelbrot.png"));
    }

    public SendPhoto negativeAction() throws IOException {
        if (targetID != null && reply.hasPhoto()) {
            urlm = new URLManipulator(new URL("https://api.telegram.org/bot"
                    + new Secret()
                    .getToken()
                    + "/getFile?file_id="
                    + reply
                    .getPhoto()
                    .get(0)
                    .getFileId()));

            urlm.urlGET();
            PictureManipulator pm = new PictureManipulator(urlm.getURL("photos/").openStream());
            pm.negative();

            return new SendPhoto()
                    .setChatId(chatID)
                    .setReplyToMessageId(msgID)
                    .setPhoto(new File("negative.png"));
        }

        return null;
    }

    public SendMessage nextPrimeAction() {
        msgToSend = new SendMessage()
                .setChatId(chatID)
                .setReplyToMessageId(msgID);

        if (words.length == 2) {
            msgToSend.
                    setText(new BigInteger(words[1])
                            .nextProbablePrime() + " is the next probable prime");
        } else {
            msgToSend
                    .setText("Give me a number");
        }

        return msgToSend;
    }

    public SendPhoto photoRoutine() {
        SendPhoto sp = new SendPhoto()
                .setChatId(chatID)
                .setReplyToMessageId(msgID);

        ArrayList<String> listedWords = new ArrayList<>(Arrays.asList(words));
        if (listedWords.contains("e?") || listedWords.contains("and?")) {
            return sp
                    .setPhoto(new File("photos/AND.jpg"));
        } else if (text.contains("\uD83D\uDC4D") || text.contains("\uD83D\uDC4C")) {
            return sp
                    .setPhoto(new File("photos/picardia.png"));
        }

        return null;
    }

    public SendPhoto pixelatedAction() throws IOException {
        if (targetID != null && reply.hasPhoto()) {
            urlm = new URLManipulator(new URL("https://api.telegram.org/bot"
                    + new Secret()
                    .getToken()
                    + "/getFile?file_id="
                    + reply
                    .getPhoto()
                    .get(0)
                    .getFileId()));

            urlm.urlGET();
            PictureManipulator pm = new PictureManipulator(urlm.getURL("photos/").openStream());

            if (words.length == 2 && words[1].matches("\\d+") && words[1].length() < 10) {
                pm.pixelate(Integer.parseInt(words[1]));
            } else {
                pm.pixelate(10);
            }

            return new SendPhoto()
                    .setChatId(chatID)
                    .setReplyToMessageId(msgID)
                    .setPhoto(new File("pixelated.png"));
        }

        return null;
    }

    public SendMessage primeAction() {
        msgToSend = new SendMessage()
                .setChatId(chatID)
                .setReplyToMessageId(msgID);

        if (words.length == 2) {
            BigInteger bg = new BigInteger(words[1]);
            if (bg.isProbablePrime(1662803)) {
                if (bg.compareTo(new BigInteger("3317044064679887385961981")) > 0) {
                    msgToSend.
                            setText(words[1] + " is probably prime");
                } else {
                    msgToSend.
                            setText(words[1] + " is prime for sure");
                }
            } else {
                msgToSend.
                        setText(words[1] + " is not prime for sure");
            }
        } else {
            msgToSend
                    .setText("Give me a number");
        }

        return msgToSend;
    }

    public SendMessage rateAction() {
        return new SendMessage()
                .setChatId(chatID)
                .setReplyToMessageId(msgID)
                .setText(new Random()
                        .nextInt(12) + "/12");
    }

    public SendMessage replaceAction() {
        msgToSend = new SendMessage()
                .setChatId(chatID)
                .setReplyToMessageId(reply.getMessageId());

        if (targetID != null) {
            words = msg.getText().split(" ", 3);

            if (words[1].equals("!all")) {
                words[1] = reply.getText();
            }

            if (words[2].equals("!del")) {
                words[2] = "\u00AD";
            }

            String result = reply
                    .getText()
                    .replaceAll(words[1], words[2]);

            msgToSend
                    .setText(result);

        } else {
            msgToSend
                    .setText("This function works when you reply to a message");
        }

        return msgToSend;
    }

    public SendMessage roadAction() {
        return new SendMessage()
                .setChatId(chatID)
                .setReplyToMessageId(msgID)
                .setText(new Maps(text)
                        .getDirections());
    }

    public SendMessage rollAction() {
        return new SendMessage()
                .setChatId(chatID)
                .setReplyToMessageId(msgID)
                .setText(new Dice(text).getRoll());
    }

    public SendMessage consoleMessageAction() {
        String[] data = msg.getText().split("\\s+", 4);
        msgToSend = new SendMessage()
                .setChatId(data[1])
                .setText(data[3]);

        if (!data[2].equals("null")) {
            return msgToSend
                    .setReplyToMessageId(Integer.valueOf(data[2]));
        }

        return msgToSend;
    }

    public SendPhoto simplifyAction() throws IOException {
        if (targetID != null && reply.hasPhoto()) {
            urlm = new URLManipulator(new URL("https://api.telegram.org/bot"
                    + new Secret()
                    .getToken()
                    + "/getFile?file_id="
                    + reply
                    .getPhoto()
                    .get(0)
                    .getFileId()));

            urlm.urlGET();
            PictureManipulator pm = new PictureManipulator(urlm.getURL("photos/").openStream());
            pm.simplify();

            return new SendPhoto()
                    .setChatId(chatID)
                    .setReplyToMessageId(msgID)
                    .setPhoto(new File("simplify.png"));
        }

        return null;
    }

    public SendMessage solveAction() {
        return new SendMessage()
                .setChatId(chatID)
                .setReplyToMessageId(msgID)
                .setText(new ExpressionSolver(msg.getText()).solve());
    }

    public SendMessage urbanAction() {
        return new SendMessage()
                .setChatId(chatID)
                .setReplyToMessageId(msgID)
                .setText(new UrbanDictionary(text).toString());
    }

    public SendVoice voiceRoutine() {
        SendVoice sv = new SendVoice()
                .setChatId(chatID)
                .setReplyToMessageId(msgID);

        if (text.contains("ahhhhhhhh")) {
            return sv
                    .setVoice(new File("voices/" + starWarsVoices
                            .get(rand.nextInt(starWarsVoices.size())) + ".mp3"
                    ));
        } else if (text.contains("oof")) {
            return sv
                    .setVoice(new File("voices/" + oofVoices
                            .get(rand.nextInt(oofVoices.size())) + "_oof.mp3"
                    ));
        }

        return null;
    }

    public SendMessage weatherAction() {
        return new SendMessage()
                .setChatId(chatID)
                .setReplyToMessageId(msgID)
                .setParseMode("MarkdownV2")
                .setText(new Weather(text).getWeather());
    }
}
