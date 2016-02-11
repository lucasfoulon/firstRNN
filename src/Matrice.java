import java.util.Arrays;


public class Matrice {

	double[] dotMatrices2d1d(double t1[][], double t2[]) {
		
		double res[] = new double[t1.length];
		Arrays.fill(res,0.0f);
		
		//System.out.println("Longueur t1 "+t1.length);
		//System.out.println("Longueur t2 "+t2.length);
		
		for(int i = 0 ; i < t1.length ; i++) {
			//System.out.println("Longueur t1"+i+" "+t1[i].length);
			for(int j = 0 ; j < t2.length ; j++) {
				
				//System.out.println("i =  "+i+" et j = "+j);
				
				//System.out.println("res "+i+ " = " + res[i]);
				res[i] += (t1[i][j] * t2[j]);
				//System.out.println("t1[i][j] = " +t1[i][j]);
				//System.out.println("t2j = " +t2[j]);
				//System.out.println("res "+i+ " = " + res[i]);
			}
		}
		
		return res;
	}
	
	double[] sumMatrices1d(double t1[], double t2[]) {
		
		double res[] = new double[t1.length];
		Arrays.fill(res,0.0f);
		
		for(int i = 0 ; i < t1.length ; i++) {
			res[i] = (t1[i] + t2[i]);
		}
		
		return res;
	}

	public double[] tanh(double[] tab) {
		
		double res[] = new double[tab.length];
		
		for(int i = 0 ; i < tab.length ; i++) {
			res[i] = Math.tanh(tab[i]);
		}
		
		return res;
	}

	public double[] probaNext(double[] tab) {

		double res[] = new double[tab.length];
		
		double sum = 0.0f;
		
		for(int i = 0 ; i < tab.length ; i++) {
			sum += Math.exp(tab[i]);
		}
		
		//System.out.println("sum : "+sum);
		//System.out.println(" ");
		
		for(int i = 0 ; i < tab.length ; i++) {
			res[i] = Math.exp(tab[i]) / sum ;
		}
		
		return res;
	}

	public double[][] dotMatrices1d1dT(double[] t1, double[] t2) {
		
		double res[][] = new double[t1.length][t2.length];
		
		for(int i = 0 ; i < t1.length ; i++) {
			for(int j = 0 ; j < t2.length ; j++) {
				res[i][j] = (t1[i] * t2[j]);
			}
		}
		
		return res;
	}

	public double[][] sumMatrices2d(double[][] t1, double[][] t2) {
		
		//System.out.println("taille t1 "+t1.length+" "+t1[0].length);
		//System.out.println("taille t2 "+t2.length+" "+t2[0].length);
		
		double res[][] = new double[t1.length][t2[0].length];
		
		for(int i = 0 ; i < t1.length ; i++) {
			for(int j = 0 ; j < t2[0].length ; j++) {
				res[i][j] = (t1[i][j] + t2[i][j]);
			}
		}
		
		return res;
	}

	public double[][] transpose2D(double[][] t) {
		
		double res[][] = new double[t[0].length][t.length];
		
		for(int i = 0 ; i < t.length ; i++) {
			for(int j = 0 ; j < t[i].length ; j++) {
				res[j][i] = t[i][j];
			}
		}
		
		return res;
	}

	public double[] dotMatrices1d1d(double[] t1, double[] t2) {
		
		double res[] = new double[t1.length];
		
		for(int i = 0 ; i < t1.length ; i++) {
			res[i] = t1[i] * t2[i];
		}
		
		return res;
	}

	public double[] diffMatrices1d(double[] t1, double[] t2) {
		
		double res[] = new double[t1.length];
		Arrays.fill(res,0.0f);
		
		for(int i = 0 ; i < t1.length ; i++) {
			res[i] = (t1[i] - t2[i]);
		}
		
		return res;
	}

	public void clip(double[] tab, int min, int max) {
		
		for(int i = 0 ; i < tab.length ; i++) {
			if(tab[i] < min)
				tab[i] = min;
			else if(tab[i] > max)
				tab[i] = max;
		}
	}

	public void clip(double[][] tab, int min, int max) {
		
		for(int i = 0 ; i < tab.length ; i++) {
			for(int j = 0 ; j < tab[i].length ; j++) {
				if(tab[i][j] < min)
					tab[i][j] = min;
				else if(tab[i][j] > max)
					tab[i][j] = max;
			}
		}
	}

	public double[][] dotMatrices2d2d(double[][] t1, double[][] t2) {
		
		//System.out.println("taille t1 "+t1.length+" "+t1[0].length);
		//System.out.println("taille t2 "+t2.length+" "+t2[0].length);
		
		double res[][] = new double[t1.length][t2[0].length];
		
		for(int i = 0 ; i < t1.length ; i++) {
			for(int j = 0 ; j < t2[0].length ; j++) {
				res[i][j] = (t1[i][j] * t2[i][j]);
			}
		}
		
		return res;
	}
	
	public double[][] divMatrices2d2d(double[][] t1, double[][] t2) {
		
		//System.out.println("taille t1 "+t1.length+" "+t1[0].length);
		//System.out.println("taille t2 "+t2.length+" "+t2[0].length);
		
		double res[][] = new double[t1.length][t2[0].length];
		
		for(int i = 0 ; i < t1.length ; i++) {
			for(int j = 0 ; j < t2[0].length ; j++) {
				res[i][j] = (t1[i][j] / t2[i][j]);
			}
		}
		
		return res;
	}

	private double[] divMatrices1d1d(double[] t1, double[] t2) {

		double res[] = new double[t1.length];
		
		for(int i = 0 ; i < t1.length ; i++) {
			res[i] = (t1[i] / t2[i]);
		}
		
		return res;
	}

	public double[][] adagradUpdate(double[][] param, double learning_rate,
			double[][] dparam, double[][] mem) {
		
		double add[][] = this.addTerm(mem,0.00000001f);
		
		/*System.out.println("**** add ****");
		for(int i = 0 ; i < add.length ; i++) {
			for(int j = 0 ; j < add[0].length ; j++) {
				System.out.println("add : "+add[i][j]);
			}
		}*/
		
		double sqrt[][] = this.sqrt(add);
		
		/*System.out.println("**** sqrt ****");
		for(int i = 0 ; i < sqrt.length ; i++) {
			for(int j = 0 ; j < sqrt[0].length ; j++) {
				System.out.println("sqrt : "+sqrt[i][j]);
			}
		}*/
		
		double div [][] = this.divMatrices2d2d(dparam, sqrt);
		
		param = this.sumMatrices2d(param, this.dotTerm(div, -learning_rate) );
		
		//param  = this.divMatrices2d2d(dparam, sqrt);
		
		return param;
	}

	public double[] adagradUpdate(double[] param, double learning_rate,
			double[] dparam, double[] mem) {

		double add[] = this.addTerm(mem,0.00000001f);
		
		double sqrt[] = this.sqrt(add);
		
		double div[] = this.divMatrices1d1d(dparam, sqrt);
		
		param = this.sumMatrices1d(param, this.dotTerm(div, -learning_rate) );
		
		return param;
	}

	private double[] sqrt(double[] tab) {

		double res[] = new double[tab.length];
		
		for(int i = 0 ; i < tab.length ; i++) {
			res[i] = (Math.sqrt(tab[i]));
		}
		
		return res;
	}

	private double[][] sqrt(double[][] tab) {

		double res[][] = new double[tab.length][tab[0].length];
		
		for(int i = 0 ; i < tab.length ; i++) {
			for(int j = 0 ; j < tab[0].length ; j++) {
				res[i][j] = (Math.sqrt(tab[i][j]));
				//System.out.println("res before sqrt : "+tab[i][j]);
				//System.out.println("res sqrt : "+res[i][j]);
			}
		}
		
		return res;
	}

	private double[][] addTerm(double[][] tab, float f) {
		
		double res[][] = new double[tab.length][tab[0].length];
		
		for(int i = 0 ; i < tab.length ; i++) {
			for(int j = 0 ; j < tab[0].length ; j++) {
				res[i][j] = (tab[i][j] + f);
			}
		}
		
		return res;
	}

	private double[] addTerm(double[] tab, float f) {

		double res[] = new double[tab.length];
		
		for(int i = 0 ; i < tab.length ; i++) {
			res[i] = (tab[i] + f);
		}
		
		return res;
	}
	

	private double[][] dotTerm(double[][] tab, double f) {
		
		double res[][] = new double[tab.length][tab[0].length];
		
		for(int i = 0 ; i < tab.length ; i++) {
			for(int j = 0 ; j < tab[0].length ; j++) {
				res[i][j] = (tab[i][j] * f);
			}
		}
		
		return res;
	}

	private double[] dotTerm(double[] tab, double f) {

		double res[] = new double[tab.length];
		
		for(int i = 0 ; i < tab.length ; i++) {
			res[i] = (tab[i] * f);
		}
		
		return res;
	}
	
	public int indexOfProbaMax(double[] tab) {
		
		int maxIndex = 0;
		for (int i = 1; i < tab.length; i++){
		
			double newnumber = tab[i];
			if ((newnumber > tab[maxIndex])){
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	public int randomChoice(double[] tab) {
		
		int maxIndex = 0;
		double randNumber = Math.random();
		
		double sumProba1 = 0.0f;
		double sumProba2 = 0.0f;
		
		//System.out.println("random : "+randNumber);
		
		for (int i = 1; i < tab.length; i++){
		
			sumProba1 += tab[i];
			
			if ((sumProba1 >= randNumber) && (sumProba2 < randNumber)){
				return i;
			}
			
			sumProba2 += tab[i];
			
		}
		return 0;
	}
}
