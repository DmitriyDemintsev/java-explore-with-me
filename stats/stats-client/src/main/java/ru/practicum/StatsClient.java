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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StatsClient {

    public static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RestTemplate template;
    private final String serverUrl;

    @Autowired
    public StatsClient(@Value("${stats-service.url}") String serverUrl, RestTemplateBuilder builder) {
        this.serverUrl = serverUrl;
        template = builder
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public StatsDto createStats(StatsDto statsDto) {
        var typeReference = new ParameterizedTypeReference<StatsDto>() {
        };
        return makeAndSendRequest(HttpMethod.POST, "/hit", null, statsDto, typeReference).getBody();
    }

    public List<HitDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("start", start.format(FORMAT));
        parameters.add("end", end.format(FORMAT));
        parameters.addAll("uris", uris);
        parameters.add("unique", Boolean.toString(unique));
        var typeReference = new ParameterizedTypeReference<List<HitDto>>() {
        };
        return makeAndSendRequest(HttpMethod.GET, "/stats",
                parameters, null, typeReference).getBody();
    }

    private <T, R> ResponseEntity<R> makeAndSendRequest(HttpMethod method,
                                                        String path,
                                                        @Nullable MultiValueMap<String, String> parameters,
                                                        @Nullable T body,
                                                        ParameterizedTypeReference<R> typeReference) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, null);
        URI uri = UriComponentsBuilder.fromHttpUrl(serverUrl)
                .path(path)
                .queryParams(parameters)
                .build(Map.of());
        return template.exchange(uri, method, requestEntity, typeReference);
    }
}
