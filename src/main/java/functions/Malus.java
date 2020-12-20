package functions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class Malus {

    private final List<String> lines;
    private Random rand;
    private String line;
    private final String path = "malus.txt";

    public Malus() throws IOException {
        this.lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
    }

    public Malus(Random rand) throws IOException {
        this.lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
        this.rand = rand;
        getLine();
    }

    public boolean add(String s) throws IOException {
        if (!lines.contains(s)) {
            lines.add(s);
            FileWriter writer = new FileWriter(new File(path));

            for (String line : lines) {
                writer.write(line + "\n");
            }

            writer.close();
            return true;
        }

        return false;
    }

    public void getLine() {
        line = lines
                .get(rand
                .nextInt(lines
                .size()));
    }

    public boolean remove(String s) throws IOException {
        if (lines.contains(s)) {
            lines.remove(s);
            FileWriter writer = new FileWriter(new File(path));

            for (String line : lines) {
                writer.write(line + "\n");
            }

            writer.close();
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        if (line.contains("-n")) {
            int value = rand.nextInt(20) - 10;
            String replacement = value + "";

            if (value >= 0) {
                replacement = "+" + replacement;
            }

            line = line.replace("-n", replacement);
        }

        return line;
    }
}
