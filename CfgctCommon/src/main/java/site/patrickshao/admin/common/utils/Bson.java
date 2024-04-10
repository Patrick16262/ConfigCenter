package site.patrickshao.admin.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.io.IOException;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/4
 */
public final class Bson {
    private static final ObjectMapper mapper = new ObjectMapper(new MessagePackFactory());
    public static  <T> byte[] serialize(T object) {
        try {
            return mapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserialize(byte[] bytes, Class<T> clazz) {
        try {
            return mapper.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
