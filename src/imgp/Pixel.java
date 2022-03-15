package imgp;

public interface Pixel {
	// Conversie date din Pixel in formatul ARGB al ImageIO
	int toARGB();
	// Detalii despre pixel
	void printPixelInfo();
}
