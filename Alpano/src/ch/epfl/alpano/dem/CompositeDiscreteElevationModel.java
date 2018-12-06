package ch.epfl.alpano.dem;

import static java.util.Objects.requireNonNull;

import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;

/**
 * 
 * @author Paul-Louis DELACOUR (269625)
 * @author Luc MICHELS (273666)
 * représente l'union de deux modèles du terrain discrets
 */
final class CompositeDiscreteElevationModel implements DiscreteElevationModel, AutoCloseable {

    private final Interval2D interval;
    private final DiscreteElevationModel dem1;
    private final DiscreteElevationModel dem2;
    
    /**
     * 
     * @param dem1
     * @param dem2
     * créer un dem composé de deux autres dem
     * @throws NullPointerException si un des arguments est null
     */
    public CompositeDiscreteElevationModel(DiscreteElevationModel dem1, DiscreteElevationModel dem2) {
        Preconditions.checkArgument( dem1.extent().isUnionableWith(dem2.extent()) );//faire un assert equals
        this.dem1= requireNonNull(dem1);
        this.dem2= requireNonNull(dem2);
        
        this.interval = dem1.extent().union(dem2.extent());
        
    }

    @Override
    public void close() throws Exception {
        dem1.close();
        dem2.close();
    }
    @Override
    public Interval2D extent() {
        return interval;
    }

    @Override
    public double elevationSample(int x, int y){
        Preconditions.checkArgument(extent().contains(x, y), "Le point n'est pas contenu dans l'étendu");
        return dem1.extent().contains(x, y) 
                ? dem1.elevationSample(x, y)
                : dem2.elevationSample(x, y);
    }

}
