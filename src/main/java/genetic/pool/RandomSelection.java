package genetic.pool;

import genetic.ADN;
import genetic.GeneticAlgorithm;

import java.util.List;
import java.util.Random;

public class RandomSelection<D> extends MatingPool<D> {

    public RandomSelection(List<ADN<D>> origin) {
        super(origin);
    }

    @Override
    public ADN<D> getRandomParent() {
        Random rand = new Random();
        return super.origin.get(rand.nextInt(GeneticAlgorithm.numPop));
    }

}
