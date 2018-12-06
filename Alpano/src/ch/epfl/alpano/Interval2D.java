package ch.epfl.alpano;

import java.util.Objects;

/**
 * 
 * @author Paul-Louis DELACOUR (269625)
 * @author Luc MICHELS (273666)
 * un intervalle d'entiers à 2 dimensions
 */
public final class Interval2D {

    private final Interval1D iX;
    private final Interval1D iY;
    
    public Interval2D(Interval1D iX, Interval1D iY){
        Preconditions.checkNullPointer( (iX != null && iY!= null), "un des 2 intervalles est nul");
        this.iX=iX;
        this.iY=iY;
    }
    /**
     * retourne le premier intervalle du produit cartésien
     * @return le premier intervalle du produit cartésien
     */
    public Interval1D iX(){
        return iX;
    }
    
    /**
     * retourne le second intervalle du produit cartésien
     * @return le second intervalle du produit cartésien
     */
    public Interval1D iY(){
        return iY;
    }
    
    /**
     * retourne vrai si l'interval contient la paire (x,y)
     * @param x
     * @param y
     * @return vrai si l'interval contient la paire (x,y)
     */
    public boolean contains(int x, int y){
        return (iX.contains(x) && iY.contains(y));
    }
    
    /**
     * retourne la taille de l'interval, c'est à dire le nombre d'éléments qu'il contient.
     * @return taille de l'intervalle, c-à-d 
     * le nombre d'éléments qu'il contient
     */
    public int size(){
        return iX.size()*iY.size();
    }
    
    /**
     * retourne la taille de l'intersection du récepteur 
     * this avec l'argument that
     * @param that
     * @return taille de l'intersection du récepteur 
     * this avec l'argument that
     */
    public int sizeOfIntersectionWith(Interval2D that){
       int sizeIntersectionOfIX = this.iX().sizeOfIntersectionWith(that.iX());
       int sizeIntersectionOfIY = this.iY().sizeOfIntersectionWith(that.iY());
       return sizeIntersectionOfIX*sizeIntersectionOfIY;
    }
    
    /**
     * retourne l'union englobante du récepteur this et de l'argument that
     * @param that
     * @return union englobante du récepteur this et de l'argument that
     */
    public Interval2D boundingUnion(Interval2D that){
        return new Interval2D(this.iX().boundingUnion(that.iX()), this.iY().boundingUnion( that.iY() ));
    }
    
    /**
     * retourne vrai ssi le récepteur this 
     * et l'argument that sont unionables
     * @param that
     * @return  retourne vrai ssi le récepteur this 
     * et l'argument that sont unionables
     */
    public boolean isUnionableWith(Interval2D that){
        return (this.size()+that.size()-this.sizeOfIntersectionWith(that) == this.boundingUnion(that).size() );
    }
    
    /**
     * 
     * @param that
     * @return l'union du récepteur et de that ou lève l'exception 
     * IllegalArgumentException s'ils ne sont pas unionables
     */
    public Interval2D union(Interval2D that)  {
        Preconditions.checkArgument(this.isUnionableWith(that), "Les intervalles ne sont pas unionables");
        return this.boundingUnion(that); // Dans le cas où les intervalles sont unionables, 
                                         // alors l'union est la même que l'union globale
    }
    
    /**
     * redéfinit la méthode equals héritée de Object et 
     * retourne vrai ssi thatO est également une instance 
     * de Interval2D et l'ensemble de ses éléments est égal 
     * à celui du récepteur.
     * retourne true ssi les interval2D sont constituées des mêmes interval1D.
     */
    @Override
    public boolean equals(Object obj){
        return (obj instanceof Interval2D) && 
                (this.iX().equals( ((Interval2D)obj).iX() ) && this.iY().equals( ((Interval2D)obj).iY() )) ;
    }
    
    @Override
    public int hashCode(){
        return Objects.hash(iX(), iY());
    }
    
    /**
     * redéfinit la méthode toString héritée de Object 
     * et qui retourne une chaîne composée de la 
     * représentation textuelle des deux intervalles 
     * (dans l'ordre) séparés par la lettre x ou le 
     * symbole de multiplication ×
     * retourne [borne inferieur1..borne superieur1]x[borne inferieur2..borne superieur2]

     */
    public String toString(){
        return iX().toString() + "x" + iY().toString();
    }
}
