package net.securechat.Server.events;

import net.securechat.Server.User;

public interface onUserMessage {
    void OnUserMessage(User user, String message);
}
