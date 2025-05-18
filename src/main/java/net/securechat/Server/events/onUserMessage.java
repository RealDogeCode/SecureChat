package net.securechat.Server.events;

import net.securechat.Server.User;

import java.io.IOException;

public interface onUserMessage {
    void OnUserMessage(User user, String message) throws IOException;
}
