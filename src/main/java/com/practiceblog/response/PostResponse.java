package com.practiceblog.response;


import com.practiceblog.entity.Post;
import lombok.Builder;
import lombok.Getter;

//서비스 정책에 맞는 response 만듬

@Getter
public class PostResponse {
    private final Long id;
    private final String title;
    private final String content;

    public PostResponse(Post post) {
        this.id =post.getId();
        this.title =post.getTitle();
        this.content =post.getContent();
    }

    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
