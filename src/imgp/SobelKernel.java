package imgp;

public class SobelKernel extends Kernel {
	double [][] kernel;
	SobelKernel(){
		// Se va initializa kernel-ul de tip Sobel pe directia X
		double[][] kernel = {
				{-1.0, 0.0, 1.0},
				{-2.0, 0.0, 2.0},
				{-1.0, 0.0, 1.0}};
		this.kernel = kernel;
		this.size = 3;
	}
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
				result[i][j] = super.matrixConvolution(littleMatrix, kernel);
			}
		return result;
		
	}

	double[][] apply(int[][] matrix) {
		return apply(matrix, "extend"); // Daca nu se specifica niciun parametru pentru extend mode, acesta va fi extend
	}
	@Override
	protected void printKernelData() {
		System.out.println("Kernel " + size + "x" + size + " operator Sobel");
		for (int i = 0; i < size; i++){
			for(int j = 0; j < size; j++)
				System.out.print(kernel[i][j] + " ");
			System.out.println();
		}
	}

}
