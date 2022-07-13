package tech.goksi.supportbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.goksi.supportbot.config.Config;
import tech.goksi.supportbot.utils.Constants;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class Main {
    public static void main(String[] args)  {
        Logger logger = LoggerFactory.getLogger(Main.class.getName());
        Config config = new Config();
        config.initConfig();
        File file = new File("tessdata/eng.traineddata");
        if(!file.exists()){
            URL url;
            HttpURLConnection httpURLConnection;
            ExecutorService executorService =  Executors.newSingleThreadExecutor();
            try{
                url = new URL(Constants.TESSERACT_ENG);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                if(!file.getParentFile().exists()){
                    File parentFile = file.getParentFile();
                    if(!parentFile.mkdir()){
                        logger.warn("Error while creating directory error while creating tessdata directory !");
                    }
                }
            }catch (Exception e){
                logger.error("Error while opening connection to tesseract-ocr github !", e);
                return;
            }
            AtomicLong downloaded = new AtomicLong();
            long total = httpURLConnection.getContentLength();

            Future<?> task = executorService.submit(() -> {
                try(BufferedInputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                    OutputStream outputStream = Files.newOutputStream(file.toPath())){
                    byte[] buffer = new byte[1024];
                    int length;
                    while((length = inputStream.read(buffer)) != -1){
                        outputStream.write(buffer, 0, length);
                        downloaded.addAndGet(length);
                    }
                }catch (Exception e){
                    logger.error("Error while downloading OCR data, please remove tessdata folder and try again !", e);
                }
            });
            while(!task.isDone()){
                logger.info("Downloading OCR data... {}%", (int) ((downloaded.get() * 100 )/ total ));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    logger.error("Interrupted while thread was on sleep !", e);
                }
            }
            executorService.shutdown();
        }
        Bot bot = new Bot(config);
        bot.init();
    }
}
