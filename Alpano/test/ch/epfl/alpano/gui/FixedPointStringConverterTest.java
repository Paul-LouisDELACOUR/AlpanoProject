package ch.epfl.alpano.gui;

import static org.junit.Assert.*;

import org.junit.Test;

public class FixedPointStringConverterTest {

    @Test
    public void basicTest(){
        FixedPointStringConverter c =
                new FixedPointStringConverter(1);

        
              System.out.println(c.fromString("12"));       // 120
              System.out.println(c.fromString("12.3"));     // 123
              System.out.println(c.fromString("12.34"));    // 123
              System.out.println(c.fromString("12.35"));    // 124
              System.out.println(c.fromString("12.36789")); // 124
              System.out.println(c.toString(678)); //"67.8"
             assertEquals(c.fromString("12").intValue(), 120,0);
             assertEquals(c.fromString("12.3").intValue(),123);
             assertEquals(c.fromString("12.34").intValue(),123);
             assertEquals(c.fromString("12.35").intValue(),124);
             assertEquals(c.fromString("12.36789").intValue(), 124);
             assertEquals(c.toString(678), "67.8");
    }

}
