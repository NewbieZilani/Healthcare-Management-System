package com.moin.CommunityPortal.service;

import com.moin.CommunityPortal.dto.CommentDto;
import com.moin.CommunityPortal.exception.CustomException;
import com.moin.CommunityPortal.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface CommentService {
    CommentDto createComment(CommentDto commentDto) throws CustomException;
    CommentDto updateComment(Long commentId, CommentDto commentDto) throws CustomException;
    List<CommentDto> getAllCommentsForPost(Long postId);
    CommentDto getCommentById(Long commentId) throws CustomException;
}
