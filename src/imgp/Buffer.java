package imgp;

import java.awt.image.BufferedImage;

public class Buffer {
	private BufferedImage image = null; // Vom pastra in interiorul buffer-ului obiecte de tipul BufferedImage
	private boolean available = false;
	
	public synchronized BufferedImage get() {
		while (!available) {
			try {
				wait();
				// Asteapta "producatorul" sa puna o imagine
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}
	available = false;
	notifyAll();
	return image;
	}
	
	public synchronized void put(BufferedImage image) {
		while (available) {
			try {
				wait();
				// Asteapta "consumatorul" sa preia imaginea
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		this.image = image;
		available = true;
		notifyAll();
	}
}

