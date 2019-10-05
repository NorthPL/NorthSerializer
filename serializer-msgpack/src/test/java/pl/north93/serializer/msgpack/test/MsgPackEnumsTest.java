package pl.north93.serializer.msgpack.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;


import org.junit.jupiter.api.Test;

import pl.north93.serializer.msgpack.MsgPackSerializationFormat;
import pl.north93.serializer.platform.NorthSerializer;
import pl.north93.serializer.platform.template.impl.NorthSerializerImpl;

public class MsgPackEnumsTest
{
    private final NorthSerializer<byte[]> serializer = new NorthSerializerImpl<>(new MsgPackSerializationFormat());

    public enum TestEnum
    {
        TEST_1,
        TEST_2,
        TEST_3,
        TEST_4,
        TEST_5
    }

    public enum TestExtendedEnum
    {
        TEST_1
                {
                    @Override
                    void test()
                    {
                    }
                };

        abstract void test();
    }

    @Test
    public void serializeSimpleEnumValue()
    {
        final byte[] bytes = this.serializer.serialize(TestEnum.class, TestEnum.TEST_2);
        final Object deserialized = this.serializer.deserialize(TestEnum.class, bytes);

        assertSame(TestEnum.class, deserialized.getClass());
        assertEquals(TestEnum.TEST_2, deserialized);
    }

    @Test
    public void serializeDynamicEnumValue()
    {
        final byte[] bytes = this.serializer.serialize(Object.class, TestEnum.TEST_2);
        final Object deserialized = this.serializer.deserialize(Object.class, bytes);

        assertSame(TestEnum.class, deserialized.getClass());
        assertEquals(TestEnum.TEST_2, deserialized);
    }

    @Test
    public void serializeSimpleExtendedEnumValue()
    {
        final byte[] bytes = this.serializer.serialize(TestExtendedEnum.class, TestExtendedEnum.TEST_1);
        final Object deserialized = this.serializer.deserialize(TestExtendedEnum.class, bytes);

        assertEquals(TestExtendedEnum.TEST_1, deserialized);
    }

    @Test
    public void serializeDynamicExtendedEnumValue()
    {
        final byte[] bytes = this.serializer.serialize(Object.class, TestExtendedEnum.TEST_1);
        final Object deserialized = this.serializer.deserialize(Object.class, bytes);

        assertEquals(TestExtendedEnum.TEST_1, deserialized);
    }
}
