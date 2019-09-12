package experiments;

import environment.BinarySequence;
import environment.ComparisonNetwork;
import environment.NetworkUtilities;

import java.util.Random;
import java.util.Set;

/**
 * A program that produces a number (specified by the user) of random networks on 16 wires with
 * 28 comparisons whose input is the unsorted outputs of Green32. The best network (the network
 * with the smallest amount of unsorted outputs) is then shown.
 *
 * BEST FITNESS: 29 unsorted outputs
 *
 * @author Jake Nocentino
 * @version Created on 8/29/19
 */
public class RandomCompleter {

    private static final int NUM_NETWORKS = 1000000;
    private static final int NUM_COMPARATORS = 28;
    private static final int NUM_WIRES = 16;
    private static Random rand = new Random();

    public static void main(String[] args) {

        ComparisonNetwork bestNetwork = new ComparisonNetwork(NUM_WIRES);
        Set<BinarySequence> unsortedGreenOutputs = NetworkUtilities.getGreen32Outputs();
        Set<BinarySequence> bestUnsortedOutputs = unsortedGreenOutputs;

        for (int i = 0; i < NUM_NETWORKS; i++) {
            //ComparisonNetwork randomNetwork = NetworkUtilities.createRandomNetwork(NUM_WIRES, NUM_COMPARATORS);
            ComparisonNetwork randomNetwork = createRandomNetwork();
            Set<BinarySequence> unsortedOutputs = randomNetwork.operateOnTheseBinary(unsortedGreenOutputs);
            if (unsortedOutputs.size() < bestUnsortedOutputs.size()) {
                bestUnsortedOutputs = unsortedOutputs;
                bestNetwork = randomNetwork;
            }
        }
        System.out.println("Best unsorted outputs from random completion on Green32:\n");
        NetworkUtilities.printUnsortedOutputs(bestUnsortedOutputs);
        System.out.println("The 28-comparison network that produced these outputs:\n");
        System.out.println(bestNetwork);
    }

    /**
     * Creates a ComparisonNetwork containing randomly assigned comparators.
     *
     * @return a random ComparisonNetwork object
     */
    private static ComparisonNetwork createRandomNetwork() {

        ComparisonNetwork randomNetwork = new ComparisonNetwork(NUM_WIRES);
        for (int i = 0; i < NUM_COMPARATORS; i++) {
            int randI = rand.nextInt(i + 1);
            int randTop = rand.nextInt(NUM_WIRES);
            int randBottom = rand.nextInt(NUM_WIRES);
            randomNetwork.addComparator(randI, randTop, randBottom);
        }
        return randomNetwork;
    }
}
