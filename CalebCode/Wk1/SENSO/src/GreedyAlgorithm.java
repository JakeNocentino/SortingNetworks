import java.util.*;

/**
 * @author Caleb Beard
 */

public class GreedyAlgorithm {
    private static final int NUM_WIRES = 16;
    private enum Utility {HIGHEST, MIDDLE, LOWEST}

    public static void main(String[] args) {
        while (true)
        {
            ComparisonNetwork network = new ComparisonNetwork(NUM_WIRES);

            ArrayList<Lattice> lattices = new ArrayList<>();
            for (int wire = 1; wire <= NUM_WIRES; wire++)
                lattices.add(new Lattice(NUM_WIRES, wire));

            for (int i = 1; i <= NUM_WIRES / 2; i++)
                computeSubgoal(i, lattices, network);

            HashSet<Integer> input = new HashSet<>();
            for (int i = 0; i < (int) Math.pow(2, NUM_WIRES); i++)
                input.add(i);

            System.out.println(network.getUnsorted(input));
            System.out.println(network.length());
            if (network.length() > 0)
                break;
        }
    }

    /**
     * Computes the subgoal, setting the needed values in level subgoal + 1 to false for
     * the subgoal wire, and setting all the values in level n - subgoal + 1 to true for
     * its dual wire.
     *
     * @param subgoal the subgoal being computed
     * @param lattices the lattices to keep track of the boolean function outputs
     * @param network the network to add the eventual comparator to.
     */

    private static void computeSubgoal(int subgoal, ArrayList<Lattice> lattices, ComparisonNetwork network) {
        while (true) {
            HashSet<Integer> zeros = getZeros(subgoal, lattices);
            HashSet<Integer> ones = getOnes(subgoal, lattices);

            if (zeros.isEmpty() && ones.isEmpty())
                break;

            Map<Utility, ArrayList<Comparator>> utilities = Map.of(Utility.HIGHEST, new ArrayList<>(), Utility.MIDDLE, new ArrayList<>(), Utility.LOWEST, new ArrayList<>());

            for (int wireOne = subgoal; wireOne < getDual(subgoal); wireOne++)
                for (int wireTwo = wireOne + 1; wireTwo <= getDual(subgoal); wireTwo++)
                    utilities.get(assignUtility(zeros, ones, wireOne, wireTwo)).add(new Comparator(wireOne - 1, wireTwo - 1));

            Comparator comparator = getNextComparator(utilities);
            network.addComparator(comparator);
            lattices.get(comparator.getWireOne()).addComparatorBetween(lattices.get(comparator.getWireTwo()));
        }
    }

    /**
     * Get the next comparator to be added, prioritizing by the highest utility.
     *
     * @param utilities the utilities map
     * @return the comparator to be added.
     */

    private static Comparator getNextComparator(Map<Utility, ArrayList<Comparator>> utilities) {
        if (!utilities.get(Utility.HIGHEST).isEmpty())
            return choice(utilities.get(Utility.HIGHEST));
        else if (!utilities.get(Utility.MIDDLE).isEmpty())
            return choice(utilities.get(Utility.MIDDLE));

        return choice(utilities.get(Utility.LOWEST));
    }

    /**
     * Assign a utility based on the usefulness of adding a comparator between the
     * specified two wires.
     *
     * @param zeros wires where zeros need to be collected from
     * @param ones wires where ones need to be collected from
     * @param wireOne the top wire of the comparator
     * @param wireTwo the bottom wire of the comparator
     * @return the assigned utility for the comparator at the given two wires.
     */

    private static Utility assignUtility(HashSet<Integer> zeros, HashSet<Integer> ones, int wireOne, int wireTwo) {
        if (zeros.contains(wireOne) && zeros.contains(wireTwo) && ones.contains(wireOne) && ones.contains(wireTwo))
            return Utility.HIGHEST;
        else if ((zeros.contains(wireOne) && zeros.contains(wireTwo)) || (ones.contains(wireOne) && ones.contains(wireTwo)))
            return Utility.MIDDLE;

        return Utility.LOWEST;
    }

    /**
     * Get all the wires where zeros can be collected from to compute the current subgoal.
     *
     * @param subgoal the subgoal being computed
     * @param lattices the lattices to keep track of the boolean function outputs
     * @return the set of all wires where zeros need to be collected from.
     */

    private static HashSet<Integer> getZeros(int subgoal, ArrayList<Lattice> lattices) {
        HashSet<Integer> zeros = new HashSet<>();
        ArrayList<Boolean> subgoalLevel = lattices.get(subgoal - 1).getLevel(subgoal + 1);

        if (subgoalLevel.contains(true)) {
            zeros.add(subgoal);

            for (int i = 0; i < subgoalLevel.size(); i++) {
                if (subgoalLevel.get(i)) {
                    for (int j = subgoal + 1; j <= getDual(subgoal); j++) {
                        if (!lattices.get(j - 1).getLevel(subgoal + 1).get(i)) {
                            zeros.add(j);
                            break;
                        }
                    }
                }
            }
        }

        return zeros;
    }

    /**
     * Get all the wires where ones can be collected from to compute the current dual.
     *
     * @param subgoal the subgoal being computed, its dual is the focus here
     * @param lattices the lattices to keep track of the boolean function outputs
     * @return the set of all wires where ones need to be collected from.
     */

    private static HashSet<Integer> getOnes(int subgoal, ArrayList<Lattice> lattices) {
        int dual = getDual(subgoal);
        HashSet<Integer> ones = new HashSet<>();
        ArrayList<Boolean> dualLevel = lattices.get(dual - 1).getLevel(dual);

        if (dualLevel.contains(false)) {
            ones.add(dual);

            for (int i = 0; i < dualLevel.size(); i++) {
                if (!dualLevel.get(i)) {
                    for (int j = dual - 1; j >= subgoal ; j--) {
                        if (lattices.get(j - 1).getLevel(dual).get(i)) {
                            ones.add(j);
                            break;
                        }
                    }
                }
            }
        }

        return ones;
    }

    /**
     * Gets the dual for a given subgoal.
     *
     * @param subgoal the subgoal whose dual is wanted
     * @return the dual of the subgoal wire.
     */

    private static int getDual(int subgoal) {
        return NUM_WIRES - subgoal + 1;
    }

    /**
     * Return a random element from the supplied list.
     *
     * @param comparators the list to pick from
     * @return a random comparator.
     */

    private static Comparator choice(ArrayList<Comparator> comparators) {
        Random rand = new Random();

        return comparators.get(rand.nextInt(comparators.size()));
    }

    /**
     * Prints out the lattices one by one.
     *
     * @param lattices the lattice array to be printed.
     */

    private static void printLattices(ArrayList<Lattice> lattices) {
        for (Lattice lattice : lattices)
            System.out.println(lattice);
    }
}