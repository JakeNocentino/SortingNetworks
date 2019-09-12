package environment;

import java.util.Random;
import java.util.Set;

/**
 * A class for managing many different kinds of useful utilities that one can use in a
 * comparison network.
 *
 * @author Jake Nocentino
 * @version Created on 8/28/19
 */
public class NetworkUtilities {

    private static Random rand = new Random();

    /**
     * Runs Green32 and returns all 151 unsorted outputs.
     *
     * @return the 151 unsorted outputs from running all binary inputs on Green32.
     */
    public static Set<BinarySequence> getGreen32Outputs() {
        ComparisonNetwork green32 = createGreen32();
        return green32.operateOnAllBinary();
    }

    /**
     * Creates a ComparisonNetwork containing randomly assigned comparators.
     *
     * @return a random ComparisonNetwork object
     */
    public static ComparisonNetwork createRandomNetwork(int numWires, int numComparators) {

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
    public static ComparisonNetwork hillClimbMutate(ComparisonNetwork networkToMutate) {
        ComparisonNetwork newNetwork = new ComparisonNetwork(networkToMutate.numWires(), networkToMutate.getNetwork());

        // randomize indices
        int randComparatorIndex = rand.nextInt(networkToMutate.size());
        int topIndex = rand.nextInt(networkToMutate.numWires());
        int bottomIndex = rand.nextInt(networkToMutate.numWires());

        Comparator mutatedComparator = new Comparator(topIndex, bottomIndex);
        newNetwork.replace(randComparatorIndex, mutatedComparator);
        return newNetwork;
    }

    /**
     * Prints all of the unsorted outputs for the Set of BinarySequences provided as the argument.
     * @param unsortedOutputs the Set of BinarySequence outputs that are unsorted
     */
    public static void printUnsortedOutputs(Set<BinarySequence> unsortedOutputs) {
        int i = 0;
        for (BinarySequence seq : unsortedOutputs)
            System.out.println(++i + ".) " + seq);
    }

    /**
     * Create and returns the first 32 comparisons of the Green sorting network.
     *
     * @return a ComparisonNetwork object of the first 32 comparisons of Green's sorting network.
     */
    private static ComparisonNetwork createGreen32() {
        final int NUM_WIRES = 16;
        ComparisonNetwork green32 = new ComparisonNetwork(NUM_WIRES);

        // using some big brain logic to create Green32
        for (int i = 0; i < NUM_WIRES; i += 2)
            green32.addComparator(i, i + 1);
        for (int i = 0; i < NUM_WIRES; i += 4) {
            green32.addComparator(i, i + 2);
            green32.addComparator(i + 1, i + 3);
        }
        for (int i = 0; i < 4; i++) {
            green32.addComparator(i, i + 4);
            green32.addComparator(i + 8, i + 12);
        }
        for (int i = 0; i < 8; i++)
            green32.addComparator(i, i + 8);
        return green32;
    }
}