package imgp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;

public class CannyEdgeDetector {
	// Parametrii filtrului de detectie
	int gaussianSize; // Marimea kernelului pentru filtrul gaussian
	double gaussianSigma; // Sigma pentru filtrul gaussian
	
	double lowThr; // Limita pentru pixeli slabi
	double highThr; // Limita pentru pixeli puternici
	int strongPixel; // Valoarea unui pixel puternic
	int weakPixel; // Valoarea unui pixel slab
	
	public CannyEdgeDetector(int gaussianSize, double gaussianSigma, double lowThr, double highThr, int strongPixel, int weakPixel){
		// Constructor cu parametri
		this.gaussianSize = gaussianSize;
		this.gaussianSigma = gaussianSigma;
		
		this.highThr = 255 * highThr; // lowThr va avea o valoare intre 0 si 1
		this.lowThr = 255 * lowThr; // highThr va avea o valoare intre 0 si 1
		
		this.strongPixel = strongPixel;
		this.weakPixel = weakPixel;
		
	}
	
	private int fixOutOfRangeRGBValues(double value){
		// Functie helper pentru a aduce valorile in intervalul 0 - 255
		return value > 255 ? 255 : (value < 0 ? 0 : (int) value);
	}
	private void toGrayscale(int [][] pixelArray){
		// Conversie RGB-Grayscale pentru matricea de pixeli pixelArray
		for(int i = 0; i < pixelArray.length; i++)
			for(int j = 0; j < pixelArray[0].length; j++) {
				RGBPixel currentPixel =  new RGBPixel(pixelArray[i][j]); // Se extrage pixelul current
				GrayscalePixel grayPixel = currentPixel.toGrayscale(); // Se converteste pixelul RGB in GrayscalePixel
				pixelArray[i][j] = grayPixel.getGray(); // Se extrage valoarea de gri din pixel
			}		
	}
	private void gaussianFilter(int [][] pixelArray){
		// Aplicare filtru gaussian pentru matriceapixelArray
		GaussianFilter gaussianFilter = new GaussianFilter(gaussianSize, gaussianSigma);
		double [][] gaussianArray = gaussianFilter.apply(pixelArray); // Se aplica filtrul intregii matrici, rezultatul va fi de tip double.
		for(int i = 0; i < pixelArray.length; i++)
			for(int j = 0; j < pixelArray[0].length; j++)
				pixelArray[i][j] = fixOutOfRangeRGBValues(gaussianArray[i][j]); // Se corecteaza valorile invalide si se converteste in int
	}
	private double[][] sobelOperation(int [][] pixelArray){
		// Aplicare operatie Sobel pentru matricea pixelArray
		SobelOperator sobelOperator = new SobelOperator();
		double [][] sobelMag = sobelOperator.apply(pixelArray); // Calculam modulul gradientului pentru fiecare pixel.
		double [][] sobelTheta = sobelOperator.getThetaValues(pixelArray); // Calculam unghiul gradientului pentru fiecare pixel.
		for(int i = 0; i < pixelArray.length; i++)
			for(int j = 0; j < pixelArray[0].length; j++)
				pixelArray[i][j] = fixOutOfRangeRGBValues(sobelMag[i][j]); // Se vor corecta valorile incorecte si se va converti rezultatul la int.
		return sobelTheta; // Vom returna matricea ce contine unghiul gradientului pentru fiecare pixel.
	}
	private void nonMaxSupression(int [][] pixelArray, double[][] sobelTheta){
		// Aplicare algoritm Non Max Supression
		for(int i = 0; i < pixelArray.length; i++)
			for(int j = 0; j < pixelArray[0].length; j++){
				double angle = Math.toDegrees(sobelTheta[i][j]); // Vom folosi valoarea in grade a unghiurilor.
				int q = 255, r = 255; // Valorile maxime ale pixelilor
				// Pentru fiecare pixel vom verifica vecinii din fata si din spatele sau, in functie de unghiul theta
				// Vom folosi un interval ce va aproxima pentru fiecare valoare (0, 45, 90, 135)
				// 0 grade
				if((0 <= angle && angle < 22.5) || (157.5 <= angle && angle <= 180)){
					try{
						q = pixelArray[i][j + 1]; // Pixelul din fata
					} catch (IndexOutOfBoundsException e){
						q = 255;
					}
					try{
						r = pixelArray[i][j - 1]; // Pixelul din spate
					} catch (IndexOutOfBoundsException e){
						r = 255;
					}
				}
				// 45 grade
				else if(22.5 <= angle && angle < 67.5){
					try{
						q = pixelArray[i + 1][j - 1]; // Pixelul din fata
					} catch (IndexOutOfBoundsException e){
						q = 255;
					}
					try{
						r = pixelArray[i - 1][j + 1]; // Pixelul din spate
					} catch (IndexOutOfBoundsException e){
						r = 255;
					}
				}
				// 90 grade
				else if(67.5 <= angle && angle < 112.5){
					try{
						q = pixelArray[i + 1][j]; // Pixelul din fata
					} catch (IndexOutOfBoundsException e){
						q = 255;
					}
					try{
						r = pixelArray[i - 1][j]; // Pixelul din spate
					} catch (IndexOutOfBoundsException e){
						r = 255;
					}
				}
				// 135 grade
				else if(112.5 <= angle && angle < 157.5)
					try{
						q = pixelArray[i - 1][j - 1]; // Pixelul din fata
					} catch (IndexOutOfBoundsException e){
						q = 255;
					}
					try{
						r = pixelArray[i + 1][j - 1]; // Pixelul din spate
					} catch (IndexOutOfBoundsException e){
						r = 255;
					}
				
				if(pixelArray[i][j] < q || pixelArray[i][j] < r)
					pixelArray[i][j] = 0; // Daca pixelul este mai slab decat vecinii sai va fi eliminat (valoarea va fi setata la 0)
			}	
	}
	private void doubleThreshold(int [][] pixelArray){
		// Vom folosi 2 praguri pentru a clasifica pixelii ramasi in matricea pixelArray
		for(int i = 0; i < pixelArray.length; i++)
			for(int j = 0; j < pixelArray[0].length; j++)
				if(pixelArray[i][j] >= highThr) pixelArray[i][j] = strongPixel; // Pixelii ce au valoarea mai mare decat highThr sunt considerati puternici
				else if(pixelArray[i][j] < lowThr) pixelArray[i][j] = 0; // Pixelii ce au valoarea mai mica decat lowThr sunt stersi
				else pixelArray[i][j] = weakPixel; // Pixelii ce au valoarea mai mica decat highThr, dar mai mare decat lowThr sunt considerati slabi
	}
	private void edgeTracking(int [][] pixelArray){
		// Vom aplica metoda Edge Tracking By Hysteresis
		// Pixelii slabi ce au un vecin puternic vor deveni, de asemenea, puternici; altfel, vor fi stersi
		for(int i = 0; i < pixelArray.length; i++)
			for(int j = 0; j < pixelArray[0].length; j++)
				if(pixelArray[i][j] == weakPixel){
					try{
						if(pixelArray[i + 1][j - 1] == strongPixel)
							pixelArray[i][j] = strongPixel;
						else pixelArray[i][j] = 0;
						} catch (IndexOutOfBoundsException e){}
					try{
						if(pixelArray[i + 1][j] == strongPixel)
							pixelArray[i][j] = strongPixel;
						else pixelArray[i][j] = 0;
						} catch (IndexOutOfBoundsException e){}
					try{
						if(pixelArray[i + 1][j + 1] == strongPixel)
							pixelArray[i][j] = strongPixel;
						else pixelArray[i][j] = 0;
						} catch (IndexOutOfBoundsException e){}
					try{
						if(pixelArray[i][j - 1] == strongPixel)
							pixelArray[i][j] = strongPixel;
						else pixelArray[i][j] = 0;
						} catch (IndexOutOfBoundsException e){}
					try{
						if(pixelArray[i][j + 1] == strongPixel)
							pixelArray[i][j] = strongPixel;
						else pixelArray[i][j] = 0;
						} catch (IndexOutOfBoundsException e){}
					try{
						if(pixelArray[i - 1][j - 1] == strongPixel)
							pixelArray[i][j] = strongPixel;
						else pixelArray[i][j] = 0;
						} catch (IndexOutOfBoundsException e){}
					try{
						if(pixelArray[i - 1][j] == strongPixel)
							pixelArray[i][j] = strongPixel;
						else pixelArray[i][j] = 0;
						} catch (IndexOutOfBoundsException e){}
					try{
						if(pixelArray[i - 1][j + 1] == strongPixel)
							pixelArray[i][j] = strongPixel;
						else pixelArray[i][j] = 0;
						} catch (IndexOutOfBoundsException e){}
				}	
	}
	private void writeImage(BufferedImage result, int [][] pixelArray, String output, String fileType){
		int height = pixelArray.length;
		int width = pixelArray[0].length;
		result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // Pentru salvarea imaginii, vom folosi o imagine ARGB de dimensiune width x height
		
		for(int i = 0; i < height; i++)
			for(int j = 0; j < width; j++){
				GrayscalePixel pixel = new GrayscalePixel(pixelArray[i][j]); // Conversie int - grayscalePixel
				result.setRGB(j, i, pixel.toARGB()); // Conversie grayscalePixel - ARGB + salvare in imagine
			}
		
		File outputFile = new File(output + "." + fileType); // Vom scrie imaginea in fisierul output.fileType
		try {
			ImageIO.write(result, fileType, outputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Fisierul nu a putut fi scris! Verificati numele/extensia acestuia.");
		}
	}
	public void apply(String input, String output, String fileType, boolean writeSteps){
		int [][] pixelArray = null; // Initializam matricea de pixeli
		BufferedImage result = null; // Initializam imaginea rezultat
		
		File inputFile = new File(input + "." + fileType); // Fisierul sursa este input.fileType
		
		try {
			// Vom folosi un obiect de tipul ImageReader pentru a citi header-ul imaginii sursa
			long startTime = System.currentTimeMillis();
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("bmp");
			ImageReader reader = (ImageReader)readers.next();
			
			ImageInputStream iis = ImageIO.createImageInputStream(inputFile);
			reader.setInput(iis, true);
			int width = 0;
			int height = 0;
			try{
				width = reader.getWidth(0); // Extragem din header latimea
				height = reader.getHeight(0); // Extragem din header inaltimea
			} catch(IllegalStateException e){
				System.out.println("Fisierul sursa nu a fost setat!");
				System.exit(1);
			}
			
			Buffer b = new Buffer(); // Initializam buffer-ul pentru sincronizare
			Producer p = new Producer(b, input, fileType); // Producer thread, preia cate un sfert din imaginea sursa si pune in buffer
			Consumer c = new Consumer(b, width, height); // Consumer threa, preia din buffer si converteste in matrice de int
			p.start(); // Lansam in executie thread-ul p
			c.start(); // Lansam in executie thread-ul c
			
			try {
				p.join(); // Asteptam finalizarea thread-ului p
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			try {
				c.join(); // Asteptam finalizarea thread-ului c
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long readTime = System.currentTimeMillis();
			System.out.println("Citirea imaginii a durat " + (readTime - startTime) / 1000.0f + " secunde.");
			// Executia celor 2 thread-uri s-a finalizat, vom continua cu prelucrarea
			startTime = System.currentTimeMillis();
			pixelArray = c.getPixelArray(); // Extragem matricea de pixeli de la consumer
			
			// PAS 1 - Conversie RGB - Grayscale
			toGrayscale(pixelArray);
			if(writeSteps) writeImage(result, pixelArray, output + "_1", "bmp"); // writeSteps = true - vom genera cate un fisier pentru fiecare pas
			
			// PAS 2 - Filtru Gaussian
			gaussianFilter(pixelArray);
			if(writeSteps) writeImage(result, pixelArray, output + "_2", "bmp"); // writeSteps = true - vom genera cate un fisier pentru fiecare pas
			
			// PAS 3 - Operator Sobel
			double [][] sobelTheta = sobelOperation(pixelArray);
			if(writeSteps) writeImage(result, pixelArray, output + "_3", "bmp"); // writeSteps = true - vom genera cate un fisier pentru fiecare pas
			
			// PAS 4 - Non-Max Supression
			nonMaxSupression(pixelArray, sobelTheta);
			if(writeSteps) writeImage(result, pixelArray, output + "_4", "bmp"); // writeSteps = true - vom genera cate un fisier pentru fiecare pas
			
			// PAS 5 - Double Threshold
			doubleThreshold(pixelArray);
			if(writeSteps) writeImage(result, pixelArray, output + "_5", "bmp"); // writeSteps = true - vom genera cate un fisier pentru fiecare pas
			
			// PAS 6 - Edge Tracking by Hysteresis
			edgeTracking(pixelArray);
			long executionTime = System.currentTimeMillis();
			System.out.println("Executia prelucrarii a durat " + (executionTime - startTime) / 1000.0f + " secunde.");
			startTime = System.currentTimeMillis();
			writeImage(result, pixelArray, output, "bmp");	// Generam fisierul final.
			long writeTime = System.currentTimeMillis();
			System.out.println("Scrierea imaginii finale a durat " + (writeTime - startTime) / 1000.0f + " secunde.");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
