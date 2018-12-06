package ch.epfl.alpano;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.toRadians;

import java.io.File;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.gui.ChannelPainter;
import ch.epfl.alpano.gui.ImagePainter;
import ch.epfl.alpano.gui.PanoramaRenderer;
import javafx.scene.image.Image;
final class DrawPanoramaColor {
    final static File HGT_FILE = new File("N46E007.hgt");

    final static int IMAGE_WIDTH = 500;
    final static int IMAGE_HEIGHT = 200;

    final static double ORIGIN_LON = toRadians(7.65);
    final static double ORIGIN_LAT = toRadians(46.73);
    final static int ELEVATION = 600;
    final static double CENTER_AZIMUTH = toRadians(180);
    final static double HORIZONTAL_FOV = toRadians(60);
    final static int MAX_DISTANCE = 100_000;

    final static PanoramaParameters PARAMS =
      new PanoramaParameters(new GeoPoint(ORIGIN_LON,
                                          ORIGIN_LAT),
                             ELEVATION,
                             CENTER_AZIMUTH,
                             HORIZONTAL_FOV,
                             MAX_DISTANCE,
                             IMAGE_WIDTH,
                             IMAGE_HEIGHT);

    public static void draw() throws Exception {
      try (DiscreteElevationModel dDEM =
           new HgtDiscreteElevationModel(HGT_FILE)) {
        ContinuousElevationModel cDEM =
          new ContinuousElevationModel(dDEM);
        Panorama p = new PanoramaComputer(cDEM)
          .computePanorama(PARAMS);
        ChannelPainter distance = p::distanceAt;
        ChannelPainter pente = (x,y) -> p.slopeAt(x,y);
              ChannelPainter teinte = distance.div(100000).cycling().mul(360);
              ChannelPainter saturation = distance.div(200000).clamped().inverted();
              ChannelPainter luminosite = pente.mul(2).div((float)Math.PI).inverted().mul(0.7f).add(0.3f);
              ChannelPainter opacite = distance.map(x -> x == Float.POSITIVE_INFINITY ? 0 : 1);
                
             
              ImagePainter l = ImagePainter.hsb(teinte, saturation, luminosite, opacite);

              Image i = PanoramaRenderer.renderPanorama(p, l);
              ImageIO.write(SwingFXUtils.fromFXImage(i, null),
                            "png",
                            new File("niesen-shaded.png"));
        
      }
    }
    
    
  }
