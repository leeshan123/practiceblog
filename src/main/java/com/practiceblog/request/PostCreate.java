package com.practiceblog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@Getter
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "컨텐츠을 입력해주세요.")
    private String content;

    //위에다 할때는 모순이 생길 때가 있음.
    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }
    // 빌더의 장점: 가독성에 좋다
    // 필요한 값만 받을 수 있다. // -> (오버로딩 가능한 조건 찾아보세요)
    // 객체의 불변성
}
