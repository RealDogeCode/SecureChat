package org.SecureChat;

public class MessagePacket extends Packet{
    @PacketField
    String message;

    @Override
    public Byte getType() {
        return 0x1;
    }

    @Override
    public ResponsePacket getResponse() {
        return null;
    }
}
