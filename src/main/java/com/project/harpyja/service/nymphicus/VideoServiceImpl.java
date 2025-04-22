package com.project.harpyja.service.nymphicus;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.harpyja.model.nymphicus.ActivityGestureLogs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VideoServiceImpl implements VideoService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String otididaeUrl;

    public VideoServiceImpl(RestTemplate restTemplate,
                            ObjectMapper objectMapper,
                            @Value("${services.otididae-url}") String otididaeUrl) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.otididaeUrl = otididaeUrl;
    }


    @Override
    @Async
    public void requestGenerateVideo(MultipartFile file,
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

        // Monta o corpo multipart
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        // Arquivo
        Resource fileAsResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        body.add("file", fileAsResource);

        // JSON fields
        String timeLinesJson = objectMapper.writeValueAsString(timeLines.getActivities());
        body.add("timeLines", timeLinesJson);
        body.add("sessionId", sessionId);
        body.add("duration", duration);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(otididaeUrl, requestEntity, String.class);

        // Log response
        System.out.println("Response status code: " + response.getStatusCodeValue());
        System.out.println("Response body: " + response.getBody());
    }
}
