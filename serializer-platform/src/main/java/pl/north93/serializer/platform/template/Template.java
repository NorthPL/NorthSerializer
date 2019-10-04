package pl.north93.serializer.platform.template;

import pl.north93.serializer.platform.FieldInfo;
import pl.north93.serializer.platform.context.DeserializationContext;
import pl.north93.serializer.platform.context.SerializationContext;

public interface Template<T, SERIALIZATION_CONTEXT extends SerializationContext, DESERIALIZATION_CONTEXT extends DeserializationContext>
{
    void serialise(SERIALIZATION_CONTEXT context, FieldInfo field, T object) throws Exception;

    T deserialize(DESERIALIZATION_CONTEXT context, FieldInfo field) throws Exception;
}
