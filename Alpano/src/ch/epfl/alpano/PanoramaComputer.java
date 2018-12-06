package ch.epfl.alpano;

import static java.util.Objects.requireNonNull;
import static java.lang.Math.*;

import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
/**
 * 
 * @author Paul-Louis DELACOUR (269625)
 * @author Luc MICHELS (273666)
 * représente un calculateur de panorama.
 */
public final class PanoramaComputer {
    
    public final static double RESEARCHINTERVAL = 64; //metre
    private final static double EPSILON = 4;//precision en metre
    private final static double K = 0.13;//coefficient de réfraction
    private final static double RELATIVED = (1-K)/(2*Distance.EARTH_RADIUS);
    private final ContinuousElevationModel dem;
    
    /**
     * construit un calculateur de panorama obtenant les données du MNT continu passé en argument, 
     * ou lève l'exception NullPointerException s'il est nul.
     * @param dem
     * @throws NullPointerException si dem est null
     */
    public PanoramaComputer(ContinuousElevationModel dem){
        this.dem = requireNonNull(dem);
    }
    /**
     * retourne le panorama spécifié par les paramètres.
     * @param parameters
     * @return le panorama spécifié par les paramètres.
     * il créer le panorama en utilisant la méthode des rayons avec 
     * un elevationprofile pour chaque azimuth du panorama et en buildant 
     * le panoram au fur et à mesure qu'il évolue sur les profiles
     */
    public Panorama computePanorama(PanoramaParameters parameters){
       
        Panorama.Builder panoramaBuilder = new Panorama.Builder(parameters);
        int width = parameters.width();
        int height = parameters.height();
        int maxDistance = parameters.maxDistance();
        GeoPoint origin = parameters.observerPosition();
        double ray0 = parameters.observerElevation();
        
        for(int x = 0; x <= width-1 ;++x){
            
            ElevationProfile profile = new ElevationProfile(dem, origin, parameters.azimuthForX(x), maxDistance);
            double lastIntersectionDistance = 0;
            for (int y = height -1; y >= 0 ;--y){ //on lance les rayons de bas en haut
                double altitudeForY = parameters.altitudeForY(y);
                double raySlope = tan(altitudeForY);
                DoubleUnaryOperator f = rayToGroundDistance(profile, ray0, raySlope);
                double borneInf = Math2.firstIntervalContainingRoot(f, lastIntersectionDistance, maxDistance, RESEARCHINTERVAL);
                
              if(borneInf != Double.POSITIVE_INFINITY){ // Si la borneInf est positive infinity, cela 
                                                       // veut dire que le rayon n'a pas de limites donc qu'on est dans le ciel
                  lastIntersectionDistance = Math2.improveRoot(f, borneInf, borneInf+RESEARCHINTERVAL, EPSILON);
                  GeoPoint pointAtIntersection = profile.positionAt(lastIntersectionDistance);
                  float distance = (float)(lastIntersectionDistance/cos(altitudeForY)); 
                  float longitude = (float)pointAtIntersection.longitude();
                  float latitude = (float)pointAtIntersection.latitude();
                  float elevation = (float)profile.elevationAt(lastIntersectionDistance);
                  float slope = (float)profile.slopeAt(lastIntersectionDistance);
              
                  panoramaBuilder.setDistanceAt(x, y, distance).setElevationAt(x, y, elevation).setLatitudeAt(x, y, latitude)
                  .setLongitudeAt(x, y, longitude).setSlopeAt(x, y, slope);
              }
              else{
                  break;//comme il n'y a pas eu d'intersection avec le sol il n'y en aurra pas dans le futur pour ce y
              }
            }
        }
        return panoramaBuilder.build();
    }
   /**
    * retourne la fonction donnant la distance entre un rayon d'altitude initiale ray0 et de pente de raySlope, et le profil altimétrique profile.
    *  avec la formule : r0+x.tanα−p(x)+(1−k)/(2R).x^2
    * @param profile: profil altimétrique @see {@link ElevationProfile}
    * @param ray0: altitude initiale en m
    * @param raySlope: pente initiale
    * @return la fonction donnant la distance entre un rayon d'altitude initiale ray0 et de pente de raySlope, et le profil altimétrique profile.
    *  avec la formule : r0+x.tanα−p(x)+(1−k)/(2R).x^2
    */
    public static DoubleUnaryOperator rayToGroundDistance(ElevationProfile profile, double ray0, double raySlope){
        return x -> ray0 + x * raySlope - profile.elevationAt(x) + RELATIVED*Math2.sq(x);
            
    }
   
    
}
