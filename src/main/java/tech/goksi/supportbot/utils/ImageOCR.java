package tech.goksi.supportbot.utils;

import net.dv8tion.jda.api.entities.Message;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.function.Consumer;

public class ImageOCR {
    private final Message.Attachment attachment;
    private final Logger logger;
    public ImageOCR(Message.Attachment attachment){
        this.attachment = attachment;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public void getText(final Consumer<String> consumer){
        ITesseract instance = new Tesseract();
        instance.setDatapath("tessdata/");
        instance.setLanguage(Constants.OCR_LANGUAGE);

        attachment.retrieveInputStream().thenAcceptAsync(is -> {
            try {
                consumer.accept(instance.doOCR(ImageIO.read(is)));
            } catch (IOException e) {
                logger.error("Error while reading attachment input stream", e);
            } catch (TesseractException e){
                logger.error("Error while performing OCR", e);
            }
        });
    }
}
