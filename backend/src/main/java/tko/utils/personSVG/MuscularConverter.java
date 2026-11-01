package tko.utils.personSVG;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter(autoApply = true)
public class MuscularConverter implements AttributeConverter<List<Muscle>,String> {

    @Override
    public String convertToDatabaseColumn(List<Muscle> muscles) {
        return muscles.stream().map(Muscle::getId).collect(Collectors.joining(","));
    }

    @Override
    public List<Muscle> convertToEntityAttribute(String s) {
        return Arrays.stream(s.split(",")).map(Muscle::getById).collect(Collectors.toList());
    }
}
