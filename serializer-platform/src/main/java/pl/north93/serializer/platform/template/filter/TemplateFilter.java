package pl.north93.serializer.platform.template.filter;

import javax.annotation.Nonnull;

import java.lang.reflect.Type;

import pl.north93.serializer.platform.template.TemplateEngine;

public interface TemplateFilter extends Comparable<TemplateFilter>
{
    int getPriority();

    boolean isApplicableTo(TemplateEngine templateEngine, Type type);

    @Override
    default int compareTo(final @Nonnull TemplateFilter other)
    {
        final int result = other.getPriority() - this.getPriority();
        if (result == 0)
        {
            return - 1;
        }

        return result;
    }
}
