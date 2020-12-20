package functions;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ExpressionSolver {
    private final String s;

    public ExpressionSolver(String s) {
        String[] words = s.split("\\s+", 2);
        this.s = words[1].replaceAll("(?=\\b[a-zA-Z])", "Math.");
    }

    public String solve() {
        final ScriptEngineManager manager = new ScriptEngineManager();
        final ScriptEngine engine = manager
                .getEngineByName("js");

        try {
            final Object result = engine.eval(this.s);
            return result.toString();
        } catch (Exception e) {
            return "Invalid input";
        }
    }
}
