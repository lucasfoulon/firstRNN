import java.util.Random;
import java.util.Arrays;

/** 
 Generate pseudo-random floating point values, with an 
 approximately Gaussian (normal) distribution.

 Many physical measurements have an approximately Gaussian 
 distribution; this provides a way of simulating such values. 
*/
public final class RandomGaussian {
    
	private Random fRandom = new Random();
  
	double getGaussian(double aMean, double aVariance){
		return aMean + fRandom.nextGaussian() * aVariance;
	}
  
	double[][] getTabGaussian(int size_x, int size_y, double mean, double variance) {
	  
		double tab[][] = new double[size_x][size_y];
	  
		for(int i = 0 ; i < size_x ; i++) {
			for(int j = 0 ; j < size_y ; j++) {
				tab[i][j] = this.getGaussian(mean, variance) * 0.01;
				//System.out.println("Gaussian : "+ tab[i][j]);
			}
		}
	  
		return tab;
	}


	public void getTabGaussian(double[][] tab, int size_x, int size_y, double mean, double variance) {
		
		for(int i = 0 ; i < size_x ; i++) {
			for(int j = 0 ; j < size_y ; j++) {
				tab[i][j] = this.getGaussian(mean, variance) * 0.01;
				//System.out.println("Gaussian : "+ tab[i][j]);
			}
		}
		
	}
	
	double[][] getTabZero(int size_x, int size_y) {
		  
		double tab[][] = new double[size_x][size_y];
	  
		for(int i = 0 ; i < size_x ; i++) {
			Arrays.fill(tab[i],0.0f);
		}
	  
		return tab;
	}

	public double[][] getTabZero(double[][] tab, int size_x, int size_y) {
		
		for(int i = 0 ; i < size_x ; i++) {
			Arrays.fill(tab[i],0.0f);
		}
	  
		return tab;
		
	}
} 