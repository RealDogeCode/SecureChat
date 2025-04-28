package org.SecureChat;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;

public class PacketSerializer {
    /**
     * @param packet The packet to serialize
     * @return The Byte[] that represent the content of the Packet
     */
    public static <T extends Packet> byte[] Serialize(T packet) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        for(Field field : packet.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if(field.isAnnotationPresent(PacketField.class)) {
                try {
                    bos.write(field.getByte(packet));
                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return bos.toByteArray();
    }
}
