package org.SecureChat.packets;

public class ResponsePacket extends Packet {
    @Override
    public Byte getType() {
        return 0x0;
    }

    @Override
    public ResponsePacket getResponse() {
        return null;
    }
}
