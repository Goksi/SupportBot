package tech.goksi.supportbot.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.goksi.supportbot.entities.Keyword;
import tech.goksi.supportbot.utils.CommonUtil;
import tech.goksi.supportbot.utils.Constants;
import tech.goksi.supportbot.utils.ImageOCR;

import java.util.Objects;

import java.util.regex.Pattern;

public class KeywordsListener extends ListenerAdapter {
    private final Logger logger;
    public KeywordsListener(){
        logger = LoggerFactory.getLogger(this.getClass());
    }
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getAuthor().isBot() || event.getMessage().getContentRaw().startsWith(Constants.COMMAND_PREFIX)) return;
        //first check for link in message
        String message = event.getMessage().getContentRaw();
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
        boolean sentResponse = answer(message, event);
        if(sentResponse) return;
        if(event.getMessage().getAttachments().size() > 0){
            Message.Attachment attachment = event.getMessage().getAttachments().get(0);
            if(Objects.equals(attachment.getFileExtension(), "txt")){
                CommonUtil.readAttachment(attachment, content -> answer(content, event));
            }else if(attachment.isImage()){
                ImageOCR ocr = new ImageOCR(attachment);
                ocr.getText(content -> answer(content, event));
            }
        }



    }

    private boolean answer(String message, MessageReceivedEvent event){
        if(message.isEmpty()) return false;
        Keyword keyword = Keyword.find(message);
        if(keyword != null){
            if(keyword.getChannels() != null && !keyword.getChannels().isEmpty() && !keyword.getChannels().contains(event.getChannel().getId())) return false;
            String response = keyword.getRandomResponse("%mention", event.getAuthor().getAsMention(),
                    "%tag", event.getAuthor().getAsTag());
            if(keyword.shouldReplay()) event.getMessage().reply(response).mentionRepliedUser(false).queue();
            else event.getMessage().getTextChannel().sendMessage(response).queue();
            if(keyword.hasEmoji()) event.getMessage().addReaction(Emoji.fromUnicode(keyword.getEmoji())).queue();
            return true;
        }
        return false;
    }
}
