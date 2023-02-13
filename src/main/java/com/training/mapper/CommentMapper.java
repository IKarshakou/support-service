package com.training.mapper;

import com.training.dto.comment.InputCommentDto;
import com.training.dto.comment.OutputCommentDto;
import com.training.entity.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = UserMapper.class)
public interface CommentMapper {

    OutputCommentDto convertToDto(Comment comment);

    Comment convertToEntity(InputCommentDto inputCommentDto);

    List<OutputCommentDto> convertListToDto(List<Comment> comments);

    List<Comment> convertListToEntity(List<InputCommentDto> commentsDto);
}
