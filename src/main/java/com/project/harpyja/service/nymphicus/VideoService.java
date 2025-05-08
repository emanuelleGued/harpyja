package com.project.harpyja.service.nymphicus;

import com.project.harpyja.model.nymphicus.ActivityGestureLogs;
import org.springframework.web.multipart.MultipartFile;

public interface VideoService {
    void requestGenerateVideo(MultipartFile file,
                              ActivityGestureLogs timeLines,
                              String sessionId,
                              String duration) throws Exception;
}

