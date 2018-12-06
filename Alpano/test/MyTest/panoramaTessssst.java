package MyTest;

import static ch.epfl.test.TestRandomizer.RANDOM_ITERATIONS;
import static ch.epfl.test.TestRandomizer.newRandom;
import static java.lang.Math.toRadians;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Random;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Panorama;
import ch.epfl.alpano.PanoramaParameters;

public class panoramaTessssst {
    final static File HGT_FILE = new File("N46E007.hgt");

    final static int IMAGE_WIDTH = 500;
    final static int IMAGE_HEIGHT = 200;

    final static double ORIGIN_LON = toRadians(7.65);
    final static double ORIGIN_LAT = toRadians(46.73);
    final static int ELEVATION = 600;
    final static double CENTER_AZIMUTH = toRadians(180);
    final static double HORIZONTAL_FOV = toRadians(60);
    final static int MAX_DISTANCE = 100_000;
    final static PanoramaParameters parameters = new PanoramaParameters(new GeoPoint(ORIGIN_LON,
            ORIGIN_LAT),
ELEVATION,
CENTER_AZIMUTH,
HORIZONTAL_FOV,
MAX_DISTANCE,
IMAGE_WIDTH,
IMAGE_HEIGHT);
    @Test
    public void allIndexValuesWork() {
        
        Panorama.Builder builder = new Panorama.Builder(parameters);
        Random rngX = newRandom();
        Random rngY = newRandom();
        for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
            int x = rngX.nextInt(IMAGE_WIDTH);
            int y = rngY.nextInt(IMAGE_HEIGHT);

            builder.setDistanceAt(x, y, 12);
            builder.setElevationAt(x, y, 12);
            builder.setLatitudeAt(x, y, 0);
            builder.setLongitudeAt(x, y, 0);
            builder.setSlopeAt(x, y, 12);
            
            
        }
        
    }
    @Test
    public void allInvalidIndexValuesWork(){
        Panorama.Builder builder = new Panorama.Builder(parameters);
        int compteur=0;
        
            
            Random rngX = newRandom();
            Random rngY = newRandom();
            for (int i = 0; i < RANDOM_ITERATIONS; ++i) {
                int x;
                
                    x  = rngX.nextInt(IMAGE_HEIGHT)+IMAGE_HEIGHT;

                  
                  
                for (int k = 0; k<RANDOM_ITERATIONS; ++k){
                    int y;
                    
                      y  = rngY.nextInt(IMAGE_HEIGHT)+IMAGE_HEIGHT;

                    
                   
                    try{
                        builder.setDistanceAt(x, y, 12);  
                    }  catch(IndexOutOfBoundsException e){
                        
                        ++compteur;
                    }
                    
                }
                          
                
            
       
        
        
    }
            assertEquals(RANDOM_ITERATIONS*RANDOM_ITERATIONS, compteur);
    }
    @Test(expected = IllegalStateException.class)
    public void cantSetSlopeIfBuiltMate(){
        Panorama.Builder builder = new Panorama.Builder(parameters);
        builder.build();
        builder.setSlopeAt(0, 0, 0);
    }
    @Test(expected = IllegalStateException.class)
    public void cantSetLongitudeIfBuiltMate(){
        Panorama.Builder builder = new Panorama.Builder(parameters);
        builder.build();
        builder.setLongitudeAt(0, 0, 0);
    }
    @Test(expected = IllegalStateException.class)
    public void cantSetLatitudeIfBuiltMate(){
        Panorama.Builder builder = new Panorama.Builder(parameters);
        builder.build();
        builder.setLatitudeAt(0, 0, 0);
    }
    @Test(expected = IllegalStateException.class)
    public void cantSetElevationIfBuiltMate(){
        Panorama.Builder builder = new Panorama.Builder(parameters);
        builder.build();
        builder.setElevationAt(0, 0, 12);
    }
    @Test(expected = IllegalStateException.class)
    public void cantSetDistanceIfBuiltMate(){
        Panorama.Builder builder = new Panorama.Builder(parameters);
        builder.build();
        builder.setDistanceAt(0, 0, 12);
    }
    @Test(expected = IllegalStateException.class)
    public void cantBuildTwiceMate(){
        Panorama.Builder builder = new Panorama.Builder(parameters);
        builder.build();
        builder.build();
    }
    
        
}
