package ch.epfl.alpano.gui;
import javafx.geometry.Insets;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public final class Alpano extends Application {
private static final int TOP_INSET = 3;
private static final int LEFT_INSET = 10;
private static final PanoramaUserParameters PANORAMA_DE_DEPART = PredefinedPanoramas.ALPES_DU_JURA;    
private static final FixedPointStringConverter INTEGER_STRING_CONVERTER = new FixedPointStringConverter(0);
private static final FixedPointStringConverter POINT_STRING_CONVERTER = new FixedPointStringConverter(4);
private static final int COLUMN_COUNT_AZIMUT_ANGLEVUE_VISIBILITE = 3;
private static final int COLUMN_COUNT_ALTITUDE_LARGEUR_HAUTEUR = 4;
private static final int COLUMN_COUNT_LATITUDE_LONGITUDE = 7;
private static final int NUMBER_HGT_FILE = 8;

public static void main(String[] args) {
    Application.launch(args);
}

          
  public void start(Stage primaryStage) {
    // … création de l'interface graphique
      List<Summit> summitList = new ArrayList<>();
      try {
        summitList = GazetteerParser.readSummitsFrom(new File("alps.txt"));
    } catch (IOException e) {
        e.printStackTrace();
        throw new Error();
    } 

    DiscreteElevationModel hgtComplete = createDiscreteElevationModel(
            "N45E006.hgt",
            "N45E007.hgt",
            "N45E008.hgt", 
            "N45E009.hgt", 
            "N46E006.hgt",
            "N46E007.hgt", 
            "N46E008.hgt", 
            "N46E009.hgt");
    
    //Création du PanoramaComputerBean et du PanoramaParametersBean
    PanoramaComputerBean panoramaComputerBean = new PanoramaComputerBean(hgtComplete, summitList);
    PanoramaParametersBean panoramaParametersBean = new PanoramaParametersBean(PANORAMA_DE_DEPART);
   
   
   BorderPane root = new BorderPane();
       StackPane panoPane = new StackPane();
           ScrollPane panoScrollPane = new ScrollPane();
               StackPane panoGroup = new StackPane();
                       ImageView panoView = new ImageView();
                       
                       Pane labelsPane = new Pane();
           StackPane updateNotice = new StackPane();
               
        GridPane paramsGrid = new GridPane();
        root.setCenter(panoPane);
        root.setBottom(paramsGrid);

        // Création du textArea contenant les informations sous la souris
        TextArea textArea = new TextArea("");
        ObjectProperty<String> textSouris = setTextArea(textArea);
        // Modification du PanoView contenant l'image du Panorama
        setPanoView(panoramaComputerBean, panoramaParametersBean, panoView,
                textSouris);

        // labelsPane
        setLabelsPane(panoramaParametersBean, panoramaComputerBean, labelsPane);
        // Update notice
        setUpdateNotice(panoramaComputerBean, panoramaParametersBean,
                updateNotice);

        // panoScrollPane
        setPanoScrollPane(panoScrollPane, panoGroup, panoView, labelsPane);
        
        // Combinaison de l'image, des étiquettes et de la notice dans le
        // panoPane
        panoPane.getChildren().addAll(panoScrollPane, updateNotice);
        
        // GridPane
        setGridPane(panoramaParametersBean, paramsGrid, textArea);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Alpano");
        primaryStage.setScene(scene);
        primaryStage.show();
  }



/**
 * créer le gridpane avec tout les paramètres ainsi que l'information contenue dans la souris et permettant de modifier les paramètres du panorama
 * @param panoramaParametersBean
 * @param paramsGrid
 * @param textArea
 */
private void setGridPane(PanoramaParametersBean panoramaParametersBean,
        GridPane paramsGrid, TextArea textArea) {
    Label latitudeL = new Label("Latitude (°) : ");
    TextField latitudeTF = createTextField(POINT_STRING_CONVERTER, panoramaParametersBean.observerLatitudeProperty(), COLUMN_COUNT_LATITUDE_LONGITUDE);
    Label longitudeL = new Label("Longitude(°) : ");
    TextField longitudeTF = createTextField(POINT_STRING_CONVERTER, panoramaParametersBean.observerLongitudeProperty(), COLUMN_COUNT_LATITUDE_LONGITUDE);
    Label altitudeL = new Label("Altitude (m) : ");
    TextField altitudeTF = createTextField(INTEGER_STRING_CONVERTER,panoramaParametersBean.observerElevationProperty(), COLUMN_COUNT_ALTITUDE_LARGEUR_HAUTEUR);
    Label azimuthL = new Label("Azimuth (°) : ");
    TextField azimuthTF = createTextField(INTEGER_STRING_CONVERTER, panoramaParametersBean.centerAzimuthProperty(), COLUMN_COUNT_AZIMUT_ANGLEVUE_VISIBILITE);
    Label angleVueL = new Label("Angle de vue (°) : ");
    TextField angleVueTF = createTextField(INTEGER_STRING_CONVERTER, panoramaParametersBean.horizontalFieldOfViewProperty(), COLUMN_COUNT_AZIMUT_ANGLEVUE_VISIBILITE);
    Label visibiliteL = new Label("Visibilité (km) : ");
    TextField visibiliteTF = createTextField(INTEGER_STRING_CONVERTER, panoramaParametersBean.maxDistanceProperty(), COLUMN_COUNT_AZIMUT_ANGLEVUE_VISIBILITE);
    Label largeurL = new Label("Largeur (px) : ");
    TextField largeurTF = createTextField(INTEGER_STRING_CONVERTER, panoramaParametersBean.widthProperty(), COLUMN_COUNT_ALTITUDE_LARGEUR_HAUTEUR);
    Label hauteurL = new Label("Hauteur (px) : ");
    TextField hauteurTF = createTextField(INTEGER_STRING_CONVERTER, panoramaParametersBean.heightProperty(), COLUMN_COUNT_ALTITUDE_LARGEUR_HAUTEUR);
    Label surechantillonnage = new Label("Suréchantillonnage : ");
    
    ChoiceBox<Integer> surechantillonnageBox = new ChoiceBox<>();
    surechantillonnageBox.getItems().addAll(0,1,2);
    StringConverter<Integer> stringConverter = new LabeledListStringConverter("non", "2×", "4×");
    surechantillonnageBox.setConverter(stringConverter);
    surechantillonnageBox.valueProperty().bindBidirectional(panoramaParametersBean.superSamplingExponentProperty());

    paramsGrid.addRow(0, latitudeL, latitudeTF,longitudeL,longitudeTF,altitudeL,altitudeTF);
    paramsGrid.addRow(1, azimuthL,azimuthTF,angleVueL,angleVueTF,visibiliteL,visibiliteTF);
    paramsGrid.addRow(2, largeurL,largeurTF,hauteurL,hauteurTF,surechantillonnage,surechantillonnageBox);
    //Création de la ChoiceBox pour le degré de suréchantillonnage
    
    paramsGrid.getChildren().forEach((x)-> GridPane.setHalignment(x, HPos.RIGHT));

   
//    GridPane.setHalignment(paramsGrid.getChildren().get(paramsGrid.getChildren().size()-1), HPos.LEFT);
    paramsGrid.add(textArea, 6, 0, 1, 3);
    paramsGrid.setAlignment(Pos.CENTER);
    paramsGrid.getChildren().forEach((x)-> GridPane.setMargin(x, new Insets(TOP_INSET, 0, 0, LEFT_INSET)));
}

/**
 * met en place le panoScrollPane qui permet de superposer l'image du panorama et ses étiquettes
 * @param panoScrollPane
 * @param panoGroup
 * @param panoView
 * @param labelsPane
 */
private void setPanoScrollPane(ScrollPane panoScrollPane, StackPane panoGroup,
        ImageView panoView, Pane labelsPane) {
    panoGroup.getChildren().addAll(panoView, labelsPane);
    panoScrollPane.setContent(panoGroup);
}

/**
 * met en place le labelsPane qui permet de dessiner les étiquettes sur le panorama
 * @param panoramaParametersBean
 * @param panoramaComputerBean
 * @param labelsPane
 */
private void setLabelsPane(PanoramaParametersBean panoramaParametersBean,PanoramaComputerBean panoramaComputerBean,
        Pane labelsPane) {
    labelsPane.prefWidthProperty().bind(panoramaParametersBean.widthProperty());
    labelsPane.prefHeightProperty().bind(panoramaParametersBean.heightProperty());
    labelsPane.setMouseTransparent(true);
    //Ajouter les étiquettes 
    labelsPane.getChildren().addAll(panoramaComputerBean.getLabels());
    Bindings.bindContent(labelsPane.getChildren(), panoramaComputerBean.getLabels());
}

/**
 * met en place l'update notice qui est l'image de notification quand le panorama est différent 
 * des paramètres du panoramaParametersBean
 * @param panoramaComputerBean
 * @param panoramaParametersBean
 * @param updateNotice
 */
private void setUpdateNotice(PanoramaComputerBean panoramaComputerBean,
        PanoramaParametersBean panoramaParametersBean, StackPane updateNotice) {
    updateNotice.setBackground(new Background( new BackgroundFill(Color.WHITE, null, null)));
    updateNotice.setOpacity(0.9);
    Text updateText = new Text(String.format("Les paramètres du panorama ont changé.%n  Cliquer ici pour mettre le dessin à jour. "));
    updateText.setFont(new Font(40));
    updateText.setTextAlignment(TextAlignment.CENTER);
    updateNotice.getChildren().add(updateText);
    
    
    
    updateNotice.visibleProperty().bind(
            (panoramaParametersBean.parametersProperty()).isNotEqualTo(panoramaComputerBean.parametersProperty()));
    
    updateNotice.setOnMouseClicked((click)->{
        panoramaComputerBean.setParameters(panoramaParametersBean.parametersProperty().get());
    });
}


/**
   * Créer un TextField avec un textFormater et son stringConverter associé, une taille de colonne et une propriété qui va être bind à la value property du textFormater. 
   * @see {@link javafx.scene.control.TextFormatter#valueProperty()}
   * @see {@link javafx.scene.control.TextFormatter}
   * @param stringConverter : StringConverter pour le degré de présicion du paramètre en question qui sera assosié au formateur de text du text field
   * @param objectPropertyToBindWith : propriété liée au paramètre contenant la valeur
   * @param prefColumn : nombre préféré de colonne pour pour le noeud contenant la valeur du paramètre 
   */
private TextField createTextField(StringConverter<Integer> stringConverter,
        ObjectProperty<Integer> objectPropertyToBindWith, int prefColumn) {
        TextField textField = new TextField();
        TextFormatter<Integer> formatterLatitude = new TextFormatter<>(stringConverter);
        textField.setTextFormatter(formatterLatitude);
     
        ((ObjectProperty<Integer>)formatterLatitude.valueProperty()).bindBidirectional(objectPropertyToBindWith);
  
        //Aligner les textes des Champs textuels à droite 
        textField.setAlignment(Pos.BASELINE_RIGHT);
     
        // Ajoute un nombre de colonnes préféré au texte
        textField.setPrefColumnCount(prefColumn);
    return textField;
}
  
  /**
   * retourne un DiscreteElevationModel, qui est construit à partir de l'union de 8 fichiers DiscreteElevationModel,
   * dont les noms sont donnés en paramètre. 
   * @param fileName : noms de tous les fichiers DiscreteELevationModel lus
   * @return :  un DiscreteElevationModel, qui est construit à partir de l'union de 8 fichiers DiscreteElevationModel,
   * dont les noms sont donnés en paramètres.
   * @throws IllegalArgumentException : si le nombre de fichier à lire n'est pas de 8
   */
  private DiscreteElevationModel createDiscreteElevationModel(String ... fileName) throws IllegalArgumentException{
      if (fileName.length!= NUMBER_HGT_FILE){
          throw new IllegalArgumentException("le nombre de fichier hgt lus, doit être de 8");
      }
      
      @SuppressWarnings("resource")
    DiscreteElevationModel firstPart = 
          new HgtDiscreteElevationModel(new File(fileName[0]))
          .union(new HgtDiscreteElevationModel(new File(fileName[1])))
          .union( new HgtDiscreteElevationModel(new File(fileName[2])))
          .union( new HgtDiscreteElevationModel(new File(fileName[3])));        
      @SuppressWarnings("resource")
    DiscreteElevationModel secondPart = 
           new HgtDiscreteElevationModel(new File(fileName[4]))
           .union(new HgtDiscreteElevationModel(new File(fileName[5])))
           .union( new HgtDiscreteElevationModel(new File(fileName[6])))
           .union( new HgtDiscreteElevationModel(new File(fileName[7])));
      
      return firstPart.union(secondPart);
     
  }
  
  

  /**
 * Création et paramètrage du textArea contenant les informations sous la souris
 * @param textArea qui va contenir l'information reçu par la souris 
 * @return la propriété qui contient le text de l'information sous la souris
 */
private ObjectProperty<String> setTextArea(TextArea textArea) {
    textArea.setEditable(false);
    textArea.setPrefRowCount(2);
    ObjectProperty<String> textSouris = new SimpleObjectProperty<String>("");
    textArea.textProperty().bind(textSouris);
    return textSouris;
}

/**
 * Modification du PanoView contenant l'image du Panorama
 * @param panoramaComputerBean : dont l'image est liée avec l'image de panoview
 * @param panoramaParametersBean : dont la widthProperty est liée avec le fit width de panoview
 * @param panoView
 */
private void setPanoView(PanoramaComputerBean panoramaComputerBean,
        PanoramaParametersBean panoramaParametersBean, ImageView panoView, ObjectProperty<String> textSouris ) {
    panoView.fitWidthProperty().bind(panoramaParametersBean.widthProperty());
    panoView.imageProperty().bind(panoramaComputerBean.imageProperty());
    panoView.setPreserveRatio(true);
    panoView.setSmooth(true);
    panoView.setOnMouseMoved((moved)->{
        Panorama panorama = panoramaComputerBean.getPanorama();
        PanoramaParameters panoramaParameters = panoramaComputerBean.parametersProperty().get().getPanoramaParameters();
        int superSamplingExponent =  panoramaComputerBean.parametersProperty().get().getSuperSamplingExponent();
        
        // Les coordonnées (x,y) de la souris doivent prendre en compte le degré de suréchantillonage pour être à la bonne position
        int x = (int) Math.scalb(moved.getX(), superSamplingExponent);
        int y = (int) Math.scalb(moved.getY(), superSamplingExponent);
        
        double longitude = panorama.longitudeAt(x, y);
        double latitude =  panorama.latitudeAt(x, y);      
        
        float distance = panorama.distanceAt(x, y, Float.POSITIVE_INFINITY);
        int altitude = (int) panorama.elevationAt(x, y);
        double azimut = panoramaParameters.azimuthForX(x);
        double elevation = panoramaParameters.altitudeForY(y);
        textSouris.set(String.format( (Locale)null, 
           "Position : %.4f°N %.4f°E %nDistance : %.1f km %nAltitude : %d m %nAzimut : %.1f° (%s)    Elévation : %.1f°" , 
           Math.toDegrees(latitude),
           Math.toDegrees(longitude),
           distance/1000.0,
           altitude,
           Math.toDegrees(azimut),
           Azimuth.toOctantString(azimut, "N", "E", "S", "O"),
           Math.toDegrees(elevation)) );
    });

    panoView.setOnMouseClicked((clicked)->{
        Panorama panorama = panoramaComputerBean.getPanorama();
        int superSamplingExponent =  panoramaComputerBean.parametersProperty().get().getSuperSamplingExponent();
        int x = (int) Math.scalb(clicked.getX(), superSamplingExponent);
        int y = (int) Math.scalb(clicked.getY(), superSamplingExponent);

        double longitude = panorama.longitudeAt(x, y);
        double latitude =  panorama.latitudeAt(x, y);
     
        String latitudeString = String.format((Locale)null, "%.4f", Math.toDegrees(latitude));
        String longitudeString = String.format((Locale)null, "%.4f", Math.toDegrees(longitude));
       
        String qy = new StringBuilder("mlat=")
                .append(latitudeString)
                .append("&mlon=")
                .append(longitudeString)
                .toString();
        String fg = new StringBuilder("map=15/")
                .append(latitudeString)
                .append("/")
                .append(longitudeString)
                .toString();
        try {
            URI osmURI =
                new URI("http", "www.openstreetmap.org", "/", qy, fg);
                java.awt.Desktop.getDesktop().browse(osmURI);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
     );
}
}
