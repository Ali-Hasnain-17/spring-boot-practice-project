package com.videoHub.videoHub.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "videos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Video {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
    private Integer likes;
    private Integer dislikes;
    private Integer views;
    private String videoUrl;
    private String thumbnailUrl;
    private VideoStatus videoStatus;
    private Date postedAt;
    private String category;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", referencedColumnName = "id")
    private Set<Tag> tags;

    @ManyToOne()
    @JoinColumn(name = "channel_id", referencedColumnName = "id")
    private Channel channel;

    @ManyToMany(mappedBy = "viewedVideos", fetch = FetchType.LAZY)
    private List<User> viewers;

    @ManyToMany(mappedBy = "likedVideos", fetch = FetchType.LAZY)
    private List<User> likers;

    @ManyToMany(mappedBy = "dislikedVideos", fetch = FetchType.LAZY)
    private List<User> dislikers;
}
