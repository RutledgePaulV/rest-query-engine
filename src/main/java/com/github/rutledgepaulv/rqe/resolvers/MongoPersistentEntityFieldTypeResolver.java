package com.github.rutledgepaulv.rqe.resolvers;

import com.github.rutledgepaulv.rqe.contexts.PropertyPath;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.function.BiFunction;

public class MongoPersistentEntityFieldTypeResolver implements BiFunction<PropertyPath, Class<?>, Class<?>> {

    private MongoMappingContext context;


    public MongoPersistentEntityFieldTypeResolver() {
        this.context = new MongoMappingContext();
    }

    public MongoPersistentEntityFieldTypeResolver(MongoMappingContext context) {
        this.context = context;
    }

    @Override
    public Class<?> apply(PropertyPath path, Class<?> root) {
        return context.getPersistentPropertyPath(path.getRawPath(), root).getLeafProperty().getActualType();
    }

}
