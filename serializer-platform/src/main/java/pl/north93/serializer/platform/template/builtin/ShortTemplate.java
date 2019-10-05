package pl.north93.serializer.platform.template.builtin;

import pl.north93.serializer.platform.template.field.FieldInfo;
import pl.north93.serializer.platform.context.DeserializationContext;
import pl.north93.serializer.platform.context.SerializationContext;
import pl.north93.serializer.platform.template.Template;

public class ShortTemplate implements Template<Short, SerializationContext, DeserializationContext>
{
    @Override
    public void serialise(final SerializationContext context, final FieldInfo field, final Short object) throws Exception
    {
        context.writeShort(field, object);
    }

    @Override
    public Short deserialize(final DeserializationContext context, final FieldInfo field) throws Exception
    {
        return context.readShort(field);
    }
}
