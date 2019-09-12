package environment;

import java.util.Arrays;

/**
 * A class that represents a binary sequence for sorting. The sequence is stored in an int array
 * and can be indexed into using the top and bottom indices of a comparator.
 *
 * @author Jake Nocentino
 * @version Created on 8/28/19
 */
public class BinarySequence {

    // the int array that stores the binary sequence
    private final int[] binarySequence;

    /**
     * A constructor that takes as input the number of wires in a comparison network and
     * initializes every element in the sequence to 0.
     *
     * @param numWires the number of wires in a comparison network
     */
    public BinarySequence(int numWires) {
        binarySequence = new int[numWires];
        for (int i = 0; i < numWires; i++)
            binarySequence[i] = 0;
    }

    /**
     * A constructor that takes as input another int array to clone.
     *
     * @param seqToClone the int array to clone
     */
    public BinarySequence(int[] seqToClone) {
        this.binarySequence = seqToClone.clone();
    }

    /**
     * Method used to get this binarySequence integer array.
     *
     * @return this objects binarySequence
     */
    public int[] getBinarySequence() { return binarySequence; }

    /**
     * Swaps two values if the value lower in the sequence is greater than the value higher in
     * the sequence.
     *
     * @param top the top wire index
     * @param bottom the bottom wire index
     */
    public void sort(int top, int bottom) {
        // if a 1 and 0 are being compared and in an unsorted position:
        if (binarySequence[top] == 1 && binarySequence[bottom] == 0) {
            binarySequence[top] = 0;
            binarySequence[bottom] = 1;
        }
    }

    /**
     * Determines if this binarySequence is in sorted order or not.
     *
     * @return true if this binarySequence is sorted, false if not.
     */
    public boolean isSorted() {
        for (int i = 0; i < binarySequence.length - 1; i++)
            if (binarySequence[i + 1] < binarySequence[i])
                return false;
        return true;
    }

    /**
     * A method that increments a binary sequence by one.
     */
    public void incrementByOne() {
        boolean carry = true;
        for (int i = (binarySequence.length - 1); i >= 0; i--) {
            if (carry) {
                if (binarySequence[i] == 0) {
                    binarySequence[i] = 1;
                    carry = false;
                } else {
                    binarySequence[i] = 0;
                    carry = true;
                }
            }
        }
    }

    /**
     * A method to check if this binarySequence is equal to all 0's to stop incrementing.
     *
     * @return false if not equal to all 0's, else true.
     */
    public boolean allZeros() {
        for (int i = 0; i < binarySequence.length; i++)
            if (binarySequence[i] != 0)
                return false;
        return true;
    }

    /**
     * Overridden equals method for use with Set and HashSet.
     *
     * @return true if this binary sequence is the same as another given binary sequence, else false
     */
    @Override
    public boolean equals(Object o) {
        // if this object is compared with itself
        if (this == o)
            return true;

        // if o is not an instance of BinarySequence
        if (!(o instanceof BinarySequence))
            return false;

        // compare data
        BinarySequence seq = (BinarySequence) o;
        for (int i = 0; i < binarySequence.length; i++)
            if (binarySequence[i] < seq.binarySequence[i] || binarySequence[i] > seq.binarySequence[i])
                return false;
        return true;
    }

    /**
     * Overridden hashCode() method for use with Set and HashSet.
     *
     * @return a hashed integer for this BinarySequence object
     */
    @Override
    public int hashCode() {
        final int PRIME1 = 7;
        final int PRIME2 = 31;
        int hash = PRIME1 * PRIME2 + Arrays.hashCode(this.binarySequence);
        return hash;
    }

    /**
     * @return a string representing this binary sequence
     */
    @Override
    public String toString() {
        String seq = "";
        for (int i = 0; i < binarySequence.length; i++)
            seq += binarySequence[i];
        return seq;
    }
}
