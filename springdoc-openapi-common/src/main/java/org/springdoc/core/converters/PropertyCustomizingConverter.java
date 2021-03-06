package org.springdoc.core.converters;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.PropertyCustomizer;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class PropertyCustomizingConverter implements ModelConverter {

    private final Optional<List<PropertyCustomizer>> propertyCustomizers;

    public PropertyCustomizingConverter(Optional<List<PropertyCustomizer>> customizers) {
        this.propertyCustomizers = customizers;
    }

    @Override
    public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain) {
        if (chain.hasNext()) {
            Schema<?> resolvedSchema = chain.next().resolve(type, context, chain);
            if (type.isSchemaProperty()) {
                propertyCustomizers.ifPresent(customizers -> customizers.forEach(customizer -> customizer.customize(resolvedSchema, type)));
            }
            return resolvedSchema;
        } else {
            return null;
        }
    }
}
