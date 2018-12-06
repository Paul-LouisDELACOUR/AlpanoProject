package ch.epfl.alpano;

import java.util.Locale;
/**
 * 
 * @author Paul-Louis DELACOUR (269625)
 * @author Luc MICHELS (273666)
 * correspond a un point sur le globe avec une longitude et une latitude
 */
public final class GeoPoint {

    private final double longitude, latitude;
    
    //La latitude et la longitude sont données en randians
    /**
     * 
     * @param longitude
     * @param latitude
     * créer le geopoint
     * @throws IllegalArgumentException si la latitude ou la longitude sont invalides
     */
    public GeoPoint(double longitude, double latitude) {
        Preconditions.checkArgument(belong(longitude, -Math.PI, Math.PI) , "La longitude doit être comprise entre -PI et PI");
        Preconditions.checkArgument(belong(latitude, -Math.PI/2, Math.PI/2) , "La latitude doit être comprise entre -PI/2 et PI/2");            
        this.longitude = longitude;
        this.latitude=latitude;
       
    }
    
    /**
     *Permet de vérier l'appartenance de value à un interval fermé borné délimité
     * par borneInf et borneSup 
     * @param value
     * @param borneInf
     * @param borneSup
     * @return un boolean qui est vrai si value appartient à l'intervalle
     */
    private boolean belong (double value, double borneInf, double borneSup ){
        return (value>= borneInf && value<= borneSup );
    }
    
    /**
     * retourne la longitude du point, en radians
     * @return la longitude du point, en radians
     */
    public double longitude(){
        return longitude;
    }
    
    /**
     * retourne la latitude du point, en radians
     * @return latitude du point, en radians
     */
    public double latitude(){
        return latitude;
    }
    
    /**
     * retourne la distance en mètres séparant les deux geopoints récepteur (this) de l'argument (that)
     * @param that (autre geopoint)
     * @return distance en mètres séparant le récepteur (this) de l'argument (that)
     */
    public double distanceTo(GeoPoint that){
        double angle = 2*Math.asin(Math.sqrt(Math2.haversin(this.latitude-that.latitude) 
                + Math.cos(this.latitude)*Math.cos(that.latitude)*Math2.haversin(this.longitude-that.longitude)));
                //2arcsin(haversin(φ1−φ2)+cosφ1cosφ2haversin(λ1−λ2)
        return Distance.toMeters(angle);
    }            
    
    /**l'azimut des deux geopoints récepteur l'argument (that) par rapport au récepteur (this)
     * @param that geopoint 
     * @return l'azimut canonique de l'argument (that) par rapport au récepteur (this) exprimé en radian
     */
    public double azimuthTo(GeoPoint that){
        double angleMath = Math.atan2( ( Math.sin(this.longitude-that.longitude)*Math.cos(that.latitude) ),
                 (Math.cos(this.latitude)*Math.sin(that.latitude) - Math.sin(this.latitude)*Math.cos(that.latitude)
                        *Math.cos(this.longitude-that.longitude) ) );
                //β=arctan( (sin(λ1−λ2).cosφ2) / (cosφ1.sinφ2-sinφ1.cosφ2.cos(λ1−λ2)) )
        return Azimuth.fromMath( Azimuth.canonicalize(angleMath));
    }
    
    /**
     * redéfinit la méthode toString héritée de Object et retourne 
     * une chaîne composée des coordonnées du point, exprimées en 
     * degrés (!) avec exactement 4 décimales, séparées par une 
     * virgule et entourées de parenthèses.
     * @return (longitude,latitude) avec une précision de 4 decimales
     */
    @Override
    public String toString(){
        double roundLatitude = Math.toDegrees(this.latitude);
        double roundLongitude =  Math.toDegrees(this.longitude);
        Locale l = null;
        String s = String.format(l, "(%.4f,%.4f)", roundLongitude, roundLatitude);
        return s;
        
    }

}
