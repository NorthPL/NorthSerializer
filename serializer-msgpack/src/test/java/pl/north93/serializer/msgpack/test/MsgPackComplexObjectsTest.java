package pl.north93.serializer.msgpack.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.north93.serializer.msgpack.MsgPackSerializationFormat;
import pl.north93.serializer.platform.NorthSerializer;
import pl.north93.serializer.platform.template.impl.NorthSerializerImpl;

public class MsgPackComplexObjectsTest
{
    private final NorthSerializer<byte[], byte[]> serializer = new NorthSerializerImpl<>(new MsgPackSerializationFormat());

    @Data
    @AllArgsConstructor
    public static class ComplexObject
    {
        Object object;
        List<Object> objects;
        List<String> strings;
    }

    @Test
    public void testComplexObjectSerialization()
    {
        final ArrayList<Object> objects = new ArrayList<>(Arrays.asList(5, false));
        final ArrayList<String> strings = new ArrayList<>(Arrays.asList("test1", "test2"));
        final ComplexObject beforeSerialization = new ComplexObject("test", objects, strings);

        final byte[] bytes = this.serializer.serialize(ComplexObject.class, beforeSerialization);
        final Object deserialized = this.serializer.deserialize(ComplexObject.class, bytes);

        assertSame(ComplexObject.class, deserialized.getClass());
        assertEquals(beforeSerialization, deserialized);
    }
}
