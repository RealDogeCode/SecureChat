package net.securechat.Server.events;

import net.securechat.Server.Room;
import net.securechat.Server.User;

public interface onRoomSwitch {
    void OnRoomSwitch(User user, Room room);
}
