package tech.goksi.supportbot.tickets;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import tech.goksi.supportbot.Bot;

public class TicketCommand extends SlashCommand {
    public TicketCommand(){
        this.name = "ticket";
        this.help = "Opens gui for requesting staff support!";
    }
    @Override
    protected void execute(SlashCommandEvent event) {
        TextInput title = TextInput.create("title", Bot.getInstance().getConfig().getString("Tickets.IssueTitle"), TextInputStyle.SHORT)
                .setPlaceholder(Bot.getInstance().getConfig().getString("Tickets.ModalTitlePlaceholder")).setMinLength(3).setMaxLength(35).build();
        TextInput desc = TextInput.create("desc", Bot.getInstance().getConfig().getString("Tickets.IssueDescription"), TextInputStyle.PARAGRAPH).setMaxLength(1500)
                .setPlaceholder(Bot.getInstance().getConfig().getString("Tickets.ModalDescriptionPlaceholder")).build();
        Modal modal  = Modal.create("support", Bot.getInstance().getConfig().getString("Tickets.ModalTitle"))
                .addActionRows(ActionRow.of(title), ActionRow.of(desc)).build();
        event.replyModal(modal).queue();
    }

}
