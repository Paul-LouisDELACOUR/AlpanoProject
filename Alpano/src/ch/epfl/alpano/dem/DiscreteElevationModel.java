package ch.epfl.alpano.dem;

import ch.epfl.alpano.Distance;
import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;
/**
 * 
 * @author Paul-Louis DELACOUR (269625)
 * @author Luc MICHELS (273666)
 * représente un modèle du terrain discret
 */
public interface DiscreteElevationModel extends AutoCloseable {
    
    /**
     * le nombre de d'échantillions dans un degré
     */
    public static final int SAMPLES_PER_DEGREE = 3600;
    
    /**
     * le nombre de d'échantillions dans un radians
     */
    public static final double SAMPLES_PER_RADIAN = SAMPLES_PER_DEGREE*180/Math.PI;
    
    /**
     * la distance entre deux échantillions
     */
    public static final double dNS = Distance.toMeters(1/DiscreteElevationModel.SAMPLES_PER_RADIAN);
    
    /**
     * retourne l'index correspondant à l'angle donné (une longitude ou une latitude),
     * exprimé en radians;
     * @param angle en radians
     * @return l'index correspondant à l'angle donné, exprimé en radians;
     * cet angle peut représenter soit une longitude, soit une latitude,
     * étant donné que la distance angulaire entre deux échantillons est égale dans les deux cas.
     */
    public static double sampleIndex(double angle){
        return angle*SAMPLES_PER_RADIAN;
    }
    
    /**
     * retourne l'étendue du MNT
     * @return l'étendue du MNT
     */
    public abstract Interval2D extent();
    
    /**
     * retourne l'échantillon d'altitude à l'index 
     * donné, en mètres, ou lève l'exception 
     * IllegalArgumentException si l'index ne fait 
     * pas partie de l'étendue du MNT.
     * @param x
     * @param y
     * @return l'échantillon d'altitude à l'index 
     * donné, en mètres
     * @throws IllegalArgumentException si l'index ne fait 
     * pas partie de l'étendue du MNT.
     */
    public abstract double elevationSample(int x, int y);
    
    /**
     * retourne un MNT discret représentant l'union du récepteur 
     * et de l'argument that, ou lève l'exception 
     * IllegalArgumentException si leurs étendues ne sont pas 
     * unionables.
     * @param that
     * @return un MNT discret représentant l'union du récepteur 
     * et de l'argument that
     * @throws IllegalArgumentException si leurs étendues ne sont pas 
     * unionables.
     */
    public default DiscreteElevationModel union(DiscreteElevationModel that){
       Preconditions.checkNullPointer(this!=null && that != null, "un des deux DiscreteElevationModel est null");
       return new CompositeDiscreteElevationModel(this, that);
    }
    
}
