package imgp;

public class GrayscalePixel implements Pixel {
	private int a, gray; // Pentru un pixel gri avem nevoie doar de 2 date, alpha si valoarea gri.
	
	GrayscalePixel(int... args){
		// Constructor 1 argument - gray
		if(args.length == 1){
			this.a = 255; // Canalul alpha este 255.
			this.gray = args[0]; // Valoarea gray este cea furnizata.
		}
		// Constructor 2 argumente - alpha, gray
		else if(args.length == 2){
			this.a = args[0]; // Valoarea alpha este primul argument.
			this.gray = args[1]; // Valoarea gray este al doilea argument.
		}
		// Constructor 4 argumente - alpha, red, green, blue
		else if(args.length == 4){
			this.a = args[0]; // Valoarea alpha este primul argument.
			this.gray = (int) (0.299 * args[1] + 0.587 * args[2] + 0.114 * args[3]); // Urmatoarele 3 argumente reprezinta valorile r, g, b; valoarea de gri este calculata folosind anumite ponderi.
		}
		// Constructor fara argumente / nr. invalid de argumente
		else {
			// Ambele valori sunt initializate cu 255.
			this.a = 255;
			this.gray = 255;
		}
	}
	// Constructor cu argument ARGB; argumentul 2 specifica daca valoarea argb este a unui pixel color sau nu.
	GrayscalePixel(int argb, boolean isGray){
		this.a = (argb >> 24) & 0xFF; // Se extrag bitii corespunzatori intensitatii
		if(isGray){
			this.gray = argb & 0xFF;  // Pentru un pixel gri, vom avea aceeasi intensitate in oricare canal rgb.
		}
		else{
			int r = (argb >> 16) & 0xFF; // Se extrag bitii corespunzatori canalului R (8-15).
			int g = (argb >> 8) & 0xFF; // Se extrag bitii corespunzatori canalului G (16-23).
			int b = argb & 0xFF;	// Se extrag bitii corespunzatori canalului B (24-31).
			this.gray = (int) (0.299 * r + 0.587 * g + 0.114 * b); // Se calculeaza valoarea folosind metoda mentionata.
		}
	}
	
	public int getGray(){
		return gray;
	}
	
	@Override
	// Codificare in formatul ARGB
	public int toARGB() {
		int argb;
		argb = (a & 0xFF) << 24;
		argb += (gray & 0xFF) << 16;
		argb += (gray & 0xFF) << 8;
		argb += gray & 0xFF;
		return argb;
	}

	@Override
	public void printPixelInfo() {
		// TODO Auto-generated method stub
		System.out.println("Grayscale Pixel");
		System.out.println("Values: /nAlpha = " + a + "/nGray = " + gray);
	}

}
