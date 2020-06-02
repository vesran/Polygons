package genetic;

import genetic.ADN;
import javafx.scene.Group;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.SortedSet;

public class Measure {

    static int charComp(String c1, String c2) {
        return 1;
    }

    static double charFitness(SortedSet<String> chars) {
        String[] target = "to be or not to be that is the question".split("");
        double fitness = 0;
        int i = 0;
        for (String c : chars) {
            if (target[i++].equals(c)) {
                fitness += 1;
            }
        }
        return fitness;
    }

    public static <D> int compFitness(ADN<D> current, ADN<D> other) {
        double delta = current.getFitness() - other.getFitness();
        if (delta > 0) {
            return 1;
        } else if (delta < 0) {
            return -1;
        } else {
            return 0;
        }
    }

}
