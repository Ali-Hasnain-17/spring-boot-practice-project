package com.videoHub.videoHub.controllers;

import com.videoHub.videoHub.DTOs.ChannelDto;
import com.videoHub.videoHub.DTOs.VideoDto;
import com.videoHub.videoHub.services.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/channel")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelService channelService;

    @GetMapping("/subscribe/{channelId}")
    public String subscribe(@PathVariable("channelId") Long channelId) {
        return channelService.subscribe(channelId);
    }

    @GetMapping("/search")
    public List<ChannelDto> searchVideo(@RequestParam("q") String channelQuery) {
        return channelService.searchChannel(channelQuery);
    }
}
