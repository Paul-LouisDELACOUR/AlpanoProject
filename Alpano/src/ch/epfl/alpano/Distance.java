package ch.epfl.alpano;

/**
 * @author Paul-Louis DELACOUR (269625)
 * @author Luc MICHELS (273666)
 * Traduit la distance entre 2 points sur le globe en mètre ou en radian.
 */


public interface Distance {

    /**
     * Rayon de la Terre en mètres
     */
    public static double EARTH_RADIUS = 6371000;
    
    /**
     * retourne la distance en radians entre 2 points 
     * @param distanceInMeters (en mètres)
     * @return la distance en radians entre 2 points 
     */
    public static double toRadians(double distanceInMeters){
        return distanceInMeters/EARTH_RADIUS;
    }
    
    /**
     * retourne la distance en mètre entre 2 points.
     * @param distanceInRadians (en radians)
     * @return la distance en mètre entre 2 points.
     */
    public static double toMeters(double distanceInRadians){
        return distanceInRadians*EARTH_RADIUS;
    }
}
