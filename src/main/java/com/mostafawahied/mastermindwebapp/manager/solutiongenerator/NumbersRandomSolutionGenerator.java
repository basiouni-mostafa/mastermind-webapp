package com.mostafawahied.mastermindwebapp.manager.solutiongenerator;

import com.mostafawahied.mastermindwebapp.exception.GameException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class NumbersRandomSolutionGenerator implements SolutionGenerator {
    private static final int MIN_NUMBER = 0;
    private static final int MAX_NUMBER = 7;
    private static final String RANDOM_API_URL = "https://www.random.org/integers/?num=%s&min=" + MIN_NUMBER + "&max=" + MAX_NUMBER + "&col=1&base=10&format=plain&rnd=new";

    @Override
    public List<String> generateSolution(int numberOfGuesses) {
        RestTemplate restTemplate = new RestTemplate();
        String solutionNumberString;
        try {
            solutionNumberString = restTemplate.getForObject(String.format(RANDOM_API_URL, numberOfGuesses), String.class);
            if (solutionNumberString == null) {
                throw new GameException("Error generating random numbers");
            }
        } catch (RestClientException e) {
            System.out.println("Error generating random numbers: + " + e.getMessage());
            return generateSolutionNumberListFallback(numberOfGuesses);
        }
        return List.of(solutionNumberString.split("\n"));
        // testing
        // return Stream.of(1, 1, 3, 5).map(String::valueOf).toList();
    }

    private List<String> generateSolutionNumberListFallback(int numberOfGuesses) {
        List<String> solutionNumberList = new ArrayList<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < numberOfGuesses; i++) {
            int randomInt = random.nextInt(MIN_NUMBER, MAX_NUMBER + 1);
            solutionNumberList.add(String.valueOf(randomInt));
        }
        return solutionNumberList;
    }
}
