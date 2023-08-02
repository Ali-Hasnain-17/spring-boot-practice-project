package com.videoHub.videoHub.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThumbnailUploadResponse {
    private Long videoId;
    private String thumbnailUrl;
}