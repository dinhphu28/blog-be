package tech.dinhphu28.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tech.dinhphu28.blog.entity.Token;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(
            value = """
                select t
                from Token t inner join User u\s
                on t.user.id = u.id\s
                where u.id = :id and (t.expired = false and t.revoked = false)\s
                """
    )
    List<Token> findAllValidTokenByUser(Integer id);

    Optional<Token> findByToken(String token);
}