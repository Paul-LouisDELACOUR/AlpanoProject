package ch.epfl.alpano.gui;

import java.util.HashMap;
import java.util.Map;
import static javafx.application.Platform.runLater;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import static ch.epfl.alpano.gui.UserParameter.*;
/**
 * Un bean qui contient les differentes propriétés avec les paramètres d'un panorama
 * @author Luc MICHELS
 * @author Paul-Louis DELACAOUR
 * 
 */
public class PanoramaParametersBean {

   
    private final Map<UserParameter, ObjectProperty<Integer>> mapProperty;
    private final ObjectProperty<PanoramaUserParameters> observableUserParam;
    
    /**
     * Un bean qui contient les différentes propriétés avec les paramètres d'un panorama
     * @param userParam les paramètres utilisateurs du panorama
     */
    public PanoramaParametersBean(PanoramaUserParameters userParam) {
        Map<UserParameter,ObjectProperty<Integer>> mapProperty=new HashMap<>();
        this.observableUserParam = new SimpleObjectProperty<PanoramaUserParameters>(userParam);
        
        for (UserParameter u : UserParameter.values()){
            ObjectProperty<Integer> p = new SimpleObjectProperty<>(userParam.get(u));
            mapProperty.put(u, p);
            p.addListener((b, o, n) ->runLater(this::synchronizeParameters));
        } 
        
        this.mapProperty=mapProperty;
        
    }
    
    /**
     * 
     * retourne la propriété qui contient les paramètres
     * see {@link ch.epfl.alpano.gui.PanoramaUserParameters#getPanoramaParameters()}
     * @return  la propriété qui contient les paramètres
     */
    public ReadOnlyObjectProperty<PanoramaUserParameters> parametersProperty(){
        return observableUserParam;
    }
    
    /**
     * retourne la propriété qui contient la longitude de l'observateur 
     * @see {@link ch.epfl.alpano.gui.PanoramaUserParameters#getObserverLongitude()}
     * @return  la propriété qui contient la longitude de l'observateur 
     */
    public ObjectProperty<Integer> observerLongitudeProperty(){
        return mapProperty.get(OBSERVER_LONGITUDE);
    }
    
    /**
     * retourne la propriété qui contient la latitude de l'observateur
     * @see {@link ch.epfl.alpano.gui.PanoramaUserParameters#getObserverLatitude()}
     * @return  la propriété qui contient la latitude de l'observateur
     */
    public ObjectProperty<Integer> observerLatitudeProperty(){
        return mapProperty.get(OBSERVER_LATITUDE);
    }
    
    /**
     * retourne la propriété qui contient l'altitude de l'observateur
     * @see {@link ch.epfl.alpano.gui.PanoramaUserParameters#getObserverElevation()}
     * @return  la propriété qui contient l'altitude de l'observateur
     */
    public ObjectProperty<Integer> observerElevationProperty(){
        return mapProperty.get(OBSERVER_ELEVATION);
    }
    
    /**
     * retourne la propriété qui contient l'azimuth central où regarde l'observateur
     * see {@link ch.epfl.alpano.gui.PanoramaUserParameters#getCenterAzimuth()}
     * @return  la propriété qui contient l'azimuth central où regarde l'observateur
     */
    public ObjectProperty<Integer> centerAzimuthProperty(){
        return mapProperty.get(CENTER_AZIMUTH);
    }
    
    /**
     * retourne la propriété qui contient la portée de vision horizontale de l'observateur
     * see {@link ch.epfl.alpano.gui.PanoramaUserParameters#getHorizontalFieldOfView()}
     * @return  la propriété qui contient la portée de vision horizontale de l'observateur
     */
    public ObjectProperty<Integer> horizontalFieldOfViewProperty(){
        return mapProperty.get(HORIZONTAL_FIELD_OF_VIEW);
    }
    
    /**
     * retourne la propriété qui contient la distance maximal du panorama
     * see {@link ch.epfl.alpano.gui.PanoramaUserParameters#getMaxDistance()}
     * @return  la propriété qui contient la distance maximal du panorama
     */
    public ObjectProperty<Integer> maxDistanceProperty(){
        return mapProperty.get(MAX_DISTANCE);
    }
    
    /**
     * retourne la propriété qui contient la largeur en pixel du panorama
     * see {@link ch.epfl.alpano.gui.PanoramaUserParameters#getWidth()}
     * @return la propriété qui contient la largeur en pixel du panorama
     */
    public ObjectProperty<Integer> widthProperty(){
       return mapProperty.get(WIDTH);
    }
    
    /**
     * retourne la propriété qui contient la hauteur en pixel du panorama
     * see {@link ch.epfl.alpano.gui.PanoramaUserParameters#getHeight()}
     * @return  la propriété qui contient la hauteur en pixel du panorama
     */
    public ObjectProperty<Integer> heightProperty(){
        return mapProperty.get(HEIGHT);
    }
    
    /**
     * retourne la propriété qui contient l'exposant de suréchantillonnage
     * see {@link ch.epfl.alpano.gui.PanoramaUserParameters#getSuperSamplingExponent()}
     * @return la propriété qui contient l'exposant de suréchantillonnage
     */
    public ObjectProperty<Integer> superSamplingExponentProperty(){
        return mapProperty.get(SUPER_SAMPLING_EXPONENT);
    }
    
    /** 
     * Lorsque les paramètres sont modifié 
     * 
     * Synchronise la propriété de chaque paramètres de mapProperty avec ceux qui sont contenues dans les propriétés du paramètre individuel lorsqu'ils sont modifiés.
     * Ces paramètres sont aussi sanitize. 
     * @see {@link ch.epfl.alpano.gui.UserParameter#sanitize(int)}
     * @param userParam
     */
    private void synchronizeParameters(){ // Utiliser dans le addlistener;
       
        PanoramaUserParameters synchronizeUserParameters = new PanoramaUserParameters(
               observerLongitudeProperty().get(), 
               observerLatitudeProperty().get(),
               observerElevationProperty().get(),
               centerAzimuthProperty().get(),
               horizontalFieldOfViewProperty().get(),
               maxDistanceProperty().get(),
               widthProperty().get(),
               heightProperty().get(),
               superSamplingExponentProperty().get()
               );
       observableUserParam.set(synchronizeUserParameters);
       for (UserParameter u : UserParameter.values()){
           mapProperty.get(u).set(synchronizeUserParameters.get(u));
       } 
       
    }
        
         
}
        


