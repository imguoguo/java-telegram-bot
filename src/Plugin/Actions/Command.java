package Plugin.Actions;

import Telegram.Update;

import java.util.ArrayList;

public interface Command {
    void actionPerformed(Update.UpdateObject req, ArrayList<String> params);
}
