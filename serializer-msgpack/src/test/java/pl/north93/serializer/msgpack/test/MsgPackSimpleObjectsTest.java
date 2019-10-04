package pl.north93.serializer.msgpack.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;


import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import pl.north93.serializer.msgpack.MsgPackSerializationFormat;
import pl.north93.serializer.platform.NorthSerializer;
import pl.north93.serializer.platform.impl.NorthSerializerImpl;

public class MsgPackSimpleObjectsTest
{
    private final NorthSerializer<byte[]> serializer = new NorthSerializerImpl<>(new MsgPackSerializationFormat());

    @ToString
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimpleObject
    {
        String string;
        Integer integer;
        Double aDouble;
    }

    @Test
    public void testSimpleObjectSerialization()
    {
        final SimpleObject beforeSerialization = new SimpleObject("testString", 10, 5.0D);

        final byte[] bytes = this.serializer.serialize(SimpleObject.class, beforeSerialization);
        final Object deserialized = this.serializer.deserialize(SimpleObject.class, bytes);

        assertSame(SimpleObject.class, deserialized.getClass());
        assertEquals(beforeSerialization, deserialized);
    }

    @Test
    public void testSimpleObjectDynamicSerialization()
    {
        final SimpleObject beforeSerialization = new SimpleObject("testString", 10, 5.0D);

        final byte[] bytes = this.serializer.serialize(Object.class, beforeSerialization);
        final Object deserialized = this.serializer.deserialize(Object.class, bytes);

        assertSame(SimpleObject.class, deserialized.getClass());
        assertEquals(beforeSerialization, deserialized);
    }
}
