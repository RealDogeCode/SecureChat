package net.securechat.Server;

import net.securechat.Server.events.onRoomSwitch;
import net.securechat.Server.events.onUserConnect;
import net.securechat.Server.events.onUserDisconnect;
import net.securechat.Server.events.onUserMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.function.Predicate;

public class Server {
    public ArrayList<User> users = new ArrayList<>();
    ArrayList<Room> rooms = new ArrayList<>();

    public onUserMessage onUserMessage;
    public onUserConnect onUserConnect;
    public onUserDisconnect onUserDisconnect;
    public onRoomSwitch onRoomSwitch;

    Boolean running = true;
    private final String host;
    private final int port;

    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start(){
        try(ServerSocket serverSocket = new ServerSocket(this.port, 5, InetAddress.getByName(this.host)))
        {
            while(running) {
                Socket clientSocket = serverSocket.accept();

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                UserThread userThread = new UserThread(this, clientSocket, in, out);
                userThread.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(String message, Predicate<User> userPredicate) {
        for(User user : users) {
            if(userPredicate.test(user)) {
                user.sendMessage(message);
            }
        }
    }

    public void addRoom(Room room){
        rooms.add(room);
    }
    public Room getRoom(String roomName){
        return this.rooms.stream().filter(room -> room.name.equals(roomName)).findFirst().get();
    }
}

class UserThread extends Thread {
    private final BufferedReader in;
    private final Socket socket;
    private final User user;

    private final Server server;

    public UserThread(Server server, Socket socket, BufferedReader in, PrintWriter out){
        this.in = in;
        this.socket = socket;
        this.user = new User(socket, out, in);
        this.user.onRoomSwitch = server.onRoomSwitch;
        this.server = server;

        server.users.add(user);
        this.server.onUserConnect.OnUserConnect(user);
    }

    @Override
    public void run() {
        while(true){
            try {
                String message;

                if(this.socket.isClosed()){
                    this.interrupt();
                }
                if ((message = in.readLine()) != null) {
                    this.server.onUserMessage.OnUserMessage(user, message);
                }
            } catch (IOException e) {
                if(e.getClass() == SocketException.class){
                    this.server.users.remove(user);
                    this.server.onUserDisconnect.OnUserDisconnect(user);
                    break;
                }
            }
        }
        this.interrupt();
    }
}

