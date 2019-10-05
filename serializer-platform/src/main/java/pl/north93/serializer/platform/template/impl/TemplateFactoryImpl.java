package pl.north93.serializer.platform.template.impl;

import static java.lang.reflect.Modifier.isStatic;
import static java.lang.reflect.Modifier.isTransient;


import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;

import lombok.ToString;
import pl.north93.serializer.platform.annotations.NorthCustomTemplate;
import pl.north93.serializer.platform.annotations.NorthTransient;
import pl.north93.serializer.platform.context.DeserializationContext;
import pl.north93.serializer.platform.context.SerializationContext;
import pl.north93.serializer.platform.template.ITemplateElement;
import pl.north93.serializer.platform.template.Template;
import pl.north93.serializer.platform.template.TemplateEngine;
import pl.north93.serializer.platform.template.TemplateFactory;
import pl.north93.serializer.platform.template.field.ReflectionFieldInfo;

@ToString(onlyExplicitlyIncluded = true)
/*default*/ class TemplateFactoryImpl implements TemplateFactory
{
    private final TemplateElementFactory templateElementFactory = new TemplateElementFactory(false); // TODO switch to true

    @Override
    public <T> Template<T, SerializationContext, DeserializationContext> createTemplate(final TemplateEngine templateEngine, final Class<T> clazz)
    {
        final NorthCustomTemplate customTemplate = clazz.getAnnotation(NorthCustomTemplate.class);
        if (customTemplate != null)
        {
            //noinspection unchecked
            return templateEngine.instantiateClass(customTemplate.value());
        }

        return this.generateTemplate(templateEngine, clazz);
    }

    private <T> Template<T, SerializationContext, DeserializationContext> generateTemplate(final TemplateEngine templateEngine, final Class<T> clazz)
    {
        final List<ITemplateElement> elements = new LinkedList<>();
        final Field[] fields = FieldUtils.getAllFields(clazz);

        for (final Field field : fields)
        {
            field.setAccessible(true);
            if (this.shouldSkipField(field))
            {
                continue;
            }

            final ReflectionFieldInfo fieldInfo = new ReflectionFieldInfo(field);

            final NorthCustomTemplate northCustomTemplate = field.getAnnotation(NorthCustomTemplate.class);
            if (northCustomTemplate != null)
            {
                final Template template = templateEngine.instantiateClass(northCustomTemplate.value());
                elements.add(this.templateElementFactory.getTemplateElement(field, fieldInfo, template));
                continue;
            }

            final var template = templateEngine.getTemplate(fieldInfo.getType());
            elements.add(this.templateElementFactory.getTemplateElement(field, fieldInfo, template));
        }

        return new TemplateImpl<>(templateEngine.getInstanceCreator(clazz), elements);
    }

    private boolean shouldSkipField(final Field field)
    {
        // Skip fields with NorthTransient annotation
        // Ignore static and transient fields
        final int modifiers = field.getModifiers();
        return field.isAnnotationPresent(NorthTransient.class) || isTransient(modifiers) || isStatic(modifiers);
    }
}
