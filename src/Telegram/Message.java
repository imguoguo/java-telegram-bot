package Telegram;


import org.json.JSONObject;

import java.util.HashMap;

public class Message {

    // 发送消息部分
    private String chat_id;
    private String text;
    private String parse_mode = "HTML";
    private boolean disable_web_page_preview = false;
    private boolean disable_notification = false;
    private int reply_to_message_id = 0;

    // 转发消息部分
    private String from_chat_id;
    private int message_id;

    // 编辑消息部分
    private String inline_message_id = "";

    public Message text(String text){
        this.text = text;
        return this;
    }

    public Message chat_id(String chat_id){
        this.chat_id = chat_id;
        return this;
    }

    public Message parse_mode(String parse_mode) {
        this.parse_mode = parse_mode;
        return this;
    }

    public Message disable_web_page_preview() {
        this.disable_web_page_preview = true;
        return this;
    }

    public Message disable_notification() {
        this.disable_notification = true;
        return this;
    }

    public Message reply_to_message_id(int reply_to_message_id) {
        this.reply_to_message_id = reply_to_message_id;
        return this;
    }

    // 此处为消息转发部分
    public Message from_chat_id(String from_chat_id){
        this.from_chat_id = from_chat_id;
        return this;
    }

    public Message message_id(int message_id){
        this.message_id = message_id;
        return this;
    }
    public int send(){
        HashMap<String, String> tmp = new HashMap<String, String>();
        tmp.put("text", text);
        tmp.put("chat_id", chat_id);
        tmp.put("parse_mode", parse_mode);
        tmp.put("disable_web_page_preview", String.valueOf(disable_web_page_preview));
        tmp.put("disable_notification", String.valueOf(disable_notification));
        tmp.put("reply_to_message_id", String.valueOf(reply_to_message_id));
        String ret = Telegram.callMethod("sendMessage", tmp);
        JSONObject jsonRet = new JSONObject(ret);
        this.message_id = jsonRet.getJSONObject("result").getInt("message_id");
        Telegram.logger.info("已将内容为 {} 的消息投递至 {}，返回的消息 id 为 {}", text, chat_id, message_id);
        return message_id;
    }

    public int edit(){
        HashMap<String, String> tmp = new HashMap<String, String>();
        tmp.put("chat_id", chat_id);
        tmp.put("message_id", String.valueOf(message_id));
        tmp.put("inline_message_id", inline_message_id);
        tmp.put("text", text);
        tmp.put("parse_mode", parse_mode);
        tmp.put("disable_web_page_preview", String.valueOf(disable_notification));
        String ret = Telegram.callMethod("editMessageText", tmp);
        JSONObject jsonRet = new JSONObject(ret);
        int message_id = jsonRet.getJSONObject("result").getInt("message_id");
        Telegram.logger.info("已将在群组 {}， 消息 id 为 {} 的消息编辑为 {}", chat_id, message_id, text);
        return message_id;
    }
    public int forward(){
        HashMap<String, String> tmp = new HashMap<String, String>();
        tmp.put("chat_id", chat_id);
        tmp.put("from_chat_id", String.valueOf(from_chat_id));
        tmp.put("disable_notification", String.valueOf(disable_notification));
        tmp.put("message_id", String.valueOf(message_id));
        String ret = Telegram.callMethod("forwardMessage", tmp);
        JSONObject jsonRet = new JSONObject(ret);
        int message_id = jsonRet.getJSONObject("result").getInt("message_id");
        Telegram.logger.info("已将 id 为 {} 的消息从 {} 转发到 {}。", message_id, from_chat_id, chat_id);
        return message_id;
    }




}
