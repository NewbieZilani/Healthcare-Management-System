package com.moin.CommunityPortal.service.serviceImpl;

import com.moin.CommunityPortal.dto.UserDto;
import com.moin.CommunityPortal.dto.VoteCountDto;
import com.moin.CommunityPortal.dto.VoteDto;
import com.moin.CommunityPortal.entity.PostEntity;
import com.moin.CommunityPortal.entity.VoteEntity;
import com.moin.CommunityPortal.enums.VoteType;
import com.moin.CommunityPortal.exception.CustomException;
import com.moin.CommunityPortal.feignclient.UserClient;
import com.moin.CommunityPortal.repository.PostRepository;
import com.moin.CommunityPortal.repository.VoteRepository;
import com.moin.CommunityPortal.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class VoteServiceImpl implements VoteService {
    @Autowired
    private VoteRepository voteRepository;
    @Autowired
    private UserClient userClient;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PostRepository postRepository;
    @Override
    public void castVote(VoteDto voteDto) throws CustomException {
            UserDto user = userClient.getCurrentUserProfile();
            if(user == null) {
                throw new CustomException("You are not authorized to vote this post");
            }

            Optional<PostEntity> postEntity = postRepository.findById(voteDto.getPostId());
            if(postEntity.isEmpty()){
                throw new CustomException("Post not found");
            }
            String patientId = user.getUserID();

            // Check if the user has already voted
            Optional<VoteEntity> existingVote = voteRepository.findByPatientIdAndPostId(patientId, voteDto.getPostId());
            if (existingVote.isPresent()) {
                // If the user has already voted, update the vote if different
                VoteEntity vote = existingVote.get();
                if (!vote.getVoteType().equals(voteDto.getVoteType())) {
                    vote.setVoteType(voteDto.getVoteType());
                    voteRepository.save(vote);
                } else {
                    // If the user has already voted and the vote is same, throw CustomException
                    throw new CustomException("You have already "+vote.getVoteType()+"d this post");
                }
            } else {
                // If the user has not voted, create a new vote
                VoteEntity newVote = new VoteEntity();
                newVote.setPatientId(patientId);
                newVote.setVoteType(voteDto.getVoteType());
                newVote.setPost(postEntity.get());
                voteRepository.save(newVote);
            }

    }

    @Override
    public VoteCountDto countVotes(Long postId) throws CustomException {

            Optional<PostEntity> postEntity = postRepository.findById(postId);
            if(postEntity.isEmpty()){
                throw new CustomException("Post not found");
            }
            Long upVoteCount = voteRepository.countByPostIdAndVoteType(postId, VoteType.Upvote);
            Long downVoteCount = voteRepository.countByPostIdAndVoteType(postId, VoteType.Downvote);

            VoteCountDto voteCountDto = new VoteCountDto();
            voteCountDto.setPostId(postId);
            voteCountDto.setUpVote(upVoteCount);
            voteCountDto.setDownVote(downVoteCount);
            return voteCountDto;

    }
}