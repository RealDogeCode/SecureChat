package org.SecureChat;

public class BroadcastPacket extends Packet{
    @Override
    public Byte getType() {
        return 0x2;
    }

    @Override
    public ResponsePacket getResponse() {
        return null;
    }
}
