package com.jack_wang.service.impl;

import com.jack_wang.dao.CommentRepository;
import com.jack_wang.po.Comment;
import com.jack_wang.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by(Sort.Direction.ASC,"createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
        return eachComment(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1) {
            comment.setParentComment(commentRepository.findById(parentCommentId).get());
        } else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    private List<Comment> eachComment(List<Comment> comments){
        List<Comment> commentsView = new ArrayList<>();
        Comment c = null;
        for(Comment comment : comments){
            c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
//        合并评论的各子代
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     *
     * @param comments root根节点 blog不为空的对象集合
     */
    private void combineChildren(List<Comment> comments){
        for(Comment comment : comments){
            List<Comment> replys = comment.getReplyComments();
            for(Comment reply : replys){
                //循环迭代，找出子代，存放到tempReplys中
                recursively(reply);
            }
            //修改顶级节点的reply集合为迭代器处理后的集合
            comment.setReplyComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }

    }
    //存放所有找出的子代集合
    private List<Comment> tempReplys = new ArrayList<>();

    /**
     * 递归迭代
     * @param comment 被迭代的对象
     */
    private void recursively(Comment comment){
        tempReplys.add(comment);//顶级节点添加到临时存放集合
        if(comment.getReplyComments().size()>0){
            List<Comment> replys = comment.getReplyComments();
            for(Comment reply : replys){
                tempReplys.add(reply);
                if(reply.getReplyComments().size()>0){
                    recursively(reply);
                }
            }
        }
    }
}
