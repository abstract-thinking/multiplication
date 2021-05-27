package microservices.book.multiplication.challenge;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import microservices.book.multiplication.user.User;
import microservices.book.multiplication.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ChallengeService {

    private final UserRepository userRepository;
    private final ChallengeAttemptRepository attemptRepository;

    public ChallengeAttempt verifyAttempt(ChallengeAttemptDTO attemptDTO) {
        ChallengeAttempt checkedAttempt = createChallengeAttempt(getUser(attemptDTO), attemptDTO);

        return attemptRepository.save(checkedAttempt);
    }

    private User getUser(ChallengeAttemptDTO attemptDTO) {
        return userRepository.findByAlias(attemptDTO.getUserAlias()).orElseGet(() -> {
            log.info("Creating new user with alias {}", attemptDTO.getUserAlias());
            return userRepository.save(new User(attemptDTO.getUserAlias()));
        });
    }

    private ChallengeAttempt createChallengeAttempt(User user, ChallengeAttemptDTO attemptDTO) {
        return new ChallengeAttempt(null,
                user,
                attemptDTO.getFactorA(),
                attemptDTO.getFactorB(),
                attemptDTO.getGuess(),
                attemptDTO.isCorrect()
        );
    }

    public List<ChallengeAttempt> getStatsForUser(final String userAlias) {
        return attemptRepository.findTop10ByUserAliasOrderByIdDesc(userAlias);
    }
}
