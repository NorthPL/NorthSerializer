package pl.north93.serializer.platform.template.builtin;

import pl.north93.serializer.platform.template.field.FieldInfo;
import pl.north93.serializer.platform.context.DeserializationContext;
import pl.north93.serializer.platform.context.SerializationContext;
import pl.north93.serializer.platform.template.Template;

public class FloatTemplate implements Template<Float, SerializationContext, DeserializationContext>
{
    @Override
    public void serialise(final SerializationContext context, final FieldInfo field, final Float object) throws Exception
    {
        context.writeFloat(field, object);
    }

    @Override
    public Float deserialize(final DeserializationContext context, final FieldInfo field) throws Exception
    {
        return context.readFloat(field);
    }
}
