package Telegram;

import Plugin.Actions.Command;
import Plugin.Actions.Message;

import java.util.ArrayList;
import java.util.HashMap;

public class Router {
    static HashMap<String, ArrayList<Command>> commands = new HashMap<String, ArrayList<Command>>();
    static ArrayList<Message> messages = new ArrayList<Message>();
    static public void command(String cmd, Command plugin){
        if(!Router.commands.containsKey(cmd)) {
            Router.commands.put(cmd, new ArrayList<Command>());
        }
        Router.commands.get(cmd).add(plugin);
    }

    static public void message(Message plugin){
        Router.messages.add(plugin);
    }

    static public ArrayList<Command> getCommands(String cmd) {
        if(!Router.commands.containsKey(cmd)) {
            return new ArrayList<Command>();
        }
        return Router.commands.get(cmd);
    }

    static public ArrayList<Message> getMessages() {
        return messages;
    }
}
