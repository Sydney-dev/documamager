package com.qdb.documan.post.service;

import com.qdb.documan.DocumamagerApplication;
import com.qdb.documan.post.dto.PostDto;
import com.qdb.documan.post.service.PostService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest(classes = DocumamagerApplication.class)
@Transactional
public class PostServiceIT {

    public static final long USER_ID = 1;
    @Autowired
    private PostService postService;

    @Test
    public void shouldGetPostForUser(){
        List<PostDto> postDtos = postService.savePosts("5", USER_ID);
        Assertions.assertThat(postDtos).isNotEmpty();
    }


}
