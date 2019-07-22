package Plugin;

import Telegram.Message;
import Telegram.Router;
import Telegram.Update;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class HelloWorld {
    final static Logger logger = LoggerFactory.getLogger(HelloWorld.class);
    public static void register(){
        logger.info("正在注册插件 HelloWorld");
        //lambda
        Router.command("test", (Update.UpdateObject req, ArrayList<String> params) -> {
            Message msg = new Message();
            msg.text(req.message.text).chat_id(req.message.chat.id).send();
        });

        Router.message((Update.UpdateObject req) -> {
            if (req.message.text.equals("喵")){
                Message msg = new Message();
                msg.text("喵呜！").chat_id(req.message.chat.id).send();
            }
        });
    }
}
