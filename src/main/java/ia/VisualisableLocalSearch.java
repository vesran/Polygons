package ia;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import util.ConvexPolygon;
import util.Measure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

abstract public class VisualisableLocalSearch {

    protected final int numPolygons = 500;
    protected final int numPoints = 4;
    protected final Group polygonsToAppend;
    protected List<ConvexPolygon> solution;
    protected double currentFitness;
    protected int currentIter;

    public VisualisableLocalSearch(Group g) {
        solution = new ArrayList<>();
        polygonsToAppend = g;
    }

    abstract public void nextStep();
    abstract public void applyAction(List<ConvexPolygon> list);

    protected void recreatingView() {
        polygonsToAppend.getChildren().clear();
        polygonsToAppend.getChildren().add(new Canvas(ConvexPolygon.max_X, ConvexPolygon.max_Y));
    }

    public void run() {
        // main loop : here should be described how to get one solution
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                recreatingView();
                nextStep();
                renderImage();
            }
        }.start();
    }

    protected void renderImage() {
        for (ConvexPolygon p : solution) {
            polygonsToAppend.getChildren().add(p);
        }
    }

    private void shrink(ConvexPolygon polygon) {
        double c = (new Random()).nextDouble();
        double meanX = 0;
        double meanY = 0;
        int j = 0;
        for (double d : polygon.getPoints()) {
            if (j++ % 2 == 0) {
                meanX += d;
            } else {
                meanY += d;
            }
        }
        meanX /= ((double)polygon.getPoints().size() / 2);
        meanY /= ((double)polygon.getPoints().size() / 2);

        for (int i = 0; i < polygon.getPoints().size()-1; i+=2) {
            double px = polygon.getPoints().get(i);
            double py = polygon.getPoints().get(i + 1);

            double vx = px - meanX;
            double vy = py - meanY;

            double npx = px - (vx * c);
            double npy = py - (vy * c);
            npy = (npy <= 0) ? 0 : npy;
            npy = (npy >= ConvexPolygon.max_Y) ? ConvexPolygon.max_Y : npy;
            npx = (npx <= 0) ? 0 : npx;
            npx = (npx >= ConvexPolygon.max_X) ? ConvexPolygon.max_X : npx;

            polygon.getPoints().set(i, npx);
            polygon.getPoints().set(i + 1, npy);
        }
    }

    private void bestColor(ConvexPolygon polygon) {
        double meanX = 0;
        double meanY = 0;
        int i = 0;
        for (double d : polygon.getPoints()) {
            if (i++ % 2 == 0) {
                meanX += d;
            } else {
                meanY += d;
            }
        }
        meanX /= ((double)polygon.getPoints().size() / 2);
        meanY /= ((double)polygon.getPoints().size() / 2);

        Color pixc = Measure.target[(int)meanX][(int)meanY];
        Color res = Color.color(pixc.getRed(), pixc.getGreen(), pixc.getBlue());
        polygon.setFill(res);
    }

    protected void addPolygon(List<ConvexPolygon> nextSolution) {
        if (nextSolution.size() < numPolygons) {
            ConvexPolygon polygon = new ConvexPolygon(numPoints);
            shrink(polygon);
            bestColor(polygon);
            nextSolution.add(polygon);
            Collections.sort(nextSolution, (p1, p2) -> -Double.compare(Measure.perimeter(p1), Measure.perimeter(p2)));
        } else {
            removePolygon(nextSolution);
            addPolygon(nextSolution);
        }
    }

    protected void shiftHue(List<ConvexPolygon> nextSolution) {
        Random rand = new Random();
        ConvexPolygon polygon = nextSolution.get(rand.nextInt(nextSolution.size()));
        Color color = (Color) polygon.getFill();
        Color c = color.deriveColor(rand.nextInt(360), 1, 1, 1);
        polygon.setFill(c);
    }

    protected void swap(List<ConvexPolygon> nextSolution) {
        Random rand = new Random();
        int i = rand.nextInt(nextSolution.size()-1);
        ConvexPolygon polygon = nextSolution.get(i);
        ConvexPolygon tmp = polygon;

        nextSolution.set(i, nextSolution.get(i+1));
        nextSolution.set(i+1, tmp);
    }

    protected void shiftOpacity(List<ConvexPolygon> nextSolution) {
        Random rand = new Random();
        ConvexPolygon polygon = nextSolution.get(rand.nextInt(nextSolution.size()));
        Color color = (Color) polygon.getFill();
        Color c = color.deriveColor(0, 1, 1, rand.nextDouble() + 0.5);
        polygon.setFill(c);
    }

    protected void removePolygon(List<ConvexPolygon> nextSolution) {
        if (nextSolution.size() > 1) {
            Random rand = new Random();
            nextSolution.remove(rand.nextInt(nextSolution.size()));
        }
    }

    protected void translatePolygon(List<ConvexPolygon> nextSolution) {
        Random rand = new Random();
        ConvexPolygon polygon = nextSolution.get(rand.nextInt(nextSolution.size()));

        double vx = (rand.nextDouble() * 2 - 1) * rand.nextInt(30);
        double vy = (rand.nextDouble() * 2 - 1) * rand.nextInt(30);

        // Choose a point from the polygon
        for (int i = 0; i < polygon.getPoints().size()-1; i+=2) {
            double px = polygon.getPoints().get(i);
            double py = polygon.getPoints().get(i + 1);

            double npx = px + vx;
            double npy = py + vy;
            npy = (npy <= 0) ? 0 : npy;
            npy = (npy >= ConvexPolygon.max_Y) ? ConvexPolygon.max_Y : npy;
            npx = (npx <= 0) ? 0 : npx;
            npx = (npx >= ConvexPolygon.max_X) ? ConvexPolygon.max_X : npx;

            polygon.getPoints().set(i, npx);
            polygon.getPoints().set(i + 1, npy);
        }
        bestColor(polygon);
    }


    protected void translatePoint(List<ConvexPolygon> nextSolution) {
        Random rand = new Random();
        ConvexPolygon polygon = nextSolution.get(rand.nextInt(nextSolution.size()));
        double meanX = 0;
        double meanY = 0;
        int i = 0;
        for (double d : polygon.getPoints()) {
            if (i++ % 2 == 0) {
                meanX += d;
            } else {
                meanY += d;
            }
        }
        meanX /= ((double)polygon.getPoints().size() / 2);
        meanY /= ((double)polygon.getPoints().size() / 2);

        // Choose a point from the polygon
        i = rand.nextInt(polygon.getPoints().size() / 2) * 2;
        double px = polygon.getPoints().get(i);
        double py = polygon.getPoints().get(i+1);

        double diffX = px - meanX;
        double diffY = py - meanY;

        double npx = px + diffX * (rand.nextDouble() * 2 - 1) / 2;
        double npy = py + diffY * (rand.nextDouble() * 2 - 1) / 1;
        npy = (npy <= 0) ? 0 : npy;
        npy = (npy >= ConvexPolygon.max_Y) ? ConvexPolygon.max_Y : npy;
        npx = (npx <= 0) ? 0 : npx;
        npx = (npx >= ConvexPolygon.max_X) ? ConvexPolygon.max_X : npx;

        polygon.getPoints().set(i, npx);
        polygon.getPoints().set(i+1, npy);
        bestColor(polygon);
        nextSolution.sort((p1, p2) -> -Double.compare(Measure.perimeter(p1), Measure.perimeter(p2)));
    }

}
