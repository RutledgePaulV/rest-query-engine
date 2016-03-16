package com.github.rutledgepaulv.rqe.resolvers;

import com.github.rutledgepaulv.qbuilders.structures.FieldPath;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.function.BiFunction;

public class MongoPersistentEntityFieldTypeResolver implements BiFunction<FieldPath, Class<?>, Class<?>> {

    private MongoMappingContext context;


    public MongoPersistentEntityFieldTypeResolver() {
        this.context = new MongoMappingContext();
    }

    public MongoPersistentEntityFieldTypeResolver(MongoMappingContext context) {
        this.context = context;
    }

    @Override
    public Class<?> apply(FieldPath path, Class<?> root) {
        return context.getPersistentPropertyPath(path.asKey(), root).getLeafProperty().getActualType();
    }

}
