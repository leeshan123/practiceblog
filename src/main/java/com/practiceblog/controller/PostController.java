package com.practiceblog.controller;


import com.practiceblog.entity.Post;
import com.practiceblog.request.PostCreate;
import com.practiceblog.response.PostResponse;
import com.practiceblog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.status;

//RESPONSEBODY + CONTROLLER
@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private  final PostService postService;

    //Http Method는 Get / POST / PUT / PATCH / DELETE / OPTIONS / HEAD / TRACE가 있다.
    // 글등록 -> PostMapping

    // SSR -> JSP, thymeleaf, ustache, freemarker
    //  html -> rendering
    // SPA ->
    // vue -> vue+SSR = nuxt
    // react -> React+SSR = next
    // -> javascript + <-> API(JSON)

    @PostMapping("/posts")
    public  void post(@RequestBody @Valid PostCreate request) throws Exception {
        //데이터를 검증하는 이유

        //1. client 개발자가 깜빡할 수 있다. 실수로 값을 안보낼 수 있다.
        //2. client bug로 값이 누락될 수 있다.
        //3. 외부에 나쁜 사람이 값을 임의로 조작해서 보낼 수 있다.
        //4. DB에 값을 저장할 때 의도치 않은 오류가 발생할 수 있다.
        //5. 서버 개발자의 편안함을 위해.

        //repository.save(params)

        //Case1. 저장한 데이터 Entity -> response로 응답하기
        //Case2. 저장한 데이터의 primary_id -> response로 응답하기
        // Client에서는 수신한 id를 글 조회 API를 통해서 데이터를 수신받음.
        //Case3. 응답 필요 없음. -> 클라이언트에서 모든 POST(글) 데이터 context를 잘 관리함.
        //Bad Case: 서버에서 -> 반드시 이렇게 할겁니다 fix는 X -> 서버에서는 차라리 유연하게 대응하는게 좋음.
        postService.write(request);


    }

    // /posts -> 글 전체 조회 (검색 + 페이징)
    // /posts/{postId} -> 글 한개만 조회
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long id){
        PostResponse response = postService.get(id);

        return response;

    }

    //조회 API
    // 여러개의 글을 조회하는 API
    // /posts
    @GetMapping("/posts")
    public List<PostResponse> getList(@PageableDefault(size = 5) Pageable pageable){
        return postService.getList(pageable);
    }

    //글이 너무 많은 경우 -> 비용이 너무 많이 든다.
    //글이 너무 많으면 -> DB가 뻗을 수 있다.
    //DB -> 어플리케이션 서버로 전달하는 시간, 트래픽 비용 등이 많이 발생할 수 있다.





  /*
  * json 형식이 아닐때 title = xx &content=xx&userId=xxx&userName=xxx&~
  * json 형식 일때
  * {
  *      "title": "xxx",
  *      "content": "xxx",
  *      "user":{
  *         "id": "xxx,
  *         "name": xxx
  *     }
  *
  * }
  * */

}
