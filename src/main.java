import Telegram.Telegram;
import Telegram.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.plugin2.main.server.Plugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;


public class main {

    final static Logger logger = LoggerFactory.getLogger(main.class);

    public static void main(String[] args) {

        try {
            logger.info("正在读取配置文件 prop.properties");
            Config config = new Config("prop.properties");
            logger.info("正在获取并配置代理");
            Telegram.setProxy(config.getProxy());
            logger.info("正在设置 Bot Token");
            Telegram.setToken(config.getBotToken());
            Telegram.setUsername(config.getUsername());
            logger.info("正在加载插件");
            PluginManager.register();
            logger.info("加载插件完毕");
        } catch (FileNotFoundException e){
            logger.error("无法找到配置文件，正在创建配置文件并退出程序，请重新配置后运行");
            return;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("无法正常读取 BotToken，请检查 prop.properties 文件是否正确填写");
            return;
        }
        logger.info("准备就绪");
        Update update = new Update();

        new Thread(() -> {
            try {
                update.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }
}
