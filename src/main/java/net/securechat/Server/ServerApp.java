package net.securechat.Server;

import net.securechat.Main;

import java.util.Objects;

public class ServerApp {
    public static void run(String address, int port) {
        Server server = new Server(address, port);

        server.addRoom(new Room("LOGIN", true));
        server.addRoom(new Room("GLOBAL", false));

        server.onUserConnect = (user -> {
            Main.LOGGER.info(user.toString() + " Joined The Server");
            user.sendMessage("----- Welcome to the Server -----");
            user.switchRoom(server.getRoom("LOGIN"));

            user.sendMessage("Please, Insert Your Username:");
        });

        server.onRoomSwitch = (user, room) -> {
            Main.LOGGER.info(user.toString() + " Switched Room To " + room.name);

            user.sendMessage("You are now connected to the "+ room.name +" room.");
        };

        server.onUserDisconnect = (user -> {
            Main.LOGGER.info(user.toString() + " Left The Server.");
        });

        server.onUserMessage = ((user, message) -> {
            Main.LOGGER.info(String.format("[%s] <%s> %s%n", user.currentRoom.name, user, message));

            if(user.currentRoom.isSystemRoom) {
                if(user.currentRoom.name.equals("LOGIN")){
                    user.setName(message);
                    user.switchRoom(server.getRoom("GLOBAL"));
                    server.broadcast(user + " Joined The Server", u -> !Objects.equals(u.username, user.username));
                }
                return;
            }
            server.broadcast(String.format("[%s] <%s> %s", user.currentRoom.name, user, message),
                    u -> true);
        });

        server.start();
    }
}
