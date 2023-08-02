package com.videoHub.videoHub.services;

import com.videoHub.videoHub.DTOs.ChannelDto;
import com.videoHub.videoHub.models.Channel;
import com.videoHub.videoHub.models.User;
import com.videoHub.videoHub.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ChannelService {
    private final ChannelRepository channelRepository;
    private final UserService userService;

    public String subscribe(Long channelId) {
        Channel channel = getChannelById(channelId);
        List<User> subscribers = channel.getSubscribers();
        User currentLoggedInUser = userService.getLoggedInUserEmail();
        Boolean isAlreadySubscribed = false;
        if (currentLoggedInUser != null) {
            isAlreadySubscribed = subscribers
                    .stream()
                    .anyMatch(subscriber -> subscriber.getId() == currentLoggedInUser.getId());
            if (!isAlreadySubscribed) {
                subscribers.add(currentLoggedInUser);
                channel.setSubscribers(subscribers);
                channelRepository.save(channel);
            } else {
                subscribers.remove(currentLoggedInUser);
                channel.setSubscribers(subscribers);
                channelRepository.save(channel);
            }
        }
        return isAlreadySubscribed ? "Channel Unsubscribed" : "Channel subscribed";
    }

    public List<ChannelDto> searchChannel(String channelQuery) {
        List<Channel> channels = channelRepository.findByNameContaining(channelQuery);
        return channels
                .stream()
                .map(channel -> mapEntityToDto(channel))
                .toList();
    }

    private ChannelDto mapEntityToDto(Channel channel) {
        return ChannelDto
                .builder()
                .id(channel.getId())
                .name(channel.getName())
                .build();
    }

    private Channel getChannelById(Long channelId) {
        return channelRepository.findById(channelId).get();
    }
}
