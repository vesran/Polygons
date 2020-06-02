package genetic;

import genetic.pool.*;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class GeneticAlgorithm<D> {

    public static final int chromosomeSize = "to be or not to be that is the question".length();
    public static final int numPop = 100;
    public static final double mutationRate = 1;
    public static final int numIter = 500;

    private List<ADN<D>> population;

    public GeneticAlgorithm(Comparator<D> comp, Supplier<D> generator, Function<SortedSet<D>, Double> f) {
        this.population = new ArrayList<>(numPop);
        for (int i = 0; i < numPop; i++) {
            this.population.add(new ADN<>(comp, generator, f));
        }
    }

    public ADN<D> run() {
        int i = 0;
        Collections.sort(this.population, Measure::compFitness);
        while (i++ < numIter && this.population.get(this.numPop - 1).getFitness() < 39) {
            System.out.println("----------------------- Iteration " + i + "-------------------------------");
            System.out.println(this);
            this.population = this.nextPopulation();
            Collections.sort(this.population, Measure::compFitness);
        }
        System.out.println(this);
        System.out.println("Number of iteration : " + i);
        return this.population.get(this.population.size() - 1);
    }

    public List<ADN<D>> nextPopulation() {
        // Fitness is already calculated when instantiating an ADN
        // Here, we only have to deal with selection and reproduction
        // Create a mating pool
        Random rand = new Random();
        ADN<D> child, parent1, parent2;
        List<ADN<D>> nextPop = new ArrayList<>(numPop);
        MatingPool<D> mpool = new TournamentSelection<>(this.population);
        for (int i = 0; i < numPop; i++) {
            // Select 2 parents from the mating pool and create a new child
            parent1 = mpool.getRandomParent();
            parent2 = mpool.getRandomParent();
            child = parent1.crossover(parent2);
            if (rand.nextDouble() < mutationRate) {
                child.mutate();
            }
            nextPop.add(child);
        }
        return nextPop;
    }

    @Override
    public String toString() {
        StringBuilder strb = new StringBuilder();
        for (ADN<D> living : this.population) {
            strb.append(living.toString()).append("\n");
        }
        return strb.toString();
    }
}
