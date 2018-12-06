package ch.epfl.alpano;

import java.util.Objects;
import static java.lang.Math.min;
import static java.lang.Math.max;
/**
 * 
 * @author Paul-Louis DELACOUR (269625)
 * @author Luc MICHELS (273666)
 * un intervalle d'entiers à une dimension
 */
public final class Interval1D {

    
    private final int includedFrom;
    private final int includedTo;
        
    public Interval1D(int includedFrom, int includedTo) {
        Preconditions.checkArgument(includedTo>=includedFrom, "Les bornes ne sont pas bonnes");;
        
        this.includedFrom=includedFrom;
        this.includedTo=includedTo;
        
    }
    
    /**
     * 
     * @return la borne inférieure de l'intervalle
     */
    public int includedFrom(){
        return includedFrom;
    }
    
    /**
     * 
     * @return la borne supérieure de l'intervalle
     */
    public int includedTo(){
        return includedTo;
    }
    
    /**
     * retourne vrai si l'entier v appartient a l'intervalle
     * @param v entier a vérifier s'il appartient a l'intervalle
     * @return vrai si l'entier v appartient a l'intervalle
     */
    public  boolean contains(int v){
        return (v>= includedFrom && v<= includedTo);
    }
    
    /**
     * retourne la taille de l'intervalle 
     * @return la taille de l'intervalle 
     */
    public int size(){
        return includedTo-includedFrom+1;
    }
    
    /**
     * retourne la taille de l'intersection du récepteur 
     * this et de l'argument that
     * @param that
     * @return la taille de l'intersection du récepteur 
     * this et de l'argument that
     */
    public int sizeOfIntersectionWith(Interval1D that){
        
        int min = max(this.includedFrom(),that.includedFrom()); // La borne inférieure est 
                                                                    //le maximum entre les 2 bornes Inférieurs
        int max = min(this.includedTo(), that.includedTo());  // La borne supérieure est
                                                                   // le minimum entre les 2 bornes supérieures
        
        int taille = max-min+1; // nombre d'élément de l'intersection
        
        return taille<=0 ? 0 : taille;
       // Si la taille est négative on a une union disjointe.
    }
    
    
    /**
     * retourne l'union englobante du récepteur 
     * this et de l'argument that
     * @param that
     * @return l'union englobante du récepteur 
     * this et de l'argument that
     */
    
    public Interval1D boundingUnion(Interval1D that){
        int min = min( includedFrom() , that.includedFrom());
        int max = max( includedTo() , that.includedTo());
        
        return new Interval1D(min, max);
       
    }
    
  
    /**
     * retourne vrai ssi le récepteur this et l'argument that sont unionables
     * @param that (un autre Interval1D)
     * @return true ssi le récepteur this et l'argument that sont unionables
     */
    public boolean isUnionableWith(Interval1D that){
        return ( size() + that.size() - sizeOfIntersectionWith(that)  ==  boundingUnion(that).size() );
    }
    
    
    /**
     * retourne l'intervalle qui va être unifié avec l'instance courante d'intervalle
     * @param that : intervalle qui va etre unifié avec l'instance courante d'intervalle
     * @return l'intervalle qui va être unifié avec l'instance courante d'intervalle
     */
    public Interval1D union(Interval1D that){
        Preconditions.checkArgument(isUnionableWith(that), "les intervales ne sont pas interconnecte");
        return boundingUnion(that);//dans le cas ou les intervalles sont unionable alors l'union est la meme que l'union globale
    }
    
    /**
     * retourne true si les intervalles sont égaux c a d que les bornes des intervalles sont égales
     * @return true si les intervalles sont égaux
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Interval1D && (includedFrom() == ((Interval1D)obj).includedFrom() && includedTo() == ((Interval1D)obj).includedTo());
    }

    
  /**
   * retourne le hash des bornes de l'intervalle.
   * @return le hash des bornes de l intervalle.
   */
    @Override
    public int hashCode() {
      return Objects.hash(includedFrom(), includedTo());
    }
    /**
     * retourne la représentation d'un Interval1D
     * @return [borne inferieur..borne superieur]";
     */
    @Override
    public String toString() {
       
        return "[" + includedFrom() + ".." + includedTo() +"]";
    }

}
