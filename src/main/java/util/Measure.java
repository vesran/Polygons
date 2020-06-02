package util;

import javafx.scene.Group;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.List;

public class Measure {

    public static Color[][] target;
    public static int maxX;
    public static int maxY;

    public static double error(List<ConvexPolygon> ls) {
        // formation de l'image par superposition des polygones
        Group image = new Group();
        for (ConvexPolygon p : ls)
            image.getChildren().add(p);

        // Calcul de la couleur de chaque pixel.Pour cela, on passe par une instance de
        // WritableImage, qui possède une méthode pour obtenir un PixelReader.
        WritableImage wimg = new WritableImage(Measure.maxX, Measure.maxY);
        image.snapshot(null,wimg);
        PixelReader pr = wimg.getPixelReader();
        // On utilise le PixelReader pour lire chaque couleur
        // ici, on calcule la somme de la distance euclidienne entre le vecteur (R,G,B)
        // de la couleur du pixel cible et celui du pixel de l'image générée

        double res=0;
        for (int i = 0; i< Measure.maxX; i++){
            for (int j = 0; j< Measure.maxY; j++){
                Color c = pr.getColor(i, j);
                res += Math.pow(c.getBlue()- Measure.target[i][j].getBlue(),2)
                        +Math.pow(c.getRed()- Measure.target[i][j].getRed(),2)
                        +Math.pow(c.getGreen()- Measure.target[i][j].getGreen(),2);
            }
        }
        return Math.sqrt(res);
    }

    public static double perimeter(ConvexPolygon polygon) {
        double perimeter = 0.0;
        double x1, x2, y1, y2;
        for (int i = 0; i < polygon.getPoints().size()-1; i+=4) {
            x1 = polygon.getPoints().get(i);
            y1 = polygon.getPoints().get(i+1);
            if (i+3 > polygon.getPoints().size()) {
                x2 = polygon.getPoints().get(0);
                y2 = polygon.getPoints().get(1);
            } else {
                x2 = polygon.getPoints().get(i + 2);
                y2 = polygon.getPoints().get(i + 3);
            }
            perimeter += ((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
        }
        return perimeter;
    }
}
