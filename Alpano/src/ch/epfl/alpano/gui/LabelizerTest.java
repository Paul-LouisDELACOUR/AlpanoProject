package ch.epfl.alpano.gui;

import static ch.epfl.alpano.summit.GazetteerParser.readSummitsFrom;
import static java.nio.charset.StandardCharsets.US_ASCII;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import ch.epfl.alpano.dem.ContinuousElevationModel;
import ch.epfl.alpano.dem.DiscreteElevationModel;
import ch.epfl.alpano.dem.HgtDiscreteElevationModel;
import ch.epfl.alpano.gui.Labelizer.Sommet;
import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;

public class LabelizerTest implements PredefinedPanoramas{
    
    
    
            @Test
    public void test() throws Exception {
          PanoramaUserParameters NIESEN = PredefinedPanoramas.NIESEN;
          File niesenFile = new File ("N46E007.hgt");
          DiscreteElevationModel dDEM = new HgtDiscreteElevationModel(niesenFile);
          ContinuousElevationModel cDEM =new ContinuousElevationModel(dDEM);
               
          List<Summit> readSummits = new ArrayList<>(GazetteerParser.readSummitsFrom(new File("alps.txt")));        
         Labelizer labelizer = new Labelizer (cDEM, readSummits);
//         List<Sommet> visibleSommet = labelizer.visibleSommetsAndValid(NIESEN.getPanoramaParameters());
//         visibleSommet.sort(Sommet::compareTo);
//         for (Sommet s : visibleSommet){
//            System.out.println(s);
//         }

            
     }

}
