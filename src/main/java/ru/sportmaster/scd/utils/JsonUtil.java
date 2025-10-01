package ru.sportmaster.scd.utils;

import static java.util.Objects.nonNull;
import static org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted;
import static ru.sportmaster.scd.consts.ParamNames.ID;
import static ru.sportmaster.scd.utils.ClassUtils.isCollection;
import static ru.sportmaster.scd.utils.ConvertUtil.isDateValue;
import static ru.sportmaster.scd.utils.UiUtil.DATE_FORMAT;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.FloatNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import jakarta.persistence.Column;
import jakarta.persistence.Tuple;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.util.ReflectionUtils;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;
import ru.sportmaster.scd.ui.view.type.UiViewAttribute;

@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
public class JsonUtil {
    public static List<String> getAllFields(ObjectNode node) {
        List<String> result = new ArrayList<>();
        Iterator<String> fields = node.fieldNames();
        fields.forEachRemaining(result::add);
        return result;
    }

    public static String getLocalizedMessage(MessageSource messageSource, LocalizedProperty property) {
        for (String label : property.getMessageKeys()) {
            String value = messageSource.getMessage(label, null, null, LocaleContextHolder.getLocale());
            if (value != null) {
                return value;
            }
        }
        return property.getLastKey();
    }

    public static String getLocalizedMessage(MessageSource messageSource, String label) {
        String value = messageSource.getMessage(label, null, null, LocaleContextHolder.getLocale());
        if (value != null) {
            return value;
        }
        return label;
    }

    public static Object getId(Class<?> type, JsonNode value) {
        if (value == null) {
            return null;
        }
        boolean isEmpty = value.isObject() && value.isEmpty();
        if (isEmpty || value.isNull()) {
            return null;
        }
        boolean isNull = value.isObject() && (!value.has(ID) || value.get(ID).isNull());
        if (isNull) {
            return null;
        }

        Field field = ReflectionUtils.findField(type, ID);
        assert field != null;

        if (field.getType().equals(Long.class)) {
            return value.get(ID).asLong();
        } else if (field.getType().equals(String.class)) {
            return value.get(ID).asText();
        } else {
            return getCompositeId(field, value.get(ID));
        }
    }

    public static Object convertType(Field field, JsonNode node) {
        if (isCollection(field.getType())) {
            return convertCollection(field, node);
        } else {
            return convertType(field.getType(), node);
        }
    }

    public static Object convertType(Class<?> type, JsonNode node) {
        if (nonNull(node) && !node.isNull()) {
            return
                Optional.ofNullable(NodeType.getNodeType(type))
                    .map(nodeType -> nodeType.convertType(node))
                    .orElse(null);
        }
        return null;
    }

    public static Object convertType(JsonNode node) {
        if (nonNull(node) && !node.isNull()) {
            return
                Optional.ofNullable(NodeType.getNodeType(node))
                    .map(nodeType -> nodeType.convertType(node))
                    .orElse(null);
        }
        return null;
    }

    private static List<Object> convertCollection(Field field, JsonNode node) {
        Class<?> innerClass = (Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
        return StreamSupport.stream(node.spliterator(), false).map(i -> convertType(innerClass, i)).toList();
    }

    @SneakyThrows
    public static Object createInstance(Class<?> type) {
        return type.getConstructor().newInstance();
    }

    @SneakyThrows
    private static Object getCompositeId(Field idField, JsonNode node) {
        Class<?> idType = idField.getType();
        Object id = createInstance(idType);

        Stream.of(idType.getDeclaredFields())
            .filter(field -> field.isAnnotationPresent(Column.class))
            .forEach(field -> {
                ReflectionUtils.makeAccessible(field);
                ReflectionUtils.setField(field, id, convertType(field, node.get(field.getName())));
            });

        return id;
    }

    public static <T> T getNullableValue(JsonNode node, Function<JsonNode, T> mapFunction, String... path) {
        JsonNode current = node;
        for (String part : path) {
            if (current != null && !current.isNull() && current.has(part)) {
                current = current.get(part);
            } else {
                return null;
            }
        }
        return current.isNull() ? null : mapFunction.apply(current);
    }

    public static long getColumnIndex(ObjectNode node, String target) {
        long index = 0;
        Iterator<String> it = node.fieldNames();
        while (it.hasNext()) {
            if (target.equals(it.next())) {
                break;
            } else {
                index++;
            }
        }
        return index;
    }

    public static ObjectNode convertTupleToObjectNode(Tuple tuple, List<String> fields) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();

        for (int i = 0; i < fields.size(); i++) {
            ObjectNode current = node;
            String field = fields.get(i);
            String[] path = field.split("\\.");
            if (path.length == 1) {
                current.putPOJO(field, tuple.get(i));
            } else {
                for (int j = 0; j < path.length; j++) {
                    if (j != path.length - 1) {
                        current = current.putObject(path[j]);
                    } else {
                        current.putPOJO(field.substring(field.lastIndexOf(".") + 1), tuple.get(i));
                    }
                }
            }
        }
        return node;
    }

    public static boolean isJsonObject(String json) {
        return json.startsWith("{") && json.endsWith("}");
    }

    public static ObjectNode convertExcelRowToObjectNode(
        Row row, List<UiViewAttribute> attributes, ObjectMapper mapper
    ) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        for (int i = 0; i < attributes.size(); i++) {
            UiViewAttribute attr = attributes.get(i);
            Cell cell = row.getCell(i);
            if (cell != null) {
                putExcelCellToObjectNode(node, attr.getName(), cell, mapper);
            } else {
                node.putNull(attr.getName());
            }
        }

        return node;
    }

    @SneakyThrows
    private static void putExcelCellToObjectNode(ObjectNode node, String field, Cell cell, ObjectMapper mapper) {
        switch (cell.getCellType()) {
            case NUMERIC -> {
                if (isCellDateFormatted(cell)) {
                    node.put(field, cell.getLocalDateTimeCellValue().format(DATE_FORMAT));
                } else {
                    node.put(field, cell.getNumericCellValue());
                }
            }
            case STRING -> {
                String val = cell.getStringCellValue();
                if (isJsonObject(val)) {
                    node.set(field, mapper.readTree(val));
                } else {
                    node.put(field, val);
                }
            }
            case FORMULA -> node.put(field, cell.getCellFormula());
            case BOOLEAN -> node.put(field, cell.getBooleanCellValue());
            default -> node.putNull(field);
        }
    }

    private enum NodeType {
        BOOL(List.of(Boolean.class, boolean.class), BooleanNode.class) {
            @Override
            Object convertType(@NonNull JsonNode node) {
                return !node.isNull() && node.asBoolean();
            }
        },
        INT(List.of(Integer.class, int.class), IntNode.class) {
            @Override
            Object convertType(@NonNull JsonNode node) {
                return node.isNull() ? null : node.asInt();
            }
        },
        LONG(List.of(Long.class, long.class), LongNode.class) {
            @Override
            Object convertType(@NonNull JsonNode node) {
                return node.isNull() ? null : node.asLong();
            }
        },
        FLOAT(List.of(Float.class, float.class), FloatNode.class) {
            @Override
            Object convertType(@NonNull JsonNode node) {
                return node.isNull() ? null : node.asDouble();
            }
        },
        DOUBLE(List.of(Double.class, double.class), DoubleNode.class) {
            @Override
            Object convertType(@NonNull JsonNode node) {
                return node.isNull() ? null : node.asDouble();
            }
        },
        TEXT(List.of(String.class), TextNode.class) {
            @Override
            Object convertType(@NonNull JsonNode node) {
                return node.asText();
            }
        },
        DATE_TIME(List.of(LocalDateTime.class), TextNode.class) {
            @Override
            Object convertType(@NonNull JsonNode node) {
                return LocalDateTime.parse(node.asText());
            }
        },
        DATE(List.of(LocalDate.class), TextNode.class) {
            @Override
            Object convertType(@NonNull JsonNode node) {
                return LocalDate.parse(node.asText(), UiUtil.DATE_FORMAT);
            }
        };

        private final List<Class<?>> typeList;
        private final Class<? extends JsonNode> nodeType;

        NodeType(List<Class<?>> typeList, Class<? extends JsonNode> nodeType) {
            this.typeList = typeList;
            this.nodeType = nodeType;
        }

        private static NodeType getNodeType(Class<?> type) {
            for (NodeType nodeType : NodeType.values()) {
                if (nodeType.typeList.contains(type)) {
                    return nodeType;
                }
            }
            return null;
        }

        private static NodeType getNodeType(JsonNode node) {
            if (node.isTextual() && isDateValue(node.asText())) {
                return NodeType.DATE;
            }
            for (NodeType nodeType : NodeType.values()) {
                if (nodeType.nodeType == node.getClass()) {
                    return nodeType;
                }
            }
            return null;
        }

        abstract Object convertType(@NonNull JsonNode node);
    }
}
