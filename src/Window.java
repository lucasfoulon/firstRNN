import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Window extends JFrame implements KeyListener {

	private JPanel pan = new JPanel();
	private JButton bouton = new JButton("Mon bouton");
	private JSlider slider = new JSlider();
	private JLabel ecran = new JLabel();
	private Boolean on = false;
	private int valueCursor = 0;
	
	private JLabel label = new JLabel("Valeur actuelle : 0");
	
	public Window() {

		JFrame fenetre = new JFrame();
		this.setTitle("first NN");
	    this.setSize(400, 200);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    bouton.addActionListener(new ButtonOnListener());
	    
	    pan.add(bouton);

	    JPanel panEcran = new JPanel();
	    panEcran.setPreferredSize(new Dimension(220, 30));
	    pan.add(panEcran, BorderLayout.SOUTH);
	    panEcran.add(ecran);
	    
	    //slide.setLocation(400,0);
	    this.setLocation(0,0);
	    
	    //JSlider slider = new JSlider();
	    
	    slider.setMaximum(100);
		slider.setMinimum(0);
		slider.setValue(0);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMinorTickSpacing(10);
		slider.setMajorTickSpacing(20);
		slider.addChangeListener(new ChangeListener(){
			
			public void stateChanged(ChangeEvent event){
				label.setText("Valeur actuelle : " + ((JSlider)event.getSource()).getValue());
				valueCursor = ((JSlider)event.getSource()).getValue();
			}
		});      
	    this.setContentPane(pan);

		this.getContentPane().add(label, BorderLayout.SOUTH); 
		this.getContentPane().add(slider, BorderLayout.CENTER);
		//this.getContentPane().add(label, BorderLayout.SOUTH);     
	    
	    //slide.setVisible(true);
	    this.setVisible(true);
	    addKeyListener(this);
	    requestFocus();
	}
	
	public double getOn() {
		if(this.on)
			return 1.0f;
		else
			return 0.0f;
	}
	
	public double getValueCursor() {
		if(this.valueCursor > 20)
			return 1.0f;
		else
			return 0.0f;
	}
	
	class ButtonOnListener implements ActionListener {
		
		//private Boolean on = false;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(on) {
			    bouton.setBackground(null);
				on = false;
			}
			else {
			    bouton.setBackground(Color.yellow);
				on = true;
			}
		}

	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {        
		//ecran.setText("Touche pressée : " + arg0.getKeyCode() + 
        //    " (" + arg0.getKeyChar() + ")");
		if(arg0.getKeyChar() == 'a')
			bouton.doClick();
		if(arg0.getKeyChar() == 'p')
			slider.setValue(30);
		if(arg0.getKeyChar() == 'n')
			slider.setValue(10);
	}
	@Override
	public void keyReleased(KeyEvent arg0) {
		
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
		
	}

	public void setLum(int etatLum) {
		if(etatLum == 0) {
		    bouton.setBackground(null);
			on = false;
		}
		else {
		    bouton.setBackground(Color.yellow);
			on = true;
		}
	}
}
