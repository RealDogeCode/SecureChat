package org.SecureChat.packets;

public class CommandPacket extends Packet {

    @Override
    public Byte getType() {
        return 0x3;
    }

    @Override
    public ResponsePacket getResponse() {
        return null;
    }
}
