package imgp;

public class GaussianFilter extends Kernel {

	double [][] kernelData; // Matrice pentru a salva kernelul
	GaussianFilter(int size, double sigma){
		this.size = size;
		kernelData = new double[size][size]; 
		// Calcul kernel cu formula matematica
		int k = size / 2;
		double k_H = 1 / (2 * Math.PI * Math.pow(sigma, 2));
		for(int i = 0; i < size; i++)
			for(int j = 0; j < size; j++)
				kernelData[i][j] = k_H * Math.exp(-(Math.pow(i - k, 2) + Math.pow(j - k, 2)) / (2 * Math.pow(sigma, 2)));
	}

	double[][] apply(int[][] matrix, String borderMode){
		int rows = matrix.length;
		int cols = matrix[0].length;

		double [][] result = new double[rows][cols];
		int[][] extendedMatrix = super.extendMatrix(matrix, size, borderMode); // Se extinde matricea prin modul specificat
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < cols; j++){
				int [][] littleMatrix = new int[size][size]; 
				// Pentru convolutie vom folosi doar o parte din matrice la un moment dat, astfel, selectam o subamtrice kernelSize x kernelSize din matricea extinsa
				for(int ii = 0; ii < size; ii++)
					for(int jj = 0; jj < size; jj++)
						littleMatrix[ii][jj] = extendedMatrix[i + ii][j + jj];
				result[i][j] = super.matrixConvolution(littleMatrix, kernelData);
			}
		return result;
		
	};
	double[][] apply(int[][] matrix){
		return apply(matrix, "extend"); // Daca nu se specifica niciun parametru pentru extend mode, acesta va fi extend
		};
		
	@Override
	protected void printKernelData() {
		// TODO Auto-generated method stub
		System.out.println("Kernel " + size + "x" + size + " filtru gaussian");
		for (int i = 0; i < size; i++){
			for(int j = 0; j < size; j++)
				System.out.print(kernelData[i][j] + " ");
			System.out.println();
		}
	}
}