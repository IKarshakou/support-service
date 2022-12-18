package com.training.mapper;

import com.training.dto.comment.InputCommentDto;
import com.training.dto.comment.OutputCommentDto;
import com.training.entity.Comment;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(imports = {UserMapper.class})
public interface CommentMapper {

    OutputCommentDto convertToDto(Comment comment);

    Comment convertToEntity(InputCommentDto inputCommentDto);

    default List<Comment> convertToEntityList(InputCommentDto inputCommentDto) {
        List<Comment> comments = new ArrayList<>();
        if (inputCommentDto != null) {
            comments.add(convertToEntity(inputCommentDto));
        }
        return comments;
    }

    List<OutputCommentDto> convertListToDto(List<Comment> comments);

    List<Comment> convertListToEntity(List<InputCommentDto> commentsDto);
}
