package org.mm.Repository;

import org.mm.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class CommentRepository extends JpaRepository<Comment, Integer> {
}