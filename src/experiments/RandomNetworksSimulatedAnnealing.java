package experiments;

import environment.BinarySequence;
import environment.ComparisonNetwork;
import environment.NetworkUtilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * In this setting, we use simulated annealing for 12-16 wires of size equal to the smallest known
 * sorting networks (e.g, for 16 wires 60 comparisons).
 *
 * For simulated annealing, for K times, we construct a random network and mutate it for K
 * iterations. KM should then be approximate to N, which is the number of random networks created
 * in RandomNetworksNormal. This is so we "preserve" the same amount of randomness being applied
 * to each method.
 *
 * When we refer to fitness, we mean the number of unsorted outputs generated from all binary
 * sequences ran on a given comparison network.
 *
 * K values to try: 10, 50, 100, 500 1000
 * M values to try: 10, 50, 100, 500, 1000
 *
 *
 * 12 WIRES | SIZE 39 | K = 2 | M = 5000
 * --------------------------------------
 * BEST FITNESS:    57
 * AVERAGE FITNESS: 93.129
 *
 * 13 WIRES | SIZE 45 | K = 2 | M = 5000
 * --------------------------------------
 * BEST FITNESS:    55
 * AVERAGE FITNESS: 123.799
 *
 * 14 WIRES | SIZE 51 | K = 2 | M = 5000
 * --------------------------------------
 * BEST FITNESS:    115
 * AVERAGE FITNESS: 164.135
 *
 * 15 WIRES | SIZE 56 | K = 2 | M = 5000
 * --------------------------------------
 * BEST FITNESS:    137
 * AVERAGE FITNESS: 224.697
 *
 * 16 WIRES | SIZE 60 | K = 2 | M = 5000
 * --------------------------------------
 * BEST FITNESS:    140
 * AVERAGE FITNESS: 320.853
 *
 *
 * 12 WIRES | SIZE 39 | K = 100 | M = 100
 * --------------------------------------
 * BEST FITNESS:    42
 * AVERAGE FITNESS: 95.508
 *
 * 13 WIRES | SIZE 45 | K = 100 | M = 100
 * --------------------------------------
 * BEST FITNESS:    60
 * AVERAGE FITNESS: 126.507
 *
 * 14 WIRES | SIZE 51 | K = 100 | M = 100
 * --------------------------------------
 * BEST FITNESS:    63
 * AVERAGE FITNESS: 167.506
 *
 * 15 WIRES | SIZE 56 | K = 100 | M = 100
 * --------------------------------------
 * BEST FITNESS:    102
 * AVERAGE FITNESS: 229.652
 *
 * 16 WIRES | SIZE 60 | K = 100 | M = 100
 * --------------------------------------
 * BEST FITNESS:    155
 * AVERAGE FITNESS: 325.254
 *
 * @author Jake Nocentino
 * @version Created on 9/8/19
 */

public class RandomNetworksSimulatedAnnealing {

    private static final int NUM_ITERATIONS = 5;     // K
    private static final int TEMPERATURE = 100000;  // M
    private static final int MIN_WIRES = 12;
    private static final int MAX_WIRES = 17;
    private static final boolean PRINT_DATA_FLAG = false;
    private static final Random rand = new Random();

    public static void main(String[] args) {

        // map each num_wires to its best known length
        HashMap<Integer, Integer> wiresToSize = new HashMap<>();
        for (int i = MIN_WIRES; i < MAX_WIRES; i++) {
            switch (i) {
                case 12:
                    wiresToSize.put(i, 39);
                    break;
                case 13:
                    wiresToSize.put(i, 45);
                    break;
                case 14:
                    wiresToSize.put(i, 51);
                    break;
                case 15:
                    wiresToSize.put(i, 56);
                    break;
                case 16:
                    wiresToSize.put(i, 60);
                    break;
            }
        }

        // perform experiment for wire sizes of 12 - 16
        for (int i = MIN_WIRES; i < MAX_WIRES; i++) {
            ComparisonNetwork bestOverallNetwork = new ComparisonNetwork(i);
            Set<BinarySequence> bestOverallOutputs = new HashSet<>();
            float avgFitness = 0;
            System.out.printf("WIRE %d%n-------%n", i);

            // perform simulated annealing NUM_NETWORKS (K) times
            for (int k = 0; k < NUM_ITERATIONS; k++) {
                System.out.printf("Iteration %d%n", k);
                ComparisonNetwork bestCurrentNetwork = createRandomNetwork(i, wiresToSize.get(i));
                Set<BinarySequence> bestCurrentOutputs = bestCurrentNetwork.operateOnAllBinary();
                avgFitness += bestCurrentOutputs.size();

                // initialize first run-through
                if (k == 0) {
                    bestOverallNetwork = bestCurrentNetwork;
                    bestOverallOutputs = bestCurrentOutputs;
                }

                // loop until system has cooled (for TEMPERATURE iterations)
                for (int m = TEMPERATURE; m >= 0; m--) {
                    ComparisonNetwork newNetwork = createRandomNetwork(i, wiresToSize.get(i));
                    Set<BinarySequence> newOutputs = newNetwork.operateOnAllBinary();
                    avgFitness += newOutputs.size();

                    // simulated annealing process
                    if (acceptanceProbability(bestCurrentOutputs.size(), newOutputs.size(),
                            m) > Math.random()) {
                        bestCurrentNetwork = newNetwork;
                        bestCurrentOutputs = newOutputs;
                    }
                    //temperature *= coolingRate;
                }
                if (bestCurrentOutputs.size() < bestOverallOutputs.size()) {
                    bestOverallNetwork = bestCurrentNetwork;
                    bestOverallOutputs = bestCurrentOutputs;
                }
                System.out.printf("Iteration %d best fitness: %d%n", k, bestCurrentOutputs.size());
            }
            avgFitness /= NUM_ITERATIONS * TEMPERATURE; // final step for calculating avg fitness

            System.out.printf("Best fitness on %d wires with size %d: %d%n", i,
                    wiresToSize.get(i), bestOverallOutputs.size());
            System.out.printf("Average fitness on %d wires with size %d: %.3f%n%n", i,
                    wiresToSize.get(i), avgFitness);

            if (PRINT_DATA_FLAG) {
                NetworkUtilities.printUnsortedOutputs(bestOverallOutputs);
                System.out.printf("The %d-comparison network that produced these outputs:%n", wiresToSize.get(i));
                System.out.println(bestOverallNetwork);
            }
        }
    }

    /**
     * Creates a ComparisonNetwork containing randomly assigned comparators.
     *
     * @return a random ComparisonNetwork object
     */
    private static ComparisonNetwork createRandomNetwork(int numWires, int numComparators) {
        ComparisonNetwork randomNetwork = new ComparisonNetwork(numWires);
        for (int i = 0; i < numComparators; i++) {
            int randI = rand.nextInt(i + 1);
            int randTop = rand.nextInt(numWires);
            int randBottom = rand.nextInt(numWires);
            randomNetwork.addComparator(randI, randTop, randBottom);
        }
        return randomNetwork;
    }

    /**
     * Calculates and returns an acceptance probability for a given mutation based on fitness of
     * the new mutation and temperature.
     *
     * @param fitness fitness of network without mutation
     * @param newFitness fitness of newly mutated network
     * @param temperature current temperature for simulated annealing
     * @return 1.0 if the new network fitness is better than the current network, else an
     * expected probability based on the current temperature.
     */
    private static double acceptanceProbability(int fitness, int newFitness, double temperature) {
        // if the fitness is lower, accept it
        if (newFitness < fitness) {
            return 1.0;
        }
        // else, calculate a probability of accepting the lower fitness
        return Math.exp((fitness - newFitness) / temperature);
    }
}