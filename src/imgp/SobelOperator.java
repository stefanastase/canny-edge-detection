package imgp;
public class SobelOperator extends SobelKernel{
	
	double [][] kernelY;
	
	SobelOperator(){
		// Pentru a implementa corect operatorul Sobel, vom adauga inca un kernel Sobel, pe directa Y
		double[][] Sy = {
				{1.0, 2.0, 1.0},
				{0.0, 0.0, 0.0},
				{-1.0, -2.0, -1.0}};
		this.kernelY = Sy;
	}
	
	@Override
	double[][] apply(int[][] matrix, String borderMode) {
		int rows = matrix.length;
		int cols = matrix[0].length;

		double [][] result = new double[rows][cols];
		int[][] extendedMatrix = super.extendMatrix(matrix, size, borderMode);
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < cols; j++){
				int [][] littleMatrix = new int[size][size];
				// Pentru convolutie vom folosi doar o parte din matrice la un moment dat, astfel, selectam o subamtrice kernelSize x kernelSize din matricea extinsa
				for(int ii = 0; ii < size; ii++)
					for(int jj = 0; jj < size; jj++)
						littleMatrix[ii][jj] = extendedMatrix[i + ii][j + jj];
				double gX = super.matrixConvolution(littleMatrix, kernel); // Calculam gradientul folosind kernelul X
				double gY = super.matrixConvolution(littleMatrix, kernelY); // Calculam gradientul folosind kernelul Y
				
				result[i][j] = Math.sqrt(gX*gX + gY*gY); // Rezultatul este modulul vectorului format de Gx si Gy
			}
		return result;
		
	}

	double[][] apply(int[][] matrix) {
		return apply(matrix, "extend");
	}
	
	double[][] getThetaValues(int[][] matrix, String borderMode){
		int rows = matrix.length;
		int cols = matrix[0].length;

		double [][] result = new double[rows][cols];
		int[][] extendedMatrix = super.extendMatrix(matrix, size, borderMode);
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < cols; j++){
				int [][] littleMatrix = new int[size][size];
				// Pentru convolutie vom folosi doar o parte din matrice la un moment dat, astfel, selectam o subamtrice kernelSize x kernelSize din matricea extinsa
				for(int ii = 0; ii < size; ii++)
					for(int jj = 0; jj < size; jj++)
						littleMatrix[ii][jj] = extendedMatrix[i + ii][j + jj];
				double gX = super.matrixConvolution(littleMatrix, kernel); // Calcul gradient x
				double gY = super.matrixConvolution(littleMatrix, kernelY); // Calcul gradient y
				
				result[i][j] = Math.atan2(gY, gX); // Pentru prelucrarile ulterioare vom avea nevoie si de format de vectorul gradient
			}
		return result;
	}
	double[][] getThetaValues(int[][] matrix){
		return getThetaValues(matrix, "extend"); // Daca nu se specifica niciun parametru pentru extend mode, acesta va fi extend
	}
	
	@Override
	protected void printKernelData() {
		System.out.println("2 Kernel-uri " + size + "x" + size + " operator Sobel");
		System.out.println("Kernel X");
		for (int i = 0; i < size; i++){
			for(int j = 0; j < size; j++)
				System.out.print(kernel[i][j] + " ");
			System.out.println();
		}
		System.out.println("Kernel Y");
		for (int i = 0; i < size; i++){
			for(int j = 0; j < size; j++)
				System.out.print(kernelY[i][j] + " ");
			System.out.println();
		}
		
	}

}
