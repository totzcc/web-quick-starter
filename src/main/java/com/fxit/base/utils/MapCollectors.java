package com.fxit.base.utils;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class MapCollectors {
    public static <T, K, U>
    Collector<T, ?, Map<K, U>> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper) {
        return Collectors.toMap(keyMapper, valueMapper, (v1, v2) -> v1);
    }

    public static <T, K>
    Collector<T, ?, Map<K, T>> toMap(Function<? super T, ? extends K> keyMapper) {
        return Collectors.toMap(keyMapper, v -> v, (v1, v2) -> v1);
    }
}