package ch.epfl.alpano.gui;

import ch.epfl.alpano.Panorama;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
/**
 * interface fonctionelle représentant un peintre d'image. Il calcule une couleur à un point donné.
 * @author Luc MICHELS
 * @author Paul-Louis DELACOUR
 * 
 */
@FunctionalInterface
public interface ImagePainter {
    
    /**
     * retourne la couleur en ce point
     * @param x : abcisse du point
     * @param y : ordonnée du point
     * @return la couleur en ce point
     */
    public abstract Color colorAt(int x, int y);
    
    /**
     * retourne l'imagePainter hsb associée aux channels en arguments
     * @param teinte
     * @param saturation
     * @param luminosite
     * @param opacite
     * @return l'imagePainter hsb associée aux channels en arguments
     */
    public static ImagePainter hsb(ChannelPainter teinte, ChannelPainter saturation, ChannelPainter luminosite, ChannelPainter opacite){
        return (x,y) -> Color.hsb(teinte.valueAt(x, y), saturation.valueAt(x, y), luminosite.valueAt(x, y), opacite.valueAt(x,y));
    }
    
    /**
     * retourne l'imagePainter gris associée aux channels en arguments
     * @param teinteDeGris
     * @param opacite
     * @return l'imagePainter gris associée aux channels en arguments
     */
    public static ImagePainter gray(ChannelPainter teinteDeGris, ChannelPainter opacite){
        return (x,y) -> Color.gray(teinteDeGris.valueAt(x, y), opacite.valueAt(x, y));
    }
    
   /**
    * retourne l'image correspondant au panorama avec le ChannelPainter par défault qui a été défini pour le projet
    * @see {@link ch.epfl.alpano.gui.ChannelPainter}
    * @param p : panorama dont on va dessiner l'image
    * @return l'image correspondant au panorama avec le ChannelPainter par défault
    */
    public static Image defaultImage(Panorama p){
        ChannelPainter distance = p::distanceAt;
        ChannelPainter pente = (x,y) -> p.slopeAt(x,y);
        ChannelPainter teinte = distance.div(100000).cycling().mul(360);
        ChannelPainter saturation = distance.div(200000).clamped().inverted();
        ChannelPainter luminosite = pente.mul(2).div((float)Math.PI).inverted().mul(0.7f).add(0.3f);
        ChannelPainter opacite = distance.map(x -> x == Float.POSITIVE_INFINITY ? 0 : 1);
                
        return PanoramaRenderer.renderPanorama(p, ImagePainter.hsb(teinte, saturation, luminosite, opacite));

    }
}
