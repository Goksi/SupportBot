package tech.goksi.supportbot.utils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;


public class Web {

    public static String read(String URL) throws Exception{
        StringBuilder result = new StringBuilder();
        java.net.URL url = new URL(URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoOutput(true);
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line).append('\n');
            }
        }
        return result.toString();
    }

    public static String uploadToHaste(String text) throws IOException {
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        for(Character c : text.toCharArray()) {
            if(counter == 0 && Character.isWhitespace(c)){
                continue;
            }
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

}
