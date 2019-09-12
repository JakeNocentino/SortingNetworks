package experiments;

import environment.BinarySequence;
import environment.Comparator;
import environment.ComparisonNetwork;
import environment.NetworkUtilities;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 * In this setting, we use hill climbing for 12-16 wires of size equal to the smallest known
 * sorting networks (e.g, for 16 wires 60 comparisons).
 *
 * For hill climbing, for K times, we construct a random network and mutate it for K iterations.
 * KM should then be approximate to N, which is the number of random networks created in
 * RandomNetworksNormal. This is so we "preserve" the same amount of randomness being applied
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
 * BEST FITNESS:    42
 * AVERAGE FITNESS: 94.989
 *
 * 13 WIRES | SIZE 45 | K = 2 | M = 5000
 * --------------------------------------
 * BEST FITNESS:    61
 * AVERAGE FITNESS: 124.412
 *
 * 14 WIRES | SIZE 51 | K = 2 | M = 5000
 * --------------------------------------
 * BEST FITNESS:    68
 * AVERAGE FITNESS: 166.331
 *
 * 15 WIRES | SIZE 56 | K = 2 | M = 5000
 * --------------------------------------
 * BEST FITNESS:    109
 * AVERAGE FITNESS: 227.682
 *
 * 16 WIRES | SIZE 60 | K = 2 | M = 5000
 * --------------------------------------
 * BEST FITNESS:    211
 * AVERAGE FITNESS: 320.114
 *
 *
 * 12 WIRES | SIZE 39 | K = 100 | M = 100
 * --------------------------------------
 * BEST FITNESS:    37
 * AVERAGE FITNESS: 94.911
 *
 * 13 WIRES | SIZE 45 | K = 100 | M = 100
 * --------------------------------------
 * BEST FITNESS:    55
 * AVERAGE FITNESS: 129.473
 *
 * 14 WIRES | SIZE 51 | K = 100 | M = 100
 * --------------------------------------
 * BEST FITNESS:    76
 * AVERAGE FITNESS: 165.263
 *
 * 15 WIRES | SIZE 56 | K = 100 | M = 100
 * --------------------------------------
 * BEST FITNESS:    97
 * AVERAGE FITNESS: 230.653
 *
 * 16 WIRES | SIZE 60 | K = 100 | M = 100
 * --------------------------------------
 * BEST FITNESS:    142
 * AVERAGE FITNESS: 319.718
 *
 *
 * 12 WIRES | SIZE 39 | K = 5 | M = 100,000
 * --------------------------------------
 * BEST FITNESS:    34
 * AVERAGE FITNESS: 93.084
 *
 * 13 WIRES | SIZE 45 | K = 5 | M = 100,000
 * --------------------------------------
 * BEST FITNESS:    44
 * AVERAGE FITNESS: 124.110
 *
 * 14 WIRES | SIZE 51 | K = 5 | M = 100,000
 * --------------------------------------
 * BEST FITNESS:    54
 * AVERAGE FITNESS: 163.998
 *
 * 15 WIRES | SIZE 56 | K = 5 | M = 100,000
 * --------------------------------------
 * BEST FITNESS:    90
 * AVERAGE FITNESS: 224.410
 *
 * 16 WIRES | SIZE 60 | K = 5 | M = 100,000
 * --------------------------------------
 * BEST FITNESS:    120
 * AVERAGE FITNESS: 319.776
 * 
 *
 * POSSIBLE BUG: Check to make sure I am performing the mutations correctly. Look into how Java
 * passes objects and returns them, etc.
 *
 * @author Jake Nocentino
 * @version Created on 9/6/19
 */
public class RandomNetworksHillClimbing {

    private static final int NUM_ITERATIONS = 5;     // K
    private static final int NUM_MUTATIONS = 100000;  // M
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
            ComparisonNetwork bestOverallNetwork = createRandomNetwork(i, wiresToSize.get(i));
            Set<BinarySequence> bestOverallOutputs = bestOverallNetwork.operateOnAllBinary();
            float avgFitness = 0;

            // perform hill climbing NUM_NETWORKS (K) times
            for (int k = 0; k < NUM_ITERATIONS; k++) {
                System.out.printf("Iteration %d on %d wires%n", k, i);
                ComparisonNetwork bestCurrentNetwork = createRandomNetwork(i, wiresToSize.get(i));
                Set<BinarySequence> bestCurrentOutputs = bestCurrentNetwork.operateOnAllBinary();
                avgFitness += bestCurrentOutputs.size();

                if (k == 0) {
                    bestOverallNetwork = bestCurrentNetwork;
                    bestOverallOutputs = bestCurrentOutputs;
                }

                // main hill-climbing algorithm done here
                for (int m = 0; m < NUM_MUTATIONS; m++) {
                    ComparisonNetwork mutatedNetwork = mutate(bestCurrentNetwork);
                    Set<BinarySequence> mutatedOutputs = mutatedNetwork.operateOnAllBinary();
                    avgFitness += mutatedOutputs.size();
                    if (mutatedOutputs.size() < bestCurrentOutputs.size()) {
                        bestCurrentNetwork = mutatedNetwork;
                        bestCurrentOutputs = mutatedOutputs;
                    }
                }
                if (bestCurrentOutputs.size() < bestOverallOutputs.size()) {
                    bestOverallNetwork = bestCurrentNetwork;
                    bestOverallOutputs =  bestCurrentOutputs;
                }
            }


            avgFitness /= NUM_ITERATIONS * NUM_MUTATIONS; // final step for calculating avg fitness

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
     * A method that mutates a network for hill-climbing algorithms.
     *
     * The mutation of a comparison network is defined by choosing an existing comparator within
     * the network at random, creating a new comparator with randomly initialized top and bottom
     * wire indices, and replacing the previously chosen existing comparator with the newly
     * created comparator.
     *
     * @param networkToMutate the network to mutate
     * @return a new network representing the mutation performed upon the parameter network
     */
    private static ComparisonNetwork mutate(ComparisonNetwork networkToMutate) {
        ComparisonNetwork newNetwork = new ComparisonNetwork(networkToMutate.numWires(), networkToMutate.getNetwork());
        int randComparatorIndex = rand.nextInt(networkToMutate.size());
        int topIndex = rand.nextInt(networkToMutate.numWires());
        int bottomIndex = rand.nextInt(networkToMutate.numWires());
        Comparator mutatedComparator = new Comparator(topIndex, bottomIndex);
        newNetwork.replace(randComparatorIndex, mutatedComparator);
        return newNetwork;
    }
}
