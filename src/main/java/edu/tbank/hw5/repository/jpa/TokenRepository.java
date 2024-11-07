package edu.tbank.hw5.repository.jpa;

import edu.tbank.hw5.entity.Token;
import edu.tbank.hw5.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
    List<Token> findAllByUserAndActiveTrue(User user);
}
