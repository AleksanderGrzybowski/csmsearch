package pl.kelog.csmsearch;

import java.util.HashMap;
import java.util.Map;

public class Utils {
    
    private static final Map<String, String> replacements = new HashMap<>();
    
    static {
        replacements.put("ą", "a");
        replacements.put("ę", "e");
        replacements.put("ó", "o");
        replacements.put("ż", "z");
        replacements.put("ź", "z");
        replacements.put("ł", "l");
        replacements.put("ś", "s");
        replacements.put("ń", "n");
        replacements.put("ć", "c");
    }
    
    public static String normalize(String input) {
        input = input.toLowerCase();
        for (Map.Entry<String, String> replacement : replacements.entrySet()) {
            input = input.replace(replacement.getKey(), replacement.getValue());
        }
        return input;
    }
}
