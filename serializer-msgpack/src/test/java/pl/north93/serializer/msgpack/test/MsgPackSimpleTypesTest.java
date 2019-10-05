package pl.north93.serializer.msgpack.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;


import org.junit.jupiter.api.Test;

import pl.north93.serializer.msgpack.MsgPackSerializationFormat;
import pl.north93.serializer.platform.NorthSerializer;
import pl.north93.serializer.platform.template.impl.NorthSerializerImpl;

// serializacja prostych wartosci
public class MsgPackSimpleTypesTest
{
    private final NorthSerializer<byte[]> serializer = new NorthSerializerImpl<>(new MsgPackSerializationFormat());

    @Test
    public void stringTest()
    {
        final byte[] bytes = this.serializer.serialize(String.class, "test");
        final Object deserialized = this.serializer.deserialize(String.class, bytes);

        assertSame(String.class, deserialized.getClass());
        assertEquals("test", deserialized);
    }

    @Test
    public void booleanTest()
    {
        final byte[] bytes = this.serializer.serialize(Boolean.class, true);
        final Object deserialized = this.serializer.deserialize(Boolean.class, bytes);

        assertSame(Boolean.class, deserialized.getClass());
        assertEquals(true, deserialized);
    }

    @Test
    public void shortTest()
    {
        final short test = 42;

        final byte[] bytes = this.serializer.serialize(Short.class, test);
        final Object deserialized = this.serializer.deserialize(Short.class, bytes);

        assertSame(Short.class, deserialized.getClass());
        assertEquals(test, deserialized);
    }

    @Test
    public void integerTest()
    {
        final byte[] bytes = this.serializer.serialize(Integer.class, 42);
        final Object deserialized = this.serializer.deserialize(Integer.class, bytes);

        assertSame(Integer.class, deserialized.getClass());
        assertEquals(42, deserialized);
    }

    @Test
    public void floatTest()
    {
        final float PI = 3.14F;

        final byte[] bytes = this.serializer.serialize(Float.class, PI);
        final Object deserialized = this.serializer.deserialize(Float.class, bytes);

        assertSame(Float.class, deserialized.getClass());
        assertEquals(PI, deserialized);
    }

    @Test
    public void doubleTest()
    {
        final double PI = 3.14D;

        final byte[] bytes = this.serializer.serialize(Double.class, PI);
        final Object deserialized = this.serializer.deserialize(Double.class, bytes);

        assertSame(Double.class, deserialized.getClass());
        assertEquals(PI, deserialized);
    }

    @Test
    public void longTest()
    {
        final byte[] bytes = this.serializer.serialize(Long.class, 42L);
        final Object deserialized = this.serializer.deserialize(Long.class, bytes);

        assertSame(Long.class, deserialized.getClass());
        assertEquals(42L, deserialized);
    }
}
