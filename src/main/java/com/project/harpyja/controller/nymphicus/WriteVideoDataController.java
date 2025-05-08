package com.project.harpyja.controller.nymphicus;

// WriteVideoDataController.java

import com.project.harpyja.model.nymphicus.ActivityGestureLogs;
import com.project.harpyja.model.nymphicus.Device;
import com.project.harpyja.model.nymphicus.Session;
import com.project.harpyja.repository.nymphicus.SessionRepository;
import com.project.harpyja.service.nymphicus.VideoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/v2/videos")
public class WriteVideoDataController {

    private final SessionRepository sessionRepository;
    private final VideoService videoService;

    public WriteVideoDataController(SessionRepository sessionRepository,
                                    VideoService videoService) {
        this.sessionRepository = sessionRepository;
        this.videoService = videoService;
    }

    @PostMapping(path = "/write", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> writeVideoData(
            @RequestParam("key") String key,
            @RequestPart("file") MultipartFile file,
            @RequestPart("device") Device device,
            @RequestPart("activityGestureLogs") ActivityGestureLogs activityGestureLogs,
            @RequestPart("duration") String durationStr
    ) throws Exception {
        if (key == null || key.isBlank()) {
            return ResponseEntity.badRequest().body("Missing 'key' parameter");
        }

        long duration;
        try {
            duration = Long.parseLong(durationStr);
        } catch (NumberFormatException ex) {
            return ResponseEntity.badRequest().body("Invalid duration format");
        }

        // Cria sessão
        Session session = new Session();
        session.setId(UUID.randomUUID().toString());
        session.setKey(key);
        session.setDevice(device);
        session.setActivities(activityGestureLogs);
        session.setDuration(duration);
        session.setStatus("InProgress");
        session.setCreatedAt(Instant.now());
        sessionRepository.saveActionsToMongo(session);

        // Chama serviço de vídeo de forma assíncrona
        videoService.requestGenerateVideo(file, activityGestureLogs, session.getId(), durationStr);

        String responseMsg = String.format(
                "File received: %s%nDevice: %s%nActivity Gestures: %s%nDuration: %d",
                file.getOriginalFilename(), device, activityGestureLogs, duration
        );
        return ResponseEntity.status(HttpStatus.OK).body(responseMsg);
    }
}
