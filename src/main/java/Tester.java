import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import util.ConvexPolygon;
import util.Measure;

public class Tester extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Measure.maxX = 200;
        Measure.maxY = 200;
        ConvexPolygon.max_X = Measure.maxX;
        ConvexPolygon.max_Y = Measure.maxY;
        Group root = new Group();
        Scene scene = new Scene(root, Measure.maxX, Measure.maxY);
        scene.setFill(Color.BLACK);

        ConvexPolygon c1 = new ConvexPolygon(5);
        ConvexPolygon c2 = new ConvexPolygon(3);

        root.getChildren().add(c1);
        root.getChildren().add(c2);

        stage.setScene(scene);
        stage.show();
    }
}
