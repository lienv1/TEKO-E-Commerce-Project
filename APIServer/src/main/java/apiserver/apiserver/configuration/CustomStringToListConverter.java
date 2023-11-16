package apiserver.apiserver.configuration;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CustomStringToListConverter implements Converter<String, List<String>> {
    private final String delimiter;
    
    public CustomStringToListConverter(@Value("${custom.request.param.delimiter:¿}") String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public List<String> convert(String source) {
        return Arrays.asList(source.split(Pattern.quote(delimiter)));
    }
}
