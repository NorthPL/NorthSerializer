package pl.north93.serializer.platform.template.impl;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

import lombok.ToString;
import pl.north93.serializer.platform.template.ITemplateElement;
import pl.north93.serializer.platform.template.Template;
import pl.north93.serializer.platform.template.field.FieldInfo;

@ToString(of = {"field", "template"})
class MethodHandleTemplateElement implements ITemplateElement
{
    private static final Lookup LOOKUP = MethodHandles.lookup();
    private static final MethodType GETTER_TYPE = MethodType.methodType(Object.class, Object.class);
    private static final MethodType SETTER_TYPE = MethodType.methodType(void.class, Object.class, Object.class);
    private final FieldInfo field;
    private final MethodHandle getter;
    private final MethodHandle setter;
    private final Template template;

    public MethodHandleTemplateElement(final Field field, final FieldInfo fieldInfo, final Template template)
    {
        try
        {
            this.getter = LOOKUP.unreflectGetter(field).asType(GETTER_TYPE);
            this.setter = LOOKUP.unreflectSetter(field).asType(SETTER_TYPE);
        }
        catch (final IllegalAccessException e)
        {
            throw new RuntimeException("Failed to unreflect getter or setter.", e);
        }
        this.field = fieldInfo;
        this.template = template;
    }

    @Override
    public Object get(final Object instance)
    {
        try
        {
            return this.getter.invokeExact(instance);
        }
        catch (final Throwable throwable)
        {
            throw new RuntimeException("Something went wrong while getting field...", throwable);
        }
    }

    @Override
    public void set(final Object instance, final Object value)
    {
        try
        {
            this.setter.invokeExact(instance, value);
        }
        catch (final Throwable throwable)
        {
            throw new RuntimeException("Something went wrong while setting field...", throwable);
        }
    }

    @Override
    public FieldInfo getFieldInfo()
    {
        return this.field;
    }

    @Override
    public Template getTemplate()
    {
        return this.template;
    }
}
