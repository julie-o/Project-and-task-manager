package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControlUtils {
    public static ControlUtils instance;

    public static ControlUtils getInstance(){
        if(instance == null){
            instance = new ControlUtils();
        }
        return instance;
    }

    public String includesSpecialCharacter(String text){
        StringBuilder foundChars = new StringBuilder();

        Pattern checkPattern = Pattern.compile("[^\\p{L}0-9 ]");
        Matcher match = checkPattern.matcher(text);

        for (int i = 0; i < text.length(); i++) {
            if (match.find()) {
                if (!foundChars.toString().contains(match.group(0))){{
                    foundChars.append(match.group(0));
                }}
            }
        }

        return foundChars.toString();
    }

    public String removeQuotes(String input){
        if (input.isBlank()||input.isEmpty()){
            return "";
        } else if (input.length()>=2){
            String output=input;
            if (input.startsWith("\"")){
                output = input.substring(1);
            }
            if (input.endsWith("\"")) {
                output = output.substring(0, output.length()-1);
            }
            return output;
        } else {
            return input;
        }
    }
}
