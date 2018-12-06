package ch.epfl.alpano.gui;

import java.io.File;

import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.stage.Stage;

public final class JavaFXMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // TODO: Code principal.

        PanoramaParameters param = PredefinedPanoramas.NIESEN.getPanoramaParameters();
        Labelizer label = new Labelizer(new ContinuousElevationModel(new HgtDiscreteElevationModel(new File("N46E007.hgt"))), GazetteerParser.readSummitsFrom(new File("alps.txt")));

        for(Node n : label.labels(param))
            System.out.println(n);
        Platform.exit();
    }
}
