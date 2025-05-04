package org.SecureChat;

public abstract class Packet {
    /**
     * @return The first byte sent, which indicates how to handle
     *         the specific type of package on the remote side.
     */
    public abstract Byte getType();
    /**
     * @param packet The packet to serialize
     * @return The Byte[] that represent the content of the Packet
     */
    public <T extends Packet> byte[] toBytes(T packet){
        return PacketSerializer.Serialize(packet);
    }

    /**
     * @param bytes bytes payload of the packet
     * @return Instance of the packet
     */
    public <T extends Packet> T fromBytes(byte[] bytes){
        //noinspection unchecked
        return (T) PacketDeserializer.DeserializePacket(bytes, this.getClass());
    }

    /// @return Returns {@code True} if the Packet as the correct format, {@code False} otherwise.
    public boolean validate() {
        return true; // Placeholder
    }
    /**
     * Retrieves a {@link ResponsePacket} object.
     * @return a {@code ResponsePacket}, or {@code null} if no response is available.
     */
    public abstract ResponsePacket getResponse();
}
