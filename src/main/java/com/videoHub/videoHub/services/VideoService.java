package com.videoHub.videoHub.services;

import com.videoHub.videoHub.DTOs.ThumbnailUploadResponse;
import com.videoHub.videoHub.DTOs.VideoDto;
import com.videoHub.videoHub.DTOs.VideoUploadResponse;
import com.videoHub.videoHub.models.*;
import com.videoHub.videoHub.repository.ChannelRepository;
import com.videoHub.videoHub.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.View;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {
    private final CloudinaryService cloudinaryService;
    private final UserService userService;
    private final VideoRepository videoRepository;
    private final ChannelRepository channelRepository;
    public VideoUploadResponse uploadVideo(MultipartFile videoFile, Long channelId) throws IOException {
        Optional<Channel> channel = channelRepository.findById(channelId);
        String videoUrl = cloudinaryService.uploadFile(videoFile, "video");
        Video video = Video
                .builder()
                .videoUrl(videoUrl)
                .channel(channel.get())
                .build();
        Video savedVideo = videoRepository.save(video);
        return new VideoUploadResponse(savedVideo.getId(), videoUrl);
    }

    public VideoDto saveVideoDetails(Long videoId, VideoDto videoDto) {
        Set<Tag> tags = videoDto.getTags()
                .stream()
                .map(tag -> Tag.builder().name(tag).build())
                .collect(Collectors.toSet());

        Video video = getVideoById(videoId);
        video.setTitle(videoDto.getTitle());
        video.setDescription(videoDto.getDescription());
        video.setVideoStatus(videoDto.getVideoStatus());
        video.setLikes(videoDto.getLikes());
        video.setDislikes(videoDto.getDislikes());
        video.setViews(videoDto.getViews());
        video.setTags(tags);

        videoRepository.save(video);
        return videoDto;
    }

    public ThumbnailUploadResponse uploadThumbnail(Long videoId, MultipartFile thumbnail) throws IOException {
        Video video = getVideoById(videoId);
        String thumbnailUrl = cloudinaryService.uploadFile(thumbnail, "image");
        video.setThumbnailUrl(thumbnailUrl);
        videoRepository.save(video);
        return new ThumbnailUploadResponse(videoId, thumbnailUrl);
    }

    public List<VideoDto> getVideos() {
        List<Video> videos = videoRepository.findAll();
        List<VideoDto> videoDtos = videos
                .stream()
                .map(video -> mapEntityToDto(video))
                .toList();
        return videoDtos;
    };

    public VideoDto getVideoDetails(Long videoId) {
        Video video = getVideoById(videoId);
        incrementViewCount(video);
        return mapEntityToDto(video);
    }

    public List<VideoDto> searchVideo(String videoQuery) {
        List<Video> videos = videoRepository.findByTitleContaining(videoQuery);
        return videos
                .stream()
                .map(video -> mapEntityToDto(video))
                .toList();
    }

    public VideoDto likeVideo(Long videoId) {
        Video video = getVideoById(videoId);
        User currentLoggedInUser = userService.getLoggedInUserEmail();
        Integer likes = video.getLikes();
        if (currentLoggedInUser != null) {
            Boolean isAlreadyLiked = video
                    .getLikers()
                    .stream()
                    .anyMatch(liker -> liker.getId() == currentLoggedInUser.getId());
            if (!isAlreadyLiked) {
                likes++;
                video.setLikes(likes);
                video.getLikers().add(currentLoggedInUser);
                videoRepository.save(video);
            } else {
                likes--;
                video.setLikes(likes);
                video.getLikers().remove(currentLoggedInUser);
                videoRepository.save(video);
            }
        }
        return mapEntityToDto(video);
    }

    public VideoDto dislikeVideo(Long videoId) {
        Video video = getVideoById(videoId);
        User currentLoggedInUser = userService.getLoggedInUserEmail();
        Integer dislikes = video.getDislikes();
        if (currentLoggedInUser != null) {
            Boolean isAlreadyDisliked = video
                    .getDislikers()
                    .stream()
                    .anyMatch(disliker -> disliker.getId() == currentLoggedInUser.getId());
            if (!isAlreadyDisliked) {
                dislikes++;
                video.setDislikes(dislikes);
                video.getDislikers().add(currentLoggedInUser);
                videoRepository.save(video);
            } else {
                dislikes--;
                video.setDislikes(dislikes);
                video.getDislikers().remove(currentLoggedInUser);
                videoRepository.save(video);
            }
        }
        return mapEntityToDto(video);
    }

    private void incrementViewCount(Video video) {
        User currentLoggedInUser = userService.getLoggedInUserEmail();
        if (currentLoggedInUser != null) {
            Boolean isAlreadyViewed = video
                    .getViewers()
                    .stream()
                    .anyMatch(viewer -> viewer.getId() == currentLoggedInUser.getId());
            if (!isAlreadyViewed) {
                Integer views = video.getViews();
                views++;
                video.setViews(views);
                video.getViewers().add(currentLoggedInUser);
                videoRepository.save(video);
            }
        }
    }

    public List<VideoDto> getTrendingVideos() {
        List<Video> videos = videoRepository.findAll(Sort.by(Sort.Direction.DESC, "views"));
        return videos
                .stream()
                .limit(50)
                .map(video -> mapEntityToDto(video))
                .toList();
    }

    public List<VideoDto> getPublicVideos() {
        List<Video> videos = videoRepository.findByVideoStatus(VideoStatus.PUBLIC);
        return videos
                .stream()
                .map(video -> mapEntityToDto(video))
                .toList();
    }

    public List<VideoDto> getMyVideos() {
        User user = userService.getLoggedInUserEmail();
        Channel channel = channelRepository.findByOwner(user);
        List<Video> videos = videoRepository.findByChannel(channel);
        return videos
                .stream()
                .map(video -> mapEntityToDto(video))
                .toList();
    }

    public List<VideoDto> getRecentlyWatchedVideos() {
        return null;
    }

    private Video getVideoById(Long videoId) {
        return videoRepository.findById(videoId).get();
    }

    private VideoDto mapEntityToDto(Video video) {
        Set<String> tags = video.getTags()
                .stream()
                .map(tag -> tag.getName())
                .collect(Collectors.toSet());

        return VideoDto
                .builder()
                .videoId(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .videoUrl(video.getVideoUrl())
                .thumbnailUrl(video.getThumbnailUrl())
                .videoStatus(video.getVideoStatus())
                .likes(video.getLikes())
                .dislikes(video.getDislikes())
                .views(video.getViews())
                .tags(tags)
                .build();
    }
}



