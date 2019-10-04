package pl.north93.serializer.platform.impl;

import java.lang.reflect.Type;

import pl.north93.serializer.platform.NorthSerializer;
import pl.north93.serializer.platform.SerializationFormat;
import pl.north93.serializer.platform.context.DeserializationContext;
import pl.north93.serializer.platform.context.SerializationContext;
import pl.north93.serializer.platform.template.Template;
import pl.north93.serializer.platform.template.TemplateEngine;
import pl.north93.serializer.platform.ClassResolver;
import pl.north93.serializer.platform.CustomFieldInfo;
import pl.north93.serializer.platform.FieldInfo;

public class NorthSerializerImpl<OUTPUT> implements NorthSerializer<OUTPUT>
{
    private final TemplateEngine                    templateEngine;
    private final SerializationFormat<OUTPUT, ?, ?> serializationFormat;

    public NorthSerializerImpl(final SerializationFormat<OUTPUT, ?, ?> serializationFormat, final ClassResolver classResolver)
    {
        this.templateEngine = new TemplateEngineImpl(classResolver, serializationFormat.getTypePredictor());
        this.serializationFormat = serializationFormat;
        this.serializationFormat.configure(this.templateEngine);
    }

    public NorthSerializerImpl(final SerializationFormat<OUTPUT, ?, ?> serializationFormat)
    {
        this(serializationFormat, new DefaultClassResolver());
    }

    @SuppressWarnings("unchecked")
    @Override
    public OUTPUT serialize(final Type type, final Object object)
    {
        final SerializationContext context = this.serializationFormat.createSerializationContext(this.templateEngine);
        final Template<Object, SerializationContext, DeserializationContext> template = this.templateEngine.getTemplate(type);

        try
        {
            template.serialise(context, this.createRootField(type), object);
            return (OUTPUT) context.finalizeAndGetResult();
        }
        catch (final Exception e)
        {
            throw new RuntimeException("Exception thrown when serializing " + object, e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T deserialize(final Type type, final OUTPUT serialized)
    {
        final DeserializationContext context = this.serializationFormat.createDeserializationContext(this.templateEngine, serialized);
        final Template<?, SerializationContext, DeserializationContext> template = this.templateEngine.getTemplate(type);

        try
        {
            return (T) template.deserialize(context, this.createRootField(type));
        }
        catch (final Exception e)
        {
            throw new RuntimeException("Exception thrown when deserializing " + serialized, e);
        }
    }

    @Override
    public SerializationFormat<OUTPUT, ?, ?> getSerializationFormat()
    {
        return this.serializationFormat;
    }

    @Override
    public TemplateEngine getTemplateEngine()
    {
        return this.templateEngine;
    }

    // reprezentuje glówne pole przy wejsciu do pierwszej templatki
    private FieldInfo createRootField(final Type type)
    {
        return new CustomFieldInfo(null, type);
    }
}
