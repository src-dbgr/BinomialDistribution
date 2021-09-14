import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BinomialDistribution {
	SecureRandom rand = new SecureRandom();
	int iterations = 100000;
	int maxPossiblePositives = 50;
	int checkBinomialDistributionValue = 43;
	double successRate = 0.95;

	public static void main(String[] args) {
		BinomialDistribution bd = new BinomialDistribution();
		long startTime = System.nanoTime();
		Map<Integer, Integer> iterateExperiment = bd.iterateExperiment();
		List<Integer> sortedKeys = new ArrayList<Integer>(iterateExperiment.keySet());
		Collections.sort(sortedKeys);
		for (Integer key : sortedKeys) {
			System.out.println("BinomialDistributionValue: " + key + " Probability: " + (iterateExperiment.get(key) * 1.0 / bd.iterations));
		}
		long stopTime = System.nanoTime();
		System.out.println("\nTime Taken to Calculate:");
		System.out.println(TimeUnit.NANOSECONDS.toMillis((stopTime - startTime)) + " Milliseconds");
		System.out.println("\nRunning " + bd.iterations + " iterations.");

		System.out.println(
				"\nHeuristic Binomial Distribution Probability Result for: " + bd.checkBinomialDistributionValue);
		System.out.println(iterateExperiment.get(bd.checkBinomialDistributionValue) * 1.0 / bd.iterations);

		System.out.println(
				"\nMathematicalBinomial Distribution Probability Result for: " + bd.checkBinomialDistributionValue);
		System.out.println(bd.calcBinomialDistributionMath(bd.checkBinomialDistributionValue));
	}

	int calcDistribution() {
		int positives = 0;
		for (int i = 0; i < maxPossiblePositives; i++) {
			positives = rand.nextDouble() <= successRate ? ++positives : positives;
		}
		return positives;
	}

	Map<Integer, Integer> iterateExperiment() {
		Map<Integer, Integer> results = new HashMap<Integer, Integer>();
		int positive = 0;
		for (int i = 0; i < iterations; i++) {
			positive = calcDistribution();
			results.put(positive, results.containsKey(positive) ? (results.get(positive) + 1) : 1);
		}
		return results;
	}

	// P(positives,negatives|successrate) = (maxPossiblePositives over positives) *
	// (successrate)^positives * (1 - successrate)^negatives
	double calcBinomialDistributionMath(int checkProbabilityFor) {
		int positives = checkProbabilityFor;
		int negatives = (maxPossiblePositives - checkProbabilityFor);
		return binomial(maxPossiblePositives, positives) * Math.pow(successRate, (double) positives)
				* Math.pow((1 - successRate), negatives);
	}

	class Distribution {
		int positive = 0;
		int negative = 0;

		public Distribution(int positive, int negative) {
			this.positive = positive;
			this.negative = negative;
		}

		@Override
		public String toString() {
			return "Positve: " + this.positive + " Negative: " + this.negative;
		}
	}

	public int binomial(int n, int k) {
		checkNonNegative("n", n);
		checkNonNegative("k", k);
		checkArgument(k <= n, "k (%s) > n (%s)", k, n);
		if (k > (n >> 1)) {
			k = n - k;
		}
		if (k >= biggestBinomials.length || n > biggestBinomials[k]) {
			return Integer.MAX_VALUE;
		}
		switch (k) {
		case 0:
			return 1;
		case 1:
			return n;
		default:
			long result = 1;
			for (int i = 0; i < k; i++) {
				result *= n - i;
				result /= i + 1;
			}
			return (int) result;
		}
	}

	// binomial(biggestBinomials[k], k) fits in an int, but not
	// binomial(biggestBinomials[k]+1,k).
	int[] biggestBinomials = { Integer.MAX_VALUE, Integer.MAX_VALUE, 65536, 2345, 477, 193, 110, 75, 58, 49, 43, 39, 37,
			35, 34, 34, 33 };

	double checkNonNegative(String role, double x) {
		if (!(x >= 0)) { // not x < 0, to work with NaN.
			throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
		}
		return x;
	}

	public void checkArgument(boolean b, String errorMessageTemplate, int p1, int p2) {
		if (!b) {
		}
	}

}
