package MyTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.ElevationProfile;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;

public class HgtDiscreteElevationModelTest {
   //private final static HgtDiscreteElevationModel test = new HgtDiscreteElevationModel(new File("N45E006"));
    
    @Test(expected = IllegalArgumentException.class)
    public void throwIllegalArgumentException() throws Exception{
        File file = new File("hgt");
        HgtDiscreteElevationModel test =new HgtDiscreteElevationModel(file);
        test.close();
    }
    @Test(expected = IllegalArgumentException.class)
    public void throwIllegalArgumentException2() throws Exception{
        File file = new File("Z45E006.hgt");
        HgtDiscreteElevationModel test =new HgtDiscreteElevationModel(file);
        test.close();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void throwIllegalArgumentException3() throws Exception{
        File file = new File("N45Z006.hgt");
        HgtDiscreteElevationModel test =new HgtDiscreteElevationModel(file);
        test.close();
    }
    @Test(expected = IllegalArgumentException.class)
    public void throwIllegalArgumentException4() throws Exception{
        File file = new File("NAZE006.hgt");
        HgtDiscreteElevationModel test =new HgtDiscreteElevationModel(file);
        test.close();
    }
    @Test(expected = IllegalArgumentException.class)
    public void throwIllegalArgumentException5() throws Exception{
        File file = new File("N45EAED.hgt");
        HgtDiscreteElevationModel test =new HgtDiscreteElevationModel(file);
        test.close();
    }
    @Test(expected = IllegalArgumentException.class)
    public void throwIllegalArgumentException6() throws Exception{
        File file = new File("N45E006.png");
        HgtDiscreteElevationModel test =new HgtDiscreteElevationModel(file);
        test.close();
    }
    @Test(expected = IllegalArgumentException.class)
    public void throwIllegalArgumentException7() throws Exception{
        File file = new File("N45E0AE.hgt");
        HgtDiscreteElevationModel test =new HgtDiscreteElevationModel(file);
        test.close();
    }
    @Test(expected = IllegalArgumentException.class)
    public void throwIllegalArgumentException8() throws Exception{
        File file = new File("N45E0A6.hgt");
        HgtDiscreteElevationModel test =new HgtDiscreteElevationModel(file);
        test.close();
    }
    @Test(expected = IllegalArgumentException.class)
    public void throwIllegalArgumentException9() throws Exception{
        File file = new File("N45EA06.hgt");
        HgtDiscreteElevationModel test =new HgtDiscreteElevationModel(file);
        test.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void elevationProfileErrorTest1() throws Exception{
        
        
        ElevationProfile test = new ElevationProfile(new ContinuousElevationModel(new HgtDiscreteElevationModel(new File("N45E006.hgt"))), new GeoPoint(0, 0), -300, 100);
        
        
    }

    @Test(expected = IllegalArgumentException.class)
    public void elevationProfileErrorTest2() throws Exception{
        
        
        ElevationProfile test2 = new ElevationProfile(new ContinuousElevationModel(new HgtDiscreteElevationModel(new File("N45E006.hgt"))), new GeoPoint(0, 0), 0, -100);
        
    }
    @Test(expected = NullPointerException.class)
    public void elevationProfileErrorTest3() throws Exception{
        
        
        ElevationProfile test = new ElevationProfile(null, new GeoPoint(0, 0), 0, 100);
        
        
    }
    @Test(expected = NullPointerException.class)
    public void elevationProfileErrorTest4() throws Exception{
        
        
        ElevationProfile test = new ElevationProfile(new ContinuousElevationModel(new HgtDiscreteElevationModel(new File("N45E006.hgt"))), null, 0, 100);
        
        
    }
    @Test(expected = NullPointerException.class)
    public void elevationProfileErrorTest5() throws Exception{
        
        ElevationProfile test = new ElevationProfile(null, null, 0, 100);

        
        
    }
//    @Test 
//    public void validNameLengthTest(){
//        assertTrue(test.validNameLength("0123456789AB"));
//    }
    
    @Test 
    public void extentTest() throws Exception{
        Interval1D latitude = new Interval1D(45*3600, 46*3600);
        Interval1D longitude = new Interval1D(6*3600, 7*3600);
        Interval2D inter = new Interval2D(longitude, latitude);
        HgtDiscreteElevationModel test = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
        
        assertTrue(inter.equals(test.extent()));
        test.close();
    }

    @Test 
    public void elevationSampleTest(){
        int expected = 465; // Coorepond à la 1ere altitude du fichier N45E006
        HgtDiscreteElevationModel test = new HgtDiscreteElevationModel(new File("N45E006.hgt"));
        int calculated = (int)test.elevationSample(6*3600,46*3600);
        System.out.println(calculated);
        assertEquals(expected, calculated , 0);
    }
    
}
