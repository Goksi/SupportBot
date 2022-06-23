package tech.goksi.supportbot.utils;

public class CommonUtils {

    public static String formatString(String string, String... replacements){
        String newString = string;
        if(replacements.length % 2 != 0) throw new IllegalArgumentException("Number of replacements must be even !");
        for(int i = 0; i < replacements.length; i += 2){
            newString = newString.replaceAll(replacements[i], replacements[i+1]);
        }
        return newString;
    }
}
