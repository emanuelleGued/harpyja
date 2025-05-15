package com.project.harpyja.model.nymphicus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class ActivityGestureUtils {

    public static ActivityGestureLogs extractActivityGestureLogs(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("activityGestureLogs file is missing");
        }

        byte[] compressedData = file.getBytes();

        byte[] jsonData = decompressGzip(compressedData);

        String jsonString = new String(jsonData, StandardCharsets.UTF_8);

        ObjectMapper objectMapper = new ObjectMapper();
        ActivityGestureLogs logs = new ActivityGestureLogs();
        logs.setActivities(objectMapper.readValue(jsonString,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ActivityGesture.class)));

        return logs;
    }

    private static byte[] decompressGzip(byte[] compressedData) throws IOException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(compressedData);
             GZIPInputStream gis = new GZIPInputStream(bis);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = gis.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        }
    }
}