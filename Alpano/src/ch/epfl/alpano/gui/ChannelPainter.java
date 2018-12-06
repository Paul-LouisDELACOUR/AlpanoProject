package ch.epfl.alpano.gui;

import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.Panorama;

/**
 * interface fonctionnelle représentant un peintre de canal
 * @author Luc MICHELS
 * @author Paul-Louis DELACOUR
 */
@FunctionalInterface
public interface ChannelPainter {
    
    /**
     * retourne la valeur du canal en un point
     * @param x : abcisse du point
     * @param y : ordonnée du point
     * @return la valeur du canal en ce point
     */
    public abstract float valueAt(int x, int y);
    
    /**
     * retourne un peintre de canal(ChannelPainter) étant donné un panorama. 
     * La valeur pour un point est la différence de distance entre le plus lointain des voisins 
     * et le point en question. 
     * Pour les points situés dans les bords, dont certains voisins sont hors du panorama, 
     * on considère que la distance à l'observateur de ces voisins est nulle.
     * @param panorama : Panorama considéré.
     * @return Un ChannelPainter dont la valeur pour un point est la différence de distance
     *  entre le plus lointain des voisins et le point en question.
     *  Pour les points situés dans les bords, dont certains voisins sont hors du panorama, 
     * on considère que la distance à l'observateur de ces voisins est nulle
     */
    public static ChannelPainter maxDistanceToNeighbors(Panorama panorama) {
        return (x,y) -> Math.max(
                        Math.max(panorama.distanceAt(x - 1, y, 0f),
                                panorama.distanceAt(x + 1, y, 0f)),
                        Math.max(panorama.distanceAt(x, y - 1, 0f),
                                panorama.distanceAt(x, y + 1, 0f)))
                        - panorama.distanceAt(x, y, 0f);
    }
    
    /**
     * retourne le ChannelPainter dont la valeur d'un point est augmentée d'une constante
     * @param constante : float, valeur ajoutée aux valeurs produites par le peintre.  
     * @return le ChannelPainter dont la valeur d'un point est augmentée d'une constante
     */
    public default ChannelPainter add(float constante){
        return (x,y) -> valueAt(x,y)+constante ;
    }
    
    /**
     * retourne le channelPainter dont la valeur d'un point est diminuée d'une constante
     * @param constante : float, valeur soustraite aux valeurs produites par le peintre
     * @return le ChannelPainter dont le valeur d'un point est diminuée d'une constante
     */
    public default ChannelPainter sub(float constante){
        return (x,y) -> valueAt(x,y)-constante;
    }
    
    /**
     * retourne le ChannelPainter dont la valeur d'un point est multipliée par une constante
     * @param constante : float, valeur soustraite aux valeurs produites par le peintre.
     * @return le ChannelPainter dont la valeur d'un point est multipliée par une constante
     */
    public default ChannelPainter mul(float constante){
        return (x,y) -> valueAt(x,y)*constante;
    }
    
    /**
     * Retourne le ChannelPainter dont la valeur d'un point est divisée par une constante
     * @param constante : float, valeur divisant chaque valeurs produites par le peintre
     * @return le ChannelPainter dont la valeur d'un point est divisée par une constante
     */
    public default ChannelPainter div(float constante){
        return (x,y) -> valueAt(x,y)/constante;
    }
    
    /**
     * Retourne le ChannelPainter dont la valeur d'un point est modifiée par la fonction en paramètre
     * @param op : DoubleUnaryOperator, fonction appliqué à chaque valeurs produites par le peintre 
     * @return le ChannelPainter dont la valeur d'un point est modifiée par op (DoubleUnaryOperator)
     */
    public default ChannelPainter map(DoubleUnaryOperator op){
        return (x,y) -> (float) op.applyAsDouble(valueAt(x,y));
    }
    
    /**
     * Retourne le ChannelPainter dont la valeur en un point est le maximum entre ( 0 et le (minimum entre  1 et lui même) ).
     * @return le ChannelPainter dont la valeur en un point est le maximum entre ( 0 et le (minimum entre  1 et lui même) ).
     */
    public default ChannelPainter clamped(){
        return (x,y) -> Math.max(0f, Math.min(valueAt(x,y), 1));
    }
    
    /**
     * Retourne le ChannelPainter dont la valeur d'un point n'est que sa partie décimale
     * @return le ChannelPainter dont la valeur d'un point n'est que sa partie décimale
     */
    public default ChannelPainter cycling(){
        return (x,y) -> valueAt(x,y)%1;
    }
    
    /**
     * Retourne le channelPainter dont la valeur d'un point est 1 dont on lui soustrait lui-même
     * @return  le channelPainter dont la valeur d'un point est 1 dont on lui soustrait lui-même
     */
    public default ChannelPainter inverted(){
        return (x,y) -> 1-valueAt(x,y);
    }
}
