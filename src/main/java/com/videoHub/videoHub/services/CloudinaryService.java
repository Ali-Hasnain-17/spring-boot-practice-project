package com.videoHub.videoHub.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService {
    @Value("${cloudinary.cloud.name}")
    private String cloudName;
    @Value("${cloudinary.api.key}")
    private String apiKey;
    @Value("${cloudinary.api.secret}")
    private String apiSecret;

    public String uploadFile(MultipartFile video, String fileType) throws IOException {
        Cloudinary cloudinary = configure();
        Map uploadResult = cloudinary
                .uploader()
                .upload(video.getBytes(), ObjectUtils.asMap("resource_type", fileType));
        String publicId = uploadResult.get("public_id").toString();
        String videoUrl = cloudinary
                .url()
                .publicId(publicId)
                .resourceType(fileType)
                .generate();
        return videoUrl;
    }

    public Cloudinary configure() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        return new Cloudinary(config);
    }

}