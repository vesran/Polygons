package genetic.pool;

import genetic.ADN;
import genetic.GeneticAlgorithm;
import genetic.Measure;
//import genetic.Measure;

import java.util.*;

public class TournamentSelection<D> extends MatingPool<D> {

    private SortedSet<ADN<D>> selected;
    private final int nbSelected = GeneticAlgorithm.numPop / 2;
    private final double p = 1;

    public TournamentSelection(List<ADN<D>> origin) {
        super(origin);
        this.selected = new TreeSet<>((e1, e2) -> -Measure.compFitness(e1, e2));

        Random rand = new Random();
        for (int i = 0; i < this.nbSelected; i++) {
            this.selected.add(super.origin.get(rand.nextInt(GeneticAlgorithm.numPop)));
        }
    }

    @Override
    public ADN<D> getRandomParent() {
        Random rand = new Random();
        for (ADN<D> possibleParent : this.selected) {
            if (rand.nextDouble() < p) {
                return possibleParent;
            }
        }
        return selected.first();
    }

}
