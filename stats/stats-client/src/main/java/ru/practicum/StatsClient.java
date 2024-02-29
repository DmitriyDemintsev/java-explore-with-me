package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.HitDto;

import java.util.List;
import java.util.Map;

@Service
public class StatsClient {
    private static final String API_PREFIX = "/hits";

    private final RestTemplate template;

    @Autowired
    public StatsClient(@Value("${stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
        template = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public List<HitDto> getStats(String uri) {
        Map<String, Object> parameters = Map.of(
                "uri", uri);
        var typeReference = new ParameterizedTypeReference<List<HitDto>>() {};
        return makeAndSendRequest(HttpMethod.GET, "", parameters, null, typeReference).getBody();
    }

    private <T, R> ResponseEntity<R> makeAndSendRequest(HttpMethod method,
                                                        String path,
                                                        @Nullable Map<String, Object> parameters,
                                                        @Nullable T body,
                                                        ParameterizedTypeReference<R> typeReference) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, null);

        ResponseEntity<R> response;
        if (parameters != null) {
            response = template.exchange(path, method, requestEntity, typeReference, parameters);
        } else {
            response = template.exchange(path, method, requestEntity, typeReference);
        }
        return response;
    }
}
