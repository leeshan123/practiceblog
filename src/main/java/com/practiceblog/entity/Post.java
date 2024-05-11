package com.practiceblog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String title;

    @Lob
    private  String content;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public  String getTitle(){
        return this.title.substring(0,Math.min(title.length(),10));
    }
}
