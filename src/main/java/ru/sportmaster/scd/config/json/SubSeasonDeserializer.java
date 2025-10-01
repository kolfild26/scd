package ru.sportmaster.scd.config.json;

import static ru.sportmaster.scd.utils.JpaUtil.MAX_ORDER;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.Optional;
import lombok.SneakyThrows;
import ru.sportmaster.scd.entity.replenishment.SubSeason;
import ru.sportmaster.scd.entity.replenishment.SubSeason_;
import ru.sportmaster.scd.repository.dictionary.SubSeasonRepository;
import ru.sportmaster.scd.utils.BeanUtil;

public class SubSeasonDeserializer extends JsonDeserializer<SubSeason> {

    @Override
    public SubSeason deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        if (parser.getText() != null) {
            var repository = BeanUtil.getBean(SubSeasonRepository.class);
            toFieldName(parser, SubSeason_.NAME);
            String name = parser.nextTextValue();
            toFieldName(parser, SubSeason_.ORDER);
            Long order = parser.nextLongValue(MAX_ORDER);
            return Optional.ofNullable(repository.getByName(name))
                .orElseGet(() -> new SubSeason(null, name, order));
        }
        return null;
    }

    @SneakyThrows
    private void toFieldName(JsonParser parser, String name) {
        JsonToken field = parser.nextToken();
        while (field != null) {
            if (JsonToken.FIELD_NAME == field && name.equals(parser.getText())) {
                return;
            }
            field = parser.nextToken();
        }
    }
}
