package pl.north93.serializer.platform.template;

import java.lang.reflect.Type;

import pl.north93.serializer.platform.TypePredictor;
import pl.north93.serializer.platform.InstanceCreator;
import pl.north93.serializer.platform.context.DeserializationContext;
import pl.north93.serializer.platform.context.SerializationContext;

public interface TemplateEngine
{
    /**
     * Szuka klasy o podanej nazwie.
     *
     * @param name Nazwa klasy.
     * @return Klasa o podanej nazwie.
     */
    Class<?> findClass(String name);

    /**
     * Próbuje przeksztalcic podany Type na Class.
     * Spowoduje to ewentualną ukratę typu generycznego.
     *
     * @param type Type do skonwertowania na Class.
     * @return Class pobrane z danego Type.
     */
    Class<?> getRawClassFromType(Type type);

    Type[] getTypeParameters(Type type);

    Type createParameterizedType(Class clazz, Type[] parameters);

    boolean isNeedsDynamicResolution(Type type);

    boolean isTypePredictingSupported();

    TypePredictor<SerializationContext, DeserializationContext> getTypePredictor();

    <T> InstanceCreator<T> getInstanceCreator(Class<T> clazz);

    default <T> T instantiateClass(final Class<T> clazz)
    {
        final InstanceCreator<T> instanceCreator = this.getInstanceCreator(clazz);
        return instanceCreator.newInstance();
    }

    void register(TemplateFilter filter, Template<?, ?, ?> template);

    Template<Object, SerializationContext, DeserializationContext> getTemplate(Type type);
}
