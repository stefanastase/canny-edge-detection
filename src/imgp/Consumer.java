package imgp;

import java.awt.image.BufferedImage;

public class Consumer extends Thread {
	private Buffer buffer; // Vom folosi un buffer pentru a asigura sincronizarea
	private int [][] pixelArray; // Vom retine matricea de pixeli rezultata din imagine
	private int width, height; // Vom retine dimensiunile imaginii finale
	
	public Consumer (Buffer b, int w, int h) {
		// Initializam datele cu parametrii furnizati
		buffer = b;
		width = w;
		height = h;
		pixelArray = new int[height][width];
	}
	
	public void run () {
		BufferedImage value = null;
		// Vom folosi 2 bucle for, prima pentru jumatatile sus/jos, iar cea de-a doua pentru jumatatile stanga dreapta
		for (int i = 0; i < 2; i++){
			for (int j = 0; j < 2; j++){
			value = buffer.get(); // Luam din buffer imaginea
			
			// Calculam dimensiunile sfertului de imagine
			int width = value.getWidth();
			int height = value.getHeight();
			
			// Vom folosi inca 2 bucle for pentru a parcurge sfertul de imagine preluat, fiecare pixel va fi plasat la pozitia corecta in matricea de pixeli finala.
			// Ordinea este: sfertul stanga sus, sfertul dreapta sus, sfertul stanga jos, sfertul dreapta jos.
			for(int ii = 0; ii < height; ii++)
				for(int jj = 0; jj < width; jj++){
					pixelArray[i * height + ii][j * width + jj] = value.getRGB(jj, ii); // Cadranul in care va fi plasat este dat de (i * height, j * width).
				}
			System.out.println("Consumatorul a citit sfertul :\t" + (i * 2 + j));
			}
		}
	}
	public int [][] getPixelArray(){
		return pixelArray; // Se va returna matricea de pixeli pentru toata imaginea.
	}
}
