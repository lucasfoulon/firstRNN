import java.util.Arrays;


public class RNN {
	
	private int taille_data = 80;
	private int vocab_size = 5;
	
	private int hidden_size = 10;
	private int seq_lenght = 5;
	private double learning_rate = 0.1f;
	
	double Wxh[][] = new double[hidden_size][vocab_size];
	double Whh[][] = new double[hidden_size][hidden_size];
	double Why[][] = new double[vocab_size][hidden_size];
	double bh[] = new double[hidden_size];
	double by[] = new double[vocab_size];
	
	int n = 0;
	int p = 0;
	
	/* Adagrad */
	double mWxh[][] = new double[hidden_size][vocab_size];
	double mWhh[][] = new double[hidden_size][hidden_size];
	double mWhy[][] = new double[vocab_size][hidden_size];
	double mbh[] = new double[hidden_size];
	double mby[] = new double[vocab_size];
	
	/* backpass */
	double dWxh[][] = new double[hidden_size][vocab_size];
	double dWhh[][] = new double[hidden_size][hidden_size];
	double dWhy[][] = new double[vocab_size][hidden_size];
	double dbh[] = new double[hidden_size];
	double dby[] = new double[vocab_size];
	double dhnext[];
	double hprev[] = new double[hidden_size];
	
	double hs[][] = new double[this.seq_lenght+1][this.hidden_size];
	
	double ys[][] = new double[seq_lenght][hidden_size];
	double ps[][] = new double[seq_lenght][hidden_size];
	
	double smooth_loss;
	double loss = 0.0f;
	
	RandomGaussian gaussian = new RandomGaussian();		
	/* pour les equations de matrice */
	Matrice matrice = new Matrice();
	
	public RNN() {
		
		double MEAN = 0.0f; 
	    double VARIANCE = 1.0f;
		
		//this.taille_data = t_d;
		//this.vocab_size = v_s;
		
		//this.Wxh = new double[hidden_size][vocab_size];
		gaussian.getTabGaussian(this.Wxh,hidden_size,vocab_size,MEAN,VARIANCE);
		gaussian.getTabGaussian(this.Whh,hidden_size,hidden_size,MEAN,VARIANCE);
		gaussian.getTabGaussian(this.Why,vocab_size,hidden_size,MEAN,VARIANCE);

		/*for(int i = 0 ; i < this.Wxh.length ; i++) {
			for(int j = 0 ; j < this.Wxh[0].length ; j++) {
				System.out.println("Wxh : "+this.Wxh[i][j]);
			}
		}*/
		
		this.bh = new double[hidden_size];
		Arrays.fill(this.bh,0.0f);
		this.by = new double[vocab_size];
		Arrays.fill(this.by,0.0f);
		/*for(int i = 0 ; i < hidden_size ; i++) {
			  for(int j = 0 ; j < vocab_size ; j++)
				  System.out.println(Wxh[i][j]);
		}*/
		
		//this.mWxh = gaussian.getTabZero(hidden_size,vocab_size);
		//this.mWhh = gaussian.getTabZero(hidden_size,hidden_size);
		//this.mWhy = gaussian.getTabZero(vocab_size,hidden_size);
		gaussian.getTabZero(this.mWxh,hidden_size,vocab_size);
		gaussian.getTabZero(this.mWhh,hidden_size,hidden_size);
		gaussian.getTabZero(this.mWhy,vocab_size,hidden_size);
		//this.mbh = new double[hidden_size];
		Arrays.fill(mbh,0.0f);
		//this.mby = new double[vocab_size];
		Arrays.fill(mby,0.0f);
		
		this.smooth_loss = - Math.log( (1.0f) / (this.vocab_size) ) * this.seq_lenght;
		System.out.println("1er smooth loss : " + this.smooth_loss);
	}
	
	public void lancerAppr(int nbrIte, double data[]) {
		
		double hprev[] = new double[hidden_size];
		//Arrays.fill(by,0.0f); Pourquoi??
		
		while (n<nbrIte) {
			
			if( (this.p+this.seq_lenght+1) >= this.taille_data || this.n == 0 ) {
				/* Reset RNN memory */
				Arrays.fill(hprev,0.0f);
				p = 0;
			}
			
			double inputs[];
			inputs = Arrays.copyOfRange(data,this.p,this.p+this.seq_lenght);
			double targets[];
			targets = Arrays.copyOfRange(data,this.p+1,this.p+this.seq_lenght+1);
			
			if(n % 100 == 0) {
				double sample[] = sample(this.hprev, inputs[0], 22);
				System.out.println("");
				for(int t = 0 ; t < sample.length ; t++) {
					System.out.print((int)sample[t]+"/");
				}
				System.out.println("");
			}
			
			lossFun(inputs,targets,hprev);
			
			smooth_loss = smooth_loss * 0.999 + loss * 0.001;
			
			if(n%100 == 0)
				System.out.println("Smooth loss "+smooth_loss);
			
			/* Wxh, dWxh, mWxh */
			this.mWxh = matrice.sumMatrices2d(this.mWxh, matrice.dotMatrices2d2d(this.dWxh, this.dWxh));
			this.Wxh = matrice.adagradUpdate(this.Wxh,this.learning_rate,this.dWxh,this.mWxh);

			/* Whh, dWhh, mWhh */
			this.mWhh = matrice.sumMatrices2d(this.mWhh, matrice.dotMatrices2d2d(this.dWhh, this.dWhh));
			this.Whh = matrice.adagradUpdate(this.Whh,this.learning_rate,this.dWhh,this.mWhh);
			
			/* Why, dWhy, mWhy */
			this.mWhy = matrice.sumMatrices2d(this.mWhy, matrice.dotMatrices2d2d(this.dWhy, this.dWhy));
			this.Why = matrice.adagradUpdate(this.Why,this.learning_rate,this.dWhy,this.mWhy);
			
			/* bh, dbh, mbh */
			this.mbh = matrice.sumMatrices1d(this.mbh, matrice.dotMatrices1d1d(this.dbh, this.dbh));
			this.bh = matrice.adagradUpdate(this.bh,this.learning_rate,this.dbh,this.mbh);
			
			/* by, dby, mby */
			this.mby = matrice.sumMatrices1d(this.mby, matrice.dotMatrices1d1d(this.dby, this.dby));
			this.by = matrice.adagradUpdate(this.by,this.learning_rate,this.dby,this.mby);
			
			this.p += this.seq_lenght;
			this.n++;
		}
		
	}
	
	private double[] sample(double[] h, double seed_ix, int n) {
		
		double x[] = new double[this.vocab_size];
		Arrays.fill(x,0.0f);
		x[(int) seed_ix] = 1.0f;
		
		double ixes[] = new double[n];
		
		System.out.println("début choix");
		
		for(int t = 0 ; t < n ; t++) {
			
			double sum1[] = matrice.dotMatrices2d1d(this.Wxh, x);
			double sum2[] = matrice.dotMatrices2d1d(this.Whh, h); //premiere fois

			double sum[] = matrice.sumMatrices1d(sum1, matrice.sumMatrices1d(sum2,this.bh));
			
			h = matrice.tanh(sum);
			
			double y[] = matrice.sumMatrices1d(matrice.dotMatrices2d1d(this.Why, h), this.by);
			
			double p[] = matrice.probaNext(y);
			
			/*for(int i = 0 ; i < p.length ; i++)
				System.out.println("proba "+i+" : "+p[i]);*/
			
			int index = matrice.randomChoice(p);
			
			ixes[t] = (double)index;
			
			//System.out.println(index);
			
			Arrays.fill(x,0.0f);
			x[index] = 1.0f;
		}
		
		System.out.println("\nfin choix");
		
		return ixes;
	}

	public void lossFun(double inputs[], double targets[], double hprev[]){
		
		/* ATTENTION pour hs penser à pas mettre de +1 deux fois */
		//double hs[][] = new double[this.seq_lenght+1][this.hidden_size];
		hs[0] = Arrays.copyOf(hprev, hprev.length);
		
		gaussian.getTabZero(ys,this.seq_lenght,this.hidden_size);
		gaussian.getTabZero(ps,this.seq_lenght,this.hidden_size);
		
		this.loss = 0.0f;
		
		double xs[][] = new double[this.seq_lenght][this.vocab_size];
		
		/* forward pass */
		for(int i = 0 ; i < this.seq_lenght ; i++) {
			Arrays.fill(xs[i],0.0f);
			xs[i][(int)inputs[i]] = 1;
			
			double sum1[] = matrice.dotMatrices2d1d(this.Wxh, xs[i]);
			double sum2[] = matrice.dotMatrices2d1d(this.Whh, hs[i]); //premiere fois
			
			double sum[] = matrice.sumMatrices1d(sum1, matrice.sumMatrices1d(sum2,this.bh));
			
			hs[i+1] = matrice.tanh(sum);
			//System.out.println("taille de hs i+1 "+hs[i+1].length);
			//System.out.println("taille de  Why "+this.Why.length);
			//System.out.println("taille de  Why i "+this.Why[0].length);
			//System.out.println("contenu "+hs[i+1][6]);
			
			ys[i] = matrice.sumMatrices1d(matrice.dotMatrices2d1d(this.Why, hs[i+1]), this.by);

			/*for(int k = 0 ; k < by.length ; k++)
				System.out.println(by[k]);
			for(int k = 0 ; k < ys[i].length ; k++)
				System.out.println(ys[i][k]);*/
			//System.out.println(matrice.probaNext(ys[i]));
			ps[i] = matrice.probaNext(ys[i]);
			
			//System.out.println(Math.log(Math.exp(4)));
			
			//System.out.println(ps[i][(int)targets[i]]);
			//System.out.println(-Math.log(ps[i][(int)targets[i]]));
			this.loss += -Math.log(ps[i][(int)targets[i]]);
		}
		
		/* Backpass : calcul gradient */
		gaussian.getTabZero(this.dWxh,this.hidden_size,this.vocab_size);
		gaussian.getTabZero(this.dWhh,this.hidden_size,this.hidden_size);
		gaussian.getTabZero(this.dWhy,this.vocab_size,this.hidden_size);
		this.dbh = new double[this.hidden_size];
		Arrays.fill(dbh,0.0f);
		this.dby = new double[this.vocab_size];
		Arrays.fill(dby,0.0f);
		this.dhnext = new double[hs[0].length];
		Arrays.fill(dhnext,0.0f);
		
		for(int i = (this.seq_lenght-1) ; i >= 0 ; i--) {
			double dy[] = Arrays.copyOf(ps[i], ps[i].length);
			dy[(int)targets[i]] -= 1;
			this.dWhy = matrice.sumMatrices2d(this.dWhy, matrice.dotMatrices1d1dT(dy, hs[i+1]));
			this.dby = matrice.sumMatrices1d(this.dby, dy);
			
			double dh[] = matrice.sumMatrices1d(matrice.dotMatrices2d1d(matrice.transpose2D(this.Why), dy), dhnext);
			
			/*for(int j = 0 ; j < dh.length ; j++) {
				System.out.println(""+dh[j]);
			}*/
			
			/*System.out.println("**** hs **** "+i);
			for(int k = 0 ; k < hs[i].length ; k++) {
				System.out.println("hs : "+hs[i][k]);
			}
			
			System.out.println("**** dh **** "+i);
			for(int k = 0 ; k < dh.length ; k++) {
				System.out.println("dh : "+dh[k]);
			}*/
			
			double hsCarre [] = matrice.dotMatrices1d1d(hs[i+1], hs[i+1]);
			
			/*System.out.println("**** hs carre **** "+i);
			for(int k = 0 ; k < hsCarre.length ; k++) {
				System.out.println("hsCarre : "+hsCarre[k]);
			}*/
			
			double mat1[] = new double[hidden_size];
			Arrays.fill(mat1,1.0f);
			
			double unMoinsHsCarre[] = matrice.diffMatrices1d(mat1,hsCarre);
			
			/*System.out.println("**** unMoinsHsCarre **** "+i);
			for(int k = 0 ; k < unMoinsHsCarre.length ; k++) {
				System.out.println("unMoinsHsCarre : "+unMoinsHsCarre[k]);
			}*/
			
			double dhraw[] = matrice.dotMatrices1d1d(dh,unMoinsHsCarre);
			
			/*System.out.println("**** dhraw **** "+i);
			for(int k = 0 ; k < dhraw.length ; k++) {
				System.out.println("dhraw : "+dhraw[k]);
			}*/
			
			this.dbh = matrice.sumMatrices1d(this.dbh, dhraw);
			
			/*System.out.println("**** 1 ****");
			for(int k = 0 ; k < this.dWxh.length ; k++) {
				for(int j = 0 ; j < this.dWxh[0].length ; j++) {
					System.out.println("dWxh : "+this.dWxh[k][j]);
				}
			}*/
			
			/*System.out.println("**** xs **** "+i);
			for(int k = 0 ; k < xs[i].length ; k++) {
				System.out.println("xs i : "+xs[i][k]);
			}
			

			System.out.println("**** dhraw **** "+i);
			for(int k = 0 ; k < dhraw.length ; k++) {
				System.out.println("dhraw : "+dhraw[k]);
			}*/
			
			
			this.dWxh = matrice.sumMatrices2d(this.dWxh, matrice.dotMatrices1d1dT(dhraw, xs[i]));
			/*System.out.println("**** 1 ****");
			for(int k = 0 ; k < this.dWxh.length ; k++) {
				for(int j = 0 ; j < this.dWxh[0].length ; j++) {
					System.out.println("dWxh : "+this.dWxh[k][j]);
				}
			}*/
			this.dWhh = matrice.sumMatrices2d(this.dWhh, matrice.dotMatrices1d1dT(dhraw, hs[i])); //deuxieme fois
			this.dhnext = matrice.dotMatrices2d1d(matrice.transpose2D(this.Whh), dhraw);
		}
		
		matrice.clip(this.dWxh,-5,5);
		matrice.clip(this.dWhh,-5,5);
		matrice.clip(this.dWhy,-5,5);
		matrice.clip(this.dbh,-5,5);
		matrice.clip(this.dby,-5,5);
		
		hprev = hs[inputs.length-1];
		//System.out.println(Math.tanh(-0.02195565));
	}
	
	public double allumerLum(double[] seed_ix, int n) {
		
		double[] h = Arrays.copyOf(this.hprev, this.hprev.length);
		
		double x[] = new double[this.vocab_size];
		Arrays.fill(x,0.0f);
		x[(int) seed_ix[0]] = 1.0f;
		
		double ixes = 0.5f;
		
		System.out.println("début choix");
		
		for(int t = 0 ; t < n-1 ; t++) {
			
			double sum1[] = matrice.dotMatrices2d1d(this.Wxh, x);
			double sum2[] = matrice.dotMatrices2d1d(this.Whh, h); //premiere fois

			double sum[] = matrice.sumMatrices1d(sum1, matrice.sumMatrices1d(sum2,this.bh));
			
			h = matrice.tanh(sum);
			
			double y[] = matrice.sumMatrices1d(matrice.dotMatrices2d1d(this.Why, h), this.by);
			
			double p[] = matrice.probaNext(y);
			
			/*for(int i = 0 ; i < p.length ; i++)
				System.out.println("proba "+i+" : "+p[i]);*/
			
			int index = matrice.randomChoice(p);
			
			//ixes[t] = (double)index;
			
			//System.out.println(index);
			
			Arrays.fill(x,0.0f);
			x[(int) seed_ix[t+1]] = 1.0f;
		}
		
		double sum1[] = matrice.dotMatrices2d1d(this.Wxh, x);
		double sum2[] = matrice.dotMatrices2d1d(this.Whh, h); //premiere fois

		double sum[] = matrice.sumMatrices1d(sum1, matrice.sumMatrices1d(sum2,this.bh));
		
		h = matrice.tanh(sum);
		
		double y[] = matrice.sumMatrices1d(matrice.dotMatrices2d1d(this.Why, h), this.by);
		
		double p[] = matrice.probaNext(y);
		
		/*for(int i = 0 ; i < p.length ; i++)
			System.out.println("proba "+i+" : "+p[i]);*/
		
		int index = matrice.randomChoice(p);
		
		ixes = (double)index;
		
		//System.out.println(index);
		
		//Arrays.fill(x,0.0f);
		//x[(int) seed_ix[t+1]] = 1.0f;
		
		System.out.println("\nfin choix");
		
		return ixes;
	}

}
