package Telegram;

import Plugin.Actions.Command;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Update {
    final static Logger logger = LoggerFactory.getLogger(Update.class);
    private int offset = 0;
    private int limit = 100;
    private int timeout = 3600;

    public class Message {
        Message(JSONObject message) {
            this.message_id = message.getInt("message_id");
            this.date = message.getInt("date");
            this.chat = new Update.Chat(message.getJSONObject("chat"));

            this.from = message.has("from") ? new Update.User(message.getJSONObject("from")) : null;
            this.forward_from = message.has("forward_from") ? new Update.User(message.getJSONObject("forward_from")) : null;
            this.forward_from_chat = message.has("forward_from_chat") ? new Update.Chat(message.getJSONObject("forward_from_chat")) : null;
            this.forward_from_message_id = message.has("forward_from_message_id") ? message.getInt("forward_from_message_id") : 0;
            this.forward_signature = message.has("forward_signature") ? message.getString("forward_signature") : null;
            this.forward_sender_name = message.has("forward_sender_name") ? message.getString("forward_sender_name") : null;
            this.forward_date = message.has("forward_date") ? message.getInt("forward_date") : 0;
            this.reply_to_message = message.has("reply_to_message") ? new Update.Message(message.getJSONObject("reply_to_message")) : null;
            this.edit_date = message.has("edit_date") ? message.getInt("edit_date") : 0;
            this.media_group_id = message.has("media_group_id") ? message.getString("media_group_id") : null;
            this.author_signature = message.has("author_signature") ? message.getString("author_signature") : null;
            this.text = message.has("text") ? message.getString("text") : null;

            if (message.has("entities")){
                JSONArray entitiesArray = message.getJSONArray("entities");
                this.entities = new ArrayList<MessageEntity>();
                for (int i = 0; i < entitiesArray.length(); i++) {
                    JSONObject entityObject = entitiesArray.getJSONObject(i);
                    this.entities.add(new MessageEntity(entityObject));
                }
            }

            if (message.has("caption_entities")){
                this.caption_entities = new ArrayList<MessageEntity>();
                JSONArray entitiesArray = message.getJSONArray("caption_entities");
                for (int i = 0; i < entitiesArray.length(); i++) {
                    JSONObject entityObject = entitiesArray.getJSONObject(i);
                    this.caption_entities.add(new MessageEntity(entityObject));
                }
            }

            // TODO 有空再写


        }

        public int message_id;
        public Update.User from;
        public int date;
        public Update.Chat chat;
        public Update.User forward_from;
        public Update.Chat forward_from_chat;
        public int forward_from_message_id;
        public String forward_signature;
        public String forward_sender_name;
        public int forward_date;
        public Update.Message reply_to_message;
        public int edit_date;
        public String media_group_id;
        public String author_signature;
        public String text;
        public ArrayList<MessageEntity> entities;
        public ArrayList<MessageEntity> caption_entities;
        // TODO entities caption_entities audio document animation game photo sticker video voice video_note contact location venue poll new_chat_members
        // TODO  new_chat_photo
        public String caption;
        public Update.User left_chat_member;
        public String new_chat_title;
    }

    public class User {
        User(JSONObject user) {
            this.id = String.valueOf(user.get("id"));
            this.is_bot = user.getBoolean("is_bot");
            this.first_name = user.getString("first_name");
            this.last_name = user.has("last_name") ? user.getString("last_name") : null;
            this.username = user.has("username") ? user.getString("username") : null;
            this.language_code = user.has("language_code") ? user.getString("language_code") : null;
        }

        public String id;
        public boolean is_bot;
        public String first_name;
        public String last_name;
        public String username;
        public String language_code;
    }

    public class Chat {
        Chat(JSONObject chat) {
            this.id = String.valueOf(chat.get("id"));
            this.type = chat.getString("type");
            this.title = chat.has("title") ? chat.getString("title") : null;
            this.username = chat.has("username") ? chat.getString("username") : null;
            this.first_name = chat.has("first_name") ? chat.getString("first_name") : null;
            this.last_name = chat.has("last_name") ? chat.getString("last_name") : null;
            this.all_members_are_administrators = chat.has("all_members_are_administrators") ? chat.getBoolean("all_members_are_administrators") : false;
            this.description = chat.has("description") ? chat.getString("description") : null;
            this.invite_link = chat.has("invite_link") ? chat.getString("invite_link") : null;
            this.pinned_message = chat.has("pinned_message") ? new Message(chat.getJSONObject("pinned_message")) : null;
            this.sticker_set_name = chat.has("sticker_set_name") ? chat.getString("sticker_set_name") : null;
            this.can_set_sticker_set = chat.has("can_set_sticker_set") ? chat.getBoolean("can_set_sticker_set") : false;
        }


        public String id;
        public String type;
        public String title;
        public String username;
        public String first_name;
        public String last_name;
        public boolean all_members_are_administrators;
        // TODO photo
        public String description;
        public String invite_link;
        public Update.Message pinned_message;
        public String sticker_set_name;
        public boolean can_set_sticker_set;
    }
    public class MessageEntity {
        MessageEntity(JSONObject entity){
            this.type = entity.getString("type");
            this.offset = entity.getInt("offset");
            this.length = entity.getInt("length");
            this.url = entity.has("url") ? entity.getString("url") : null;
            this.user = entity.has("user") ? new Update.User(entity.getJSONObject("first_name")) : null;

        }
        public String type;
        public int offset;
        public int length;
        public String url;
        public User user;
    }
    public class UpdateObject {
        public int update_id;
        public Message message;
        public Message edited_message;
        public Message channel_post;
        public Message edited_channel_post;
    }

    public Update offset(int offset) {
        this.offset = offset;
        return this;
    }

    public Update limit(int limit) {
        this.limit = limit;
        return this;
    }

    public Update timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    //    public Update allowed_updates(String allowed_updates){
//        this.allowed_updates = allowed_updates;
//        return this;
//    }
    private UpdateObject parse(JSONObject update) {
        UpdateObject ret = new UpdateObject();
        ret.message = update.has("message") ? new Message(update.getJSONObject("message")) : null;
        return ret;
    }

    private void analyzeReq(UpdateObject req){
        if (req.message != null){
            if (req.message.text != null){
                logger.info("[" + req.message.from.first_name + "] " + req.message.text);
                if (req.message.text.startsWith("/")) {
                    String text = req.message.text.substring(1);
                    String[] tmpParams = text.split(" ");
                    String[] cmdParams = tmpParams[0].split("@");

                    if (cmdParams.length > 1 && !Telegram.getUsername().equals(cmdParams[1]))
                        return;
                    ArrayList<String> params = new ArrayList<String>(Arrays.asList(tmpParams));
                    params.remove(0);

                    for(Command plugin: Router.getCommands(cmdParams[0])) {
                        plugin.actionPerformed(req, params);
                    }
                    return;
                }

                for (Plugin.Actions.Message plugin : Router.getMessages()) {
                    plugin.actionPerformed(req);
                }

            }
        }

    }
    public void start() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                HashMap<String, String> tmp = new HashMap<String, String>();
                tmp.put("offset", String.valueOf(this.offset));
                tmp.put("limit", String.valueOf(this.limit));
                tmp.put("timeout", String.valueOf(this.timeout));

                String update = Telegram.callMethod("getUpdates", tmp);
                JSONObject updateObject = new JSONObject(update);

                if (updateObject.getBoolean("ok")) {
                    JSONArray resultArray = updateObject.getJSONArray("result");
                    for (int i = 0; i < resultArray.length(); i++) {
                        JSONObject resultObject = resultArray.getJSONObject(i);
                        this.offset = resultObject.getInt("update_id") + 1;

                        new Thread(() -> {
                            try {
                                UpdateObject req = this.parse(resultObject); // 各种设置
                                this.analyzeReq(req); // 抛到 analyzeReq 分析后抛到各 Router 执行
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }).start();
                    }
                }
            } catch (Exception e) {
                logger.error(e.toString());
//                e.printStackTrace();
            }
        }
    }
}