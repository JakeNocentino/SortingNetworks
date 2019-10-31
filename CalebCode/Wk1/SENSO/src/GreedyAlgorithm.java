import java.util.*;

/**
 * @author Caleb Beard
 */

public class GreedyAlgorithm {
    private static final int NUM_WIRES = 16;

    public static void main(String[] args) {
        while (true)
        {
            ComparisonNetwork network = new ComparisonNetwork(NUM_WIRES);

            ArrayList<Lattice> lattices = new ArrayList<>();
            for (int wire = 1; wire <= NUM_WIRES; wire++)
                lattices.add(new Lattice(NUM_WIRES, wire));

//        printLattices(lattices);

            for (int i = 1; i <= NUM_WIRES / 2; i++)
            {
                computeSubgoal(i, lattices, network);
            }

//        printLattices(lattices);
//        System.out.println(network);

            HashSet<Integer> input = new HashSet<>();
            for (int i = 0; i < (int) Math.pow(2, NUM_WIRES); i++)
            {
                input.add(i);
            }

//        System.out.println(input);
        System.out.println(network.getUnsorted(input));
            System.out.println(network.length());
            if (network.length() > 0)
                break;
        }
    }

    private static void computeSubgoal(int subgoal, ArrayList<Lattice> lattices, ComparisonNetwork network) {
        int dual = NUM_WIRES - subgoal + 1;

        while (true) {
            HashSet<Integer> zeros = new HashSet<>();
            HashSet<Integer> ones = new HashSet<>();

            ArrayList<Boolean> subgoalLevel = lattices.get(subgoal - 1).getLevel(subgoal + 1);
            ArrayList<Boolean> dualLevel = lattices.get(NUM_WIRES - subgoal).getLevel(dual);

            if (subgoalLevel.contains(true)) {
                zeros.add(subgoal);

                for (int i = 0; i < subgoalLevel.size(); i++) {
                    if (subgoalLevel.get(i)) {
                        for (int j = subgoal + 1; j <= dual; j++) {
                            if (!lattices.get(j - 1).getLevel(subgoal + 1).get(i)) {
                                zeros.add(j);
                                break;
                            }
                        }
                    }
                }
            }

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

            if (zeros.isEmpty() && ones.isEmpty())
                break;

            Map<Integer, ArrayList<Comparator>> utilities = Map.of(2, new ArrayList<>(), 1, new ArrayList<>(), 0, new ArrayList<>());

            for (int wireOne = subgoal; wireOne < dual; wireOne++) {
                for (int wireTwo = wireOne + 1; wireTwo <= dual; wireTwo++) {
                    Comparator comparator = new Comparator(wireOne - 1, wireTwo - 1);

                    if (zeros.contains(wireOne) && zeros.contains(wireTwo) && ones.contains(wireOne) && ones.contains(wireTwo))
                        utilities.get(2).add(comparator);
                    else if ((zeros.contains(wireOne) && zeros.contains(wireTwo)) || (ones.contains(wireOne) && ones.contains(wireTwo)))
                        utilities.get(1).add(comparator);
                    else
                        utilities.get(0).add(comparator);
                }
            }

            Comparator comparator;
            if (!utilities.get(2).isEmpty())
                comparator = choice(utilities.get(2));
            else if (!utilities.get(1).isEmpty())
                comparator = choice(utilities.get(1));
            else
                comparator = choice(utilities.get(0));

            network.addComparator(comparator);
            lattices.get(comparator.getWireOne()).addComparatorBetween(lattices.get(comparator.getWireTwo()));
        }
    }

    private static Comparator choice(ArrayList<Comparator> comparators) {
        Random rand = new Random();

        return comparators.get(rand.nextInt(comparators.size()));
    }

    private static void printLattices(ArrayList<Lattice> lattices) {
        for (Lattice lattice : lattices)
            System.out.println(lattice);
    }
}