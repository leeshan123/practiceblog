package com.practiceblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practiceblog.entity.Post;
import com.practiceblog.repository.PostRepository;
import com.practiceblog.request.PostCreate;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static javax.management.Query.value;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();;
    }

    @Test
    @DisplayName("/posts 요청시 Helloo World를 출력한다.")
    void test() throws Exception {
        //글 제목
        //글 내용

        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                ) //application/json 형태로 보낸다.
                .andExpect(status().isOk())
                .andExpect(content().string("{}"))
                .andDo(print()); //요청 서머리를 줌.
    }

    @Test
    @DisplayName("/posts 요청시 title값은 필수다.")
    void test2() throws Exception {
        //글 제목
        //글 내용

        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                ) //application/json 형태로 보낸다.
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))

//                .andExpect(content().string("Hello World"))
                .andDo(print()); //요청 서머리를 줌.
    }

    @Test
    @DisplayName("/posts 요청시 DB의 값이 저장된다.")
    void test3() throws Exception {

//        PostCreate request = new PostCreate("제목입니다.", "내용입니다.");
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        System.out.println(json);

        //when
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                ) //application/json 형태로 보낸다.
                .andExpect(status().isOk())
                .andDo(print()); //요청 서머리를 줌.
        Assertions.assertEquals(1L,postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());

    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        //given
        Post post = Post.builder()
                .title("123456123456123456")
                .content("bar")
                .build();
        postRepository.save(post);

        //when
        mockMvc.perform(get("/posts/{postId}",post.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                        //application/json 형태로 보낸다.
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value(post.getTitle()))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andDo(print()); //요청 서머리를 줌.

        //then

    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1,31)
                .mapToObj(i ->
                        Post.builder()
                                .title("호돌맨 제목 "+i)
                                .content("반포자이 "+i)
                                .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        //when
        mockMvc.perform(get("/posts?page=1&sort=id,desc&size=5")
                        .contentType(MediaType.APPLICATION_JSON))
                //application/json 형태로 보낸다.
                // 리스트안에 오브젝트가 하나씩 있는 형태로 들어온다.
                //[{"id":1,"title":"1234561234","content":"bar"},{"id":2,"title":"1234561234","content":"bar"}]
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()",Matchers.is(5)))
                .andExpect(jsonPath("$[0].title").value("호돌맨 제목 30"))
                .andExpect(jsonPath("$[0].content").value("반포자이 30"))
                .andDo(print()); //요청 서머리를 줌.

        //then

    }

}