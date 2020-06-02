package ia;

import javafx.scene.Group;
import util.ConvexPolygon;
import util.Measure;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class BeamSearch extends VisualisableLocalSearch {

    private final int bfactor = 30;
    private final int k = 10;
    private List<List<ConvexPolygon>> solutions;

    private int[] stats = new int[6];
    private int chosen;
    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public BeamSearch(Group g) {
        super(g);
        solutions = new ArrayList<>(k);
        for (int i = 0; i < 1; i++) addPolygon(solution);
        solutions.add(solution);
    }

    @Override
    public void nextStep() {
        System.out.print("Fitness : " + currentFitness + " -- Number of polygons : " + solution.size() + " -- ");
        System.out.print("stats : ");
        int sum = Arrays.stream(stats).sum();
        for (double d : stats) System.out.print(df2.format(d/sum) + " | ");
        System.out.println("Iteration : " + ++currentIter);

        Map<List<ConvexPolygon>, Double> pool = new HashMap<>(k * bfactor);
        List<ConvexPolygon> nextSolution;
        for (List<ConvexPolygon> possibleSolution : solutions) {
            for (int i = 0; i < bfactor; i++) {
                // Create a copy
                nextSolution = new ArrayList<>();
                for (ConvexPolygon p : possibleSolution) {
                    nextSolution.add(p.clone());
                }
                applyAction(nextSolution);
                pool.put(nextSolution, 1/Measure.error(nextSolution));
            }
        }
        System.out.println(pool.size());
        // TODO : Use Map
        solutions = pool.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList())
                        .subList(0, k);
        solution = solutions.get(0);
        currentFitness = pool.get(solution);
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
}
