package ch.epfl.alpano.gui;

import static ch.epfl.alpano.gui.UserParameter.CENTER_AZIMUTH;
import static ch.epfl.alpano.gui.UserParameter.HEIGHT;
import static ch.epfl.alpano.gui.UserParameter.HORIZONTAL_FIELD_OF_VIEW;
import static ch.epfl.alpano.gui.UserParameter.MAX_DISTANCE;
import static ch.epfl.alpano.gui.UserParameter.OBSERVER_ELEVATION;
import static ch.epfl.alpano.gui.UserParameter.OBSERVER_LATITUDE;
import static ch.epfl.alpano.gui.UserParameter.OBSERVER_LONGITUDE;
import static ch.epfl.alpano.gui.UserParameter.SUPER_SAMPLING_EXPONENT;
import static ch.epfl.alpano.gui.UserParameter.WIDTH;
import static java.lang.Math.scalb;
import static java.lang.Math.toRadians;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;
/**
 * 
 * @author Luc MICHELS
 * @author Paul-Louis DELACOUR
 * PanoramaParameters pour un utilisateur c'est à dire les parametres d'un panorama mais avec des unités intuitives pour un utilisateur
 * @see PanoramaParameters
 */
public final class PanoramaUserParameters {
    private final Map<UserParameter, Integer> map;
    private static final int HEIGHT_MAX = UserParameter.HEIGHT.getMaxValue();
    
    private int MAXIMAL_VERTICAL_FIELD_OF_VIEW = 170;

    /**
     * construit un {@link PanoramaUserParameters} avec les valeurs en arguments qui sont "sanitize" (@see {@link UserParameter.sanitize} && {@link UserParameter.sanitizeHeight})
     * @param paramToValue
     */
    public PanoramaUserParameters(Map<UserParameter, Integer> map) {
        Map<UserParameter, Integer> correctMap = new EnumMap<>(UserParameter.class);
        
        for (UserParameter key : map.keySet()){
            correctMap.put(key, key.sanitize(map.get(key)) );
        }
        
        modifyMapForHeight(correctMap);
        this.map = Collections.unmodifiableMap(new EnumMap<>(correctMap));
        }

  /**
   * construit un {@link PanoramaUserParameters} avec les valeurs en arguments qui sont "sanitize" (@see {@link UserParameter.sanitize} && {@link UserParameter.sanitizeHeight})
   * @param longitude
   * @param latitude
   * @param elevation
   * @param centerAzimuth
   * @param horizontalFieldOfView
   * @param maxDistance
   * @param width
   * @param height
   * @param superSamplingExponent
   */
    public PanoramaUserParameters(int longitude, int latitude, int observerElevation, int centerAzimuth, 
            int horizontalFieldOfView, int maxDistance, int width, int height, int superSamplingExponent){
        this( parametersToMap(
                longitude, 
                latitude,
                observerElevation, 
                centerAzimuth, 
                horizontalFieldOfView, 
                maxDistance, 
                width, 
                height, 
                superSamplingExponent));
    }

    /**
     * retourne une map<UserParameter, Integer> avec tout les parametres associés aux attributs correspondant
     * @param longitude
     * @param latitude
     * @param elevation
     * @param centerAzimuth
     * @param horizontalFieldOfView
     * @param maxDistance
     * @param width
     * @param height
     * @param superSamplingExponent
     * @return une map<UserParameter, Integer> avec tout les parametres associés aux attributs correspondant
     */
    private static Map<UserParameter, Integer> parametersToMap(int ... parameters){
        Map <UserParameter, Integer> enumMap = new EnumMap<>(UserParameter.class);
        UserParameter[] enumParam = UserParameter.values();
        for( int i = 0; i<enumParam.length;++i){
            enumMap.put(enumParam[i], parameters[i]);
        }
       return enumMap; 
    }

    /**
     * retourne la valeur associé au paramètre
     * @param parameter
     * @return la valeur associé au paramètre
     */
    public int get(UserParameter parameter) {
        return map.get(parameter);
    }

    /**
     * retourne la longitude de l'observateur en dix-millième de degrés
     * @return la longitude de l'observateur en dix-millième de degrés
     */
    public int getObserverLongitude() {
        return map.get(OBSERVER_LONGITUDE);
    }

    /**
     * retourne la latitude de l'observateur en dix-millième de degrés
     * @return la latitude de l'observateur en dix-millième de degrés
     */
    public int getObserverLatitude() {
        return map.get(OBSERVER_LATITUDE);
    }

    /**
     * retourne l'altitude de l'observateur en m
     * @return l'altitude de l'observateur en m                      
     */                                                              
    public int getObserverElevation() {                              
        return map.get(OBSERVER_ELEVATION);
    }

    /**
     * retourne la direction ou regarde l'observateur en azimuth degré
     * @return la direction ou regarde l'observateur en azimuth degré
     */
    public int getCenterAzimuth() {
        return map.get(CENTER_AZIMUTH);
    }

    /**
     * retourne la vision de vue horizontal de l'observateur en degré
     * @return la vision de vue horizontal de l'observateur en degré
     */
    public int getHorizontalFieldOfView() {
        return map.get(HORIZONTAL_FIELD_OF_VIEW);
    }

    /**
     * retourne la distance maximale que peut voir l'observateur en km
     * @return la distance maximale que peut voir l'observateur en km
     */
    public int getMaxDistance() {
        return map.get(MAX_DISTANCE);
    }

    /**
     * retourne la largeur en pixel du panorama
     * @return la largeur en pixel du panorama
     */
    public int getWidth() {
        return map.get(WIDTH);
    }

    /**
     * retourne la hauteur en pixel du panorama
     * @return la hauteur en pixel du panorama                                     
     */                                                                            
    public int getHeight() {                                                       
        return map.get(HEIGHT);                                                    
    }                                                                              
                                                                                   
    /**                                                                            
     * retourne l'exposant de suréchantillonnage qui permet d'augmenter la qualité de l'image en ajoutant plus de points dans le calcul d'un pixel
     * @return  l'exposant de suréchantillonnage qui permet d'augmenter la qualité de l'image en ajoutant plus de points dans le calcul d'un pixel
     */
    public int getSuperSamplingExponent() {
        return map.get(SUPER_SAMPLING_EXPONENT);
    }

    /**
     * retourne le PanoramaParameters associé en prenant en compte l'exposant de suréchantillonnage
     * @return le PanoramaParameters associé en prenant en compte l'exposant de suréchantillonnage
     */
    public PanoramaParameters getPanoramaParameters() {
        int superSamplingExponent = getSuperSamplingExponent();
        return new PanoramaParameters(
                //On divise par 10_000 pour revenir en dégré
                new GeoPoint(toRadians(getObserverLongitude()/10_000d), toRadians(getObserverLatitude()/10_000d)),
                getObserverElevation(), 
                toRadians(getCenterAzimuth()),
                toRadians(getHorizontalFieldOfView()), 
                getMaxDistance()*1_000, //On convertie de km en m
                (int) scalb(getWidth(), superSamplingExponent),
                (int) scalb(getHeight(),superSamplingExponent));
    }

    /**
     * retourne le PanoramaParameters associé sans prendre en compte l'exposant de suréchantillonnage
     * @return le PanoramaParameters associé sans prendre en compte l'exposant de suréchantillonnage
     */
    public PanoramaParameters getPanoramaDisplayParameters() {
        return new PanoramaParameters(
                new GeoPoint(toRadians(getObserverLongitude()/10_000d), toRadians(getObserverLatitude()/10_000d)),
                getObserverElevation(), 
                toRadians(getCenterAzimuth()),
                toRadians(getHorizontalFieldOfView()), 
                getMaxDistance()*1_000,
                getWidth(),
                getHeight());
    }
    /**
     * modifie le paramètre height de la map pour que le modèle décrit soit 
     * fonctionnel.
     * @param map
     */
    private void modifyMapForHeight(Map<UserParameter, Integer> map){
       int width = map.get(UserParameter.WIDTH);
       int horizontalField = map.get(UserParameter.HORIZONTAL_FIELD_OF_VIEW);
       
       int height = map.get(UserParameter.HEIGHT);
       
       int maxHeight = Math.min(HEIGHT_MAX ,(int)( (MAXIMAL_VERTICAL_FIELD_OF_VIEW * (width-1.0)) / horizontalField +1 )  );
       if (height != maxHeight){
           map.put(UserParameter.HEIGHT, height <= maxHeight ? height : maxHeight); //si le hauteur entrée est inferieure à la hauteur maximale on la garde sinon on la ramène à cette borne supérieure
       }    
    }
    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PanoramaUserParameters && this.map.equals(((PanoramaUserParameters)obj).map));
    }
}
