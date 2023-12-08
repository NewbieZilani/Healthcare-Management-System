package com.moin.CommunityPortal.controller;

import com.moin.CommunityPortal.dto.CommentDto;
import com.moin.CommunityPortal.dto.PostDto;
import com.moin.CommunityPortal.exception.CustomException;
import com.moin.CommunityPortal.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/createComment")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto CommentDto) throws CustomException {
        CommentDto comment = commentService.createComment(CommentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }
}
