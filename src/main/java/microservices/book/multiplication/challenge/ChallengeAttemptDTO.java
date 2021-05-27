package microservices.book.multiplication.challenge;

import lombok.Value;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Value
public class ChallengeAttemptDTO {

    @Min(1) @Max(99)
    int factorA;
    @Min(1) @Max(99)
    int factorB;
    @NotBlank
    String userAlias;
    @Positive
    int guess;

    public boolean isCorrect() {
        return guess == factorA * factorB;
    }

}
