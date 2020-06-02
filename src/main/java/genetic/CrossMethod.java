package genetic;

import java.util.Iterator;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

public class CrossMethod {

    static <D> ADN<D> singlePoint(ADN<D> current, ADN<D> other) {
        Random rand = new Random();
        int point = rand.nextInt(GeneticAlgorithm.chromosomeSize);
        SortedSet<D> mix = new TreeSet<>(current.getComp());
        Iterator<D> itThis = current.getChromosome().iterator();
        Iterator<D> itOther = other.getChromosome().iterator();
        int i = 0;
        while (itThis.hasNext()) {
            if (i++ >= point) {
                itThis.next();
                mix.add(itOther.next());
            } else {
                mix.add(itThis.next());
                itOther.next();
            }
        }
        return new ADN<>(mix, current.getComp(), current.getGenerator(), current.getFitnessFunc());
    }
}
