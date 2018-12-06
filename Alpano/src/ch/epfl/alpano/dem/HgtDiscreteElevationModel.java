package ch.epfl.alpano.dem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;

import ch.epfl.alpano.Interval1D;
import ch.epfl.alpano.Interval2D;
import ch.epfl.alpano.Preconditions;
/**
 * 
 * @author Paul-Louis DELACOUR (269625)
 * @author Luc MICHELS (273666)
 * représente un MNT discret obtenu au format HGT
 */
public final class HgtDiscreteElevationModel implements DiscreteElevationModel {


    private final  Interval2D extent;
    private final static int CORRECTLENGTH = (SAMPLES_PER_DEGREE+1)*(SAMPLES_PER_DEGREE+1)*2;
    private ShortBuffer b;
    
    /**
     * construit un MNT discret dont les échantillons proviennent du 
     * fichier HGT passé en argument
     * @param file
     * @throws IllegalArgumentException si le nom du fichier est invalide ou si sa longueur n'est pas celle attendue 
     */
    public HgtDiscreteElevationModel(File file){
        String name = file.getName();
        int longitude;
        int latitude;
        Interval1D inter1;
        Interval1D inter2;
        try  {
            
            if (!validFileLength(file)|| !nameTest(name) ) {

                throw new IllegalArgumentException();
            } else {
                try (FileInputStream s = new FileInputStream(file)){
                 
                long l= file.length();
                this.b = s.getChannel().map(MapMode.READ_ONLY, 0, l)
                        .asShortBuffer();
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException();
        } catch(NumberFormatException nfe){
            throw new IllegalArgumentException();
        } 
        longitude = getLongitude(name);
        latitude = getLatitude(name);
        inter1 = new Interval1D( longitude , longitude + SAMPLES_PER_DEGREE  );
        inter2 = new Interval1D( latitude , latitude + SAMPLES_PER_DEGREE  );
        extent = new Interval2D(inter1,inter2);
    }
    
    /**
     * retourne vrai si le la taille du nom du fichier est valide c a d d'une taille de 11 caractères
     * @param name
     * @return vrai si le la taille du nom du fichier est valide c a d d'une taille de 11 caractères
     */
    private boolean validNameLength(String name){
        return name.length()==11;
    }
    
    /**
     * retourne vrai si la première lettre est soit N ou S
     * @param name
     * @return vrai si la première lettre est soit N ou S
     */
    private boolean valid1Letter(String name){
        char a0 = name.charAt(0);
      return  (a0 == 'N' || a0 == 'S');
    }
    /**
     * retourne vrai si la latitude est valide c a d entre la 2nd et la 3ème place et entre -90 et 90 degrés inclus 
     * @param name
     * @return vrai si la latitude est valide c a d entre la 2nd et la 3ème place et entre -90 et 90 degrés inclus 
     */
    private boolean validLatitude(String name){
        String s = name.substring(1,3);
        int latitude = Integer.parseInt(s);
        return (latitude>=-90 || latitude<=90);
        
    }
    /**
     * retourne vrai si la 4ème lettre est soit E ou W
     * @param name
     * @return vrai si la 4ème lettre est soit E ou W
     */
    private boolean validEorW(String name){
        char c = name.charAt(3);
        return (c=='E' || c=='W' );
    }
    /**
     * retourne vrai si la latitude est valide c a d entre la 5ème et la 7ème place et entre 0 et 180 degrés inclus 
     * @param name
     * @return vrai si la latitude est valide c a d entre la 5ème et la 7ème place et entre 0 et 180 degrés inclus 
     */
    private boolean validLongitude(String name){ 
        String s = name.substring(4, 7);
        int longitude = Integer.parseInt(s);
        return (longitude>=0 || longitude<=180); // Inutile de tester pour -180 car on test dejà 
                                                //avant si la longitude est positive ou négative
    }
    /**
     * retourne vrai si l'extension du fichier est ".hgt"
     * @param name
     * @return vrai si l'extension du fichier est ".hgt"
     */
    private boolean validExtension(String name){
        String s = name.substring(7);
        return (s.equals(".hgt"));
    }
    
    /**
     * retourne vrai si le nom est au bon format
     * @param name
     * @return vrai si le nom est au bon format
     */
    private boolean nameTest(String name){
       return validNameLength(name)
               && valid1Letter(name) 
               && validLatitude(name)
               && validEorW(name)
               && validLongitude(name)
               && validExtension(name);
        
    }    
        
    private boolean validFileLength(File file){
        return CORRECTLENGTH == file.length();
    }
    
    /**
     * retourne la latitude en secondes d'angle
     * @return la latitude en secondes d'angle
     */
    private int getLatitude(String name){
        String s = name.substring(1,3);
        char c = name.charAt(0);
        if (c=='N'){
            return Integer.parseInt(s)*SAMPLES_PER_DEGREE;
        } else if(c=='S') {
            return -Integer.parseInt(s)*SAMPLES_PER_DEGREE;
        } else {
            throw new IllegalArgumentException("Le nom du fichier n'est pas correcte");            
        }
    }
    
    /**
     * retourne la longitude en secondes d'angle
     * @return la longitude en secondes d'angle
     * @throws IllegalArgumentException
     */
    private int getLongitude(String name){
        String s = name.substring(4,7);
        char c = name.charAt(3);
        if (c=='E'){
            return Integer.parseInt(s)*SAMPLES_PER_DEGREE;
        } else if(c=='W') {
            return -Integer.parseInt(s)*SAMPLES_PER_DEGREE;
        } else {
            throw new IllegalArgumentException("Le nom du fichier n'est pas correcte");            
        }
    }

    
    

    @Override
    public void close() throws IOException {
        b=null; 
    }
    
    @Override
    public Interval2D extent() {
        return extent;
    }

    @Override
    public double elevationSample(int x, int y){
      Preconditions.checkArgument(extent.contains(x, y), "l'index (x,y) n'est pas dans l'intervalle");
        return b.get(index(x,y));
    }
    
    /**
     * retourne l'index auquel se trouve le fichier 
     * correspondant au point de coordonnées (x,y)
     * @param x
     * @param y
     * @return l'index auquel se trouve le fichier 
     * correspondant au point de coordonnées (x,y)
     */
    private int index(int x, int y){
        int indexX= x-extent().iX().includedFrom(); // comme le buffer contient des élements à partir de l'index 0, on n'a pas besoin de faire +1
        int indexY= extent().iY().includedTo()-y; // Correspond au nombre de lignes avant d'arriver à celle correspondant à l'index
        return indexY*extent.iX().size()+indexX;
    }
    
    

}
