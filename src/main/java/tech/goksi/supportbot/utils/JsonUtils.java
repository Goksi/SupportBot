package tech.goksi.supportbot.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import tech.goksi.supportbot.Bot;

import java.awt.*;
import java.util.Objects;

public class JsonUtils {

    public static MessageEmbed jsonToEmbed(JsonObject jsonMain){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        JsonObject json = jsonMain.getAsJsonObject("embed");
        JsonPrimitive titleObj = json.getAsJsonPrimitive("title");
        if (titleObj != null){
            String title = titleObj.getAsString();
            embedBuilder.setTitle(title);
        }

        JsonObject authorObj = json.getAsJsonObject("author");
        if (authorObj != null) {
            String authorName = authorObj.get("name").getAsString();
            embedBuilder.setAuthor(authorName,null, authorObj.get("icon_url").getAsString());

        }

        JsonPrimitive descObj = json.getAsJsonPrimitive("description");
        if (descObj != null){
            String desc = descObj.getAsString();
            embedBuilder.setDescription(desc);
        }

        JsonPrimitive colorObj = json.getAsJsonPrimitive("color");
        if (colorObj != null){
            embedBuilder.setColor(new Color(colorObj.getAsInt()));
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
        JsonObject thumbnailObj = json.getAsJsonObject("thumbnail");
        if (thumbnailObj != null){
            embedBuilder.setThumbnail(thumbnailObj.get("url").getAsString());
        }

        JsonObject footerObj = json.getAsJsonObject("footer");
        if (footerObj != null){
            String content = footerObj.get("text").getAsString();
            if (footerObj.has("icon_url"))
                embedBuilder.setFooter(content, footerObj.get("icon_url").getAsString().replaceAll("%footerUrl", Objects.requireNonNull(Bot.getInstance().getJda().getSelfUser().getAvatarUrl())));
            else
                embedBuilder.setFooter(content);
        }

        return embedBuilder.build();
    }
}
