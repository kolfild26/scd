package ru.sportmaster.scd.utils;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import java.nio.ByteBuffer;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Служебный класс, предоставляющий функционал конвертации данных.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ConvertUtil {
    /**
     * Преобразование значения из входных параметров к типу Date.
     * @param inputValue - значение из входных параметров
     * @return - LocalDate или null
     */
    public static LocalDate getDateValue(Object inputValue) {
        if (isNull(inputValue)) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return LocalDate.parse((String) inputValue, formatter);
    }

    public static String getDateString(Object date) {
        if (isNull(date)) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return formatter.format((TemporalAccessor) date);
    }

    /**
     * Преобразование значения из входных параметров к типу UUID.
     * @param inputValue - значение из входных параметров
     * @return - UUID или null
     */
    @SuppressWarnings("unused")
    public static UUID getUuidValue(Object inputValue) {
        if (isNull(inputValue)) {
            return null;
        }
        return UUID.fromString((String) inputValue);
    }

    public static Long getLongValue(Object inputValue) {
        if (isNull(inputValue)) {
            return null;
        }
        if (inputValue instanceof Integer integerValue) {
            return Long.valueOf(integerValue);
        }
        if (inputValue instanceof String stringValue) {
            return Long.valueOf(stringValue);
        }
        return (Long) inputValue;
    }

    public static Integer getIntValue(Object inputValue) {
        if (isNull(inputValue)) {
            return null;
        }
        if (inputValue instanceof Integer integerValue) {
            return integerValue;
        }
        if (inputValue instanceof String stringValue) {
            return Integer.valueOf(stringValue);
        }
        return ((Long) inputValue).intValue();
    }

    public static boolean getBooleanValue(Object inputValue) {
        if (nonNull(inputValue) && inputValue instanceof Boolean booleanValue) {
            return booleanValue;
        }
        return false;
    }

    public static String localDateTimeToString(LocalDateTime inputValue) {
        return Optional.ofNullable(inputValue)
            .map(LocalDateTime::toString)
            .orElse(null);
    }

    /**
     * Преобразовать UUID в байтовый массив.
     *
     * @param uuid объект UUID
     * @return байтовый массив
     */
    public static byte[] uuidToBytes(UUID uuid) {
        if (isNull(uuid)) {
            return new byte[0];
        } else {
            ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
            bb.putLong(uuid.getMostSignificantBits());
            bb.putLong(uuid.getLeastSignificantBits());
            return bb.array();
        }
    }

    /**
     * Преобразовать байтовый массив в объект UUID.
     *
     * @param bytes байтовый массив
     * @return объект UUID
     */
    public static UUID bytesToUuid(byte[] bytes) {
        if (isNull(bytes)) {
            return null;
        } else {
            ByteBuffer bb = ByteBuffer.wrap(bytes);
            long firstLong = bb.getLong();
            long secondLong = bb.getLong();
            return new UUID(firstLong, secondLong);
        }
    }

    /**
     * Преобразовать строку в CLOB.
     *
     * @param s    - строка.
     * @param conn - соединение.
     * @return - CLOB.
     * @throws SQLException - выбрасывается исключение в случае возникновения ошибки БД.
     */
    public static Clob stringToClob(String s, Connection conn) throws SQLException {
        if (s == null || s.trim().isEmpty()) {
            return null;
        } else {
            Clob clob = conn.createClob();
            clob.setString(1, s);
            return clob;
        }
    }

    /**
     * Преобразовать строку в BLOB.
     *
     * @param s    - строка.
     * @param conn - соединение.
     * @return - BLOB.
     * @throws SQLException - выбрасывается исключение в случае возникновения ошибки БД.
     */
    public static Blob stringToBlob(String s, Connection conn) throws SQLException {
        if (s == null || s.trim().isEmpty()) {
            return null;
        } else {
            Blob blob = conn.createBlob();
            blob.setBytes(1, s.getBytes());
            return blob;
        }
    }

    /**
     * Преобразовать CLOB в строку.
     *
     * @param clob - CLOB.
     * @return - строка.
     * @throws SQLException - выбрасывается исключение в случае возникновения ошибки БД.
     */
    public static String clobToString(Clob clob) throws SQLException {
        return clob == null ? null : clob.getSubString(1, (int) clob.length());
    }

    /**
     * Преобразовать BLOB в байтовый массив.
     *
     * @param blob - BLOB.
     * @return - байтовый массив.
     * @throws SQLException - выбрасывается исключение в случае возникновения ошибки БД.
     */
    @SuppressWarnings("unused")
    public static byte[] blobToBytes(Blob blob) throws SQLException {
        if (blob == null) {
            return new byte[0];
        } else {
            return blob.getBytes(1L, (int) blob.length());
        }
    }

    public static String getBlobString(Blob blob) throws SQLException {
        return new String(blob.getBytes(1, (int) blob.length()));
    }

    public static boolean isDateValue(Object inputValue) {
        try {
            return getDateValue(inputValue) != null;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
}
