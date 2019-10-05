package pl.north93.serializer.platform.template.builtin;

import pl.north93.serializer.platform.template.field.FieldInfo;
import pl.north93.serializer.platform.context.DeserializationContext;
import pl.north93.serializer.platform.context.SerializationContext;
import pl.north93.serializer.platform.template.Template;

public class LongTemplate implements Template<Long, SerializationContext, DeserializationContext>
{
    @Override
    public void serialise(final SerializationContext context, final FieldInfo field, final Long object) throws Exception
    {
        context.writeLong(field, object);
    }

    @Override
    public Long deserialize(final DeserializationContext context, final FieldInfo field) throws Exception
    {
        return context.readLong(field);
    }
}
