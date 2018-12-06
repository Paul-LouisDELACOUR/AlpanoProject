package ch.epfl.alpano;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
public class CompareImages {
    static final int white = Integer.MAX_VALUE  ;
    static final int black = 0;
    static File fileA = new File("niesen-shaded.png");
    static File fileB = new File("niesen-shadedEcho.png");
     
    public static void main(String[] args) {
        compareImageWithDifference(fileA, fileB,"niesen");
    }
    
    public static float compareImage(File fileA, File fileB) {

        float percentage = 0;
        try {
            // take buffer data from both image files //
            BufferedImage biA = ImageIO.read(fileA);
            DataBuffer dbA = biA.getData().getDataBuffer();
            int sizeA = dbA.getSize();
            BufferedImage biB = ImageIO.read(fileB);
            DataBuffer dbB = biB.getData().getDataBuffer();
            int sizeB = dbB.getSize();
            int count = 0;
            
            // compare data-buffer objects //
            if (sizeA == sizeB) {

                for (int i = 0; i < sizeA; i++) {

                    if (dbA.getElem(i) == dbB.getElem(i)) {
                        count = count + 1;
                        
                    }
                    

                }
                percentage = ((float)(count * 100)) / (float)sizeA;
            } else {
                System.out.println("Both the images are not of same size");
            }

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Failed to compare image files ...");
        }
        
        
        return (percentage);
        
    }

/**

     * @param fileA
     * @param fileB
     */
    public static float compareImageWithDifference(File fileA, File fileB, String whatIsCompared) {
        ArrayList<Double> errorPercentage = new ArrayList<>();
        float percentage = 0;
        try {
            // take buffer data from both image files //
            BufferedImage biA = ImageIO.read(fileA);
            int heightA = biA.getHeight();
            int widthA = biA.getWidth();
            BufferedImage biB = ImageIO.read(fileB);
            int heightB = biB.getHeight();
            int widthB = biB.getWidth();
            int count = 0;
            
            // compare data-buffer objects //
            
            if (heightA == heightB && widthB == widthA) {
                
                BufferedImage differences =
                        new BufferedImage(widthA, heightA, TYPE_INT_RGB);
                for (int x = 0; x < widthA; x++) {
                    
                    for(int y = 0; y < heightA; ++y){
                        if (biA.getRGB(x, y) == biB.getRGB(x, y)) {
                            count = count + 1;
                            differences.setRGB(x, y, white);
                        }
                        else{
                            differences.setRGB(x, y, black);
                            double pourcentageErreur = (1-((double)biA.getRGB(x, y)/biB.getRGB(x, y)))*100;
                            errorPercentage.add(pourcentageErreur);
                        }
                    }
                    
                    

                }
                percentage = ((float)(count * 100)) / (float)(heightA*widthA);
                ImageIO.write(differences, "png", new File("differencesOf"+whatIsCompared+".png"));
            } else {
                System.out.println("Both the images are not of same size");
            }

        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Failed to compare image files ...");
        }
        
        System.out.println(fileA.getName()+": "+percentage);
        return (percentage);
        
    }    
        
    

}
