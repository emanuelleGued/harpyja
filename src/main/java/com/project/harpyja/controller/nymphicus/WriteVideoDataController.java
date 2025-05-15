package com.project.harpyja.controller.nymphicus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.harpyja.model.nymphicus.ActivityGestureLogs;
import com.project.harpyja.model.nymphicus.ActivityGestureUtils;
import com.project.harpyja.model.nymphicus.Device;
import com.project.harpyja.model.nymphicus.Session;
import com.project.harpyja.repository.nymphicus.SessionRepository;
import com.project.harpyja.service.nymphicus.VideoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v2/videos")
public class WriteVideoDataController {

    private final SessionRepository sessionRepository;
    private final VideoService videoService;
    private final ObjectMapper objectMapper;

    public WriteVideoDataController(SessionRepository sessionRepository,
                                    VideoService videoService,
                                    ObjectMapper objectMapper) {
        this.sessionRepository = sessionRepository;
        this.videoService = videoService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(path = "/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> writeVideoData(
            @RequestParam("key") String key,
            @RequestPart("file") MultipartFile videoFile,
            @RequestPart("device") String deviceJson,
            @RequestPart("activityGestureLogs") MultipartFile activityGestureLogsFile,
            @RequestPart("duration") String durationStr) {

        // Validações básicas
        if (!StringUtils.hasText(key)) {
            return ResponseEntity.badRequest().body("Missing 'key' parameter");
        }

        // Parse do duration
        long duration;
        try {
            duration = Long.parseLong(durationStr);
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body("Invalid duration format");
        }

        // Parse do device
        Device device;
        try {
            device = objectMapper.readValue(deviceJson, Device.class);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Invalid device data");
        }

        // Processamento do activityGestureLogs (arquivo GZIP)
        ActivityGestureLogs activityGestureLogs;
        try {
            activityGestureLogs = ActivityGestureUtils.extractActivityGestureLogs(activityGestureLogsFile);
        } catch (IOException e) {
            return ResponseEntity.badRequest()
                    .body("Failed to process activityGestureLogs: " + e.getMessage());
        }

        // Criação da sessão
        Session session = new Session();
        session.setId(UUID.randomUUID().toString());
        session.setKey(key);
        session.setDevice(device);
        session.setActivities(activityGestureLogs);
        session.setDuration(duration);
        session.setStatus("InProgress");
        session.setCreatedAt(Instant.now());

        try {
            sessionRepository.saveActionsToMongo(session);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to save session");
        }

        // Chamada assíncrona ao serviço de vídeo
        try {
            videoService.requestGenerateVideo(videoFile, activityGestureLogs, session.getId(), durationStr);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to start video generation");
        }

        String responseMsg = String.format(
                "File received: %s%nDevice: %s%nActivity Gestures: %s%nDuration: %d",
                videoFile.getOriginalFilename(), device, activityGestureLogs, duration);

        return ResponseEntity.ok(responseMsg);
    }
}