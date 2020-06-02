package genetic;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class ADN<D> {

    private SortedSet<D> chromosome;
    private double fitness;
    private Supplier<D> generator;
    private Comparator<D> comp;
    private Function<SortedSet<D>, Double> fitnessFunc;

    public ADN(Comparator<D> comp, Supplier<D> generator, Function<SortedSet<D>, Double> fitnessFunc) {
        this.comp = comp;
        this.generator = generator;
        this.fitnessFunc = fitnessFunc;
        this.chromosome = new TreeSet<>(comp);
        while (this.chromosome.size() < GeneticAlgorithm.chromosomeSize) {
            this.chromosome.add(this.generator.get());
        }
        this.fitness = this.calculateFitness();
    }

    public ADN(SortedSet<D> chromosome, Comparator<D> comp, Supplier<D> generator, Function<SortedSet<D>, Double> f) {
        this.comp = comp;
        this.fitnessFunc = f;
        this.generator = generator;
        this.chromosome = chromosome;
        this.fitness = this.calculateFitness();
    }

    public Function<SortedSet<D>, Double> getFitnessFunc() {
        return this.fitnessFunc;
    }

    public double getFitness() {
        return this.fitness;
    }

    public SortedSet<D> getChromosome() {
        return this.chromosome;
    }

    public Comparator<D> getComp() {
        return this.comp;
    }

    public Supplier<D> getGenerator() {
        return this.generator;
    }

    public ADN<D> crossover(ADN<D> other) {
//        return new ADN<>(this.chromosome, this.comp, this.generator);
        return CrossMethod.singlePoint(this, other);
    }

    private double calculateFitness() {
        return this.fitnessFunc.apply(this.chromosome);
//        return Measure.charFitness((SortedSet<String>)this.chromosome);
    }

    public void mutate() {
        SortedSet<D> result = new TreeSet<>(this.comp);
        Random rand = new Random();
        int t = rand.nextInt(GeneticAlgorithm.chromosomeSize);
        int i = 0;
        for (D element : this.chromosome) {
            if (i++ == t) {
                result.add(this.generator.get());
            } else {
                result.add(element);
            }
        }
        this.chromosome = result;
        this.fitness = this.calculateFitness();
    }

    @Override
    public String toString() {
        StringBuilder strb = new StringBuilder();
        strb.append("fitness : ").append(this.fitness).append(" | ");
        for (D element : this.chromosome) {
            strb.append(element);
        }
        return strb.toString();
    }
}
