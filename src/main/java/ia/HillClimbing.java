package ia;

import javafx.scene.Group;
import util.ConvexPolygon;
import util.Measure;

import java.text.DecimalFormat;
import java.util.*;

public class HillClimbing extends VisualisableLocalSearch {

    private LinkedList<Double> fitnessScores;
    private int[] stats = new int[6];
    private int chosen;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public HillClimbing(Group g) {
        super(g);
        fitnessScores = new LinkedList<>();
        for (int i = 0; i < 30; i++) fitnessScores.add(0.0);
        for (int i = 0; i < 1; i++) addPolygon(solution);
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
//                swap(nextSolution);
                translatePolygon(nextSolution);
                break;
            case 3:
                shiftOpacity(nextSolution);
                break;
            case 4:
                translatePoint(nextSolution);
                break;
            case 5:
                break;
            default:
                throw new RuntimeException("The chosen number does not correspond to any of the listed action.");
        }
    }

    public void nextStep() {
        // inside main loop : here should be described how to get one solution
        // Print stats and info
        System.out.print("Fitness : " + currentFitness + " -- Number of polygons : " + solution.size() + " -- ");
        System.out.print("stats : ");
        int sum = Arrays.stream(stats).sum();
        for (double d : stats) System.out.print(df2.format(d/sum) + " | ");
        System.out.print("Iteration : " + ++currentIter);

        // Create a new polygon and check if adding it improve the solution
        List<ConvexPolygon> nextSolution = new ArrayList<>();
        for (ConvexPolygon p : solution) {
            nextSolution.add(p.clone());
        }
        applyAction(nextSolution);

        double nextFitness = 1/Measure.error(nextSolution);

        if (nextFitness > currentFitness) {
            fitnessScores.addLast(nextFitness - currentFitness);
            fitnessScores.removeFirst();
            currentFitness = nextFitness;
            solution = nextSolution;
            stats[chosen]++;
        } else {
            fitnessScores.addLast(0.0);
            fitnessScores.removeFirst();
        }
        System.out.println(" -- Improv : " + String.format("%6.3e", fitnessScores.stream().mapToDouble(x->x).sum() / fitnessScores.size()));
    }
}
