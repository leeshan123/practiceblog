package com.practiceblog.service;

import com.practiceblog.entity.Post;
import com.practiceblog.repository.PostRepository;
import com.practiceblog.request.PostCreate;
import com.practiceblog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;



@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate){
        //postCreate -> Entitiy

        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);

    }

    public PostResponse get(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        PostResponse response = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        return response;
    }

    public List<PostResponse> getList(Pageable page) {
        Pageable pageable = PageRequest.of(0, 5, Sort.Direction.DESC,"id");
        return postRepository.findAll(pageable).stream()
                .map(post -> new PostResponse(post))
                .collect(Collectors.toList());
    }

}
