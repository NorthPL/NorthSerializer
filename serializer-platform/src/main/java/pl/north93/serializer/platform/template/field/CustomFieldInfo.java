package pl.north93.serializer.platform.template.field;

import java.lang.reflect.Type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public final class CustomFieldInfo implements FieldInfo
{
    private final String name;
    private final Type type;
}
