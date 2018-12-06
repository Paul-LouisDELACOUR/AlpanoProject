package MyTest;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ch.epfl.alpano.summit.GazetteerParser;
import ch.epfl.alpano.summit.Summit;

public class Main {

    public static void main(String[] args) throws IOException {
        
        File file = new File("alps.txt");
        List<Summit> list = GazetteerParser.readSummitsFrom(file);
        System.out.println(list);

    }

}
