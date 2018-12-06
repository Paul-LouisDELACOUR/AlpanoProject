package ch.epfl.alpano;

import static java.util.Objects.requireNonNull;

/**
 * 
 * @author Paul-Louis DELACOUR (269625)
 * @author Luc MICHELS (273666)
 * représente les paramètres nécessaires au dessin d'un panorama.
 */
public final class PanoramaParameters {

    private final GeoPoint observerPosition;
    private final int observerElevation;
    private final double centerAzimuth;
    private final double horizontalFieldOfView;
    private final int maxDistance;
    private final int width;
    private final int height;
    private final double verticalFieldOfView;
    
    private final double delta; 
   
    
    /**
     * 
     * @param observerPosition : Le point où se situe l'observateur.
     * @param observerElevation : altitude de l'observateur en m.
     * @param centerAzimuth : azimuth du centre du panorama en radians
     * @param horizontalFieldOfView : angle de vu horizontale en radians 
     * @param maxDistance : distance maximale de visibilité en m
     * @param width : largeur du panorama en pixels
     * @param height : hauteur du panorama en pixels
     * construit un objet contenant les paramètres passés en argument et qui correspondent à ceux 
     * énumérés à la section
     * 
     * @throws NullPointerException : si la position de l'observateur est nulle
     * @throws IllegalArgumentException : si l'azimut central n'est pas canonique, 
     * si l'angle de vue horizontal n'est pas compris entre 0 (exclu) et 2π(inclu) 
     * ou si la largeur, hauteur et distance maximales ne sont pas strictement positives
     */
    public PanoramaParameters(GeoPoint observerPosition, int observerElevation, double centerAzimuth, 
            double horizontalFieldOfView, int maxDistance, int width, int height){
        Preconditions.checkArgument(Azimuth.isCanonical(centerAzimuth),"L'azimuth doit être canonique");
        Preconditions.checkArgument(horizontalFieldOfView>0 && horizontalFieldOfView<=Math2.PI2, "l'angle de vue horizontal n'est pas compris entre 0 (exclu) et 2π(inclu)");
        Preconditions.checkArgument(width>0 && height>0 && maxDistance>0, "Hauteur, largeur et distanceMax doivent êtres strictement positives");
        
        this.observerPosition=requireNonNull(observerPosition);
        this.observerElevation=observerElevation;
        this.centerAzimuth = centerAzimuth;
        this.horizontalFieldOfView=horizontalFieldOfView;
        this.maxDistance=maxDistance;
        this.width=width;
        this.height=height;
        
        this.delta = horizontalFieldOfView/(width-1.0); ;
        this.verticalFieldOfView = horizontalFieldOfView * (height-1.0)/(width-1.0);
    }
    
    /**
     * retourne la position de l'observateur
     * @return la position de l'observateur
     */
    public GeoPoint observerPosition(){
        return observerPosition;
    }
    
    /**
     * retourne l'altitude de l'observateur en m.
     * @return l'altitude de l'observateur en m.
     */
    public int observerElevation(){
        return observerElevation;
    }
    
    /**
     * retourne l'azimuth du centre du panorama(en radians)
     * @return l'azimuth du centre du panorama(en radians)
     */
    public double centerAzimuth() {
        return centerAzimuth;
    }
    
    /**
     * retourne l'ange de vu horizontale (en radians)
     * @return l'ange de vu horizontale (en radians)
     */
    public double horizontalFieldOfView() {
        return horizontalFieldOfView;
    }
    
    /**
     * retourne la distance maximale de visibilité (en pixel)
     * @return la distance maximale de visibilité (en pixel)
     */
    public int maxDistance() {
        return maxDistance;
    }
    
    /**
     * retourne la largeur du panorama (en pixel)
     * @return la largeur du panorama (en pixel)
     */
    public int width() {
        return width;
    }
    
    /**
     * retourne la hauteur du panorama (en pixel)
     * @return la hauteur du panorama (en pixel)
     */
    public int height() {
        return height;
    }
    
    /**
     * retourne le nombre de radians dans un pixel (en radians/pixel)
     * @return le nombre de radians dans un pixel (en radians/pixel)
     */
    public double delta(){
        return delta;
    }
    
    /**
     * retourne l'angle de vue verticale (en radians)
     * @return l'angle de vue vertical (en radians)
     */
    public double verticalFieldOfView(){
       //L'angle correspondant à l'espace entre deux pixels est 
        //identique horizontalement et verticalement, et est nommé δ
        // δ=Vh/(w−1) = Vv/(h-1)   => Vv = (h-1)/(w-1)*Vh;
        return verticalFieldOfView;
    }
   
    /**
     * retourne l'azimut correspondant à l'index de pixel horizontal x, ou lève l'exception 
     * IllegalArgumentException si celui-ci est inférieur à zéro, ou supérieur à la largeur-1
     * @param x
     * @return l'azimut correspondant à l'index de pixel horizontal x
     * IllegalArgumentException si celui-ci est inférieur à zéro, ou supérieur à la largeur-1
     * @throws IllegalArgumentException : si l'index de pixel est inférieur à zéro, ou supérieur à la largeur-1
     */
    public double azimuthForX(double x){
        Preconditions.checkArgument(x>=0 && x<=width-1 , "l'index du pixel horizontal est hors de l'interval");
        double leftAzimuth = centerAzimuth - ((width-1)/2.0)*delta;
        return Azimuth.canonicalize(leftAzimuth + x*delta);
    }
    
    /**
     * retourne l'index de pixel horizontal correspondant à l'azimut donné
     * @param a : l'angle où l'observateur regarde (en radians).
     * @return l'index de pixel horizontal correspondant à l'azimut donné (en pixel)
     * @throws IllegalArgumentException si cet azimut n'appartient pas à la zone visible.
     */
  public double xForAzimuth(double a){
      
      double borneInf = centerAzimuth-(horizontalFieldOfView/2);
      Preconditions.checkArgument(Math.abs(Math2.angularDistance(a, centerAzimuth))<=horizontalFieldOfView/2, "L'azimut n'appartient pas à la zone visible");
      return Azimuth.canonicalize(a-borneInf)/delta;
}
  
  
    /**
     * retourne l'élévation correspondant à 
     * l'index de pixel vertical y, ou lève l'exception IllegalArgumentException 
     * @param y : index de l'altitude (en pixels)
     * @return l'élévation correspondant à l'index de pixel vertical y
     * @throws  IllegalArgumentException si celui-ci est inférieur à zéro, ou supérieur à la hauteur-1
     */
    public double altitudeForY(double y){
        int MaxY = height-1;
        Preconditions.checkArgument(y>=0 && y<=MaxY, "l'index de pixel vertical y n'est pas dans les norme du paysage");

        double upperCornerElevation = (MaxY/2.0)*delta;
        return upperCornerElevation -y*delta;
    }
    
    /**
     * retourne l'index de pixel vertical correspondant à l'élévation donnée, ou lève 
     * l'exception IllegalArgumentException si celle-ci n'appartient pas à la zone visible.
     * @param a : l'angle d'élévation verticale (en randians)
     * @return  l'index de pixel vertical correspondant à l'élévation donnée, ou lève 
     * l'exception IllegalArgumentException si celle-ci n'appartient pas à la zone visible.
     */
    public double yForAltitude(double a){
        double borneSup = verticalFieldOfView/2.0;
        Preconditions.checkArgument(Math.abs(a)*2<=verticalFieldOfView, "L'elevation n'appartient pas à la zone visible");
        return Azimuth.canonicalize( borneSup - a)  / delta; 
    }
    
   
    
    /**
     * (visible  que dans son paquetage (Alpano) )
     * retourne vrai si et seulement si l'index passé est un index de pixel valide
     * @param x abcisse du point
     * @param y ordonne du point
     * @return vrai si et seulement si l'index passé est un index de pixel valide
     */
     boolean isValidSampleIndex(int x, int y){
        return (x>=0 && x<=width-1 && y>=0 && y<=height-1);
    }
    
    /**
     * visibles que dans son paquetage (Alpano)
     * retourne l'index linéaire du pixel d'index donné.
     * L'index linéaire est simplement un index à une dimension, qui vaut 0 pour 
     * le pixel d'index (0,0), 1 pour celui d'index (1,0), et ainsi de suite jusqu'au 
     * pixel en bas à droite de l'image, dont l'index est (w−1,h−1)
     * et l'index linéaire w⋅h−1w⋅h−1.
     * @param x abcisse du point
     * @param y ordonne du point
     * @return l'index linéaire du pixel d'index donné.
     * L'index linéaire est simplement un index à une dimension, qui vaut 0 pour 
     * le pixel d'index (0,0), 1 pour celui d'index (1,0), et ainsi de suite jusqu'au 
     * pixel en bas à droite de l'image, dont l'index est (w−1,h−1)
     * et l'index linéaire w⋅h−1
     */
     int linearSampleIndex(int x, int y){
         Preconditions.checkArgument(isValidSampleIndex(x, y));
         return y*width+x;
    }
   

}
