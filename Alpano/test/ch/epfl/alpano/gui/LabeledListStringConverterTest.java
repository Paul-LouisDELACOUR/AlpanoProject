package ch.epfl.alpano.gui;

import static org.junit.Assert.*;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
public class LabeledListStringConverterTest {

    @Test
    public void basicCreationLabeledList(){
        LabeledListStringConverter c =
                new LabeledListStringConverter("zéro", "un", "deux");
        int fromString = c.fromString("deux");
        String toString = c.toString(0);
        System.out.println(c.fromString("deux"));
       System.out.println(c.toString(0));
        assertTrue(fromString==2);     
        assertTrue(toString.equals("zéro") );
              
    }
    
    

}
