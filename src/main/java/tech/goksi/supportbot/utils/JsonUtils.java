package tech.goksi.supportbot.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;

public class JsonUtils {
    public static MessageEmbed jsonToEmbed(JsonObject json, User user, String modalTitle, String modalDescription){
        EmbedBuilder embedBuilder = new EmbedBuilder();

        JsonPrimitive titleObj = json.getAsJsonPrimitive("title");
        if (titleObj != null){
            embedBuilder.setTitle(titleObj.getAsString().replaceAll("%modalTitle", modalTitle));
        }

        JsonObject authorObj = json.getAsJsonObject("author");
        if (authorObj != null) {
            String authorName = authorObj.get("name").getAsString().replaceAll("%name", user.getAsTag());
            String authorIconUrl = authorObj.get("icon_url").getAsString();
            if (authorIconUrl != null)
                embedBuilder.setAuthor(authorName, authorIconUrl);
            else
                embedBuilder.setAuthor(authorName);
        }

        JsonPrimitive descObj = json.getAsJsonPrimitive("description");
        if (descObj != null){
            embedBuilder.setDescription(descObj.getAsString().replaceAll("%modalDesc", modalDescription));
        }

        JsonPrimitive colorObj = json.getAsJsonPrimitive("color");
        if (colorObj != null){
            Color color = new Color(colorObj.getAsInt());
            embedBuilder.setColor(color);
        }

        JsonArray fieldsArray = json.getAsJsonArray("fields");
        if (fieldsArray != null) {

            fieldsArray.forEach(field -> {
                String name = field.getAsJsonObject().get("name").getAsString();
                String content = field.getAsJsonObject().get("value").getAsString();
                boolean inline = field.getAsJsonObject().get("inline").getAsBoolean();
                embedBuilder.addField(name, content, inline);
            });
        }
        JsonPrimitive thumbnailObj = json.getAsJsonPrimitive("thumbnail");
        if (thumbnailObj != null){
            embedBuilder.setThumbnail(thumbnailObj.getAsString());
        }

        JsonObject footerObj = json.getAsJsonObject("footer");
        if (footerObj != null){
            String content = footerObj.get("text").getAsString();
            String footerIconUrl = footerObj.get("icon_url").getAsString();

            if (footerIconUrl != null)
                embedBuilder.setFooter(content, footerIconUrl);
            else
                embedBuilder.setFooter(content);
        }

        return embedBuilder.build();
    }
}
