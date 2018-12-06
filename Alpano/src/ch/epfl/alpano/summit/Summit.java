package ch.epfl.alpano.summit;

import ch.epfl.alpano.GeoPoint;
import static java.util.Objects.requireNonNull;

/**
 * 
 * @author Paul-Louis DELACOUR (269625)
 * @author Luc MICHELS (273666)
 * représente un sommet
 */
public final class Summit {

    
    private final String name;
    private final GeoPoint position;
    private final int elevation;
   
    /**
     * 
     * @param name
     * @param position
     * @param elevation
     * construit un sommet dont le nom, la position et l'altitude sont ceux donnés.
     * @throws NullPointerException si name ou position sont null
     */
    public Summit(String name, GeoPoint position, int elevation){
        this.name=requireNonNull(name);
        this.position=requireNonNull(position);
        this.elevation=elevation;
    }
    
    /**
     * retourn le nom du sommet
     * @return le nom du sommet  
     */
    public String name() {
        return name;
    }
    
    /**
     * retourne le geoPoint correspondant à la position du sommet
     * @return le geoPoint correspondant à la position du sommet
     */
    public GeoPoint position() {
        return position;
    }
    
    /**
     * retourne l'altitude du sommet
     * @return l'altitude du sommet
     */
    public int elevation() {
        return elevation;
    }
    
    @Override
    public String toString(){
        return name() + " " +  position() + " " + elevation();
    }

}
