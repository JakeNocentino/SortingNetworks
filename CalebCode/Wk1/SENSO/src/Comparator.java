import java.util.Random;

/**
 * A representation of a comparator in a comparison network.
 *
 * @author Caleb Beard
 */

public class Comparator {
    private int wireOne;
    private int wireTwo;

    public Comparator(int wireOne, int wireTwo) {
        this.wireOne = wireOne;
        this.wireTwo = wireTwo;
    }

    /**
     * A static method to get a random comparator operating on a set number of wires.
     *
     * @param wires the number of wires the comparator can choose from
     * @return the randomly generated comparator
     */

    public static Comparator getRandom(int wires) {
        Random rand = new Random();
        int wireOne = rand.nextInt(wires);
        int wireTwo = rand.nextInt(wires);

        while (wireTwo == wireOne)
            wireTwo = rand.nextInt(wires);

        return new Comparator(wireOne < wireTwo ? wireOne : wireTwo, wireOne < wireTwo ? wireTwo : wireOne);
    }

    public int getWireOne() { return wireOne; }

    public int getWireTwo() { return wireTwo; }

    @Override
    public String toString() { return "(" + wireOne + ", " + wireTwo + ")"; }
}
