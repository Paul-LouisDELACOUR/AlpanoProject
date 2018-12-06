package ch.epfl.alpano.gui;

/**
 * énumeration contenant toute les bornes où les valeurs des attributs d'un panorama sont valides
 * dans l'ordre suivant:<br>
 * [OBSERVER_LONGITUDE,<br> 
 * OBSERVER_LATITUDE, <br>
 * OBSERVER_ELEVATION,<br>
 * CENTER_AZIMUTH, <br>
 * HORIZONTAL_FIELD_OF_VIEW, <br>
 * MAX_DISTANCE,<br>
 * WIDTH, <br>
 * HEIGHT, <br>
 * SUPER_SAMPLING_EXPONENT]
 * @author Luc MICHELS
 * @author Paul-Louis DELACOUR
 */
public enum UserParameter {
    
    OBSERVER_LONGITUDE (60_000,120_000), //dix-millième de degrés
    OBSERVER_LATITUDE (450_000,480_000), //dix-millième de degrés
    OBSERVER_ELEVATION (300,10_000), //m
    CENTER_AZIMUTH (0,359), //degré
    HORIZONTAL_FIELD_OF_VIEW (1,360), //degré
    MAX_DISTANCE (10,600),//km
    WIDTH (30,16_000),//échantillons
    HEIGHT (10, 4_000), //échantillons
    SUPER_SAMPLING_EXPONENT (0,2);//pas d'unité
    
    private final int minValue;
    private final int maxValue;
   
    /**
     * 
     * @param minValue valeur minimale de l'attribut 
     * @param maxValue valeur maximale de l'attribut 
     */
    private UserParameter(int minValue, int maxValue){
        this.minValue = minValue;
        this.maxValue = maxValue;
    }
    /**
     * retourne la valeur maximale de l'attribut
     * @return la valeur maximale de l'attribut
     */
    public int getMaxValue() {
        return maxValue;
    }
    
    /**
     * retourne la valeur minimale de l'attribut
     * @return la valeur minimale de l'attribut
     */
    public int getMinValue() {
        return minValue;
    }
    
    /**
     * renvoie une valeur ramenée dans les bornes acceptables d'un attribut s'il n'est pas valable.
     * @param value : valeur redimensionée si elle n'est pas comprises dans les bornes acceptables.
     * @return une valeur ramenée dans les bornes acceptables d'un attribut s'il n'est pas valable
     */
    public int sanitize(int value){
        if (value>maxValue){
            return maxValue;
        }
        if (value <minValue){
            return minValue;
        }
        
        return value;
    }
    
    
}
