package ch.epfl.alpano.dem;

import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;

import static java.util.Objects.requireNonNull;
/**
 * 
 * @author Paul-Louis DELACOUR (269625)
 * @author Luc MICHELS (273666)
 * représente un MNT continu, obtenu par interpolation d'un MNT discret
 */
public final class ContinuousElevationModel {

    private final DiscreteElevationModel dem;
    
 
    /**
     * On calcule le nombre de radian dans une seconde d'angle. 
     * On convertit cette longueur en mètre.
     */
    private double dNS= Distance.toMeters(1d/DiscreteElevationModel.SAMPLES_PER_RADIAN);
    
    /**
     * @param dem
     * construit un MNT continu basé sur le MNT discret passé en 
     * argument, 
     * @throws NullPointerException
     * si celui-ci est nul.
     */
     public ContinuousElevationModel(DiscreteElevationModel dem) {
        this.dem = requireNonNull(dem);
    }
     
     /**
      * retourne l'altitude au point donné, en mètres, obtenue par 
      * interpolation bilinéaire de l'extension du MNT discret 
      * passé au constructeur
      * @param p
      * @return l'altitude au point donné, en mètres, obtenue par 
      * interpolation bilinéaire de l'extension du MNT discret 
      * passé au constructeur
      */
     public double elevationAt(GeoPoint p){
         
        double x = DiscreteElevationModel.sampleIndex(p.longitude());
        double y = DiscreteElevationModel.sampleIndex(p.latitude());
             
        int x0Longitude = (int)Math.floor(x);
        int y0Latitude = (int)Math.floor(y);
        int x1Longitude = x0Longitude+1;
        int y1Latitude = y0Latitude+1;
             
        double altitude00 = elevationAtExtention(x0Longitude, y0Latitude);
        double altitude10 = elevationAtExtention(x1Longitude, y0Latitude);
        double altitude01 = elevationAtExtention(x0Longitude, y1Latitude);
        double altitude11 = elevationAtExtention(x1Longitude, y1Latitude);
             
       //Ici la valeur de x doit être représenté par rapport au point (x0;y0) 
       //pour pouvoir calculer l'interpolation donc on calcule x-x0Longitude et y-y0Latitude;      
       return Math2.bilerp(altitude00, altitude10, altitude01, altitude11, x-x0Longitude, y-y0Latitude);
          
     }
     
     
     
     /**
      * retourne la pente du terrain au point donné, en radians
      * @param p
      * @return la pente du terrain au point donné, en radians.
      */
     public double slopeAt(GeoPoint p){
        
         double x = DiscreteElevationModel.sampleIndex(p.longitude());
         double y = DiscreteElevationModel.sampleIndex(p.latitude());
              
         int x0 = (int)Math.floor(x);
         int y0= (int)Math.floor(y);
         int x1 = x0+1;
         int y1 = y0+1;
         double slope00 = slopeAtInt(x0,y0);
         double slope01 = slopeAtInt(x0,y1);
         double slope10 = slopeAtInt(x1,y0);
         double slope11 = slopeAtInt(x1,y1);
         return Math2.bilerp(slope00, slope10, slope01, slope11, x-x0,y-y0);
         
         
     }
     
     
     /**
      * retourne l'altitude correspondant au point (x,y) si celui est dans le dem.
      * Sinon renvoie 0. 
      * @param x
      * @param y
      * @return l'altitude correspondant au point (x,y) si celui est dans le dem.
      * Sinon renvoie 0. 
      */
     private double elevationAtExtention(int x, int y){
         return dem.extent().contains(x, y)
                ? dem.elevationSample(x, y)
                : 0;
     }
     
     /**
      * retourne la pente associée au point de coordonnées entières (x,y)
      * @param x
      * @param y
      * @return la pente associée au point de coordonnées entières (x,y)
      */
     private double slopeAtInt(int x, int y){
         double origine = elevationAtExtention(x,y);
         double zA = origine-elevationAtExtention(x+1,y); // calcul de la différence d'altitude entre l'altitude du point(x+1,y) et (x,y)
         double zB = origine-elevationAtExtention(x,y+1);
        return slope(zA,zB);       
     }
     
     /**
      * 
      * @param zA
      * @param zB
      * @return la pente à partir des altitudes zA et zB;
      */
     private double slope( double zA, double zB){
         return Math.acos(dNS/(Math.sqrt(zA*zA + zB*zB + dNS*dNS)));
     }
   
}
