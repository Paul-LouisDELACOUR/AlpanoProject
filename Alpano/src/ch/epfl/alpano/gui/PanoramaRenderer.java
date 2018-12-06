package ch.epfl.alpano.gui;
import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaParameters;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

/**
 * interface permettant d'obtenir l'image d'un panorama étant donné ce panorama 
 * et un peintre d'image.
 * @author Luc MICHELS
 * @author Paul-Louis DELACOUR
 */
public interface PanoramaRenderer {
    
    /**
     * l'image construite avec le panorama et l'imagePainter
     * @param panorama
     * @param imagePainter
     * @return l'image construite avec le panorama et l'imagePainter
     */
    public static Image renderPanorama(Panorama panorama, ImagePainter imagePainter){
        PanoramaParameters panoramaParameters = panorama.parameters();
        int width = panoramaParameters.width();
        int height = panoramaParameters.height();
        WritableImage image = new WritableImage(width, height);
        PixelWriter writer = image.getPixelWriter();
        for(int x = 0; x<width;++x){
            for( int y = 0; y< height; ++y){
                writer.setColor(x,y,imagePainter.colorAt(x, y));
            }
        }
        return image;
    }
}
