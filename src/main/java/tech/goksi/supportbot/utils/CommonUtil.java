package tech.goksi.supportbot.utils;


import net.dv8tion.jda.api.entities.Message;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.Collectors;


public class CommonUtil {

    private static String readRaw(String URL) throws Exception{
        StringBuilder result = new StringBuilder();
        java.net.URL url = new URL(URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.92 Safari/537.36'");
        conn.addRequestProperty("Content-Type", "text/plain; charset=utf-8");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line).append('\n');
            }
        }
        return result.toString();
    }
    public static String read(String URL) throws Exception{
        URL baseUrl = new URL(URL);
        URL rawUrl = new URL(baseUrl, "/raw" + baseUrl.getPath());
        HttpURLConnection conn = (HttpURLConnection) rawUrl.openConnection();
        conn.setRequestMethod("HEAD");
        conn.addRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.92 Safari/537.36'");
        conn.addRequestProperty("Content-Type", "text/plain; charset=utf-8");
        if(conn.getResponseCode() == 200) {
            return readRaw(rawUrl.toString());
        }else return readRaw(URL);
    }

    public static String uploadToHaste(String text) throws IOException {
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        for(Character c : text.toCharArray()) {
            if(counter == 0 && Character.isWhitespace(c)) continue;
            sb.append(c);
            if(counter == 80){
                sb.append("\n");
                counter = 0;
            }
            counter++;
        }
        URL url = new URL("https://www.toptal.com/developers/hastebin/documents");
        URLConnection connection = url.openConnection();

        connection.setRequestProperty("authority", "toptal.com");
        connection.setRequestProperty("accept", "application/json, text/javascript, /; q=0.01");
        connection.setRequestProperty("x-requested-with", "XMLHttpRequest");
        connection.setRequestProperty("user-agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.92 Safari/537.36'");
        connection.setRequestProperty("content-type", "application/json; charset=UTF-8");
        connection.setDoOutput(true);

        OutputStream stream = connection.getOutputStream();
        stream.write(sb.toString().getBytes(StandardCharsets.UTF_8));
        stream.flush();
        stream.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String response = reader.lines().collect(Collectors.joining("\n"));

        return "https://toptal.com/developers/hastebin/" + response.split("\"")[3];
    }

    public static String formatString(String string, String... replacements){
        String newString = string;
        if(replacements.length % 2 != 0) throw new IllegalArgumentException("Number of replacements must be even !");
        for(int i = 0; i < replacements.length; i += 2){
            newString = newString.replaceAll(replacements[i], replacements[i+1]);
        }
        return newString;
    }

    public  static void readAttachment(Message.Attachment attachment, final Consumer<String> consumer){
        if(attachment.getSize() < Constants.MAX_ATTACHMENT_SIZE){
            attachment.retrieveInputStream().thenAcceptAsync(is -> {
                consumer.accept(new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n")));
            });
        }
    }


    public static int randomInt(){
        int result = 0;
        for(int i = 0; i < 5; i++){
            result += (int) (Math.random() * 10);
            result *= 10;
        }
        return result / 10;
    }
}
