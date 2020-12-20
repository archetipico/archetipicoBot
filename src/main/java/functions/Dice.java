package functions;

import java.util.Arrays;
import java.util.Random;

public class Dice {

    private int faces;
    private int rolls;

    public Dice(String s) {
        String[] tokens = s.split("\\s+", 3);

        this.rolls = Integer.parseInt(tokens[1]);
        this.faces = Integer.parseInt(tokens[2]);
    }

    public String getRoll() {
        this.rolls = Math.min(this.rolls, 500);
        this.faces = Math.min(this.faces, 120);

        StringBuilder sb = new StringBuilder();
        Random rand = new Random();
        for (int i = 0; i < this.rolls; i++) {
            sb.append(rand.nextInt(this.faces + 1))
                    .append(", ");
        }

        return sb.substring(0, sb.length() - 2);
    }
}