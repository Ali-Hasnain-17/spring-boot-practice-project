package com.videoHub.videoHub.controllers;

import com.videoHub.videoHub.DTOs.ThumbnailUploadResponse;
import com.videoHub.videoHub.DTOs.VideoDto;
import com.videoHub.videoHub.DTOs.VideoUploadResponse;
import com.videoHub.videoHub.models.Video;
import com.videoHub.videoHub.services.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @GetMapping
    public List<VideoDto> getVideos() {
        return videoService.getVideos();
    }

    @PostMapping("/upload")
    public VideoUploadResponse uploadVideo(@RequestParam("video") MultipartFile video, @RequestParam("channelId") Long channelId) throws IOException {
        return videoService.uploadVideo(video, channelId);
    }

    @PutMapping("/{videoId}")
    public VideoDto saveVideoDetails(@PathVariable("videoId") Long videoId, @RequestBody VideoDto videoDto) {
        return videoService.saveVideoDetails(videoId, videoDto);
    }

    @PostMapping("/thumbnail")
    public ThumbnailUploadResponse uploadThumbnail(@RequestParam("thumbnail") MultipartFile thumbnail, @RequestParam("videoId") Long videoId) throws IOException {
        return videoService.uploadThumbnail(videoId, thumbnail);
    }

    @GetMapping("/{videoId}")
    public VideoDto getVideoDetails(@PathVariable("videoId") Long videoId) {
        return videoService.getVideoDetails(videoId);
    }

    @GetMapping("/like/{videoId}")
    public VideoDto likeVideo(@PathVariable("videoId") Long videoId) {
        return videoService.likeVideo(videoId);
    }

    @GetMapping("/dislike/{videoId}")
    public VideoDto disLikeVideo(@PathVariable("videoId") Long videoId) {
        return videoService.dislikeVideo(videoId);
    }

    @GetMapping("/search")
    public List<VideoDto> searchVideo(@RequestParam("q") String videoQuery) {
        return videoService.searchVideo(videoQuery);
    }

    @GetMapping("/trending")
    public List<VideoDto> getTrendingVideos() {
        return videoService.getTrendingVideos();
    }

    @GetMapping("/public")
    public List<VideoDto> getPublicVideos() {
        return videoService.getPublicVideos();
    }

    @GetMapping("/my")
    public List<VideoDto> getMyVideos() {
        return videoService.getMyVideos();
    }

    @GetMapping("/recent")
    public List<VideoDto> getRecentlyWatchedVideos() {
        return videoService.getRecentlyWatchedVideos();
    }
}

