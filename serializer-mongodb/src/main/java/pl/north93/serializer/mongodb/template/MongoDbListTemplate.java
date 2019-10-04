package pl.north93.serializer.mongodb.template;

import java.lang.reflect.Type;
import java.util.List;

import pl.north93.serializer.mongodb.MongoDbDeserializationContext;
import pl.north93.serializer.mongodb.MongoDbSerializationContext;
import pl.north93.serializer.platform.CustomFieldInfo;
import pl.north93.serializer.platform.FieldInfo;
import pl.north93.serializer.platform.context.DeserializationContext;
import pl.north93.serializer.platform.context.SerializationContext;
import pl.north93.serializer.platform.template.Template;
import pl.north93.serializer.platform.template.TemplateEngine;

public class MongoDbListTemplate implements Template<List<Object>, MongoDbSerializationContext, MongoDbDeserializationContext>
{
    @Override
    public void serialise(final MongoDbSerializationContext context, final FieldInfo field, final List object) throws Exception
    {
        final Type genericType = this.getGenericType(context.getTemplateEngine(), field.getType());
        final Template<Object, SerializationContext, DeserializationContext> objectSerializer = context.getTemplateEngine().getTemplate(genericType);

        final FieldInfo listFieldInfo = this.createListFieldInfo(genericType);

        context.writeStartArray(field);
        for (final Object entry : object)
        {
            objectSerializer.serialise(context, listFieldInfo, entry);
        }
        context.getWriter().writeEndArray();
    }

    @Override
    public List<Object> deserialize(final MongoDbDeserializationContext context, final FieldInfo field) throws Exception
    {
        final Type genericType = this.getGenericType(context.getTemplateEngine(), field.getType());
        final Template<Object, SerializationContext, DeserializationContext> objectSerializer = context.getTemplateEngine().getTemplate(genericType);

        final FieldInfo listFieldInfo = this.createListFieldInfo(genericType);

        context.readStartArray(field);

        final List<Object> objects = this.instantiateList(context.getTemplateEngine(), field.getType());
        while (context.hasMore())
        {
            objects.add(objectSerializer.deserialize(context, listFieldInfo));
        }

        context.readEndArray(field);

        return objects;
    }

    private FieldInfo createListFieldInfo(final Type type)
    {
        return new CustomFieldInfo(null, type);
    }

    private Type getGenericType(final TemplateEngine templateEngine, final Type type)
    {
        return templateEngine.getTypeParameters(type)[0];
    }

    @SuppressWarnings("unchecked")
    private List<Object> instantiateList(final TemplateEngine templateEngine, final Type type)
    {
        final Class<List<Object>> listClass = (Class<List<Object>>) templateEngine.getRawClassFromType(type);
        return templateEngine.instantiateClass(listClass);
    }
}
