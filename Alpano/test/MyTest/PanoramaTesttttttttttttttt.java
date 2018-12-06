package MyTest;

import static java.lang.Math.toRadians;

import java.io.File;
import java.util.Random;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Math2;
import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.Panorama.Builder;
import ch.epfl.alpano.PanoramaParameters; 

public class PanoramaTesttttttttttttttt {

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

    @Test
    public void setValidLongitudeTest() throws IllegalArgumentException, Exception{
        Builder p = new Panorama.Builder(PARAMS);
        
        Random random = new Random();
        
        float longitude=0;
        float latitude=0;
       
        
        try{
            p.setLongitudeAt(0, 0, (float)Math.PI);
        } catch (Exception e){
            
//            System.out.println(e);
        }
        for (int i=0;i<Integer.MAX_VALUE; ++i){
          try{
           longitude = (float)(random.nextDouble()*(Math2.PI2)-Math.PI);
            latitude = (float)( random.nextDouble()*(Math.PI)-Math.PI/2);
            p.setLongitudeAt(0, 0, longitude);
            p.setLatitudeAt(0, 0, latitude);
        
          } catch (Exception e){
//              System.out.println(e);
//              System.out.println(Math.toDegrees(longitude));
//              System.out.println(Math.toDegrees(latitude));
          }
        }

    }
}    
