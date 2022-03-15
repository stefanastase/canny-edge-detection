package imgp;

public abstract class Kernel {
	int size; // Dimensiunea matricii size x size
	
	// Metoda pentru a calcula produsul de convolutie dintre o matrice oarecare si kernel
	protected double matrixConvolution(int[][] matrix, double[][] kernel){
		double result = 0;
		for(int i = 0; i < matrix.length; i++)
			for(int j = 0; j < matrix[i].length; j++)
				result += matrix[i][j] * kernel[i][j];
		return result;
	}
	// Metoda pentru a extinde matricea in cazul in care dorim sa accesam celule din afara indecsilor matricei
	protected int[][] extendMatrix(int[][] matrix, int kernelSize, String mode){
		int k = kernelSize / 2; // Numarul de linii/coloane de extensie necesar in fiecare directie
		
		// Dimensiunile initiale ale matricei
		int initialRows = matrix.length; 
		int initialCols = matrix[0].length;
		
		// Dimenisunile matricei extinse
		int rows = matrix.length + 2*k;
		int cols = matrix[0].length + 2*k;
		int[][] extendedMatrix = new int[rows][cols];
		
		// Metoda extend foloseste pixelii de pe margine pentru a face extensia matricei
		if (mode == "extend"){
			for(int i = 0; i < initialRows; i++)
				for(int j = 0; j < initialCols; j++)
					extendedMatrix[i + k][j + k] = matrix[i][j]; // Copiere matrice intiiala
			
			// Extensie colturi
			for(int i = 0; i < k; i++)
				for(int j = 0; j < k; j++){
					extendedMatrix[i][j] = matrix[0][0]; // stanga sus
					extendedMatrix[initialRows + i + k][j] = matrix[initialRows - 1][0]; // stanga jos
					extendedMatrix[i][initialCols + j + k] = matrix[0][initialCols - 1]; // dreapta sus
					extendedMatrix[initialRows + i + k][initialCols + j + k] = matrix[initialRows - 1][initialCols - 1]; // dreapta jos
				}
			// Extensie pe linii
			for(int i = 0; i < k; i++)
				for(int j = 0; j < initialCols; j++){
					extendedMatrix[i][j + k] = matrix[0][j];
					extendedMatrix[initialRows + i + k][j + k] = matrix[initialRows - 1][j]; 
				}
			// Extensie pe coloane
			for(int i = 0; i < initialRows; i++)
				for(int j = 0; j < k; j++){
					extendedMatrix[i + k][j] = matrix[i][0];
					extendedMatrix[i + k][initialCols + j + k] = matrix[i][initialCols - 1]; 
				}
		}
		
		return extendedMatrix;
	}
	
	abstract double[][] apply(int[][] matrix, String borderMode); // Aplicare kernel asupra unei matrice oarecare
	protected abstract void printKernelData(); // Afisare informatii despre kernel
	
}
