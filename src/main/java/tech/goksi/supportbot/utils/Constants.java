package tech.goksi.supportbot.utils;



import tech.goksi.supportbot.Bot;

public class Constants {

    public static final String TOKEN = Bot.getInstance().getConfig().getString("BotInfo.Token");
    public static final String OWNER_ID = Bot.getInstance().getConfig().getString("BotInfo.OwnerID");
    public static final String GUILD_ID = Bot.getInstance().getConfig().getString("BotInfo.ServerID");
    public static final String TICKET_CATEGORY_ID = Bot.getInstance().getConfig().getString("Settings.TicketsCategory");
    public static final String SUPPORT_ROLE_ID = Bot.getInstance().getConfig().getString("Settings.SupportRole");
    public static final String STAFF_CHANNEL = Bot.getInstance().getConfig().getString("Settings.StaffChannel");
    public static final Long MAX_ATTACHMENT_SIZE = Bot.getInstance().getConfig().getLong("Settings.MaxSizeAttachments");
    public static final String OCR_LANGUAGE = Bot.getInstance().getConfig().getString("Settings.OCRLanguage");
    public static final String COMMAND_PREFIX = Bot.getInstance().getConfig().getString("Settings.CommandsPrefix");
    public static final String TESSERACT_ENG = "https://github.com/tesseract-ocr/tessdata/raw/main/eng.traineddata";
}
