package genetic.pool;

import genetic.ADN;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class RouletteWheel<D> extends MatingPool<D> {

    public RouletteWheel(List<ADN<D>> origin) {
        super(origin);
        double lowerBound = 0;
        double upperBound;

        // Turn fitness to prob and create interval
        double totalFitness = 0;
        for (ADN<D> adn : origin) {
            totalFitness += adn.getFitness();
        }
        for (ADN<D> adn : origin) {
            upperBound = lowerBound + adn.getFitness() / totalFitness;
            super.pool.put(new Interval(lowerBound, upperBound), adn);
            lowerBound = upperBound;
        }
    }

    @Override
    public ADN<D> getRandomParent() {
        Random rand = new Random();
        double d = rand.nextDouble();
        for (Map.Entry<Interval, ADN<D>> pair : this.pool.entrySet()) {
            if (pair.getKey().contains(d)) {
                return pair.getValue();
            }
        }
        throw new RuntimeException("No parent found. d=" + d);
    }

}
