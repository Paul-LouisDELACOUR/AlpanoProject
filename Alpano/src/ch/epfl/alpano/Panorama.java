package ch.epfl.alpano;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
/**
 * 
 * @author Paul-Louis DELACOUR (269625)
 * @author Luc MICHELS (273666)
 * représente un panorama.
 */
public final class Panorama {

    private final float[] distance;
    private final float[] longitude;
    private final float[] latitude;
    private final float[] altitude;
    private final float[] pente;
    
    private final PanoramaParameters parameters;
    /**
     * 
     * @param parameters : paramètres du panorama
     * @param distance : distance parcourrue par le rayon jusqu'à ateindre le sol (en m
)     * @param longitude : longitude de ce point d'intersection (en radians)
     * @param latitude : latitude de ce point d'intersection (en radians)
     * @param altitude : altitude de ce point d'intersection (en m)
     * @param pente : pente de ce point d'intersection
     */
    private Panorama(PanoramaParameters parameters, float[] distance, float[] longitude,
            float[] latitude, float[] altitude, float[] pente ) {
        this.distance = distance;
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
        this.pente = pente;
        this.parameters = parameters;
        
    }
    
    /**
     * retourne les paramètres du panorama
     * @return les paramètres du panorama
     */
    public PanoramaParameters parameters(){
        return parameters;
    }
    
   
    
    /**
     * retourne la distance pour le point de coordonnées données
     * @param x abcisse du point
     * @param y ordonne du point
     * @return la distance pour le point de coordonnées données
     * @throws IndexOutOfBoundsException si les indices entrées ne sont pas valides
     */
    public float distanceAt(int x, int y){
        Preconditions.checkIndexOutOfBoundsException(parameters.isValidSampleIndex(x, y)); 
        return distance[parameters.linearSampleIndex(x, y)];
           
       
    }
    
    /**
     * retourne la distance pour le point de coordonnées données, ou la valeur par défaut d 
     * si les coordonnées sont hors des bornes du panorama.
     * @param x abcisse du point
     * @param y ordonne du point
     * @param d distance par défaut
     * @return la distance pour le point de coordonnées données, ou la valeur par défaut d 
     * si les coordonnées sont hors des bornes du panorama.
     */
    public float distanceAt(int x, int y, float d){
        return  !parameters.isValidSampleIndex(x, y) ? d : distance[parameters.linearSampleIndex(x, y)];
     }
    
    /**
     * retourne la longitude pour le point de coordonnée donnée
     * @param x abcisse du point
     * @param y ordonne du point
     * @return la longitude pour le point de coordonnée donnée
     * @throws IndexOutOfBoundsException si les indices entrées ne sont pas valides
     */
    public float longitudeAt(int x, int y){
        Preconditions.checkIndexOutOfBoundsException(parameters.isValidSampleIndex(x, y)); 
        return longitude[parameters.linearSampleIndex(x, y)];
    }    

    /**
     * retourne la latitude pour le point de coordonnées données
     * @param x abcisse du point
     * @param y ordonne du point
     * @return la latitude pour le point de coordonnées données
     * @throws IndexOutOfBoundsException si les indices entrées ne sont pas valides
     */
    public float latitudeAt(int x, int y){
        Preconditions.checkIndexOutOfBoundsException(parameters.isValidSampleIndex(x, y)); 
        return latitude[parameters.linearSampleIndex(x, y)];
    
    }
    
    /**
     * retourne l'altitude pour le point de coordonnées données
     * @param x abcisse du point
     * @param y ordonne du point
     * @return l'altitude pour le point de coordonnées données
     * @throws IndexOutOfBoundsException si les indices entrées ne sont pas valides
     */
    public float elevationAt(int x, int y) {
        Preconditions.checkIndexOutOfBoundsException(parameters.isValidSampleIndex(x, y)); 
        return altitude[parameters.linearSampleIndex(x, y)];
          
    }
    
    /**
     * retourne la pente pour le point de coordonnées données
     * @param x abcisse du point
     * @param y ordonne du point
     * @return la pente pour le point de coordonnées données
     * @throws IndexOutOfBoundsException si les indices entrées ne sont pas valides
     */
    public float slopeAt(int x, int y) {
        Preconditions.checkIndexOutOfBoundsException(parameters.isValidSampleIndex(x, y)); 
        return pente[parameters.linearSampleIndex(x, y)];
          
    }
    
    /**
     * 
     * @author Paul-Louis DELACOUR (269625)
     * @author Luc MICHELS (273666)
     * représente un constructeur de panorama.
     */
    static public final class Builder{
        private boolean hasNotBeenConstructed = true;
        
        private PanoramaParameters parameters;
        private float[] distance;
        private float[] longitude;
        private float[] latitude;
        private float[] altitude;
        private float[] pente;
        
        /**
         * @param parameters les paramètres du panorama que l'on veut construire
         */
        public Builder(PanoramaParameters parameters){
            this.parameters = requireNonNull(parameters);
            int taille = parameters.width()*parameters.height();
            this.distance = new float[taille];
            this.longitude = new float[taille];
            this.latitude = new float[taille];
            this.altitude = new float[taille];
            this.pente = new float[taille];
            
            Arrays.fill(distance, Float.POSITIVE_INFINITY); 
            
        }
        
        /**
         * défini la distance au point de coordonnées (x,y) du panorama
         * @param x abcisse du point
         * @param y ordonnée du point
         * @param distance (en m)
         * @return le constructeur ainsi modifié
         * @throws IllegalStateException si le constructeur a déjà été appelé
         * @throws IndexOutOfBoundsException si point point n'est pas contenu dans la bornes
         */
        public Builder setDistanceAt(int x, int y, float distance){
            Preconditions.checkIllegalStateException(hasNotBeenConstructed,"Le constructeur a deja été appelé");
            Preconditions.checkIndexOutOfBoundsException(parameters.isValidSampleIndex(x, y), "le point nest pas contenu dans les bornes");
            int index = parameters.linearSampleIndex(x, y);
            this.distance[index] = distance; 
            return this;
            
        }
        
        /**
         * défini la longitude au point de coordonnées (x,y) du panorama
         * @param x abcisse du point
         * @param y ordonne du point
         * @param longitude en radians
         * @return le constructeur ainsi modifié
         * @throws IllegalStateException si le constructeur a déjà été appelé
         * @throws IndexOutOfBoundsException si point point n'est pas contenu dans la bornes
         * @throws IllegalArgumentException si la longitude n'est pas valide
         */
        public Builder setLongitudeAt(int x, int y, float longitude){
            Preconditions.checkIllegalStateException(hasNotBeenConstructed,"Le constructeur a deja ete appele");
            Preconditions.checkIndexOutOfBoundsException(parameters.isValidSampleIndex(x, y), "le point nest pas contenu dans les bornes");
            Preconditions.checkArgument(validLongitude(longitude), "longitude pas entre -pi et pi inclu");
            int index = parameters.linearSampleIndex(x, y);
            
            this.longitude[index] = longitude; 
            return this;
        }
        
        /**
         * défini la latitude au point de coordonnées (x,y) du panorama
         * @param x abcisse du point
         * @param y ordonnée du point
         * @param latitude
         * @return le constructeur ainsi modifié
         * @throws IllegalStateException si le constructeur a déjà été appelé
         * @throws IndexOutOfBoundsException si point point n"est pas contenu dans la bornes
         * @throws IllegalArgumentException si la latitude n'est pas valide
         */
        public Builder setLatitudeAt(int x, int y, float latitude){
            Preconditions.checkIllegalStateException(hasNotBeenConstructed,"Le constructeur a deja ete appele");
            Preconditions.checkIndexOutOfBoundsException(parameters.isValidSampleIndex(x, y), "le point nest pas contenu dans les bornes");
            Preconditions.checkArgument(validLatitude(latitude),"latitude pas entre -pi/2 et pi/2 inclu");
            int index = parameters.linearSampleIndex(x, y);
            
            this.latitude[index] = latitude; 
            return this;
        }
        
        /**
         * défini l'élévation au point de coordonnées (x,y) du panorama
         * @param x abcisse du point
         * @param y ordonnée du point
         * @param elevation
         * @return le constructeur ainsi modifié
         * @throws IllegalStateException si le constructeur a déjà été appelé
         * @throws IndexOutOfBoundsException si point point n"est pas contenu dans la bornes
         */
        public  Builder setElevationAt(int x, int y, float elevation){
            Preconditions.checkIllegalStateException(hasNotBeenConstructed,"Le constructeur a deja ete appele");
            Preconditions.checkIndexOutOfBoundsException(parameters.isValidSampleIndex(x, y), "le point nest pas contenu dans les bornes");
            int index = parameters.linearSampleIndex(x, y);
            
            this.altitude[index] = elevation; 
            
            return this;
        }
        
        /**
         * défini la pente au point de coordonnées (x,y) du panorama
         * @param x abcisse du point
         * @param y ordonnée du point
         * @param slope
         * @return le constructeur ainsi modifié
         * @throws IllegalStateException si le constructeur a déjà été appelé
         * @throws IndexOutOfBoundsException si point point n"est pas contenu dans la bornes
         */
        public Builder setSlopeAt(int x, int y, float slope){
            Preconditions.checkIllegalStateException(hasNotBeenConstructed,"Le constructeur a deja ete appele");
            Preconditions.checkIndexOutOfBoundsException(parameters.isValidSampleIndex(x, y), "le point nest pas contenu dans les bornes");
            int index = parameters.linearSampleIndex(x, y);
            
            this.pente[index] = slope; 
            
            return this;
        }
        
        /**
         * construit et puis retourne le panorama
         * @return le panorama construit
         */
        public Panorama build(){
            Preconditions.checkIllegalStateException(hasNotBeenConstructed,"Le constructeur a deja ete appele");
            hasNotBeenConstructed = false;
           Panorama pano = new Panorama(parameters, distance, longitude, latitude, altitude, pente);
            distance =null;
           longitude = null;
           latitude = null;
           altitude = null;
           pente = null;
           
            return pano;

        }
        
        /**
         * retourne vrai si la longitude est valide
         * @param longitude (en radians)
         * @return vrai si la longitude est valide
         */
        private boolean validLongitude(float longitude){
            return (longitude >= (float)-Math.PI && longitude <= (float)Math.PI);
        }
        
        /**
         * retourne vrai si la latitude est valide
         * @param latitude (en radians)
         * @return vrai si la latitude est valide
         */
        private boolean validLatitude(float latitude){
            return (latitude >= (float)-Math.PI/2 && latitude <= (float)Math.PI/2);
        }  
    }
    
    
    

}
