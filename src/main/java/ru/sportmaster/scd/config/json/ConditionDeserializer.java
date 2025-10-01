package ru.sportmaster.scd.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.stream.StreamSupport;
import ru.sportmaster.scd.ui.view.type.ICondition;
import ru.sportmaster.scd.ui.view.type.Join;
import ru.sportmaster.scd.ui.view.type.UIViewCondition;
import ru.sportmaster.scd.ui.view.type.UIViewConditionOperation;
import ru.sportmaster.scd.ui.view.type.UIViewGroupCondition;

public class ConditionDeserializer extends JsonDeserializer<ICondition> {
    public static final String JOIN_PROPERTY = "join";
    public static final String IS_SEARCH_PROPERTY = "search";
    public static final String CONDITIONS_PROPERTY = "conditions";
    public static final String FIELD_PROPERTY = "field";
    public static final String OPERATION_PROPERTY = "operation";
    public static final String VALUE_PROPERTY = "value";

    @Override
    public ICondition deserialize(JsonParser jp, DeserializationContext context) throws IOException {
        ObjectCodec codec = jp.getCodec();
        JsonNode node = codec.readTree(jp);
        return deserializeCondition(node);
    }

    private ICondition deserializeCondition(JsonNode node) {
        if (node.has(JOIN_PROPERTY)) {
            var builder = UIViewGroupCondition.builder()
                .isSearch(node.has(IS_SEARCH_PROPERTY) && node.get(IS_SEARCH_PROPERTY).asBoolean())
                .join(Join.valueOf(node.get(JOIN_PROPERTY).asText()));

            if (node.has(CONDITIONS_PROPERTY)) {
                builder.conditions(StreamSupport.stream(node.get(CONDITIONS_PROPERTY).spliterator(), false)
                    .map(this::deserializeCondition)
                    .toList());
            }

            return builder.build();
        } else {
            return UIViewCondition.builder()
                .field(node.get(FIELD_PROPERTY).asText())
                .operation(UIViewConditionOperation.valueOf(node.get(OPERATION_PROPERTY).asText()))
                .value(node.get(VALUE_PROPERTY))
                .build();
        }
    }
}
