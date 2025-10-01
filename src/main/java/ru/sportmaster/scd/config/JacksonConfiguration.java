package ru.sportmaster.scd.config;

import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import ru.sportmaster.scd.config.json.DayDeserializer;
import ru.sportmaster.scd.config.json.DaySerializer;
import ru.sportmaster.scd.config.json.LocalizedPropertyDeserializer;
import ru.sportmaster.scd.config.json.LocalizedPropertySerializer;
import ru.sportmaster.scd.config.json.TimestampSerializer;
import ru.sportmaster.scd.config.json.WeekDeserializer;
import ru.sportmaster.scd.config.json.WeekSerializer;
import ru.sportmaster.scd.entity.dictionary.Day;
import ru.sportmaster.scd.entity.dictionary.Week;
import ru.sportmaster.scd.ui.view.type.LocalizedProperty;

@Configuration
public class JacksonConfiguration {
    @Value("${spring.jpa.properties.hibernate.jdbc.time_zone}")
    private String dbTimeZonePrefix;

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder(MessageSource messageSource) {
        return new Jackson2ObjectMapperBuilder()
            .serializerByType(LocalizedProperty.class, new LocalizedPropertySerializer(messageSource))
            .serializerByType(Timestamp.class, new TimestampSerializer(dbTimeZonePrefix))
            .serializerByType(Week.class, new WeekSerializer())
            .serializerByType(Day.class, new DaySerializer())
            .deserializerByType(LocalizedProperty.class, new LocalizedPropertyDeserializer())
            .deserializerByType(Week.class, new WeekDeserializer())
            .deserializerByType(Day.class, new DayDeserializer());
    }

    @Bean
    public ObjectMapper objectMapper(MessageSource messageSource) {
        ObjectMapper mapper = objectMapperBuilder(messageSource).build();
        mapper.findAndRegisterModules();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);
        return mapper;
    }

    @Bean
    public Module javaTimeModule() {
        return new JavaTimeModule();
    }
}
