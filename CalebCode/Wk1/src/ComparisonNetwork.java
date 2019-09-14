import java.util.*;

/**
 * A blueprint for a comparison network on a certain number of wires.
 *
 * @author Caleb Beard
 */

public class ComparisonNetwork {
    private int wires;
    private List<Comparator> comparisons;

    public ComparisonNetwork(int wires) {
        this.wires = wires;
        this.comparisons = new ArrayList<>();
    }

    public ComparisonNetwork(int wires, ArrayList<Comparator> comparisons) {
        this.wires = wires;
        this.comparisons = comparisons;
    }

    /**
     * A static method to get all of the binary sequences available for testing on a
     * certain number of wires (2^wires)
     *
     * @param wires the number of wires in a network
     * @return a set of lists, each of which is a unique binary sequence
     */

    public static HashSet<ArrayList<Integer>> getAllBinarySequences(int wires) {
        HashSet<ArrayList<Integer>> sequences = new HashSet<>();

        for (int i = 0; i < Math.pow(2, wires); i++) {
            char[] val = Integer.toBinaryString(i).toCharArray();
            ArrayList<Integer> intArrayOfVal = new ArrayList<>();

            for (char c : val)
                intArrayOfVal.add(Character.getNumericValue(c));

            while (intArrayOfVal.size() < wires)
                intArrayOfVal.add(0, 0);

            sequences.add(intArrayOfVal);
        }

        return sequences;
    }

    /**
     * Randomize the comparators in this network.
     *
     * @param numComparisons the number of comparators to be used.
     */

    public void randomize(int numComparisons) {
        if (comparisons.isEmpty())
            for (int i = 0; i < numComparisons; i++)
                comparisons.add(Comparator.getRandom(wires));
        else
            for (int i = 0; i < numComparisons; i++)
                comparisons.set(i, Comparator.getRandom(wires));
    }

    /**
     * Run the inputs through the network and produce an output.
     *
     * @param input the input to be run
     * @return the output of the network
     */

    public ArrayList<Integer> run(ArrayList<Integer> input) {
        int wireOne;
        int wireTwo;

        for (Comparator comparator : comparisons) {
            wireOne = input.get(comparator.getWireOne());
            wireTwo = input.get(comparator.getWireTwo());

            if (wireOne > wireTwo) {
                input.set(comparator.getWireOne(), wireTwo);
                input.set(comparator.getWireTwo(), wireOne);
            }
        }

        return input;
    }

    /**
     * Gets the set of unsorted outputs given by running a set of inputs through this network.
     *
     * @param inputs the set of inputs to test
     * @return the set of unsorted outputs
     */

    public HashSet<ArrayList<Integer>> getUnsorted(HashSet<ArrayList<Integer>> inputs) {
        HashSet<ArrayList<Integer>> unsorted = new HashSet<>();
        ArrayList<Integer> output;

        for (ArrayList<Integer> input : inputs) {
            output = run(new ArrayList<>(input));

            for (int i = 0; i < output.size() - 1; i++)
                if (output.get(i) > output.get(i + 1)) {
                    unsorted.add(output);
                    break;
                }
        }

        return unsorted;
    }

    public int getWires() { return wires; }

    public List<Comparator> getComparisons() { return comparisons; }
}
