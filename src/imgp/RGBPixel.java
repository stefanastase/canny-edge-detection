package imgp;
public class RGBPixel implements Pixel {
	
	// 4 componente (alpha, red, green, blue)
	int a, r, g, b;
	
	RGBPixel(){
		// Constructorul fara parametri initializeaza cu 255 fiecare componenta
		a = 255; 
		r = 255;
		g = 255;
		b = 255;
	}
	
	RGBPixel(int a, int r, int g, int b){
		// Constructorul cu 4 parametri initializeaza fiecare componenta cu valoarea specificata
		this.a = a;
		this.r = r;
		this.g = g;
		this.b = b;
	}
	RGBPixel(int argb){
		// Constructorul decodifica formatul ARGB in formatul RGBPixel
		this.a = (argb >> 24) & 0xFF; // Bitii 0-7 din ARGB specifica componenta Alpha.
		this.r = (argb >> 16) & 0xFF; // Bitii 8-15 din ARGB specifica componenta Red.
		this.g = (argb >> 8) & 0xFF; // Bitii 16-23 din ARGB specifica componenta Green.
		this.b = argb & 0xFF; // Bitii 24-31 din ARGB specifica componenta Blue.
	}
	
	public GrayscalePixel toGrayscale(){
		return new GrayscalePixel(a, r, g, b); // Conversie la pixel Grayscale
	}
	
	@Override
	public int toARGB() {
		int argb;
		// Codificare in formatul ARGB
		argb = (a & 0xFF) << 24;
		argb += (r & 0xFF) << 16;
		argb += (g & 0xFF) << 8;
		argb += b & 0xFF;
		return argb;
	}
	@Override
	public void printPixelInfo() {
		// TODO Auto-generated method stub
		System.out.println("RGB Pixel");
		System.out.println("Values: /nAlpha = " + a + "/nRed = " + r + "/nGreen = " + g + "/nBlue = " + b);
	}
}
