package MyTest;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;

public class PanoramaTest {

    @Test
    public void horizontalTest(){
        PanoramaParameters p = new PanoramaParameters(new GeoPoint(0,0), 2000, Math.toRadians(180),Math.toRadians(80), 100*1000, 9,5);
        double i1 =p.xForAzimuth(Math.toRadians(140)); // (0)
        double i2 = p.xForAzimuth(Math.toRadians(180)); // 4
        double i3 = p.xForAzimuth(Math.toRadians(220)); // 8
        assertEquals(i1,0.0,1e-10);
        assertEquals(i2,4,1e-10);
        assertEquals(i3,8,1e-10);
        
        double a1 = p.azimuthForX(0);
        double a2 = p.azimuthForX(4);
        double a3 = p.azimuthForX(8);
        
       assertEquals(a1,Math.toRadians(140),1e-10);
       assertEquals(a2,Math.toRadians(180),1e-10);
       assertEquals(a3,Math.toRadians(220),1e-10);
    }

}
