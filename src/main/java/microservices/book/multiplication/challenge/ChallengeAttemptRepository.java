package microservices.book.multiplication.challenge;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChallengeAttemptRepository extends CrudRepository<ChallengeAttempt, Long> {

    List<ChallengeAttempt> findTop10ByUserAliasOrderByIdDesc(String userAlias);
}
