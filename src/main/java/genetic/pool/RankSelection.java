package genetic.pool;

import genetic.ADN;
import genetic.GeneticAlgorithm;

import java.util.List;

public class RankSelection<D> extends RouletteWheel<D> {

    public RankSelection(List<ADN<D>> origin) {
        super(origin);
        double lowerBound = 0;
        double upperBound;

        // Turn fitness to prob and create interval
        int i = 1;
        int sum = (GeneticAlgorithm.numPop * (GeneticAlgorithm.numPop + 1) / 2);
        for (ADN<D> adn : origin) {
            upperBound = lowerBound + (double)i / sum;
            super.pool.put(new Interval(lowerBound, upperBound), adn);
            lowerBound = upperBound;
        }
    }
}
