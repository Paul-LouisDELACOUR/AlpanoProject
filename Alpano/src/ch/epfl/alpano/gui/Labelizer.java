package ch.epfl.alpano.gui;

import static ch.epfl.alpano.Math2.angularDistance;
import static ch.epfl.alpano.Math2.firstIntervalContainingRoot;
import static java.lang.Math.abs;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

import ch.epfl.alpano.Azimuth;
import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaComputer;
import ch.epfl.alpano.PanoramaParameters;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.summit.Summit;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
/**
 * classe permettant de déterminer les étiquettes qui vont être dessinées sur le panorama.
 * @author Luc MICHELS
 * @author Paul-Louis DELACOUR
 * 
 */
public final class Labelizer {
    
    private final ContinuousElevationModel cem;
    private final List<Summit> summitList;
    
    /**
     * construit un étiqueteur de panorama
     * @param cem : un ContinuousElevationModel qui est la surface où le panorama est dessiné 
     * @param summitList :  les sommets présents sur la surface
     * @throws NullPointerException : si le cem ou summitList est null
     */
    public Labelizer(ContinuousElevationModel cem, List<Summit> summitList) throws NullPointerException{
        this.cem=requireNonNull(cem);
        this.summitList=requireNonNull(summitList);
    }
    
    /**
     * retourne les étiquettes du panorama qui ont été choisies pour en avoir le plus possible visible sur le panorama
     * @param param les paramètres du panorama dont on veut déterminet l'étiquetage
     * @return les étiquetes du panorama à dessiner 
     */
    public List<Node> labels(PanoramaParameters param){
        List<Sommet> visibleAndValidSommetList = visibleSommetsAndValid(param);
    
        List<Node> nodeList = new LinkedList<>();
        if (visibleAndValidSommetList.isEmpty())
            return nodeList;
        visibleAndValidSommetList.sort(Sommet::compareTo);
        
        int width = param.width();
         
        BitSet bitSet = new BitSet(width);
        
        // On get le dernier sommet qui est celui qui a le plus grand indexY possible parmis les visibles
        int beginLine = visibleAndValidSommetList.get(0).indexY-20;
        
        for (Sommet s : visibleAndValidSommetList){
            int indexX = s.indexX;
            int indexY = s.indexY;
            int nextSetBit = bitSet.nextSetBit(indexX); // On regarde le prochain bit qui est à 1 après la position courante
                                                        // Ce bit donne la position du prochain sommet
            
            // Conditions pour que le trait et son étiquette soient déssinés
            // Les 20 bits à droite de nextSetBit doivent être disponibles, ou alors aucun bit n'est à 1
            if (nextSetBit-indexX>=20 || nextSetBit ==-1){  //aucun autre sommet déjà étiqueté ne se trouve à moins de 20 pixels à sa droite.
 
               
               bitSet.set(indexX, indexX+20); // Set les 19 bits à la droite du sommet et lui-même correspondant à True; 
                                                  // indexX est inclusif puis indexX+20  exclusif
               Line line = new Line(indexX, beginLine ,indexX, indexY );
    
               Summit summit = s.getSummit();
               String name = new StringBuilder(summit.name()).append(" (").append(summit.elevation()).append(" m)").toString();
               Text textName = new Text(name); // Le text commence 2 pixels plus haut que la ligne 
               
               //Ajout de transformations au texte pour le décaller vers la gauche puis effectuer une rotation 
               textName.getTransforms().addAll(new Translate(indexX, beginLine-2),new Rotate(300, 0, 0));
               
               nodeList.add(textName);
               nodeList.add(line);
            }
          
        }
        return nodeList;
    }
    
    /**
     * retourne la liste des sommets visibles et valides sur la panorama.
     * Les sommets sont valides s'ils sont suffisament éloignés des bords.
     * @param param
     * @return la liste des sommets visibles et valides
     */
    private List<Sommet> visibleSommetsAndValid(PanoramaParameters param){
        // il est inutile de mettre un requireNonNull pour param car l'utilisation de param.height() ou param.width() renvoie une exception si param est null
        
        List<Sommet> visibleAndValidSommets = new ArrayList<>();
        
        
      //paramètres de l'observateur
        GeoPoint observerPosition = param.observerPosition();
        int observerElevation = param.observerElevation();
        int height = param.height();
        int width = param.width();
        
        int maxDistance=param.maxDistance();
        
        for(Summit s : summitList){
            GeoPoint positionDuSommet = s.position();
            double SommetDistanceToObservateur = positionDuSommet.distanceTo(observerPosition); // Distance horizontale jusqu'au sommet
            
            if ( SommetDistanceToObservateur <= maxDistance){
            
            double azimuthObserverAndSummit = Azimuth.canonicalize(observerPosition.azimuthTo(positionDuSommet));
            
            ElevationProfile elevationProfile = new ElevationProfile(cem, observerPosition, azimuthObserverAndSummit, maxDistance);
            
            // rayFlat : rayon lancé horizontalement à une hauteur initiale de elevationProfile
            DoubleUnaryOperator rayFlat = PanoramaComputer.rayToGroundDistance(elevationProfile, observerElevation, 0);
            
            // DistanceAngulaireSommetObservateur : angle de l'horizontal juqu'au sommet
            double distanceAngulaireSommetObservateur = Math.atan2(-(rayFlat.applyAsDouble(SommetDistanceToObservateur)), SommetDistanceToObservateur);
            
            //rayToGrounfDistance : rayon lancé avec la pente en direction du Sommet (distanceAnggulaireSommetObservateur) et avec une hauteur initiale de observerElevation
            DoubleUnaryOperator rayToGroundDistance = PanoramaComputer.rayToGroundDistance(elevationProfile, observerElevation, distanceAngulaireSommetObservateur);
          
                
            if(  abs(angularDistance(param.centerAzimuth(), azimuthObserverAndSummit)) <= param.horizontalFieldOfView()/2.0 
                    &&  abs(distanceAngulaireSommetObservateur) <= param.verticalFieldOfView()/2.0 
                    &&  firstIntervalContainingRoot(rayToGroundDistance, 0, SommetDistanceToObservateur, PanoramaComputer.RESEARCHINTERVAL) >= (SommetDistanceToObservateur-200) ){
                // On cherche la première intersection de rayToGroundDistance avec le Sommet    
                
                    int indexX=(int) Math.round(param.xForAzimuth(azimuthObserverAndSummit));
                    int indexY=(int) Math.round((param.yForAltitude(distanceAngulaireSommetObservateur)));
                    
                    // permet de vérifier que les sommets ne soient pas trop proches des bords
                    if ( (indexY>170 && indexY<height-20 
                            && indexX>=20  && indexX<width-20)){
                        Sommet sommet = new Sommet(s,indexX, indexY );    
                        visibleAndValidSommets.add(sommet);
                    }
                }

            
            }
        }
        return visibleAndValidSommets;
    }
    
    /**
     * Représente un sommet dans son panorama avec sa position
     * @author Luc MICHELS
     * @author Paul-Louis DELACOUR
     */
     public final class Sommet implements Comparable<Sommet>{
        private final Summit s;
        private final int indexX;
        private final int indexY; // L'index Y va de haut en bas
        /**
         * un sommet dans son panorama avec sa position
         * @param s
         * @param indexX
         * @param indexY
         */
        private Sommet(Summit s, int indexX, int indexY) {
            this.s=s;
            this.indexX=indexX;
            this.indexY=indexY;
        }
        
        /**
         * retourne l'indexX correspondant au Sommet dans son panorama
         * @return l'indexX correspondant au Sommet dans son panorama
         */
        public int getIndexX() {
            return indexX;
        }
        
        /**
         * retourne l'indeY correspondant au Sommet dans son panorama 
         * @return l'indexY correspondant au Sommet dans son panorama
         */
        public int getIndexY() {
            return indexY;
        }
        
        /**
         * retourne le Summit correspondant au Sommet
         * @return le Summit correspondant au Sommet
         */
        public Summit getSummit() {
            return s;
        }
        
        /**
         * retourne la représentation textuelle du sommet : (indexX, indexY)
         */
        public String toString(){
            StringBuilder b = new StringBuilder(s.name());
            b.append(" (").append(indexX).append(", ").append(indexY).append(")");
            return b.toString();
        }
        
        
        @Override
        public int compareTo(Sommet o) {
            // Les Sommets sont d'abord comparés par leur position dans le panorama
            // S'ils ont la même position, alors on compare leurs altitudes
         int elevation = Integer.compare(this.indexY, o.indexY);
         return elevation!=0 
                 ? elevation 
                 : Integer.compare(o.getSummit().elevation(), this.getSummit().elevation());
        }
     }
}
