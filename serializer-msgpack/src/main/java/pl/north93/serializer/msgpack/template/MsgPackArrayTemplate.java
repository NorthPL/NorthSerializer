package pl.north93.serializer.msgpack.template;

import java.lang.reflect.Array;
import java.lang.reflect.Type;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessageUnpacker;

import pl.north93.serializer.msgpack.MsgPackDeserializationContext;
import pl.north93.serializer.msgpack.MsgPackSerializationContext;
import pl.north93.serializer.platform.template.Template;
import pl.north93.serializer.platform.template.TemplateEngine;
import pl.north93.serializer.platform.template.TemplatePriority;
import pl.north93.serializer.platform.template.field.CustomFieldInfo;
import pl.north93.serializer.platform.template.field.FieldInfo;
import pl.north93.serializer.platform.template.filter.TemplateFilter;

public class MsgPackArrayTemplate implements Template<Object, MsgPackSerializationContext, MsgPackDeserializationContext>
{
    public static final class ArrayTemplateFilter implements TemplateFilter
    {
        @Override
        public int getPriority()
        {
            return TemplatePriority.NORMAL;
        }

        @Override
        public boolean isApplicableTo(final TemplateEngine templateEngine, final Type type)
        {
            final Class<?> clazz = templateEngine.getRawClassFromType(type);
            return clazz.isArray();
        }
    }

    @Override
    public void serialise(final MsgPackSerializationContext context, final FieldInfo field, final Object object) throws Exception
    {
        final MessageBufferPacker packer = context.getPacker();

        final Type elementType = this.getArrayElementType(context.getTemplateEngine(), field.getType());
        final var template = context.getTemplateEngine().getTemplate(elementType);

        final int length = Array.getLength(object);
        packer.packArrayHeader(length);
        for (int i = 0; i < length; i++)
        {
            final CustomFieldInfo arrayField = new CustomFieldInfo(null, elementType);
            template.serialise(context, arrayField, Array.get(object, i));
        }
    }

    @Override
    public Object deserialize(final MsgPackDeserializationContext context, final FieldInfo field) throws Exception
    {
        final MessageUnpacker unPacker = context.getUnPacker();

        final Class<?> elementType = this.getArrayElementType(context.getTemplateEngine(), field.getType());
        final var template = context.getTemplateEngine().getTemplate(elementType);

        final int arraySize = unPacker.unpackArrayHeader();

        final Object array = Array.newInstance(elementType, arraySize);
        for (int i = 0; i < arraySize; i++)
        {
            final CustomFieldInfo arrayField = new CustomFieldInfo(null, elementType);
            final Object element = template.deserialize(context, arrayField);

            Array.set(array, i, element);
        }

        return array;
    }

    private Class<?> getArrayElementType(final TemplateEngine templateEngine, final Type type)
    {
        final Class<?> arrayType = templateEngine.getRawClassFromType(type);
        return arrayType.getComponentType();
    }
}
