import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A class to simulate and calculate the binomial distribution using both heuristic 
 * and mathematical approaches.
 * 
 * The binomial distribution gives the probability of having a fixed number of successes
 * in a fixed number of independent trials, each with the same probability of success.
 */
public class BinomialDistribution {

    private final SecureRandom random = new SecureRandom();
    private final int iterations;
    private final int maxPossiblePositives;
    private final BigDecimal successRate;
    private final MathContext mathContext = new MathContext(34, RoundingMode.HALF_UP); // Using 34 digits precision

    /**
     * Constructor to initialize the binomial distribution parameters.
     * 
     * @param iterations          the number of iterations to simulate
     * @param maxPossiblePositives the maximum number of positive outcomes in a trial
     * @param successRate         the probability of success in each trial
     */
    public BinomialDistribution(int iterations, int maxPossiblePositives, double successRate) {
        if (iterations <= 0) {
                throw new IllegalArgumentException("Iterations must be greater than 0.");
        }
        if (maxPossiblePositives <= 0) {
                throw new IllegalArgumentException("MaxPossiblePositives must be greater than 0.");
        }
        if (successRate <= 0.0 || successRate > 1.0) {
                throw new IllegalArgumentException("Success rate must be between 0 (exclusive) and 1 (inclusive).");
        }
        this.iterations = iterations;
        this.maxPossiblePositives = maxPossiblePositives;
        this.successRate = BigDecimal.valueOf(successRate);
    }

    /**
     * The main method to execute the binomial distribution experiment.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        // Define parameters for the experiment
        int iterations = 100_000;
        int maxPositives = 50;
        double successRate = 0.95;
        int targetValue = 43;

        // Create an instance of BinomialDistribution and run the experiment
        BinomialDistribution bd = new BinomialDistribution(iterations, maxPositives, successRate);
        bd.runExperiment(targetValue);
    }

    /**
     * Runs the experiment to compare the heuristic and mathematical binomial distribution.
     * 
     * @param targetValue the specific number of successes to calculate probabilities for
     */
    public void runExperiment(int targetValue) {
        long startTime = System.nanoTime();  // Record start time for performance measurement
        Map<Integer, Long> results = calculateHeuristicDistribution();  // Perform heuristic simulation
        long stopTime = System.nanoTime();  // Record end time

        // Print the results from the heuristic approach
        printResults(results);
        System.out.println("\nTime Taken to Calculate: " + TimeUnit.NANOSECONDS.toMillis(stopTime - startTime) + " ms");

        // Calculate and print the probability for the target value using both approaches
        BigDecimal heuristicProbability = BigDecimal.valueOf(results.getOrDefault(targetValue, 0L))
                .divide(BigDecimal.valueOf(iterations), mathContext);
        BigDecimal mathematicalProbability = calculateMathematicalProbability(targetValue);

        System.out.println("\nHeuristic Probability for " + targetValue + ": " + heuristicProbability);
        System.out.println("Mathematical Probability for " + targetValue + ": " + mathematicalProbability);
    }

    /**
     * Simulates the binomial distribution using a heuristic approach by running a large 
     * number of trials.
     * 
     * @return a map containing the number of successes and their respective frequencies
     */
    private Map<Integer, Long> calculateHeuristicDistribution() {
        // Parallel stream to enhance performance during large-scale simulation
        return IntStream.range(0, iterations)
                .parallel()
                .mapToObj(i -> simulateSingleExperiment())
                .collect(Collectors.groupingByConcurrent(Integer::intValue, Collectors.counting()));
    }

    /**
     * Simulates a single binomial experiment.
     * 
     * @return the number of successes in this single experiment
     */
    private int simulateSingleExperiment() {
        // Count the number of successes in a single experiment
        return (int) IntStream.range(0, maxPossiblePositives)
                .filter(i -> random.nextDouble() <= successRate.doubleValue())
                .count();
    }

    /**
     * Calculates the exact probability of a specific number of successes using the 
     * binomial distribution formula.
     * 
     * @param positives the number of successes to calculate the probability for
     * @return the probability of having exactly the given number of successes
     */
    private BigDecimal calculateMathematicalProbability(int positives) {
        int negatives = maxPossiblePositives - positives;
        BigDecimal successProb = successRate.pow(positives, mathContext);
        BigDecimal failureProb = BigDecimal.ONE.subtract(successRate, mathContext).pow(negatives, mathContext);

        return binomialCoefficient(maxPossiblePositives, positives)
                .multiply(successProb, mathContext)
                .multiply(failureProb, mathContext);
    }

    /**
     * Computes the binomial coefficient, which is "n choose k", the number of ways to choose 
     * k successes from n trials.
     * 
     * @param n the total number of trials
     * @param k the number of successes
     * @return the binomial coefficient as a BigDecimal
     */
    private BigDecimal binomialCoefficient(int n, int k) {
        k = Math.min(k, n - k);  // Take advantage of symmetry to reduce calculations
        BigDecimal result = BigDecimal.ONE;
        for (int i = 1; i <= k; i++) {
			try {
				result = result.multiply(BigDecimal.valueOf(n - i + 1), mathContext)
										.divide(BigDecimal.valueOf(i), mathContext);
			} catch (ArithmeticException e) {
				System.err.println("An arithmetic exception occurred: " + e.getMessage());
				return BigDecimal.ZERO;
			}
        }
        return result;
    }

    /**
     * Prints the results of the heuristic distribution calculation.
     * 
     * @param results a map of the number of successes to their frequencies
     */
    private void printResults(Map<Integer, Long> results) {
        results.keySet().stream()
                .sorted()
                .forEach(key -> System.out.println("Value: " + key + " Probability: " + 
                        BigDecimal.valueOf(results.get(key))
                                  .divide(BigDecimal.valueOf(iterations), mathContext)));
    }
}
