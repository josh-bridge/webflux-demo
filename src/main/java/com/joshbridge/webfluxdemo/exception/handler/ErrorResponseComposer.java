package com.joshbridge.webfluxdemo.exception.handler;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.joshbridge.webfluxdemo.types.ErrorResponse;

/**
 * @author josh.bridge
 */
@Component
public class ErrorResponseComposer {

    private static final Collector<ExceptionResponseMapper, ?, Map<Class<?>, ExceptionResponseMapper>> EXCEPTION_MAPPER_COLLECTOR =
            Collectors.toMap(ExceptionResponseMapper::getExceptionClass, Function.identity(), mapperOrderComparator());

    private static BinaryOperator<ExceptionResponseMapper> mapperOrderComparator() {
        return (left, right) -> {
            if (AnnotationAwareOrderComparator.INSTANCE.compare(left, right) < 0) {
                return left;
            }

            return right;
        };
    }

    private static final ExceptionResponseMapper DEFAULT_MAPPER = new DefaultExceptionMapper();

    private final Map<Class<?>, ExceptionResponseMapper> mappers;

    public ErrorResponseComposer(List<ExceptionResponseMapper> mappers) {
        this.mappers = getMappersInOrder(mappers);
    }

    @SuppressWarnings("unchecked")
    public ErrorResponse compose(Throwable exception) {
        final Optional<ExceptionResponseMapper> foundMapper = findMapper(exception);

        final ExceptionResponseMapper mapper = foundMapper.orElse(DEFAULT_MAPPER);

        return mapper.map(exception);
    }

    private Optional<ExceptionResponseMapper> findMapper(Throwable ex) {
        final Optional<ExceptionResponseMapper> matchingMapper = findMatchingMapper(ex);
        if (matchingMapper.isPresent()) {
            return matchingMapper;
        }

        return findAssignableMapper(ex);
    }

    private Optional<ExceptionResponseMapper> findMatchingMapper(Throwable ex) {
        Throwable exc = ex;
        while (exc != null) {
            if (mappers.containsKey(exc.getClass())) {
                return Optional.of(mappers.get(exc.getClass()));
            }

            exc = exc.getCause();
        }

        return Optional.empty();
    }

    private Optional<ExceptionResponseMapper> findAssignableMapper(Throwable ex) {
        for (Class<?> mapperClass : mappers.keySet()) {
            if (mapperClass.isAssignableFrom(ex.getClass())) {
                return Optional.of(mappers.get(mapperClass));
            }
        }

        return Optional.empty();
    }

    private Map<Class<?>, ExceptionResponseMapper> getMappersInOrder(List<ExceptionResponseMapper> mappers) {
        return mappers.stream().collect(EXCEPTION_MAPPER_COLLECTOR);
    }

    public static class DefaultExceptionMapper extends ExceptionResponseMapper<Throwable> {

        public DefaultExceptionMapper() {
            super(Throwable.class);
        }

        @Override
        public ErrorResponse map(Throwable ex) {
            final String code = "ERR-" + ex.getClass().getSimpleName();
            final String description = ex.getLocalizedMessage();
            final int status = HttpStatus.INTERNAL_SERVER_ERROR.value();

            return new ErrorResponse(code, description, status);
        }
    }
}
