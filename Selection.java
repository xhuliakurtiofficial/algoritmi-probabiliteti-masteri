import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*; 

public class Selection implements ActionListener{
	//krijimi i dritare
	JFrame frame = new JFrame();
	//qe do mbaje panelat me elementet
	JPanel p1 = new JPanel();
	JPanel p2 = new JPanel();
	JPanel p3 = new JPanel();
	JPanel p4 = new JPanel();
	
	//krijim i fushave dhe emertimet e tyre
	JLabel headingLabel = new JLabel("Algoritmat e Skedulimit", SwingConstants.CENTER);
	JTextField arrivalTime = new JTextField(20);
	JTextField burstTime = new JTextField(20);
	JTextField priorityField = new JTextField(20);
	JLabel quantumLabel = new JLabel("Quantum");
	JSpinner quantumSpinner = new JSpinner();
	JLabel errorLabel = new JLabel("Input error", SwingConstants.CENTER);
	
	JButton calculateButton = new JButton("Llogarit");
	//krijon menune dropdown per perzgjedhjen e alg
	String [] algorithmStrings = {"1. Shortest Job First (SJF)", "2. First In First Out (FIFO)", "3. Prioritet", "4. Round Robin (RR)"};
	JComboBox algorithmsBox = new JComboBox(algorithmStrings);
	
	Font basicFont = new Font("Nirmala UI",Font.BOLD, 15);
	Border border, margin = new EmptyBorder(15, 15, 15, 15);
	

	Selection() {
		//rregullon paraqitjes
		arrivalTime.setBorder(new TitledBorder("Koha e Arritjes"));
		border = arrivalTime.getBorder(); // add padding to border
		arrivalTime.setBorder(new CompoundBorder(border, margin));
		
		burstTime.setBorder(new TitledBorder("Koha e Ekzekutimit"));
		border = burstTime.getBorder(); // add padding to border
		burstTime.setBorder(new CompoundBorder(border, margin));
		
		priorityField.setBorder(new TitledBorder("Prioriteti (Opsionale)"));
		border = priorityField.getBorder(); // add padding to border
		priorityField.setBorder(new CompoundBorder(border, margin));
		
		quantumSpinner.setValue(0);
		
		algorithmsBox.setBackground(Color.white);
		headingLabel.setFont(new Font("Nirmala UI", Font.BOLD, 20));
		
		calculateButton.addActionListener(this);
		errorLabel.setVisible(false);
		
		//bashkimi i elementeve ne nje layout te thjeshte
		p1.setLayout(new GridLayout(3, 1, 5, 10));
		p1.add(arrivalTime);
		p1.add(burstTime);
		p1.add(priorityField);
		
		p2.setLayout(new BorderLayout(5, 10));
		p2.add(headingLabel, BorderLayout.NORTH);
		p2.add(algorithmsBox, BorderLayout.SOUTH);
		
		p3.setLayout(new BorderLayout(10, 20));
		p3.add(errorLabel, BorderLayout.NORTH);
		p3.add(quantumLabel, BorderLayout.WEST);
		p3.add(quantumSpinner, BorderLayout.CENTER);
		p3.add(calculateButton, BorderLayout.SOUTH);
		
		p4.setLayout(new BorderLayout(5, 10));
		p4.add(p2, BorderLayout.NORTH);
		p4.add(p1, BorderLayout.CENTER);
		p4.add(p3, BorderLayout.SOUTH);
		p4.setBorder(margin);
		

		//rregullimi i dritares kryesore
		frame.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
		frame.add(p4);
		frame.setTitle("Algoritmet e Skedulimit");
		frame.setSize(450, 520);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);//coz we dont need the window to be resizable
		
	}
	public void actionPerformed(ActionEvent e)
	{ 
		//nqs klikohet butoni per llogaritjen ruajme te dhenat
	    String arrivalString = arrivalTime.getText();
	    String burstString = burstTime.getText();
	    String priorityString = priorityField.getText();
	    String algChosen = String.valueOf(algorithmsBox.getSelectedItem());
	    int selected = algChosen.charAt(0) - '0';
	    int quantumValue = (int)quantumSpinner.getValue();

	    System.out.println("selected: " + selected);

	    int lengthArrival = 0, lengthBurst = 0, lengthPriority = 0;
	    boolean badInput = false;

	    //kontrollon inputin nga useri per shkronja ose gjatesi te !=
	    for (String word : arrivalString.split("\\s+"))
	    {
	        lengthArrival++;
	    }

	    for (String word : burstString.split("\\s+"))
	    {
	        lengthBurst++;
	    }

	    if (arrivalString.isEmpty() || burstString.isEmpty()|| lengthArrival != lengthBurst)
	    { //gjatesi te != ose bosh
	        errorLabel.setForeground(Color.RED);
	        errorLabel.setVisible(true);
	        System.out.println("Here" + arrivalString.length());
	        System.out.println("Here" + arrivalString.length());
	        badInput = true;
	    }
	    else
	    {

	        for (int i = 0; i < lengthArrival; i++)
	        {
	            char valueArrival = arrivalString.charAt(i);

	            //nqs inputi nuk ka vtm numra dhe hapsira
	            if (!Character.isDigit(valueArrival) && valueArrival != ' ')
	            {
	                errorLabel.setForeground(Color.RED);
	                errorLabel.setVisible(true);
	                badInput = true;
	            }
	        }

	        for (int i = 0; i < lengthBurst; i++)
	        {
	            char valueBurst = burstString.charAt(i);

	            //if input is not nr
	            if (!Character.isDigit(valueBurst) && valueBurst != ' ')
	            {
	                errorLabel.setForeground(Color.RED);
	                errorLabel.setVisible(true);
	                badInput = true;
	            }
	        }

	        if (selected == 3)
	        {
	            for (String word : priorityString.split("\\s+"))
	            {
	                System.out.println("WORD LOOP");
	                lengthPriority++;
	            }

	            if (priorityString.isEmpty() || lengthPriority != lengthBurst)
	            {
	            	System.out.println("IF LENGTH");
	                errorLabel.setForeground(Color.RED);
	                errorLabel.setVisible(true);
	                badInput = true;
	                return;
	            }

	            for (int i = 0; i < lengthPriority; i++)
	            {
	            	System.out.println(lengthPriority + "LOL FOR LOOP");
	                char valuePriority = priorityString.charAt(i);

	                if (!Character.isDigit(valuePriority) && valuePriority != ' ')
	                {
	                    System.out.println("HERE" + valuePriority);
	                    errorLabel.setForeground(Color.RED);
	                    errorLabel.setVisible(true);
	                    badInput = true;
	                }
	            }
	        }
	        
	        if (selected == 4 && quantumValue <= 0) {
	        	errorLabel.setText("Quantum jo <= 0");
	        	errorLabel.setForeground(Color.RED);
                errorLabel.setVisible(true);
                badInput = true;
            }
	        
	        if (lengthBurst > 15) {
	        	errorLabel.setText("Jo me shume se 15 procese");
	        	errorLabel.setForeground(Color.RED);
                errorLabel.setVisible(true);
                badInput = true;
            }
	        
	        //nqs inputi ka qene okay pra vetem numra dhe hapesira bosha, ruaje
	        if (!badInput)
            {
                Process[] valuesProcess = new Process[lengthArrival];
                int j = 0;
                for (int i = 0; i < lengthArrival; i++)
                {valuesProcess[i] = new Process();
                    valuesProcess[i].name = "P" + String.valueOf(i + 1);
                }

                for (String word : arrivalString.split("\\s+"))
                {
                    System.out.println(word);
                    valuesProcess[j].arrival = Integer.parseInt(word);
                    j++;
                }

                j = 0;
                for (String word : burstString.split("\\s+"))
                {
                    valuesProcess[j].burst = Integer.parseInt(word);
                    j++;
                }

                if (selected == 3)
                {
                	j= 0;
                    for (String word : priorityString.split("\\s+"))
                    {
                        System.out.println(word);
                        valuesProcess[j].priority = Integer.parseInt(word);
                        j++;
                    }
                }
                
                frame.dispose(); //mbyll dritaren pasi jane ruajtur te dhenat
                //i kalojme inputet te fqja e re qe afishon rezultatet
                AlgoritmiRezultati algoritmiRrezultati = new AlgoritmiRezultati(valuesProcess, selected, quantumValue);
            }
	    }
	}
}
