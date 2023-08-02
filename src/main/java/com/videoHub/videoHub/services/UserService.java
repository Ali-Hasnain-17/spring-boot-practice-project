package com.videoHub.videoHub.services;

import com.videoHub.videoHub.DTOs.AuthRequest;
import com.videoHub.videoHub.DTOs.AuthResponse;
import com.videoHub.videoHub.DTOs.RegisterRequest;
import com.videoHub.videoHub.DTOs.UserDto;
import com.videoHub.videoHub.models.Channel;
import com.videoHub.videoHub.models.MyUserDetails;
import com.videoHub.videoHub.models.User;
import com.videoHub.videoHub.repository.ChannelRepository;
import com.videoHub.videoHub.repository.UserRepository;
import com.videoHub.videoHub.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final ChannelRepository channelRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    public UserDto registerUser(RegisterRequest request) {
        User user = User
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        Channel channel = Channel
                .builder()
                .name(request.getFirstName() + " " + request.getLastName())
                .owner(user)
                .build();

        Channel savedChannel = channelRepository.save(channel);
        return mapEntityToDto(savedChannel);
    }

    public AuthResponse authenticateUser(AuthRequest request) throws Exception {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        if (!auth.isAuthenticated()) {
            throw new Exception("User Not autheticated");
        }
        String token = jwtUtils.generateToken(request.getEmail());
        User user = userRepository.findByEmail(request.getEmail()).get();
        return AuthResponse
                .builder()
                .token(token)
                .user(mapEntityToDto(user.getChannel()))
                .build();
    }

    public User getLoggedInUserEmail() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null) return null;
        Optional<User> currentLoggedInUser = userRepository.findByEmail(userDetails.getUsername());
        return currentLoggedInUser.get();
    }

    private UserDto mapEntityToDto(Channel channel) {
        User user = channel.getOwner();
        return UserDto
                .builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .channel(channel.getName())
                .build();
    }
}
