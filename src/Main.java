import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    
	    Window win = new Window();
	    
	    double data[] = donnees();
	    
	    int i = 0;
	    
	    double d[] = new double[80];
	    
	    while(i<80){
	    	//System.out.println("Next float value: " + randomno.nextFloat());
	    	
	    	//System.out.println("valeur interrupteur : "+ win.getOn());
	    	//System.out.println("valeur slide : "+ win.getValueCursor());
	    	
	    	double sum = win.getOn() + win.getValueCursor()*2.0f;
	    	d[i] = sum;
	    	
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	i++;
	    }
	    
	    for(int j = 0 ; j < 80 ; j++) {
	    	System.out.println(d[j]);
	    }
	    
	    RNN rnn = new RNN();
	    rnn.lancerAppr(20002, data);
	    
	    List<Double> stockList = new ArrayList<Double>();
	    stockList.add(win.getOn() + win.getValueCursor()*2.0f);
	    
	    while(true){

		    stockList.add(win.getOn() + win.getValueCursor()*2.0f);
		    
		    double[] stockArr = new double[stockList.size()];
		    
		    int k = 0;
		    for(double s : stockList) {
		    	stockArr[k] = s;
		    	k++;
		    }

		    for(double s : stockArr)
		        System.out.println(s);
		    
		    double lum = rnn.allumerLum(stockArr, stockArr.length);
		    
		    int etatLum = (int) (lum%2);
		    
		    win.setLum(etatLum);
	    	
	    	try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}

	private static double[] donnees() {
		
		double d[] = new double[80];
		
		Charset encoding = Charset.defaultCharset();
		
		String filename = "/home/luk/Bureau/test_python/test_lum.txt";
        //for (String filename : args) {
            File file = new File(filename);
            try {
				handleFile(file, encoding, d);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        //}
		
		return d;
	}
	
	private static void handleFile(File file, Charset encoding, double d[])
            throws IOException {
        try (InputStream in = new FileInputStream(file);
             Reader reader = new InputStreamReader(in, encoding);
             // buffer for efficiency
             Reader buffer = new BufferedReader(reader)) {
            handleCharacters(buffer,d);
        }
    }

    private static void handleCharacters(Reader reader, double d[])
            throws IOException {
        int r;
        int cmpt = 0;
        while ((r = reader.read()) != -1) {
            char ch = (char) r;
            //System.out.println("Do something with " + ch);
            switch (ch) {
            case 'a':  
            	d[cmpt] = 0.0f;
                break;
            case 'b':  
            	d[cmpt] = 2.0f;
                break;
            case 'c':  
            	d[cmpt] = 3.0f;
                break;
            case 'd':  
            	d[cmpt] = 1.0f;
                break;
            default: 
            	d[cmpt] = 4.0f;
                break;
            }
            cmpt++;
        }
    }
}
