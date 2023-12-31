package com.videoHub.videoHub.repository;

import com.videoHub.videoHub.models.Channel;
import com.videoHub.videoHub.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {
    List<Channel> findByNameContaining(String name);
    Channel findByOwner(User user);
}
