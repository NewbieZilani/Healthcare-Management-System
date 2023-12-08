package com.moin.CommunityPortal.service.serviceImpl;

import com.moin.CommunityPortal.dto.CommentDto;
import com.moin.CommunityPortal.dto.UserDto;
import com.moin.CommunityPortal.entity.CommentEntity;
import com.moin.CommunityPortal.entity.PostEntity;
import com.moin.CommunityPortal.exception.CustomException;
import com.moin.CommunityPortal.feignclient.UserClient;
import com.moin.CommunityPortal.repository.CommentRepository;
import com.moin.CommunityPortal.repository.PostRepository;
import com.moin.CommunityPortal.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;  // Assuming you have a PostRepository
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserClient userClient;

    @Override
    public CommentDto createComment(CommentDto commentDto) throws CustomException {
        UserDto user = userClient.getCurrentUserProfile();
        Optional<PostEntity> optionalPostEntity = postRepository.findById(commentDto.getPostId());

        if (optionalPostEntity.isPresent()) {
            PostEntity postEntity = optionalPostEntity.get();
            CommentEntity commentEntity = new CommentEntity();
            commentEntity.setPatientId(user.getUserID());
            commentEntity.setCommentContent(commentDto.getCommentContent());
            commentEntity.setCommentTime(LocalDateTime.now());
            commentEntity.setPost(postEntity);
            commentEntity.setIsAvailable(true); // Assuming a default value for availability
            CommentEntity savedCommentEntity = commentRepository.save(commentEntity);

            CommentDto savedCommentDto = new CommentDto();
            savedCommentDto.setCommentId(savedCommentEntity.getCommentId());
            savedCommentDto.setPatientId(savedCommentEntity.getPatientId());
            savedCommentDto.setCommentContent(savedCommentEntity.getCommentContent());
            savedCommentDto.setCommentTime(savedCommentEntity.getCommentTime());
            savedCommentDto.setPostId(savedCommentEntity.getPost().getPostId());
            //savedCommentDto.(savedCommentEntity.getIsAvailable());

            return savedCommentDto;
        } else {
            throw new CustomException("Post not found with ID: " + commentDto.getPostId());
        }
    }


    @Override
    public CommentDto updateComment(Long commentId, CommentDto commentDto) throws CustomException {
        Optional<CommentEntity> optionalCommentEntity = commentRepository.findById(commentId);

        if (optionalCommentEntity.isPresent()) {
            CommentEntity existingCommentEntity = optionalCommentEntity.get();
            existingCommentEntity.setCommentContent(commentDto.getCommentContent());
            // Update other fields as needed

            CommentEntity updatedCommentEntity = commentRepository.save(existingCommentEntity);
            return modelMapper.map(updatedCommentEntity, CommentDto.class);
        } else {
            throw new CustomException("Comment not found with ID: " + commentId);
        }
    }
    @Override
    public List<CommentDto> getAllCommentsForPost(Long postId) {
        List<CommentEntity> commentEntities = commentRepository.findByPostId(postId);
        return commentEntities.stream()
                .map(commentEntity -> modelMapper.map(commentEntity, CommentDto.class))
                .collect(Collectors.toList());
    }
    @Override
    public CommentDto getCommentById(Long commentId) throws CustomException {
        Optional<CommentEntity> optionalCommentEntity = commentRepository.findById(commentId);

        if (optionalCommentEntity.isPresent()) {
            return modelMapper.map(optionalCommentEntity.get(), CommentDto.class);
        } else {
            throw new CustomException("Comment not found with ID: " + commentId);
        }
    }

}
