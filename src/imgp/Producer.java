package imgp;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class Producer extends Thread{
	private Buffer buffer; // Vom folosi un buffer pentru sincronizare
	private String input; // Numele fisierului sursa
	private String fileType; // Formatul fisierului sursa
	
	public Producer(Buffer b, String input, String fileType) {
		// Initializam atributele cu valorile furnizate
		buffer = b;
		this.input = input;
		this.fileType = fileType;
	}
	
	public void run () {
		try {
			// Initializam un obiect de tip ImageReader pentru a putea citi imaginea in blocuri
			Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName(fileType);
			ImageReader reader = (ImageReader)readers.next();
			
			for (int i = 0; i < 2; i++)
				for(int j = 0; j < 2; j++){
					File inputFile = new File(input + "." + fileType); 
					ImageInputStream iis = ImageIO.createImageInputStream(inputFile);
					reader.setInput(iis, true); // Selectam fisierul sursa
					
					// Vom citi cate un sfert, astfel vom retine dimensiunile pe jumatate
					int half_width = reader.getWidth(0)/2; 
					int half_height = reader.getHeight(0)/2;
					
					ImageReadParam param = reader.getDefaultReadParam();
					BufferedImage result = null;
					
					// Fiecare sfert va fi extras cu ajutorul unui dreptunghi de dimensiuni half_width x half_height, ce va fi plasat consecutiv in coltul stanga al fiecuri sfert din imagine.
					// Ordinea este: stanga sus, dreapta sus, stanga jos, dreapta jos.
					Rectangle rect = new Rectangle(j * half_width , i * half_height, half_width, half_height);
					param.setSourceRegion(rect);
					
					result = reader.read(0, param); // Se citeste sfertul de imagine
					buffer.put(result); // Se plaseaza imaginea in buffer
					
					System.out.println("Producatorul a citit sfertul :\t" + (i*2 + j));
					try {
						sleep(1000); // Se va astepta pentru sincronizare.
					} catch (InterruptedException e) {}
				}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			System.out.println("Fisierul sursa nu a putut fi citit! Verificati numele/extensia.");
		}
	}
}
