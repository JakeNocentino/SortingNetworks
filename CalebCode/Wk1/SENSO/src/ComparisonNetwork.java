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
     * Run the inputs through the network and produce an output.
     *
     * @param input the input to be run
     * @return the output of the network
     */

    public int run(int input) {
        int wireOne, wireTwo, bitOne, bitTwo, intermediate, result;

        for (Comparator comparator : comparisons) {
            wireOne = comparator.getWireOne();
            wireTwo = comparator.getWireTwo();

            bitOne = (input >> wireOne) & 1;
            bitTwo = (input >> wireTwo) & 1;
            intermediate = (bitOne ^ bitTwo);

            intermediate = (intermediate << wireOne) | (intermediate << wireTwo);
            result = input ^ intermediate;

            if (result < input)
                input = result;
        }

        return input;
    }

    /**
     * Gets the set of unsorted outputs given by running a set of inputs through this network.
     *
     * @param inputs the set of inputs to test
     * @return the set of unsorted outputs
     */

    public HashSet<Integer> getUnsorted(HashSet<Integer> inputs) {
        HashSet<Integer> unsorted = new HashSet<>();
        int output;

        for (int input : inputs) {
            output = run(input);

            if (!(output == 0 || Integer.toBinaryString(output).length() == Integer.bitCount(output)))
                unsorted.add(output);
        }

        return unsorted;
    }

    /**
     * Appends a comparator to this network.
     *
     * @param comparator the comparator to append
     */

    public void addComparator(Comparator comparator) {
        comparisons.add(comparator);
    }

    public int length() {
        return comparisons.size();
    }

    @Override
    public String toString()
    {
        return comparisons.toString();
    }
}
