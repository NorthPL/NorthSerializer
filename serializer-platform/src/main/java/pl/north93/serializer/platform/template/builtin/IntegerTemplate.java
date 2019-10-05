package pl.north93.serializer.platform.template.builtin;

import pl.north93.serializer.platform.template.field.FieldInfo;
import pl.north93.serializer.platform.context.DeserializationContext;
import pl.north93.serializer.platform.context.SerializationContext;
import pl.north93.serializer.platform.template.Template;

public class IntegerTemplate implements Template<Integer, SerializationContext, DeserializationContext>
{
    @Override
    public void serialise(final SerializationContext context, final FieldInfo field, final Integer object) throws Exception
    {
        context.writeInteger(field, object);
    }

    @Override
    public Integer deserialize(final DeserializationContext context, final FieldInfo field) throws Exception
    {
        return context.readInteger(field);
    }
}
