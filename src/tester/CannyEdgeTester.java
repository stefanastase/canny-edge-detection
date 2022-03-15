package tester;
import imgp.CannyEdgeDetector;
import java.util.Scanner;

public class CannyEdgeTester {

	public static void main(String[] args) {
		// Program de test pentru Canny Edge Detector
		System.out.println("CANNY EDGE DETECTION");
		System.out.println();
		System.out.println("Acest program prelucreaza o imagine BMP color, aplicand un algoritm pentru evidentierea marginilor.");
		if(args.length == 0){
			// In cazul in care nu se dau argumente din consola, vom introduce date de la tastatura
			Scanner s = new Scanner(System.in);
			CannyEdgeDetector ced = null;
			
			// Informatii despre Canny Edge Detector
			char ans;
			do{
				System.out.println("Doriti sa modificati parametrii impliciti? [y/n]");
				ans = s.next().charAt(0);
			} while (ans != 'y' && ans != 'n');
			
			if(ans == 'y'){
				System.out.println("Introduceti noii parametri ai transformarii.");
				System.out.print("Gaussian Size: ");
				int gaussianSize = s.nextInt();
				System.out.println();
				System.out.print("Gaussian Sigma: ");
				double gaussianSigma = s.nextDouble();
				System.out.println();
				System.out.print("Low Thershold: ");
				double lowThreshold = s.nextDouble();
				System.out.println();
				System.out.print("High Thershold: ");
				double highThreshold = s.nextDouble();
				System.out.println();
				System.out.print("Strong Pixel: ");
				int strongPixel = s.nextInt();
				System.out.println();
				System.out.print("Weak Pixel: ");
				int weakPixel = s.nextInt();
				ced = new CannyEdgeDetector(gaussianSize, gaussianSigma, lowThreshold, highThreshold, strongPixel, weakPixel);
			}
			else
				ced = new CannyEdgeDetector(3, 0.5, 0.15, 0.4, 255, 50); // Valori default
			
			// Informatii despre fiserele intrare/iesire
			long startTime = System.currentTimeMillis();
			System.out.println("Introduceti numele fisierului de intrare.");
			String input = s.next();
			System.out.println("Introduceti numele fisierului de iesire.");
			String output = s.next();
			do{
				System.out.println("Generare pasi intermediari? [y/n]");
				ans = s.next().charAt(0);
			} while (ans != 'y' && ans != 'n');
			
			boolean showSteps = ans == 'y' ? true: false;
			long readTime = System.currentTimeMillis();
			System.out.println("Citirea informatiilor despre fisier a durat " + (readTime - startTime) / 1000.0f + " secunde.");
			ced.apply("input/" + input, "output/" + output, "bmp", showSteps);
			
			s.close();
		}
		
		else if (args.length == 2){
			// Pentru 2 argumente din linia de comanda (sursa, destinatie), vom realiza o filtrare cu valorile default
			String[] input = args[0].split(".");
			String[] output = args[1].split(".");
			CannyEdgeDetector ced = new CannyEdgeDetector(3, 0.5, 0.15, 0.4, 255, 50);
			ced.apply(input[0], output[0], output[1], false);
			}
		}
}
