package net.securechat.Server;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Room {
    UUID uuid;
    public String name;
    String description;
    Set<User> operators;
    public Boolean isSystemRoom;

    public Room(String name, boolean isSystemRoom) {
        uuid = UUID.randomUUID();
        operators = new HashSet<>();
        this.name = name;
        description = "This is a sample room description";
        this.isSystemRoom = isSystemRoom;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addOperator(User operator) {
        operators.add(operator);
    }
}
