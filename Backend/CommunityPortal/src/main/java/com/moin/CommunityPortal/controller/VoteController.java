package com.moin.CommunityPortal.controller;

import com.moin.CommunityPortal.dto.CommentDto;
import com.moin.CommunityPortal.dto.VoteCountDto;
import com.moin.CommunityPortal.dto.VoteDto;
import com.moin.CommunityPortal.exception.CustomException;
import com.moin.CommunityPortal.service.VoteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vote")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @PostMapping("/cast")
    public ResponseEntity<String> castVote(@RequestBody VoteDto voteDto) {
        try {
            voteService.castVote(voteDto);
            return ResponseEntity.ok("Vote cast successfully.");
        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getHttpStatus()).body(ex.getMessage());
        }
    }

    @GetMapping("/count/{postId}")
    public ResponseEntity<VoteCountDto> countVotes(@PathVariable Long postId) {
        try {
            VoteCountDto voteCountDto = voteService.countVotes(postId);
            return ResponseEntity.ok(voteCountDto);
        } catch (CustomException ex) {
            return ResponseEntity.status(ex.getHttpStatus()).build();
        }
    }
}