package MyTest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.PanoramaParameters;

public class ParamettersTest {

    @Test
    public void gettersGetRightThingsYo() throws Exception{
        GeoPoint zero =new GeoPoint(0, 0);
        PanoramaParameters param = new PanoramaParameters(zero, 0, 1, 2, 1000, 100, 150);
        
        assertEquals(zero, param.observerPosition());
        assertEquals(0, param.observerElevation(),0);
        assertEquals(0, param.centerAzimuth(),1);
        assertEquals(0, param.height(),150);
        assertEquals(0, param.width(),100);
        assertEquals(0, param.horizontalFieldOfView(),2);
        assertEquals(0, param.maxDistance(),1000);
        
    }
    @Test
    public void xForAltitudeIsSymetric(){
        GeoPoint zero =new GeoPoint(0, 0);
        PanoramaParameters param = new PanoramaParameters(zero, 0, 1, 2, 1000, 10000, 15000);
        assertEquals(1.0, param.altitudeForY(param.yForAltitude(1)),1e-10);
        assertEquals(1.0, param.yForAltitude(param.altitudeForY(1)),1e-10);
        assertEquals(1.0, param.azimuthForX(param.xForAzimuth(1)),1e-10);
        assertEquals(1.0, param.xForAzimuth(param.azimuthForX(1)),1e-10);

    }
    @Test
    public void testWithYOnConcretreExample(){
        GeoPoint zero =new GeoPoint(0, 0);
        PanoramaParameters param = new PanoramaParameters(zero, 2000, Math.toRadians(180), Math.toRadians(80), 100*1000, 9, 5);
        assertEquals(Math.toRadians(20), param.altitudeForY(0),1e-10);
        assertEquals(Math.toRadians(-20), param.altitudeForY(4),1e-10);
        assertEquals(Math.toRadians(0), param.altitudeForY(2),1e-10);
        assertEquals(0, param.yForAltitude(Math.toRadians(20)),1e-10);
        assertEquals(4, param.yForAltitude(Math.toRadians(-20)),1e-10);
        assertEquals(2, param.yForAltitude(Math.toRadians(0)),1e-10);
      

    }
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
