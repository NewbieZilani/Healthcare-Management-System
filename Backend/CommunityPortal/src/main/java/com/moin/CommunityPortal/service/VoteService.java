package com.moin.CommunityPortal.service;

import com.moin.CommunityPortal.dto.VoteCountDto;
import com.moin.CommunityPortal.dto.VoteDto;
import com.moin.CommunityPortal.exception.CustomException;

public interface VoteService {
    void castVote(VoteDto voteDto) throws CustomException;
    VoteCountDto countVotes(Long postId) throws CustomException;
}
