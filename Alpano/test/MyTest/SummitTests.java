package MyTest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.summit.Summit;

public class SummitTests {

    @Test(expected = NullPointerException.class)
    public void throwNullPointerExceptionNameNull() throws Exception{
        Summit summitWithNull1 = new Summit(null, new GeoPoint(0, 0), 0);
  
        
    }
    @Test(expected = NullPointerException.class)
    public void throwNullPointerExceptionNameAndGeopointNull() throws Exception{
       
        
        Summit summitWithNull3 = new Summit(null, null, 0);
        
    }
    @Test(expected = NullPointerException.class)
    public void throwNullPointerExceptionGeopointNull() throws Exception{
       
        Summit summitWithNull2 = new Summit("shintz", null, 0);
       
        
    }
    @Test
    public void gettersGetRightThingsYo() throws Exception{
        GeoPoint zero =new GeoPoint(0, 0);
        Summit summit = new Summit("shintz", zero, 0);
       
        assertEquals("shintz", summit.name());
        assertEquals(zero, summit.position());
        assertEquals(0, summit.elevation(),0);
//        System.out.println(summit);
    }
    
    

}
