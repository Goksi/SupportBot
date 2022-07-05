package tech.goksi.supportbot.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.goksi.supportbot.entities.Keyword;
import tech.goksi.supportbot.utils.CommonUtil;

import java.util.regex.Pattern;

public class KeywordsListener extends ListenerAdapter {
    private final Logger logger;
    public KeywordsListener(){
        logger = LoggerFactory.getLogger(this.getClass());
    }
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        //first check for link in message
        String message = event.getMessage().getContentRaw();
        Keyword keyword;
        for(String word: message.split("\\s++")){
            if(Pattern.matches(EmbedBuilder.URL_PATTERN.pattern(), word)){
                try {
                    message = CommonUtil.read(word);
                } catch (Exception e) {
                    logger.warn("Error while reading link, proceeding with normal message", e);
                    message = event.getMessage().getContentRaw();
                }
            }
        }
        //then check for keyword in message
        keyword = Keyword.findKeyword(message);
        if(keyword != null){
            event.getMessage().reply(keyword.getRandomResponse("%mention", event.getAuthor().getAsMention(),
                    "%tag", event.getAuthor().getAsTag())).mentionRepliedUser(false).queue();
            if(keyword.hasEmoji()) event.getMessage().addReaction(keyword.getEmoji()).queue();
        }
    }
}