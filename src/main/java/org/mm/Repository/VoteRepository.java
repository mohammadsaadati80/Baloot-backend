package org.mm.Repository;

import org.mm.Entity.Commodity;
import org.mm.Entity.User;
import org.mm.Entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, String> {
    Vote findByUserIdAndCommentId(String user_id, Integer comment_id);
}
