package microservices.book.multiplication.challenge;

import microservices.book.multiplication.serviceclients.GamificationServiceClient;
import microservices.book.multiplication.user.User;
import microservices.book.multiplication.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ChallengeServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ChallengeAttemptRepository attemptRepository;
    @Mock
    private GamificationServiceClient gameClient;

    @InjectMocks
    private ChallengeService challengeService;

    @BeforeEach
    public void setUp() {
        // Keep in mind that we needed to move the
        // given(attemptRepository)... to the test cases
        // that use it to prevent the unused stubs errors.
    }

    @Test
    public void checkCorrectAttemptTest() {
        given(attemptRepository.save(any())).will(returnsFirstArg());

        ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(50, 60, "john_doe", 3000);
        ChallengeAttempt resultAttempt = challengeService.verifyAttempt(attemptDTO);

        then(resultAttempt.isCorrect()).isTrue();
        verify(userRepository).save(new User("john_doe"));
        verify(attemptRepository).save(resultAttempt);
        verify(gameClient).sendAttempt(resultAttempt);
    }

    @Test
    public void checkWrongAttemptTest() {
        given(attemptRepository.save(any())).will(returnsFirstArg());

        ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(50, 60, "john_doe", 5000);
        ChallengeAttempt resultAttempt = challengeService.verifyAttempt(attemptDTO);

        then(resultAttempt.isCorrect()).isFalse();
        verify(userRepository).save(new User("john_doe"));
        verify(attemptRepository).save(resultAttempt);
        verify(gameClient).sendAttempt(resultAttempt);
    }

    @Test
    public void checkExistingUserTest() {
        given(attemptRepository.save(any())).will(returnsFirstArg());
        User existingUser = new User(1L, "john_doe");
        given(userRepository.findByAlias("john_doe")).willReturn(Optional.of(existingUser));

        ChallengeAttemptDTO attemptDTO = new ChallengeAttemptDTO(50, 60, "john_doe", 5000);
        ChallengeAttempt resultAttempt = challengeService.verifyAttempt(attemptDTO);

        then(resultAttempt.isCorrect()).isFalse();
        then(resultAttempt.getUser()).isEqualTo(existingUser);
        verify(userRepository, never()).save(any());
        verify(attemptRepository).save(resultAttempt);
        verify(gameClient).sendAttempt(resultAttempt);
    }

    @Test
    public void retrieveStatsTest() {
        User user = new User("john_doe");
        ChallengeAttempt attempt1 = new ChallengeAttempt(1L, user, 50, 60, 3010, false);
        ChallengeAttempt attempt2 = new ChallengeAttempt(2L, user, 50, 60, 3051, false);
        List<ChallengeAttempt> lastAttempts = List.of(attempt1, attempt2);
        given(attemptRepository.findTop10ByUserAliasOrderByIdDesc("john_doe")).willReturn(lastAttempts);

        List<ChallengeAttempt> latestAttemptsResult = challengeService.getStatsForUser("john_doe");

        then(latestAttemptsResult).isEqualTo(lastAttempts);
    }
}
