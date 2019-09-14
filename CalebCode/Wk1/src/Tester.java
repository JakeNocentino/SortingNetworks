import java.util.*;

/**
 * A main script to test all of the random networks with pure randomness, hill climbing,
 * and simulated annealing.
 *
 * @author Caleb Beard
 */

public class Tester  {
    private static final int NUM_TEST_CASES = 100000;
    private static final int NUM_ALGO_TESTS = 10;
    private static final Map<Integer, Integer> BEST_KNOWN_COMPARISONS = Map.of(12, 39, 13, 45, 14, 51, 15, 56, 16, 60);

    private enum Algorithm {HC, SA}

    public static void main(String[] args) {
        HashSet<ArrayList<Integer>> inputs;

        for (int wires = 12; wires <= 16; wires++) {
            inputs = ComparisonNetwork.getAllBinarySequences(wires);

            randomNetworks(wires, inputs, BEST_KNOWN_COMPARISONS.get(wires));
            runAlgorithm(wires, Algorithm.HC, inputs, BEST_KNOWN_COMPARISONS.get(wires));
            runAlgorithm(wires, Algorithm.SA, inputs, BEST_KNOWN_COMPARISONS.get(wires));
        }

        ComparisonNetwork green32 = getGreen32();
        inputs = green32.getUnsorted(ComparisonNetwork.getAllBinarySequences(16));

        randomNetworks(16, inputs, 28);
        runAlgorithm(16, Algorithm.HC, inputs, 28);
        runAlgorithm(16, Algorithm.SA, inputs, 28);
    }

    /**
     * Generate and test random networks for the number of unique unsorted outputs they produce.
     *
     * @param wires the number of wires in the network
     * @param inputs the inputs to test
     * @param numComparisons number of comparisons to use in the network
     */

    private static void randomNetworks(int wires, HashSet<ArrayList<Integer>> inputs, int numComparisons) {
        long start = System.currentTimeMillis();
        int bestFitness = (int) Math.pow(2, wires);
        int totalFitness = 0;
        int fitness;

        ComparisonNetwork network = new ComparisonNetwork(wires);

        for (int i = 0; i < NUM_TEST_CASES; i++) {
            network.randomize(numComparisons);
            fitness = network.getUnsorted(inputs).size();
            totalFitness += fitness;

            if (fitness < bestFitness)
                bestFitness = fitness;
        }

        double avgFitness = (double) totalFitness / NUM_TEST_CASES;
        System.out.println(wires + " wires, random.");
        System.out.println("Number of networks: " + NUM_TEST_CASES);
        System.out.println("Total number of inputs: " + inputs.size());
        System.out.println("Best fitness: " + bestFitness);
        System.out.println("Average fitness: " + avgFitness);
        System.out.println("TIME: " + (System.currentTimeMillis() - start) / 1000.0 + " seconds.");
        System.out.println();
    }

    /**
     * Runs the specified algorithm (hill climbing or simulated annealing) on random
     * networks and tests their fitness.
     *
     * @param wires the number of wires for the networks
     * @param algorithm the algorithm to use
     * @param inputs the inputs for testing
     * @param numComparisons the number of comparisons to use
     */

    private static void runAlgorithm(int wires, Algorithm algorithm, HashSet<ArrayList<Integer>> inputs, int numComparisons) {
        long start = System.currentTimeMillis();
        Random rand = new Random();
        int bestFitness = (int) Math.pow(2, wires);
        int totalFitness = 0;

        ComparisonNetwork network = new ComparisonNetwork(wires);
        ComparisonNetwork mutated;

        for (int i = 0; i < NUM_ALGO_TESTS; i++) {
            network.randomize(numComparisons);
            int bestIterFitness = network.getUnsorted(inputs).size();
            totalFitness += bestIterFitness;

            for (int j = 1; j < NUM_TEST_CASES / NUM_ALGO_TESTS; j++) {
                mutated = mutate(network);
                int fitness = mutated.getUnsorted(inputs).size();

                // If the algorithm is HC, only accept the mutation if it produces a better network.
                // Otherwise, if the algorithm is SA, based on a probability sometimes a worse network may be accepted.
                if (fitness < bestIterFitness) {
                    network = mutated;
                    bestIterFitness = fitness;
                } else if (algorithm == Algorithm.SA) {
                    int probability = NUM_TEST_CASES / NUM_ALGO_TESTS - j;
                    if (rand.nextInt(NUM_TEST_CASES / NUM_ALGO_TESTS) < probability)
                        network = mutated;
                }

                totalFitness += fitness;
            }

            if (bestIterFitness < bestFitness)
                bestFitness = bestIterFitness;
        }

        double avgFitness = (double) totalFitness / NUM_TEST_CASES;
        System.out.println(wires + " wires, " + (algorithm == Algorithm.SA ? "simulated annealing" : "hill climbing") + ".");
        System.out.println("Number of networks: " + NUM_TEST_CASES);
        System.out.println("Total number of inputs: " + inputs.size());
        System.out.println("Best fitness: " + bestFitness);
        System.out.println("Average fitness: " + avgFitness);
        System.out.println("TIME: " + (System.currentTimeMillis() - start) / 1000.0 + " seconds.");
        System.out.println();
    }

    /**
     * Defines what it means to mutate a comparison network.
     *
     * @param network the network to mutate
     * @return the mutated network
     */

    private static ComparisonNetwork mutate(ComparisonNetwork network) {
        // This definition of mutation is:
        //      Get a random index
        //      Generate a random comparator and replace the one at that index
        ComparisonNetwork mutated = new ComparisonNetwork(network.getWires(), new ArrayList<>(network.getComparisons()));
        Random rand = new Random();
        int index = rand.nextInt(network.getWires());

        mutated.getComparisons().set(index, Comparator.getRandom(network.getWires()));

        return mutated;
    }

    /**
     * Generates the Green32 network
     *
     * @return the Green32 network
     */

    private static ComparisonNetwork getGreen32() {
        ArrayList<Comparator> comparators = new ArrayList<>();

        comparators.add(new Comparator(0, 1));
        comparators.add(new Comparator(2, 3));
        comparators.add(new Comparator(4, 5));
        comparators.add(new Comparator(6, 7));
        comparators.add(new Comparator(8, 9));
        comparators.add(new Comparator(10, 11));
        comparators.add(new Comparator(12, 13));
        comparators.add(new Comparator(14, 15));
        comparators.add(new Comparator(0, 2));
        comparators.add(new Comparator(1, 3));
        comparators.add(new Comparator(4, 6));
        comparators.add(new Comparator(5, 7));
        comparators.add(new Comparator(8, 10));
        comparators.add(new Comparator(9, 11));
        comparators.add(new Comparator(12, 14));
        comparators.add(new Comparator(13, 15));
        comparators.add(new Comparator(0, 4));
        comparators.add(new Comparator(1, 5));
        comparators.add(new Comparator(2, 6));
        comparators.add(new Comparator(3, 7));
        comparators.add(new Comparator(8, 12));
        comparators.add(new Comparator(9, 13));
        comparators.add(new Comparator(10, 14));
        comparators.add(new Comparator(11, 15));
        comparators.add(new Comparator(0, 8));
        comparators.add(new Comparator(1, 9));
        comparators.add(new Comparator(2, 10));
        comparators.add(new Comparator(3, 11));
        comparators.add(new Comparator(4, 12));
        comparators.add(new Comparator(5, 13));
        comparators.add(new Comparator(6, 14));
        comparators.add(new Comparator(7, 15));

        ComparisonNetwork Green32 = new ComparisonNetwork(16, comparators);

        return Green32;
    }
}
