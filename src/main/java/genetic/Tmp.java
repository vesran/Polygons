package genetic;

import java.util.Comparator;
import java.util.Random;
import java.util.SortedSet;
import java.util.function.Function;
import java.util.function.Supplier;

public class Tmp {

    public static void main(String [] args) {
        Comparator<String> comp = Measure::charComp;
        Supplier<String> generator = () -> {
            Random rand = new Random();
            String[] alph = {" ", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "x", "y", "z"};
            return alph[rand.nextInt(alph.length)];
        };
        Function<SortedSet<String>, Double> f = Measure::charFitness;

        GeneticAlgorithm<String> ga = new GeneticAlgorithm<>(comp, generator, f);
        ADN<String> res = ga.run();
        System.out.println(res);
    }
}
