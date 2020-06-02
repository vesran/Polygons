import ia.BeamSearch;
import ia.HillClimbing;
import ia.SimulatedAnnealing;
import ia.VisualisableLocalSearch;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import util.ConvexPolygon;
import util.Measure;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainGUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        String filename = "smith.png";
        String targetImagePath = "src/main/resources/" + filename;
        System.out.println(targetImagePath);
        Measure.target=null;
        Measure.maxX=0;
        Measure.maxY=0;
        try{
            BufferedImage bi = ImageIO.read(new File(targetImagePath));
            Measure.maxX = bi.getWidth();
            Measure.maxY = bi.getHeight();
            ConvexPolygon.max_X= Measure.maxX;
            ConvexPolygon.max_Y= Measure.maxY;
            Measure.target = new Color[Measure.maxX][Measure.maxY];
            for (int i=0;i<Measure.maxX;i++){
                for (int j=0;j<Measure.maxY;j++){
                    int argb = bi.getRGB(i, j);
                    int b = (argb)&0xFF;
                    int g = (argb>>8)&0xFF;
                    int r = (argb>>16)&0xFF;
                    Measure.target[i][j] = Color.rgb(r,g,b);
                }
            }
        }
        catch(IOException e){
            System.err.println(e);
            System.exit(9);
        }
        System.out.println("Read target image " + targetImagePath + " " + Measure.maxX + "x" + Measure.maxY);

        GridPane root = new GridPane();
        root.setPadding(new Insets(20));
        ImageView iv = new ImageView();
        iv.setImage(new Image(getClass().getResourceAsStream(filename)));
        root.add(iv, 1, 0, 2, 1);

        Group iaRoot = new Group();
        root.add(iaRoot, 0, 0, 1, 1);
        Canvas canvas = new Canvas(Measure.maxX, Measure.maxY);
        iaRoot.getChildren().add(canvas);

        // affichage de l'image dans l'interface graphique
        Scene scene = new Scene(root,Measure.maxX * 2 + 40, Measure.maxY + 40);
        scene.setFill(Color.BLACK);
        stage.setScene(scene);
        stage.show();

        VisualisableLocalSearch ia = new HillClimbing(iaRoot);
        ia.run();
    }
}
