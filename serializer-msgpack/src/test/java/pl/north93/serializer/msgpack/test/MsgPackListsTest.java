package pl.north93.serializer.msgpack.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import pl.north93.serializer.msgpack.MsgPackSerializationFormat;
import pl.north93.serializer.platform.NorthSerializer;
import pl.north93.serializer.platform.template.impl.NorthSerializerImpl;

public class MsgPackListsTest
{
    private final NorthSerializer<byte[]> serializer = new NorthSerializerImpl<>(new MsgPackSerializationFormat());

    @Test
    public void emptyArrayListTest()
    {
        final byte[] bytes = this.serializer.serialize(ArrayList.class, new ArrayList<>());
        final Object deserialized = this.serializer.deserialize(ArrayList.class, bytes);

        assertSame(ArrayList.class, deserialized.getClass());

        final List deserializedList = (List) deserialized;
        assertEquals(0, deserializedList.size());
    }

    @Test
    public void arrayListWithStrings()
    {
        final ArrayList<String> strings = new ArrayList<>(Arrays.asList("test1", "test2", "test3"));

        final byte[] bytes = this.serializer.serialize(ArrayList.class, strings);
        final Object deserialized = this.serializer.deserialize(ArrayList.class, bytes);

        assertSame(ArrayList.class, deserialized.getClass());

        final List deserializedList = (List) deserialized;
        assertEquals(strings, deserializedList);
    }

    @Test
    public void arrayListWithManyTypes()
    {
        final ArrayList<Object> strings = new ArrayList<>(Arrays.asList(100, "test", true));

        final byte[] bytes = this.serializer.serialize(ArrayList.class, strings);
        final Object deserialized = this.serializer.deserialize(ArrayList.class, bytes);

        assertSame(ArrayList.class, deserialized.getClass());

        final List deserializedList = (List) deserialized;
        assertEquals(strings, deserializedList);
    }

    @Test
    public void nestedArrayLists()
    {
        final ArrayList<String> nestedList = new ArrayList<>(Collections.singletonList("test"));
        final ArrayList<ArrayList<String>> outerList = new ArrayList<>(Collections.singletonList(nestedList));

        final byte[] bytes = this.serializer.serialize(ArrayList.class, outerList);
        final Object deserialized = this.serializer.deserialize(ArrayList.class, bytes);

        assertSame(ArrayList.class, deserialized.getClass());

        final List deserializedList = (List) deserialized;
        assertEquals(outerList, deserializedList);
    }
}
