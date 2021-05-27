package microservices.book.multiplication.challenge;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ChallengeGeneratorService {

    private final static int MINIMUM_FACTOR = 11;
    private final static int MAXIMUM_FACTOR = 100;

    private final Random random;

    ChallengeGeneratorService() {
        this.random = new Random();
    }

    protected ChallengeGeneratorService(final Random random) {
        this.random = random;
    }

    private int next() {
        return random.nextInt(MAXIMUM_FACTOR - MINIMUM_FACTOR) + MINIMUM_FACTOR;
    }

    public Challenge randomChallenge() {
        return new Challenge(next(), next());
    }
}
