# Binomial Distribution Simulator

This project implements a simulation and calculation of the binomial distribution using both heuristic (simulation-based) and mathematical approaches. It is designed to illustrate the probability of obtaining a certain number of successes in a fixed number of independent trials, where each trial has the same probability of success.

## Overview

The binomial distribution is a discrete probability distribution that models the number of successes in a fixed number of independent Bernoulli trials (where each trial has exactly two possible outcomes: success or failure). The probability of exactly `k` successes out of `n` trials can be calculated using the following formula:

$$ P(X = k) = \binom{n}{k} \times p^k \times (1-p)^{n-k} $$

Where:

$n$ is the total number of trials.
$k$ is the number of successes.
$p$ is the probability of success on each trial.
$\binom{n}{k}$ is the binomial coefficient, calculated as:

$$
\binom{n}{k} = \frac{n!}{k!(n-k)!}
$$

## Project Structure

### `BinomialDistribution.java`

This is the main Java class that performs the following tasks:
- **Simulates** the binomial distribution through a large number of iterations.
- **Calculates** the exact probability using the mathematical formula for binomial distribution.
- **Compares** the results of the simulation against the mathematically calculated probabilities.

### Key Components:
- **Heuristic Simulation:** 
  The program runs a series of simulated experiments to approximate the distribution. This involves repeatedly generating random trials and counting the number of successes. The results are then aggregated to approximate the probability distribution.
  
- **Mathematical Calculation:** 
  The program also calculates the exact binomial distribution for a specified number of successes using the binomial formula mentioned above. This calculation uses high-precision arithmetic with `BigDecimal` to ensure accuracy.

### Example Usage:
The main method initializes the experiment with the following parameters:
- **Iterations:** The number of simulation iterations to perform (e.g., 100,000).
- **Max Possible Positives:** The maximum number of successes (e.g., 50 trials).
- **Success Rate:** The probability of success on each trial (e.g., 95%).
- **Target Value:** The specific number of successes for which the probability is calculated (e.g., 43 successes).

### Parameters:
You can customize the behavior of the program by adjusting the following parameters within the `BinomialDistribution.java` file:

- **`iterations`**: The number of iterations for the simulation (default is `100_000`). Increasing this value enhances the accuracy of the heuristic approach but also increases computation time.
- **`maxPossiblePositives`**: The maximum number of successes (or positive outcomes) in a series of trials (default is `50`).
- **`successRate`**: The probability of success for each individual trial (default is `0.95`). This should be a value between `0` and `1`.
- **`checkBinomialDistributionValue`**: The specific number of successes for which the probability is calculated and compared between the heuristic and mathematical methods (default is `43`).

Feel free to experiment with these values to observe how they affect the simulation results and the corresponding mathematical calculations.


### Output:
- The program outputs the probability distribution based on the simulation and compares it to the exact mathematical probabilities.
- It also measures and reports the time taken to perform these calculations.

## How to Run

### Prerequisites:
- **Java SDK:** Ensure that you have Java SDK installed on your system. The project is compatible with Java 8 and above.

### Compile and Run:
1. Compile the Java file:
   ```bash
   javac BinomialDistribution.java
   ```
2. Run the program:
   ```bash
   java BinomialDistribution
   ```

## Running the Program with Scripts

To simplify the compilation and execution process, you can use the provided scripts for Windows or Unix/MacOS systems.

### Windows

For Windows, you can use the `execute.bat` script. This script will:
1. Compile the `BinomialDistribution.java` file.
2. Run the compiled Java program.
3. Clean up by deleting the generated `.class` file.
4. Pause the terminal window, so you can see the output before the window closes.

To run the script, simply double-click the `execute.bat` file in Windows Explorer, or run it from the command prompt:
```cmd
execute.bat
```

### Unix/MacOS
For Unix or MacOS, you can use the execute.sh script. This script will:

1. Compile the BinomialDistribution.java file.
2. Run the compiled Java program.
3. Clean up by deleting the generated .class file.
4. Optionally pause the terminal, waiting for you to press Enter before closing (useful when running the script manually).

Before running the script, ensure it is executable by running:

```bash
chmod +x execute.sh
```

To execute the script, run:

```bash
./execute.sh
```

These scripts provide a quick and easy way to compile, run, and clean up after running the program.

### Sample Output:
```
Value: 39 Probability: 0.00002
Value: 40 Probability: 0.00021
Value: 41 Probability: 0.00064
...

Time Taken to Calculate: 2221 ms

Heuristic Probability for 43: 0.00804
Mathematical Probability for 43: 0.008598104574966741622348937280409044
```

## Technical Details

### Precision Handling:
The use of `BigDecimal` ensures that the calculations maintain high precision, especially when dealing with probabilities close to 0 or 1 or when calculating binomial coefficients for large numbers.

### Parallelization:
The simulation is parallelized using Java Streams, which allows for faster computation by taking advantage of multi-core processors.

### Performance:
The program measures the time taken to perform the calculations, providing insights into the efficiency of the simulation versus the mathematical calculation.
