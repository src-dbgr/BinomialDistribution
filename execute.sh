#!/bin/bash

# Compile the Java file
javac BinomialDistribution.java

# Run the Java program
java BinomialDistribution

# Clean up the .class files
rm BinomialDistribution.class

# Pause (optional, not commonly used in Unix/MacOS)
read -p "Press [Enter] key to exit..."
