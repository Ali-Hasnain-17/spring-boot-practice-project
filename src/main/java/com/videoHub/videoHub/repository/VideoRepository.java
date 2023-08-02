package com.videoHub.videoHub.repository;

import com.videoHub.videoHub.models.Channel;
import com.videoHub.videoHub.models.Video;
import com.videoHub.videoHub.models.VideoStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByTitleContaining(String title);
    List<Video> findByVideoStatus(VideoStatus status);
    List<Video> findByChannel(Channel channel);
}
