package Plugin.Actions;

import Telegram.Update;

import java.util.ArrayList;

public interface Message {
    void actionPerformed(Update.UpdateObject req);
}
