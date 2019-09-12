package experiments;

import environment.BinarySequence;
import environment.ComparisonNetwork;
import environment.NetworkUtilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * In this setting, we create random networks for 12-16 wires of size equal to the smallest known
 * sorting networks (e.g, for 16 wires 60 comparisons).
 *
 * When we refer to fitness, we mean the number of unsorted outputs generated from all binary
 * sequences ran on a given comparison network.
 *
 * NUM NETWORKS GENERATES:  10,000
 *
 * 12 WIRES | SIZE 39
 * -------------------
 * BEST FITNESS:    37
 * AVERAGE FITNESS: 93.878
 *
 * 13 WIRES | SIZE 45
 * -------------------
 * BEST FITNESS:    51
 * AVERAGE FITNESS: 123.909
 *
 * 14 WIRES | SIZE 51
 * -------------------
 * BEST FITNESS:    64
 * AVERAGE FITNESS: 163.162
 *
 * 15 WIRES | SIZE 56
 * -------------------
 * BEST FITNESS: 88
 * AVERAGE FITNESS: 226.469
 *
 * 16 WIRES | SIZE 60
 * -------------------
 * BEST FITNESS: 148
 * AVERAGE FITNESS: 319.941
 *
 * CURRENT BUG: investigate and make sure using Random correctly.
 *
 * @author Jake Nocentino
 * @version Created on 9/6/19
 */
public class RandomNetworksNormal {

    private static final int NUM_NETWORKS = 10000;
    private static final int MIN_WIRES = 12;
    private static final int MAX_WIRES = 17;
    private static final boolean PRINT_DATA_FLAG = false;
    private static Random rand = new Random();

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
            ComparisonNetwork bestNetwork = new ComparisonNetwork(i);
            Set<BinarySequence> bestUnsortedOutputs = new HashSet<>();
            float avgFitness = 0;

            // create NUM_NETWORKS amount of random networks
            for (int j = 0; j < NUM_NETWORKS; j++) {
                ComparisonNetwork randomNetwork = createRandomNetwork(i, wiresToSize.get(i));
                Set<BinarySequence> unsortedOutputs = randomNetwork.operateOnAllBinary();
                avgFitness += unsortedOutputs.size();
                if (j == 0) {
                    bestUnsortedOutputs = unsortedOutputs; // make first output the best
                    bestNetwork = randomNetwork;
                }
                if (unsortedOutputs.size() < bestUnsortedOutputs.size()) {
                    bestUnsortedOutputs = unsortedOutputs;
                    bestNetwork = randomNetwork;
                }
            }

            avgFitness /= NUM_NETWORKS; // final step for calculating avg fitness

            System.out.printf("Best fitness on %d wires with size %d: %d%n", i,
                    wiresToSize.get(i), bestUnsortedOutputs.size());
            System.out.printf("Average fitness on %d wires with size %d: %.3f%n%n", i,
                    wiresToSize.get(i), avgFitness);

            if (PRINT_DATA_FLAG) {
                NetworkUtilities.printUnsortedOutputs(bestUnsortedOutputs);
                System.out.printf("The %d-comparison network that produced these outputs:%n", wiresToSize.get(i));
                System.out.println(bestNetwork);
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
}
