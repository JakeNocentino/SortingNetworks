package environment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A class that represents a comparison network. This class internally represents a comparison
 * network as an ArrayList of Comparator objects.
 *
 * @author Jake Nocentino
 * @version Created on 8/27/19
 */

public class ComparisonNetwork {

    // the comparison network
    private final ArrayList<Comparator> network;

    // the number of wires this comparison network contains
    private int numWires;

    /**
     * @param numWires the number of wires this sorting network contains
     * @param comparators the ArrayList of comparators (can also be empty)
     */
    public ComparisonNetwork(int numWires, ArrayList<Comparator> comparators) {
        this.numWires = numWires;
        this.network = comparators;
    }

    /**
     * @param numWires the number of wires this sorting network contains
     */
    public ComparisonNetwork(int numWires) {
        this.numWires = numWires;
        this.network = new ArrayList<>();
    }

    /**
     * A method that evaluates the network on a given input.
     *
     * @param binarySequence the input binarySequence fed into the network
     */
    public void operateOnOneBinary(BinarySequence binarySequence) {
        for (Comparator comparator : network) {
            comparator.sort(binarySequence);
        }
    }

    /**
     * A method to evaluate a certain Set of binarySequences.
     *
     * @param binarySequences the Set of binarySequences to evaluate
     * @return the unsortedOutputs from the Set of binarySequences
     */
    public Set<BinarySequence> operateOnTheseBinary(Set<BinarySequence> binarySequences) {
        Set<BinarySequence> unsortedOutputs = new HashSet<>();
        for (BinarySequence seq : binarySequences) {
            BinarySequence testSeq = new BinarySequence(seq.getBinarySequence());//seq.clone();
            operateOnOneBinary(testSeq);
            if (!testSeq.isSorted())
                unsortedOutputs.add(testSeq);
        }
        return unsortedOutputs;
    }

    /**
     * A method that evaluates the network on all binary inputs up until numWires.
     *
     * Here we ignore the case of a binary sequence of all zeros, because we know that is
     * already sorted.
     *
     * @return the unsorted binarySequence outputs produced by this comparison network
     */
    public Set<BinarySequence> operateOnAllBinary() {
        Set<BinarySequence> unsortedOutputs = new HashSet<>();
        BinarySequence binarySequence = new BinarySequence(numWires);

        // we already know all 0's is sorted, so increment by one to begin with
        binarySequence.incrementByOne();

        // while the sequence is not all 0's (will become all zero again once it is all 1's)
        while (!binarySequence.allZeros()) {
            BinarySequence testSeq = new BinarySequence(binarySequence.getBinarySequence());
            operateOnOneBinary(testSeq);
            if (!testSeq.isSorted())
                unsortedOutputs.add(testSeq);
            binarySequence.incrementByOne();
        }
        return unsortedOutputs;
    }

    /**
     * Adds a comparator to this sorting network.
     *
     * @param topWire the top wire index
     * @param bottomWire the bottom wire index
     */
    public void addComparator(int topWire, int bottomWire) {
        network.add(new Comparator(topWire, bottomWire));
    }

    /**
     * Adds a comparator to this sorting network.
     *
     * @param i the index in the list to be added to
     * @param topWire the top wire index
     * @param bottomWire the bottom wire index
     */
    public void addComparator(int i, int topWire, int bottomWire) {
        network.add(i, new Comparator(topWire, bottomWire));
    }

    /**
     * @return the number of comparators in this ComparisonNetwork object
     */
    public int size() {
        return network.size();
    }


    /**
     * @return the number of wires that this ComparisonNetwork object operates on
     */
    public int numWires() {
        return numWires;
    }

    /**
     * @return the ArrayList of Comparators
     */
    public ArrayList<Comparator> getNetwork() {
        return network;
    }

    /**
     * Replaces the comparator (at the supplied index argument) within this ComparisonNetwork
     * with the Comparator argument
     *
     * @param index the index of the comparator to be replaced
     * @param c the new Comparator object to replace the old one
     */
    public void replace(int index, Comparator c) {
        network.set(index, c);
    }

    /**
     * @return
     */
    public ComparisonNetwork clone() {
        return new ComparisonNetwork(this.size(), this.network);
    }

    /**
     * Overrides the toString() method to print out an accurate String representation of the
     * sorting network.
     *
     * @return String representation of this sorting network
     */
    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < network.size(); i++) {
            str += network.get(i).toString();
            str += "\n";
        }
        return str;
    }
}