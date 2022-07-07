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
}
