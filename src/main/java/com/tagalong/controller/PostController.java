package com.tagalong.controller;

import com.tagalong.dto.PostDtos.CreatePostRequest;
import com.tagalong.dto.PostDtos.PostResponse;
import com.tagalong.entity.User;
import com.tagalong.repository.UserRepository;
import com.tagalong.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<PostResponse> create(Authentication auth, @Valid @RequestBody CreatePostRequest req) {
        User poster = userRepository.findByEmail(auth.getName()).orElseThrow();
        return ResponseEntity.ok(postService.createPost(poster, req));
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostResponse>> search(@RequestParam String destination) {
        return ResponseEntity.ok(postService.searchByDestination(destination));
    }
}
