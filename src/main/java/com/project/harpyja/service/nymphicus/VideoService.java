package com.project.harpyja.service.nymphicus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.harpyja.model.nymphicus.ActivityGestureLogs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class VideoService {

    private final WebClient webClient;
    private final String otididaeUrl;

    public VideoService(WebClient.Builder webClientBuilder,
                        ObjectMapper objectMapper,
                        @Value("${services.otididae-url}") String otididaeUrl) {
        this.webClient = webClientBuilder.build();
        this.otididaeUrl = otididaeUrl;
    }

    @Async
    public void requestGenerateVideo(
            MultipartFile file,
            ActivityGestureLogs timeLines,
            String sessionId,
            String duration) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("file cannot be null or empty");
        }
        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException("sessionId cannot be empty");
        }
        if (duration == null || duration.isBlank()) {
            throw new IllegalArgumentException("duration cannot be empty");
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

        Resource fileAsResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        body.add("file", fileAsResource);

        body.add("timeLines", timeLines);
        body.add("sessionId", sessionId);
        body.add("duration", duration);

        webClient.post()
                .uri(otididaeUrl)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(body))
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(response -> {
                    System.out.println("Response status code: " + response.getStatusCodeValue());
                })
                .doOnError(error -> {
                    System.err.println("Error occurred: " + error.getMessage());
                })
                .subscribe();
    }
}