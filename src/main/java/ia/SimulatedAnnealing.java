package ia;

import javafx.scene.Group;
import util.ConvexPolygon;
import util.Measure;

import java.text.DecimalFormat;
import java.util.*;

public class SimulatedAnnealing extends VisualisableLocalSearch {

    private double temperature = 1;

    private LinkedList<Double> fitnessScores;
    private int[] stats = new int[5];
    private int chosen;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public SimulatedAnnealing(Group g) {
        super(g);
        fitnessScores = new LinkedList<>();
        for (int i = 0; i < 30; i++) fitnessScores.add(0.0);
        for (int i = 0; i < 1; i++) solution.add(new ConvexPolygon(numPoints));
    }

    private double nextTemprature() {
        return 0.000001 + Math.sin(currentIter) / 1000000;
    }

    @Override
    public void nextStep() {
        // Print stats and info
        System.out.print("Fitness : " + currentFitness + " -- Nb polygons : " + solution.size() + " -- ");
        System.out.print("stats : ");
        int sum = Arrays.stream(stats).sum();
        for (double d : stats) System.out.print(df2.format(d/sum) + " | ");
        System.out.print("Iter : " + ++currentIter);

        // Algorithm
        temperature = nextTemprature();

        List<ConvexPolygon> nextSolution = new ArrayList<>(solution.size());
        for (ConvexPolygon cp : solution) nextSolution.add(cp.clone());

        applyAction(nextSolution);

        double nextFitness = 1/ Measure.error(nextSolution);
        double diffEnergy = nextFitness - currentFitness;
        System.out.print(" -- diffEnergy = " + String.format("%6.3e", diffEnergy));
        System.out.print(" -- prob = " + String.format("%6.3e", Math.min(Math.exp(diffEnergy / temperature), 1.0)));


        if (diffEnergy > 0 || (new Random()).nextDouble() < Math.exp(diffEnergy / temperature)) {
            fitnessScores.addLast(nextFitness - currentFitness);
            fitnessScores.removeFirst();
            solution = nextSolution;
            currentFitness = nextFitness;
            stats[chosen]++;
        } else {
            fitnessScores.addLast(0.0);
            fitnessScores.removeFirst();
        }
        System.out.println(" -- Improv : " + String.format("%6.3e", fitnessScores.stream().mapToDouble(x->x).sum() / fitnessScores.size()));
    }

    @Override
    public void applyAction(List<ConvexPolygon> nextSolution) {
        Random rand = new Random();
        chosen = rand.nextInt(5);
        switch(chosen) {
            case 0:
                addPolygon(nextSolution);
                break;
            case 1:
                shiftHue(nextSolution);
                break;
            case 2:
                removePolygon(nextSolution);
                break;
            case 3:
                shiftOpacity(nextSolution);
                break;
            case 4:
                translatePoint(nextSolution);
                break;
            default:
                throw new RuntimeException("The chosen number does not correspond to any of the listed action.");
        }
    }
}
