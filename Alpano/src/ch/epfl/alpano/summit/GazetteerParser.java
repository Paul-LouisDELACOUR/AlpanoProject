package ch.epfl.alpano.summit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.alpano.GeoPoint;
import ch.epfl.alpano.dem.DiscreteElevationModel;


/**
 * @author Paul-Louis DELACOUR (269625)
 * @author Luc MICHELS (273666)
 *représente un lecteur de fichier décrivant des sommets
 */
public final class GazetteerParser {

   static private final int DEBLONGITUDE=0;
   static  private final int DEBLATITUDE=10;
   static private final int DEBALTITUDE=20;
   static private final int FINALTITUDE=26;
   static private final int DEBNOM = 36;
   static private final int EXPECTED_SIZE=3;
    
    
    /**
     * représente un lecteur de fichier décrivant des sommets
     */
    private GazetteerParser() {
    }
    
    
    /**
     * retourne un tableau dynamique non modifiable contenant les sommets lus depuis 
     * le fichier file, ou lève l'exception IOException en cas d'erreur d'entrée/sortie 
     * ou si une ligne du fichier n'obéit pas au format décrit plus haut.
     * @param file : le fichier à lire
     * @return un tableau dynamique non modifiable contenant les sommets lus depuis 
     * le fichier file
     * @throws IOException en cas d'erreur d'entrée/sortie ou si une ligne du fichier n'obéit pas au format décrit plus haut.
     */
    public static List<Summit> readSummitsFrom(File file) throws IOException{
        
        String line="";
        List<Summit> summitList = new ArrayList<Summit>();
        String nom;
        String[] longitudeTab;
        String[] latitudeTab;
        int altitude;
        try (  BufferedReader buffer= 
                new BufferedReader(
                        new InputStreamReader(
                              new FileInputStream(file),StandardCharsets.US_ASCII));
             ){
            
            while ((line= buffer.readLine())!=null ){
                double longitude;
                double latitude;
                
                    longitudeTab = line.substring(DEBLONGITUDE, DEBLATITUDE).trim().split(":");
                    latitudeTab = line.substring(DEBLATITUDE, DEBALTITUDE).trim().split(":");
                    altitude = toElevation(line);
                    nom = line.substring(DEBNOM).trim();
                    longitude = StringToRadian(longitudeTab);
                    latitude = StringToRadian(latitudeTab);
                
                summitList.add( new Summit(nom, new GeoPoint(longitude,latitude), altitude) );
            } 
            
        } catch (Exception e){
            throw new IOException();
        }
        
        return Collections.unmodifiableList(summitList);
    }
    
    
   
    /**
     * méthode prenant une chaîne de caractères (dans un tableau) contenant un angle 
     * exprimé en degrés, minutes et secondes et retournant l'angle correspondant, en radians.
     * @param tab contenant la représentation d'un angle exprimé en degré minutes et secondes.
     * @return l'angle correspondant, en radians de la chaîne de caractères (dans un tableau) 
     * représentant un angle en degrés, minutes et secondes
     * @throws IOException si la taille du tableau n'est pas celle attendue
     */
    private static double StringToRadian(String[] tab) throws IOException{
        if( tab.length!=EXPECTED_SIZE){ // Si le tableau n'est pas de la taille EXPECTED_SIZE alors le format n'est pas respecté
            throw new IOException();
        }    
        return Math.toRadians( (Integer.parseInt(tab[0])+Integer.parseInt(tab[1])/60d+
                Integer.parseInt(tab[2])/(double)DiscreteElevationModel.SAMPLES_PER_DEGREE));
    }
    
    
    /**
     * retourne le sommet correspondant, à la ligne donnée en argument
     * @param line
     * @return le sommet correspondant, à la ligne donnée en argument
     * @throws NumberFormatException si la line ne contient pas seulement des nombres
     */
    private static int toElevation(String line){
        return Integer.parseInt(line.substring(DEBALTITUDE, FINALTITUDE).trim());
    }
    
    

}
