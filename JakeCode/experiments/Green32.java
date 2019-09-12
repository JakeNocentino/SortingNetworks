package experiments;

import environment.BinarySequence;
import environment.ComparisonNetwork;
import environment.NetworkUtilities;

import java.util.Set;

/**
 * An experiment simulating the first 32 comparisons of Green's network.
 *
 * A correct run of just Green32 should produce 151 unsorted binary outputs.
 *
 * @author Jake Nocentino
 * @version Created on 8/28/19
 */
public class Green32 {
    public static void main(String args[]) {
//        ComparisonNetwork green32 = NetworkUtilities.createGreen32();
//        Set<BinarySequence> unsortedOutputs = green32.operateOnAllBinary();

        Set<BinarySequence> unsortedOutputs = NetworkUtilities.getGreen32Outputs();

        System.out.println("Green32's Unsorted Outputs:\n");
        NetworkUtilities.printUnsortedOutputs(unsortedOutputs);
        System.out.println(unsortedOutputs.size());
    }
}
