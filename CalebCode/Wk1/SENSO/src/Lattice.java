import java.util.ArrayList;
import java.util.HashMap;

/**
 * A structure representing a boolean lattice, with each level being arranged in
 * increasing numerical order.
 *
 * @author Caleb Beard
 */
public class Lattice {
    private HashMap<Integer, ArrayList<Boolean>> lattice;
    private int numWires;

    public Lattice(int numWires, int wire) {
        this.numWires = numWires;
        lattice = new HashMap<>();

        for (int i = 0; i < (int) Math.pow(2, numWires); i++) {
            int level = numWires - Integer.bitCount(i) + 1;
            String node = Integer.toBinaryString(i);
            node = zfill(node, numWires);

            if (!lattice.containsKey(level))
                lattice.put(level, new ArrayList<>());

            lattice.get(level).add(node.charAt(wire - 1) == '1');
        }
    }

    /**
     * Computes the proper conjunction and disjunction when a comparator is added between
     * this lattice and another.  It is assumed that this one is on the LHS.
     *
     * @param rhs the rhs lattice to add the comparator with.
     */

    public void addComparatorBetween(Lattice rhs) {
        for (int key : lattice.keySet()) {
            for (int i = 0; i < lattice.get(key).size(); i++) {
                boolean lhsVal = lattice.get(key).get(i);
                boolean rhsVal = rhs.lattice.get(key).get(i);

                lattice.get(key).set(i, lhsVal && rhsVal);
                rhs.lattice.get(key).set(i, lhsVal || rhsVal);
            }
        }
    }

    /**
     * Get the wanted level of the lattice.
     *
     * @param level the level to get
     * @return the wanted level.
     */

    public ArrayList<Boolean> getLevel(int level) {
        return lattice.get(level);
    }

    /**
     * Pads the given string on the left with zeros until it fills a given length.
     *
     * @param string the string to pad
     * @param length the length to fill
     * @returnthe padded string.
     */

    private String zfill(String string, int length) {
        while (string.length() != length)
            string = "0" + string;

        return string;
    }

    @Override
    public String toString() {
        return lattice.toString();
    }
}
