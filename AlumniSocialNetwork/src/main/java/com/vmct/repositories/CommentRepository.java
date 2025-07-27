
package com.vmct.repositories;

import com.vmct.pojo.Comment;
import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Optional<Comment> findById(Long id); // Tìm comment theo ID

    boolean save(Comment comment);       // Lưu hoặc cập nhật comment

    boolean deleteById(Long id);         // Xóa comment theo ID

    int countByPostId(Long postId);      // Đếm comment theo bài viết

    List<Comment> findByPostId(Long postId); // Lấy tất cả comment của bài viết (gồm gốc và reply)

    List<Comment> findByParentId(Long parentId); // Lấy các reply của 1 comment cụ thể

}
