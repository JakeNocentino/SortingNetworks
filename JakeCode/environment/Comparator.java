package environment;

/**
 * A simple Comparator class used for the ComparisonNetwork class.
 *
 * As of right now, this class only sorts BinarySequence objects. Maybe change this in the
 * future, but it is not a problem right now.
 * 
 * @author Jake Nocentino
 * @version Created on 8/27/19
 */

public class Comparator {

    // top wire index
    private int topWire;

    // bottom wire index
    private int bottomWire;

    /**
     * Allows the user to specify the wire index for the Comparator to be placed.
     *
     * The Math.min(x, y) and Math.max(x, y) methods are used because, when generating randomly
     * indexed comparators for any given reason, it is impossible to tell what int is the desired
     * int to pass to topWire and bottomWire.
     *
     * @param topWire the top wire index
     * @param bottomWire the bottom wire index
     */
    public Comparator(int topWire, int bottomWire) {
        this.topWire = Math.min(topWire, bottomWire);
        this.bottomWire = Math.max(topWire, bottomWire);
    }

    /**
     * Sorts two binary values in a binarySequence of numbers. The method indexes into a binarySequence
     * using the top and bottom index and determines if the values need must be swapped.
     *
     * @param binarySequence a BinarySequence object
     */
    public void sort(BinarySequence binarySequence) {
        if (binarySequence.getBinarySequence()[topWire] == 1 &&
                binarySequence.getBinarySequence()[bottomWire] == 0) {
            binarySequence.getBinarySequence()[topWire] = 0;
            binarySequence.getBinarySequence()[bottomWire] = 1;
        }
    }

    /**
     * @return the indices of the top and bottom wires.
     */
    @Override
    public String toString() {
        return "Top: " + topWire + "\tBot: " + bottomWire;
    }
}