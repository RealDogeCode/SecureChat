package net.securechat.Server;

import net.securechat.Server.events.onRoomSwitch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class User {
    public onRoomSwitch onRoomSwitch;

    Socket socket;
    Thread thread;
    PrintWriter out;
    BufferedReader in;

    UUID uuid;
    String username;
    public Room currentRoom;

    boolean isMuted;

    public User(Socket socket, PrintWriter out, BufferedReader in) {
        this.uuid = UUID.randomUUID();
        this.out = out;
        this.socket = socket;
    }

    public void setName(String username) {
        this.username = username;
    }

    public void switchRoom(Room room) {
        onRoomSwitch.OnRoomSwitch(this, room);
        this.currentRoom = room;
    }

    public void setMuted(boolean muted) {
        isMuted = muted;
    }
    public void disconnect() throws IOException {
        thread.interrupt();
        socket.close();
    }

    public void sendMessage(String message) {
        try{
            out.println(message);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return this.username != null ? this.username :  uuid.toString();
    }
}
