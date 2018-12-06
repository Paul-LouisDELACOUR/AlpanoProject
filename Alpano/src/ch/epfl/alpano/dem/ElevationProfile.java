package ch.epfl.alpano.dem;

import static java.lang.Math.PI;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.util.Objects.requireNonNull;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.Distance;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.Preconditions;
/**
 * 
 * @author Paul-Louis DELACOUR (269625)
 * @author Luc MICHELS (273666)
 * représente un profil altimétrique suivant un arc de grand cercle
 */
public final class ElevationProfile {

    private final ContinuousElevationModel elevationModel;
    private final double azimuth;
    private final double length;
    private final double [][] positionArray;
    /**
     * un intervalle de 4096 m pour l'interpolation
     */
    private final static int DELTA = 4096;
    
    /**
     * 
     * @param elevationModel
     * @param origin
     * @param azimuth
     * @param length
     *  qui construit un profil altimétrique basé sur le MNT donné 
     *  et dont le tracé débute au point origin, suit le grand cercle 
     *  dans la direction donnée par azimuth, et a une longueur de 
     *  length mètres ; 
     *  @throws IllegalArgumentException si l'azimuth n'est pas canonique, 
     *  ou si la longueur n'est pas strictement positive ; 
     *  @throws NullPointerException si l'un des deux autres arguments est null.
     */
    public ElevationProfile(ContinuousElevationModel elevationModel,GeoPoint origin, 
            double azimuth, double length){
       if( !Azimuth.isCanonical(azimuth) || length<=0){
           throw new IllegalArgumentException("L'azimuth doit être canonique et la longueur strictement positive"); 
       }
       this.elevationModel = requireNonNull(elevationModel);
       this.azimuth = azimuth;
       this.length=length;
       this.positionArray=positionArray(requireNonNull(origin));
       
       
    }
    
    /**
     * retourne un tableau contenant des positions avec des écarts de DELTA distance entre 2 mesures.
     * La 1ère ligne contient toutes les distances.
     * La 2ème contient les longitudes.
     * La 3ème les latitudes.
     * @return un tableau contenant des positions avec des écarts de DELTA distance entre 2 mesures.
     */
    private double[][] positionArray(GeoPoint origin){
        int taille = (int) Math.ceil(length/DELTA+1);
        double[][] tab = new double[3][taille];
        int distance=0;
       double latitude0 = origin.latitude();
       double longitude0 = origin.longitude();
       double angle =Azimuth.toMath(azimuth);
       
       for (int i=0; i<taille; ++i){
            double x = Distance.toRadians(distance);
            double latitude = latitude(latitude0, x, angle );
            double longitude = longitude(longitude0, x, angle, latitude);
             tab[0][i]=distance;
             tab[1][i]=longitude;
             tab[2][i]=latitude;
            distance+=DELTA;
        }
        return tab;
    }
    
    /**
     * retourne la latitude calculée à un point de coordonnées (λO,φO) d'une distance x en radians;
     * @param latitude0 latitude à partir de laquelle la latitude est calculée
     * @param x : distance en radians au point (λO,φO) .
     * @param a : direction dans laquelle l'observateur regarde
     * @return la latitude calculée à un point d'une distance x en radians;
     */
    private double latitude(double latitude0, double x, double a){
        return asin(sin(latitude0)*cos(x)+ cos(latitude0)*sin(x)*cos(a));
    }
    
    /**
     * retourne la longitude calculée à un point de coordonnées (λO,φO) d'une distance x en radians;
     * @param longitude0 : longitude à partir de laquelle la latitude est calculée
     * @param x : distance en radians au point (λO,φO) .
     * @param a :  direction dans laquelle l'observateur regarde
     * @return la longitude calculée à un point d'une distance x en radians;
     */
    private double longitude( double longitude0, double x, double a, double latitude){
        return ( (longitude0 - asin( sin(a)*sin(x)/cos(latitude) )+PI)%(2*PI)-PI );
    }
    
    /**
     * retourne vraie si la distance x est dans les bornes du profil
     * @param x
     * @return vraie si la distance x est dans les bornes du profil
     */
    private boolean checkInterval(double x){
        return x>=0 && x<=length;
    }
    
    /**
     * retourne l'altitude du terrain à la position donnée du profil, 
     * ou lève l'exception IllegalArgumentException si cette position 
     * n'est pas dans les bornes du profil, c-à-d comprise entre 0 et la 
     * longueur du profil
     * @param x
     * @return l'altitude du terrain à la position donnée du profil
     * @throws IllegalArgumentException si cette position 
     * n'est pas dans les bornes du profil, c-à-d comprise entre 0 et la 
     * longueur du profil
     */
    public double elevationAt(double x){
        Preconditions.checkArgument(checkInterval(x), "Le point n'est pas dans l'interval");
        
        return elevationModel.elevationAt(positionAt(x));
        
    }
    
   
    /**
     * retourne les coordonnées du point à la position donnée du profil, 
     * ou lève l'exception IllegalArgumentException si cette position
     * n'est pas dans les bornes du profil
     * @param x
     * @return les coordonnées du point à la position donnée du profil, 
     * @throws IllegalArgumentException si cette position n'est pas dans les bornes du profil
     */
    public GeoPoint positionAt(double x){
        Preconditions.checkArgument(checkInterval(x), "Le point n'est pas dans l'interval");
        
        double position = Math.scalb(x, -12);  // L'appel à scalb revient à multiplier x par 2^(-12)=4096
        // sachant que les valeurs du tableaux sont décrites tous les 4096m, position correspond la position exacte en double où devrait se trouver la valeur recherchée
        int borneInf= (int) Math.floor(position); // Permet de trouver l'indice correspondant du tableau positionArray
        int borneSup = borneInf+1;
        // On calcule la longitude et la latitude par interpolation des points calculés précedemment.
        double interpolatedLong = Math2.lerp(positionArray[1][borneInf], positionArray[1][borneSup], position-borneInf);
        double interpolatedLat = Math2.lerp(positionArray[2][borneInf], positionArray[2][borneSup], position-borneInf);
        
        return new GeoPoint(interpolatedLong, interpolatedLat);
        
    }
    
    /**
     * retourne la pente du terrain à la position donnée du profil, ou lève 
     * l'exception IllegalArgumentException si cette position n'est pas 
     * dans les bornes du profil
     * @param x
     * @return pente du terrain à la position donnée du profil
     * @throws IllegalArgumentException si cette position n'est pas 
     * dans les bornes du profil.
     */
    public double slopeAt(double x){
        Preconditions.checkArgument(checkInterval(x), "Le point n'est pas dans l'interval");
        return elevationModel.slopeAt(positionAt(x));
    }

}
