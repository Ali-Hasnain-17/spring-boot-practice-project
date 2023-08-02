package com.videoHub.videoHub.DTOs;

import com.videoHub.videoHub.models.VideoStatus;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoDto {
    private long videoId;
    private String title;
    private String description;
    private Set<String> tags;
    private String thumbnailUrl;
    private Integer likes = 0;
    private Integer dislikes = 0;
    private Integer views = 0;
    private String videoUrl;
    private VideoStatus videoStatus;
}
