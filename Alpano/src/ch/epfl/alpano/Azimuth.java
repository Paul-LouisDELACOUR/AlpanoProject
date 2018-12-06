package ch.epfl.alpano;


/**
 * @author Paul-Louis DELACOUR (269625)
 * @author Luc MICHELS (273666)
 * Méthodes permettant de manipuler des nombres représentant 
 * des azimuts exprimés en radians.
 */
public interface Azimuth {
   
    /**
     * retourne vrai ssi son argument est un azimut « canonique », c-à-d compris dans l'intervalle [0;2π[
     * @param azimuth (angle en radians)
     * @return true ssi son argument (azimuth) est compris dans l'intervalle [0;2π[.
     */
    public static boolean isCanonical(double azimuth){
        return ( azimuth >= 0 && azimuth < Math2.PI2 );
    }
    
    
    /**
     * retourne l'azimut canonique équivalent à celui passé en argument, c-à-d compris dans l'intervalle [0;2π[.
     * @param azimuth (angle azimuth en radians)
     * @return l'azimut canonique équivalent à celui passé en argument, c-à-d compris dans l'intervalle [0;2π[.
     */
    public static double canonicalize(double azimuth){
        return Math2.floorMod(azimuth,Math2.PI2);
    }
    
  
    /**
     * transforme un azimuth en angle mathématique, c-à-d exprimé dans le sens antihoraire,
     *  ou lève l'exception IllegalArgumentException si son argument n'est pas un azimut canonique
     * @param azimuth (angle azimuth en radians qui doit être canonique).
     * @return l'ange azimuth converti en un angle mathématique (c'est à dire dans le sens antihoraire).
     * @throws IllegalArgumentException si son argument n'est pas un azimut canonique
     */
    public static double toMath(double azimuth) {
        Preconditions.checkArgument(isCanonical(azimuth));
       
        return canonicalize(-azimuth);
        
    }
    
   
    /**
     * transforme un angle mathématique en azimut,
     *  c-à-d exprimé dans le sens horaire, ou lève l'exception IllegalArgumentException
     *   si l'argument n'est pas compris dans l'intervalle [0;2π[
     * @param angleMath (angle mathématique en radian qui doit être dans l'intervalle [0;2π[)
     * @return l'angle angleMath converti en angle azimuth canonique;
     * @throws IllegalArgumentException si l'argument n'est pas compris dans l'intervalle [0;2π[
    */
    public static double fromMath(double angleMath){
        Preconditions.checkArgument(isCanonical(angleMath));
        return canonicalize(-angleMath);
    }
    
    
    /**
     *  retourne une chaîne correspondant à l'octant dans lequel se trouve l'azimut donné,
     *  formée en combinant les chaînes n, e, s et w correspondant aux quatre points cardinaux
     *  (resp. nord, est, sud et ouest)
     * @param azimuth (angle azimuth qui doit être canonique).
     * @param n
     * @param e
     * @param s
     * @param w
     * @return  retourne une chaîne correspondant à l'octant dans lequel se trouve l'azimut donné,
     *   formée en combinant les chaînes n, e, s et w correspondant aux quatre points cardinaux
     *  (resp. nord, est, sud et ouest)
     * @throws IllegalArgumentException si l'angle n'est pas canonique
     */
    public static String toOctantString(double azimuth, String n,String e,String s, String w){
        Preconditions.checkArgument(isCanonical(azimuth));
        int quarterNumber = 1;
        int number = 1;
        double newAzimuth = Math2.floorMod(azimuth - Math.PI/8,Math2.PI2); //on décale tout de pi/8 dans le sens trigonométrique pour avoir tout entre 0 et 2pi:
                                                                           //le nordest correspond maintenant a la portion 1 et la portion 8 devient le nord
                                                                           // L'intérêt de cette méthode est que le nord n'est plus séparé entre une partie positive et une parite négative.
        for ( double quarter = 0; quarter < Math2.PI2   ; quarter += Math.PI/4){ 
       
            if( newAzimuth >= quarter && newAzimuth < quarter +  Math.PI/4){
                quarterNumber = number;
                break;
            }else{
                ++number;
            } 
        }
        
        String octantDirection = "";
        switch(quarterNumber){
        
        case 1 :
            octantDirection += (n+e);
            break;
        case 2 : 
            octantDirection += e;
            break;
        case 3 :
            octantDirection += (s+e);
            break;
        case 4 : 
            octantDirection += s;
            break;
        case 5 :
            octantDirection += (s+w);
            break;
        case 6 : 
            octantDirection += w;
            break;
        case 7 :
            octantDirection += (n+w);
            break;
        case 8 : 
            octantDirection += n;
            break;   
        }
      
        return octantDirection;
    }
    
    
}
