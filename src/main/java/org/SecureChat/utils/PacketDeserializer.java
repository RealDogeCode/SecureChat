package org.SecureChat.utils;

import org.SecureChat.packets.Packet;
import org.SecureChat.PacketField;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.lang.reflect.Field;

public class PacketDeserializer {
    /**
     * @param bytes bytes payload of the packet
     * @return Instance of the packet
     */
    public static <T extends Packet> T DeserializePacket(byte[] bytes, Class<T> clazz){
        ByteArrayInputStream bais = new ByteArrayInputStream((bytes));
        DataInputStream dis = new DataInputStream(bais);

        try {
            T obj = clazz.getDeclaredConstructor().newInstance();

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(PacketField.class)) {
                    field.setAccessible(true);
                    if (field.getType() == Integer.class) {
                        field.set(obj, dis.readInt());
                    } else if (field.getType() == String.class) {
                        field.set(obj, dis.readUTF());
                    }
                }
            }
            return obj;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
