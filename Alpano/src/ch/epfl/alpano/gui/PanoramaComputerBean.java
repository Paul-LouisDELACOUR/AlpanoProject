package ch.epfl.alpano.gui;

import java.util.List;

import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.summit.Summit;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.image.Image;

public class PanoramaComputerBean {

    // ReadOnlyProperty est une super-classe de objectProperty
    private final PanoramaComputer computer;
   // List<Sommet> sommetList;
    private final ObjectProperty<PanoramaUserParameters> panoramaUserParametersProperty;
    private final ObjectProperty<Panorama> panoramaProperty;
    private final ObjectProperty<Image> imageProperty;
    private final ObservableList<Node> listNodeProperty;
    private final Labelizer labelizer;
    private final ObservableList<Node> unmodifiableList;
    
    /**
     * Un bean doté de 4 propriétés :
     *  le panorama, 
     *  ses paramètres (utilisateur), 
     *  son image,
     *  et ses étiquettes.
     */
    public PanoramaComputerBean( DiscreteElevationModel dem, List<Summit> summitList) {
        ContinuousElevationModel cem = new ContinuousElevationModel(dem);
        this.computer= new PanoramaComputer(cem);
        this.panoramaUserParametersProperty= new SimpleObjectProperty<>();
        panoramaUserParametersProperty.addListener((p, a, n)-> synchronizeParameters(n));
        this.panoramaProperty=new SimpleObjectProperty<>();
        this.imageProperty= new SimpleObjectProperty<>();
        this.listNodeProperty=FXCollections.observableArrayList();
        this.unmodifiableList= FXCollections.unmodifiableObservableList(listNodeProperty);
        this.labelizer=new Labelizer(cem, summitList);
        
    }
    
    public void synchronizeParameters(PanoramaUserParameters newParam){
        PanoramaParameters panoramaParameters = newParam.getPanoramaParameters();
        Panorama panorama = computer.computePanorama(panoramaParameters);
        System.out.println("computed");
        
        panoramaProperty.set(panorama);
        imageProperty.set(ImagePainter.defaultImage(panorama));
        System.out.println("drawn");

        listNodeProperty.setAll(labelizer.labels(newParam.getPanoramaDisplayParameters()));
        System.out.println("labeled");
    }
    
    /**
     * Méthodes d'accès aux paramètres du panorama
     */
    
    
    /**
     * permet d'obtenir la propriété JavaFX correspondant aux paramètres
     * @return la propriété JavaFX correspondant aux paramètres
     */
    public ObjectProperty<PanoramaUserParameters> parametersProperty(){
        return panoramaUserParametersProperty;
        //return new SimpleObjectProperty<PanoramaUserParameters>(paramBean.parametersProperty().get());
                //(ObjectProperty<PanoramaUserParameters>) paramBean.parametersProperty();
    }
    
    
    
    /**
     * permet de retourner les paramètres du PanoramaComputerBean
     * @return les paramètres du PanoramaComputerBean
     */
    public PanoramaUserParameters getParameters(){
       return panoramaUserParametersProperty.get();
    }
    
    /**
     * Permet de modifier les paramètres du Panorama
     * @param newParameters
     */
    public void setParameters(PanoramaUserParameters newParameters){
        panoramaUserParametersProperty.set(newParameters);
    }
    
    /**
     * Méthodes donnant accès au panorma
     */
    /**
     * 
     * @return
     */
    public ReadOnlyObjectProperty<Panorama> panoramaProperty(){
        return panoramaProperty;
    }
    
    /**
     * 
     * @return
     */
    public Panorama getPanorama(){
        return panoramaProperty.get();
    }
    
    /**
     * Méthodes donnant accès à l'image du panorama
     */
    public ReadOnlyObjectProperty<Image> imageProperty(){
        return imageProperty;
    }
    
    /**
     * 
     * @return
     */
   public Image getImage(){
       return imageProperty.get();
   }
   
   
   /**
    * 
    * @return
    */
   public ObservableList<Node> getLabels(){
       return unmodifiableList;
       
      
   }
   
   
    
    
}
