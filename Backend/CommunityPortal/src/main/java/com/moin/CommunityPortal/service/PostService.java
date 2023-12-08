package com.moin.CommunityPortal.service;

import com.moin.CommunityPortal.dto.PostDto;
import com.moin.CommunityPortal.exception.CustomException;

import java.util.List;

public interface PostService {
    PostDto createPost(PostDto postDto) throws CustomException;
    PostDto updatePost(Long postId, PostDto updatedPostDto) throws CustomException;
    PostDto getPostById(Long postId) throws CustomException;
    List<PostDto> getAllPosts();
}
