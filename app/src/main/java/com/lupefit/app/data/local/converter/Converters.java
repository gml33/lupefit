package com.lupefit.app.data.local.converter;

import androidx.room.TypeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Converters {

    @TypeConverter
    public static String fromStringList(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        return String.join(",", list);
    }

    @TypeConverter
    public static List<String> toStringList(String value) {
        if (value == null || value.isEmpty()) return Arrays.asList();
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    @TypeConverter
    public static String fromLongArray(long[] array) {
        if (array == null || array.length == 0) return "";
        return Arrays.stream(array)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @TypeConverter
    public static long[] toLongArray(String value) {
        if (value == null || value.isEmpty()) return new long[0];
        return Arrays.stream(value.split(","))
                .mapToLong(Long::parseLong)
                .toArray();
    }
}