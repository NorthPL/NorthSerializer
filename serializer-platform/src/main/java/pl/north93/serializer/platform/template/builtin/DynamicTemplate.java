package pl.north93.serializer.platform.template.builtin;

import java.lang.reflect.Type;

import pl.north93.serializer.platform.context.DeserializationContext;
import pl.north93.serializer.platform.context.SerializationContext;
import pl.north93.serializer.platform.template.Template;
import pl.north93.serializer.platform.template.TemplateEngine;
import pl.north93.serializer.platform.template.TemplatePriority;
import pl.north93.serializer.platform.template.field.CustomFieldInfo;
import pl.north93.serializer.platform.template.field.FieldInfo;
import pl.north93.serializer.platform.template.filter.TemplateFilter;

public class DynamicTemplate implements Template<Object, SerializationContext, DeserializationContext>
{
    private static final FieldInfo FIELD_TYPE = new CustomFieldInfo("type", String.class);

    public static final class DynamicTemplateFilter implements TemplateFilter
    {
        @Override
        public int getPriority()
        {
            return TemplatePriority.HIGHEST;
        }

        @Override
        public boolean isApplicableTo(final TemplateEngine templateEngine, final Type type)
        {
            return templateEngine.isNeedsDynamicResolution(type);
        }
    }

    @Override
    public void serialise(final SerializationContext context, final FieldInfo field, final Object object) throws Exception
    {
        final TemplateEngine templateEngine = context.getTemplateEngine();

        final Type fixedType = this.addMissingGenericType(templateEngine, field.getType(), object.getClass());
        if (this.shouldUseTypePredicting(templateEngine, fixedType))
        {
            this.serializeWithTypePredicting(context, field, fixedType, object);
        }
        else
        {
            this.serializeWithoutTypePredicting(context, field, fixedType, object);
        }
    }

    // typ moze byc przewidziany przez protokól; serializujemy bez nazwy klasy
    private void serializeWithTypePredicting(final SerializationContext context, final FieldInfo field, final Type fixedType, final Object object) throws Exception
    {
        final TemplateEngine templateEngine = context.getTemplateEngine();
        final var template = templateEngine.getTemplate(fixedType);

        template.serialise(context, field, object);
    }

    // typ nie moze byc przewidziany przez protokól; serializujemy z nazwa klasy
    private void serializeWithoutTypePredicting(final SerializationContext context, final FieldInfo field, final Type fixedType, final Object object) throws Exception
    {
        final TemplateEngine templateEngine = context.getTemplateEngine();

        context.enterObject(field);
        try
        {
            final var template = templateEngine.getTemplate(fixedType);

            context.writeString(FIELD_TYPE, object.getClass().getName());
            template.serialise(context, this.getValueField(fixedType), object);
        }
        finally
        {
            context.exitObject(field);
        }
    }

    @Override
    public Object deserialize(final DeserializationContext context, final FieldInfo field) throws Exception
    {
        final TemplateEngine templateEngine = context.getTemplateEngine();
        if (templateEngine.isTypePredictingSupported())
        {
            final var predictedTemplate = templateEngine.getTypePredictor().predictType(context, field);
            if (predictedTemplate != null)
            {
                return predictedTemplate.deserialize(context, field);
            }
        }

        return this.deserializeWithoutTypePredicting(context, templateEngine, field);
    }

    private Object deserializeWithoutTypePredicting(final DeserializationContext context, final TemplateEngine templateEngine, final FieldInfo field) throws Exception
    {
        context.enterObject(field);
        try
        {
            final String className = context.readString(FIELD_TYPE);
            final Class<?> templateClass = templateEngine.findClass(className);

            final Type fixedType = this.addMissingGenericType(templateEngine, field.getType(), templateClass);

            final var template = templateEngine.getTemplate(fixedType);
            return template.deserialize(context, this.getValueField(fixedType));
        }
        finally
        {
            context.exitObject(field);
        }
    }

    private boolean shouldUseTypePredicting(final TemplateEngine templateEngine, final Type type)
    {
        return templateEngine.isTypePredictingSupported() && templateEngine.getTypePredictor().isTypePredictable(templateEngine, type);
    }

    // jesli posiadamy informacje o generycznym typie to go dodajemy;
    // w przeciwnym wypadku nie powodujemy bledu
    private Type addMissingGenericType(final TemplateEngine templateEngine, final Type fieldType, final Class<?> templateClass)
    {
        final Type[] typeParameters = templateEngine.getGenericParameters(fieldType); // generic type
        return typeParameters.length == 0 ? templateClass : templateEngine.createParameterizedType(templateClass, typeParameters);
    }

    private FieldInfo getValueField(final Type type)
    {
        return new CustomFieldInfo("value", type);
    }
}
