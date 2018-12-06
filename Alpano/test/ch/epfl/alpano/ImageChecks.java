package ch.epfl.alpano;


import static org.junit.Assert.assertEquals;
import static ch.epfl.alpano.CompareImages.*;
import java.io.File;

import org.junit.Test;

public class ImageChecks {

	@Test
	public void ElevationProfileImage() throws Exception {
		
		DrawElevationProfile.draw();
		
		File fileA = new File("profile.png");
	    File fileB = new File("profileEcho.png");
		assertEquals(compareImageWithDifference(fileA, fileB, "profile"), 100, 0);
	}
	
	@Test
	public void ElevationAndSlopeImage() throws Exception {
		
		DrawDEM.draw();
		
		File fileA = new File("elevation.png");
	    File fileB = new File("elevationEcho.png");
	    assertEquals(compareImageWithDifference(fileA, fileB, "elevation"), 100, 0);
	    
	    fileA = new File("slope.png");
	    fileB = new File("slopeEcho.png");
	    assertEquals(compareImageWithDifference(fileA, fileB, "slope"), 100, 0);
	}
	
	@Test
	public void DEMImage() throws Exception {
		
		DrawHgtDEM.draw();
		
		File fileA = new File("dem.png");
	    File fileB = new File("demEcho.png");
		assertEquals(compareImageWithDifference(fileA, fileB, "dem"), 100, 0);
	}
	
	@Test
	public void NiesenImage() throws Exception {
		
		DrawPanorama.draw();
		
		File fileA = new File("niesen.png");
	    File fileB = new File("niesenEcho.png");
		assertEquals(compareImageWithDifference(fileA, fileB, "niesen"), 100, 0);
	}
	
	@Test
	public void NiesenProfileImageGray() throws Exception {
		
	    DrawPanoramaGray.draw();
		
		File fileA = new File("niesen-profile.png");
	    File fileB = new File("niesen-profileEcho.png");
		assertEquals(compareImageWithDifference(fileA, fileB, "niesen-profile"), 100, 0);
	}
	@Test
    public void NiesenProfileImageColor() throws Exception {
        
        DrawPanoramaColor.draw();
        
        File fileA = new File("niesen-shaded.png");
        File fileB = new File("niesen-shadedEcho.png");
        assertEquals(compareImageWithDifference(fileA, fileB, "niesen-profile-shaded"), 100, 0.1);
    }
}
