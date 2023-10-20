import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;


public class AlgoritmiRezultati implements ActionListener {
	JFrame frame = new JFrame();
	JPanel p1 = new JPanel();
	JPanel p2 = new JPanel();
	JPanel p3 = new JPanel();
	JPanel p4 = new JPanel();
	JPanel p5 = new JPanel();
	JPanel p6 = new JPanel();
	JPanel p7 = new JPanel();
	JPanel p8 = new JPanel();
	
	JLabel titleLabel;
	JLabel processLabel = new JLabel("Procesi", SwingConstants.CENTER);
	JLabel arrivalLabel = new JLabel("Arritja", SwingConstants.CENTER);
	JLabel burstLabel = new JLabel("Ekzekutimi", SwingConstants.CENTER);
	JLabel priorityLabel = new JLabel("Prioriteti", SwingConstants.CENTER);
	JLabel timeLabel;
	JLabel quantumLabel = new JLabel();
	JLabel ganttLabel = new JLabel("Diagrami Gantt:", SwingConstants.CENTER);
	JButton tryButton = new JButton("Provo tjeter");
	JButton doneButton = new JButton("Mbarova");

	Font basicFont = new Font("Nirmala UI",Font.BOLD, 15);
	Font miniHeadingFont = new Font("Nirmala UI",Font.BOLD, 17);
	Font headingFont = new Font("Nirmala UI", Font.BOLD, 25);
	Border border, borderRightGantt, margin1 = new EmptyBorder(5, 10, 5, 10);
	
	String algString, bur;
	int al;
	
	//ruan inputet e alg ne array
	Process [] values, ganttDiagram;
	//per ruajtjen e rezultateve te gantt * kohes se pritjes, perfundimit
	int length, sum = 0, max, lengthRrGantt = 0, quantum, selectedAlg = 0;
	double sumWaiting = 0, waitingTime = 0, sumEnding = 0, endingTime = 0;
	
	AlgoritmiRezultati(Process [] valuesProcess, int selected, int quantumValue) {
		//ruajme inputin e marr
		length = valuesProcess.length;
		values = new Process[length];
		quantum = quantumValue;
		selectedAlg = selected;
		
		//border per paraqitjen
		border = BorderFactory.createLineBorder(Color.black, 1);
		borderRightGantt = BorderFactory.createLineBorder(Color.black, 0);
		
		//ne baze te opsionit te selektuar nga menuja
		//therrasim alg perkates
		if (selected == 1 || selected == 2) {
			sjfFifoGUI(valuesProcess);
		}
		else if (selected == 3) {
			priorityGUI(valuesProcess);
		}
		else {
			rrGUI(valuesProcess);
		}
		
		//ruajme cilin buton klikohet
		tryButton.addActionListener(this);
		doneButton.addActionListener(this);
		
		//rregullojme gjatesine e window, per sjf, fifo e prioritet
		//do jene te njejta permasa
		//per rr me te madhe prej ganttit
		if (selected != 4) {
			if (length <= 4) {
				frame.setSize(500, 480);
			}
			else if (length > 4 && length <= 6) {
				frame.setSize(520, 550);
			}
			else if (length > 6 && length <= 9) {
				frame.setSize(550, 600);
			}
			else {
				frame.setSize(600, 750);
			}
		}
		frame.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
		frame.add(p7);
		frame.setTitle("Algoritmet e Skedulimit");
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	//nderron elemente kur nevojitet per renditjen e tyre 
	//te alg sjf, fifo, rr
	public void swapSjfFifoRR(Process a, Process b) {
		Process c = new Process();
		c.name = a.name;
		c.arrival = a.arrival;
		c.burst = a.burst;
		
		a.name = b.name;
		a.arrival = b.arrival;
		a.burst = b.burst;
		
		b.name = c.name;
		b.arrival = c.arrival;
		b.burst = c.burst;
	}
	
	//nderron elemente kur nevojitet per renditjen e tyre 
	//te alg prioritet, me vete pasi ka dhe nje fushe te re per prioritetin
	public void swapPriority(Process a, Process b) {
		Process c = new Process();
		c.name = a.name;
		c.arrival = a.arrival;
		c.priority = a.priority;
		c.burst = a.burst;
		
		a.name = b.name;
		a.arrival = b.arrival;
		a.priority = b.priority;
		a.burst = b.burst;
		
		b.name = c.name;
		b.arrival = c.arrival;
		b.priority = c.priority;
		b.burst = c.burst;
		
	}
	
	//alg sjf, ben renditjen ne baze te rregullave
	public void sjfGantt() {
		for (int i = 0; i < length - 1; i++) { 
			//rirendit ne baze te k.a, nese dy procese kane k.a te ==, rendit ne baze te k.e
			for (int j = i + 1; j < length; j++) {
				if (values[i].arrival > values[j].arrival) {
					swapSjfFifoRR(values[i], values[j]);
				}
				else if (values[i].arrival == values[j].arrival && values[i].burst > values[j].burst) {
				}
			}
		}
		//krijo hapsir per gantt me gjatesine
		ganttDiagram = new Process[length];
		//ruajme k.a e fundit per te pare a kane mberrit te gjitha proceset
		max = values[length - 1].arrival;
		
		for (int i = 0; i < length; i++) {
			//nqs jo te gjitha proceset kane mberritur
			if (sum <= max) {
				for (int j = i+1; j < length; j++) { 
					//nqs me shume se 1 proces kane mberritur, rendit ne baze te k.e
					if (values[j].burst < values[i].burst && values[j].arrival <= sum) { 
						swapSjfFifoRR(values[i], values[j]);
					}
				}

				if (i == 0) { //nqs procesi i pare smberrin te 0
					sum += values[i].arrival; 
				}
				
				sum += values[i].burst;
				ganttDiagram[i] = new Process();
				ganttDiagram[i].name = values[i].name;
				ganttDiagram[i].right = sum;
			}
			else { //nqs gjith proceset kane mberrit
				//rendit ne baze te k.e
				for (int l = i; l < length - 1; l++) {
					for (int h = l + 1; h < length; h++) {
						if (values[l].burst > values[h].burst) {
							swapSjfFifoRR(values[l], values[h]);
						}
					}
				}
				
				//ploteso diagramin gantt
				for (int a = i; a < length; a++) {
					sum += values[a].burst;
					ganttDiagram[a] = new Process();
					ganttDiagram[a].name = values[a].name;
					ganttDiagram[a].right = sum;
				}
				break;
			}
		}
	}
	
	public void sjfFifoWaiting(Process [] valuesProcess) {
		//llogarit kohen e pritjes per sjf, fifo
		for (int i = 0; i < length - 1; i++) { //-1 se mbledh vtm majtas
			//konverton emrin e procesit (pjesen nr si p1) ne nr per index
			int index = ganttDiagram[i + 1].name.charAt(1) - '0'; 
			sumWaiting += ganttDiagram[i].right - valuesProcess[index - 1].arrival; //-1 se array
		}
		waitingTime = sumWaiting / length;	
	}
	
	public void sjfFifoEnding(Process [] valuesProcess) {
		//llogarit kohen e perfundimit per sjf, fifo
		for (int i = 0; i < length; i++) {
			int index = ganttDiagram[i].name.charAt(1) - '0';
			sumEnding += ganttDiagram[i].right - valuesProcess[index - 1].arrival; //-1 se array
		}
		endingTime = sumEnding / length;	
	}

	//alg fifo, ben renditjen ne baze te rregullave
	public void fifoGantt() {
		for (int i = 0; i < length - 1; i++) {
			for (int j = i + 1; j < length; j++) {
				//vetem ne baze te k.a se fifo; behet renditja
				if (values[i].arrival > values[j].arrival) {
					swapSjfFifoRR(values[i], values[j]);
				}
			}
		}
		//krijon hapesiren per gantt
		ganttDiagram = new Process[length];
		
		for (int i = 0; i < length; i++) {
			if (i == 0) { //nese procesi i pare nk arrin ne 0 k.a
				sum += values[i].arrival; 
			}
			
			//ploteso diagramin gantt
			sum += values[i].burst;
			ganttDiagram[i] = new Process();
			ganttDiagram[i].name = values[i].name;
			ganttDiagram[i].right = sum;
		}
	}

	//alg prioritet, ben renditjen ne baze te rregullave
	public void priorityGantt() {
		for (int i = 0; i < length - 1; i++) {
			for (int j = i + 1; j < length; j++) {
				//ne baze te prioritetit me te >
				if (values[i].priority < values[j].priority) {
					swapPriority(values[i], values[j]);
				}
			}
		}
		//krijon hapesiren per gantt
		ganttDiagram = new Process[length];
		for (int i = 0; i < length; i++) {
			if (i == 0) { //nese nuk arrin p1 ne 0 k.a
				sum += values[i].arrival; 
			}
			
			//ploteso diagramin gantt
			sum += values[i].burst;
			ganttDiagram[i] = new Process();
			ganttDiagram[i].name = values[i].name;
			ganttDiagram[i].right = sum;
		}
	}
	
	public void priorityWaiting(Process [] valuesProcess) {
		//llogarit kohen e pritjes per prioritet alg
		for (int i = 0; i < length - 1; i++) { 
			if (i == 0) {
				//konverton emrin e procesit (pjesen nr si p1) ne nr per index
				int index = ganttDiagram[i].name.charAt(1) - '0';
				sumWaiting += valuesProcess[index - 1].arrival;
			}
			sumWaiting += ganttDiagram[i].right; 
		}
		
		waitingTime = sumWaiting / length;	
	}
	
	public void priorityEnding(Process [] valuesProcess) {
		//llogarit kohen e perfundimit per prioritetin
		for (int i = 0; i < length; i++) { 
			sumEnding += ganttDiagram[i].right;
		}
		endingTime = sumEnding / length;	
	}
	
	//alg rr, ben renditjen ne baze te rregullave
	public void rrGantt() {
		for (int i = 0; i < length - 1; i++) {
			for (int j = i + 1; j < length; j++) {
				//rirendit ne baze te k.a
				if (values[i].arrival > values[j].arrival) {
					swapSjfFifoRR(values[i], values[j]);
				}
			}
		}
		
		//percaktojme sa i gjate do jete gantt, ndryshe nga alg e tjere prej q
		for (int i = 0; i < length; i++) {
			int diff = values[i].burst;
			while (diff > 0) { 
				diff -= quantum;
				lengthRrGantt++;
			}
		}
		ganttDiagram = new Process[lengthRrGantt];
		
		int j = -1; //per vektorin a gantt
		for (int i = 0; i < length; i++) { //loops vektorin input fillestar
			if (j == 0) { //nqs k.a i par != 0
				sum += values[j].arrival; 
			}
			
			//kur k.e > 0
			if (values[i].burst > 0) {
				j++; //shtohet nje element te gantt
				//kontrollojme me quantumin, sa do mbetet
				if (values[i].burst >= quantum) {
					values[i].burst -= quantum;
					sum += quantum;
				}
				else if (values[i].burst < quantum) {
					sum += values[i].burst;
					values[i].burst = 0;
				}
				//e shtojme
				ganttDiagram[j] = new Process();
				ganttDiagram[j].name = values[i].name;
				ganttDiagram[j].right = sum;
			}
			
			//kur kemi shkuar ne fund te vektorit me vlerat fillestare
			//kalojme ne fillim per te pare a ka pra per tu shtuar te gantt
			if (i == length - 1 && j != lengthRrGantt - 1) {
				i = -1; //-1 e jo 0, se e rrit for loop vet
			}	
			
		}
	}
	
	public void rrEnding(Process [] valuesProcess) {
		//llogarit kohen e perfundimit per rr
		for (int i = 0; i < length; i++) {
			int index1 = i + 1; //nr e proceseve +1 se i e array
			int index2 = 0;
			
			for (int j = 0; j < lengthRrGantt; j++) {
				index2 = ganttDiagram[j].name.charAt(1) - '0'; 
				if (i + 1 == index2) {
					index1 = j;
				}
				
			}
			
			sumEnding += ganttDiagram[index1].right - valuesProcess[i].arrival; //-1 coz array
		}
		
		endingTime = sumEnding / length;
	}
	
	public void rrWaiting(Process [] valuesProcess) {
		//llogarit kohen e pritjes per rr
		for (int i = 0; i < length; i++) {
			int index1 = 0, index2 = 0, count = 0;
			for (int j = 0; j < lengthRrGantt; j++) {
				index2 = ganttDiagram[j].name.charAt(1) - '0';
				if (i + 1 == index2 && j != 0) {
					if (count == 0) {
						sumWaiting += ganttDiagram[j - 1].right - valuesProcess[i].arrival; 
					}
					else {
						sumWaiting += ganttDiagram[j - 1].right - ganttDiagram[index1].right; 
					}
					count++;
					index1 = j;
				}
				else if (i + 1 == index2 && j == 0) {
					count++;
					index1 = j;
				}
			}
		}
		
		waitingTime = sumWaiting / length;	
	}
	
	//user interface per alg sjf, fifo 
	public void sjfFifoGUI(Process [] valuesProcess) {
		//kopjo vlerat input ne nje tjt array per ti modifikuar pa humbur origjinalet qe do afishohen ne forme tabele
		for (int i = 0; i < length; i++) {
			values[i] = new Process(valuesProcess[i].name, valuesProcess[i].arrival, valuesProcess[i].burst);
		}
		
		//titulli sjf apo fifo
		if (selectedAlg == 1) {
			titleLabel = new JLabel("Shortest Job First (SJF)", SwingConstants.CENTER);
			titleLabel.setFont(headingFont);
			sjfGantt();
		}
		else {
			titleLabel = new JLabel("First In First Out (FIFO)", SwingConstants.CENTER);
			titleLabel.setFont(headingFont);
			fifoGantt();
		}
		
		//llogariten rezultatet
		sjfFifoWaiting(valuesProcess);
		sjfFifoEnding(valuesProcess);
		
		//regullim i layout me panels
		p2.setLayout(new GridLayout(length+1, 3));
		processLabel.setFont(basicFont);
		processLabel.setBorder(border);
		processLabel.setBorder(new CompoundBorder(border, margin1));
		p2.add(processLabel);
		
		arrivalLabel.setFont(basicFont);
		arrivalLabel.setBorder(border);
		arrivalLabel.setBorder(new CompoundBorder(border, margin1));
		p2.add(arrivalLabel);
		
		burstLabel.setFont(basicFont);
		burstLabel.setBorder(border);
		burstLabel.setBorder(new CompoundBorder(border, margin1));
		p2.add(burstLabel);
		
		//krijojme tabelen me vlerat fillestare
		for (int i = 0; i < length; i++) { 
			JLabel pLabel = new JLabel(valuesProcess[i].name, SwingConstants.CENTER);
			pLabel.setBorder(border);
			pLabel.setBorder(new CompoundBorder(border, margin1));
			p2.add(pLabel);
			
			JLabel aLabel = new JLabel(String.valueOf(valuesProcess[i].arrival), SwingConstants.CENTER);
			aLabel.setBorder(border);
			aLabel.setBorder(new CompoundBorder(border, margin1));
			p2.add(aLabel);
			
			JLabel bLabel = new JLabel(String.valueOf(valuesProcess[i].burst), SwingConstants.CENTER);
			bLabel.setBorder(border);
			bLabel.setBorder(new CompoundBorder(border, margin1));
			p2.add(bLabel);
		}		
		
		//rezultatet e kohes e pritjes/perfundimit
		timeLabel = new JLabel("Pritja: " + String.valueOf(waitingTime) + ".   Perfundimi: " + String.valueOf(endingTime) + ".");
		timeLabel.setFont(basicFont);
		
		ganttLabel.setFont(miniHeadingFont);
		
		p5.setLayout(new GridLayout(1, length));
		//afishohet diagrami gantt katrorat
		for (int i = 0; i < length; i++) {
			JLabel pLabel = new JLabel(ganttDiagram[i].name, SwingConstants.CENTER);
			pLabel.setBorder(border);
			pLabel.setBorder(new CompoundBorder(border, margin1));
			p5.add(pLabel);
		}
		
		//afishohen vlerat nen diagramin gantt per cdo process
		p6.setLayout(new GridLayout(1, length+1));
		JLabel startGantt = new JLabel(String.valueOf(values[0].arrival), SwingConstants.LEFT); //jo 1, po arrival
		startGantt.setBorder(borderRightGantt);
		startGantt.setBorder(new CompoundBorder(borderRightGantt, margin1));
		p6.add(startGantt);
		for (int i = 0; i < length; i++) { 
			JLabel pLabel; 
			if (i == length - 1) {
				pLabel = new JLabel(String.valueOf(ganttDiagram[i].right), SwingConstants.RIGHT);
			}
			else {
				pLabel = new JLabel(String.valueOf(ganttDiagram[i].right), SwingConstants.CENTER);
			}
			pLabel.setBorder(borderRightGantt);
			pLabel.setBorder(new CompoundBorder(borderRightGantt, margin1));
			p6.add(pLabel);
			
		}
		
		//funx per rregullimin e paneleve te grupuara
		nestPanels();		
	}
	
	//user interface per alg prioritet
	public void priorityGUI(Process [] valuesProcess) {
		//kopjo vlerat input ne nje tjt array per ti modifikuar pa humbur origjinalet qe do afishohen ne forme tabele
		for (int i = 0; i < length; i++) {
			values[i] = new Process();
			values[i].name = valuesProcess[i].name;
			values[i].priority = valuesProcess[i].priority;
			values[i].arrival = valuesProcess[i].arrival;
			values[i].burst = valuesProcess[i].burst;
		}
		
		//titulli i window
		titleLabel = new JLabel("Prioriteti", SwingConstants.CENTER);
		titleLabel.setFont(headingFont);
		
		//llogariten rezultatet
		priorityGantt();
		priorityWaiting(valuesProcess);
		priorityEnding(valuesProcess);
		
		//rregullohet layout me panels
		p2.setLayout(new GridLayout(length+1, 4));
		processLabel.setFont(basicFont);
		processLabel.setBorder(border);
		processLabel.setBorder(new CompoundBorder(border, margin1));
		p2.add(processLabel);
		
		arrivalLabel.setFont(basicFont);
		arrivalLabel.setBorder(border);
		arrivalLabel.setBorder(new CompoundBorder(border, margin1));
		p2.add(arrivalLabel);
		
		burstLabel.setFont(basicFont);
		burstLabel.setBorder(border);
		burstLabel.setBorder(new CompoundBorder(border, margin1));
		p2.add(burstLabel);
		
		priorityLabel.setFont(basicFont);
		priorityLabel.setBorder(border);
		priorityLabel.setBorder(new CompoundBorder(border, margin1));
		p2.add(priorityLabel);
		
		//afishohet tabela me vlerat fillestare
		for (int i = 0; i < length; i++) {
			JLabel pLabel = new JLabel(valuesProcess[i].name, SwingConstants.CENTER);
			pLabel.setBorder(border);
			pLabel.setBorder(new CompoundBorder(border, margin1));
			p2.add(pLabel);
			
			JLabel aLabel = new JLabel(String.valueOf(valuesProcess[i].arrival), SwingConstants.CENTER);
			aLabel.setBorder(border);
			aLabel.setBorder(new CompoundBorder(border, margin1));
			p2.add(aLabel);
			
			JLabel bLabel = new JLabel(String.valueOf(valuesProcess[i].burst), SwingConstants.CENTER);
			bLabel.setBorder(border);
			bLabel.setBorder(new CompoundBorder(border, margin1));
			p2.add(bLabel);
			
			JLabel priorityValLabel = new JLabel(String.valueOf(valuesProcess[i].priority), SwingConstants.CENTER);
			priorityValLabel.setBorder(border);
			priorityValLabel.setBorder(new CompoundBorder(border, margin1));
			p2.add(priorityValLabel);
		}
		//perfundimi & pritja
		timeLabel = new JLabel("Pritja: " + String.valueOf(waitingTime) + ".   Perfundimi: " + String.valueOf(endingTime) + ".");
		timeLabel.setFont(basicFont);
		
		ganttLabel.setFont(miniHeadingFont);
		//afishohet diagrami gantt katrorat me emrat
		p5.setLayout(new GridLayout(1, length));
		for (int i = 0; i < length; i++) {
			JLabel pLabel = new JLabel(ganttDiagram[i].name, SwingConstants.CENTER);
			pLabel.setBorder(border);
			pLabel.setBorder(new CompoundBorder(border, margin1));
			p5.add(pLabel);
		}
		
		//afishohen vlerat nen diagramin gantt per cdo process
		p6.setLayout(new GridLayout(1, length+1));
		JLabel startGantt = new JLabel(String.valueOf(values[0].arrival), SwingConstants.LEFT); //jo 1, po arrival
		startGantt.setBorder(borderRightGantt);
		startGantt.setBorder(new CompoundBorder(borderRightGantt, margin1));
		p6.add(startGantt);
		for (int i = 0; i < length; i++) { 
			JLabel pLabel; 
			if (i == length - 1) {
				pLabel = new JLabel(String.valueOf(ganttDiagram[i].right), SwingConstants.RIGHT);
			}
			else {
				pLabel = new JLabel(String.valueOf(ganttDiagram[i].right), SwingConstants.CENTER);
			}
			pLabel.setBorder(borderRightGantt);
			pLabel.setBorder(new CompoundBorder(borderRightGantt, margin1));
			p6.add(pLabel);
			
		}
		//funx per rregullimin e paneleve te grupuara
		nestPanels();	
	}
	
	//user interface per alg rr
	public void rrGUI(Process [] valuesProcess) {
		//kopjo vlerat input ne nje tjt array per ti modifikuar pa humbur origjinalet qe do afishohen ne forme tabele
		for (int i = 0; i < length; i++) {
			values[i] = new Process(valuesProcess[i].name, valuesProcess[i].arrival, valuesProcess[i].burst);
		}
		
		//titulli i window
		titleLabel = new JLabel("Round Robin (RR)", SwingConstants.CENTER);
		titleLabel.setFont(headingFont);
		
		//llogariten rezultatet
		rrGantt();
		rrWaiting(valuesProcess);
		rrEnding(valuesProcess);
		
		//rregullohet layout me panels
		p2.setLayout(new GridLayout(length+1, 3));
		processLabel.setFont(basicFont);
		processLabel.setBorder(border);
		processLabel.setBorder(new CompoundBorder(border, margin1));
		p2.add(processLabel);
		
		arrivalLabel.setFont(basicFont);
		arrivalLabel.setBorder(border);
		arrivalLabel.setBorder(new CompoundBorder(border, margin1));
		p2.add(arrivalLabel);
		
		burstLabel.setFont(basicFont);
		burstLabel.setBorder(border);
		burstLabel.setBorder(new CompoundBorder(border, margin1));
		p2.add(burstLabel);
		
		//afishohet tabela me vlerat fillestare
		for (int i = 0; i < length; i++) {
			JLabel pLabel = new JLabel(valuesProcess[i].name, SwingConstants.CENTER);
			pLabel.setBorder(border);
			pLabel.setBorder(new CompoundBorder(border, margin1));
			p2.add(pLabel);
			
			JLabel aLabel = new JLabel(String.valueOf(valuesProcess[i].arrival), SwingConstants.CENTER);
			aLabel.setBorder(border);
			aLabel.setBorder(new CompoundBorder(border, margin1));
			p2.add(aLabel);
			
			JLabel bLabel = new JLabel(String.valueOf(valuesProcess[i].burst), SwingConstants.CENTER);
			bLabel.setBorder(border);
			bLabel.setBorder(new CompoundBorder(border, margin1));
			p2.add(bLabel);
		}		
		//quantumi, perfundimi & pritja
		timeLabel = new JLabel("Quantum: " + quantum + ".  Pritja: " + String.valueOf(waitingTime) + ".   Perfundimi: " + String.valueOf(endingTime) + ".");
		timeLabel.setFont(basicFont);
		
		ganttLabel.setFont(miniHeadingFont);
		//afishohet diagrami gantt katrorat me emrat
		p5.setLayout(new GridLayout(1, lengthRrGantt));
		for (int i = 0; i < lengthRrGantt; i++) {
			JLabel pLabel = new JLabel(ganttDiagram[i].name, SwingConstants.CENTER);
			pLabel.setBorder(border);
			pLabel.setBorder(new CompoundBorder(border, margin1));
			p5.add(pLabel);
		}
		
		//afishohen vlerat nen diagramin gantt per cdo process
		p6.setLayout(new GridLayout(1, length+1));
		JLabel startGantt = new JLabel(String.valueOf(values[0].arrival), SwingConstants.LEFT); //jo 1, po arrival
		startGantt.setBorder(borderRightGantt);
		startGantt.setBorder(new CompoundBorder(borderRightGantt, margin1));
		p6.add(startGantt);
		for (int i = 0; i < lengthRrGantt; i++) { 
			JLabel pLabel; 
			if (i == lengthRrGantt - 1) {
				pLabel = new JLabel(String.valueOf(ganttDiagram[i].right), SwingConstants.RIGHT);
			}
			else {
				pLabel = new JLabel(String.valueOf(ganttDiagram[i].right), SwingConstants.CENTER);
			}
			pLabel.setBorder(borderRightGantt);
			pLabel.setBorder(new CompoundBorder(borderRightGantt, margin1));
			p6.add(pLabel);
			
		}
		
		//funx per rregullimin e paneleve te grupuara
		nestPanels();
		
		//rregullon madhesine e window ne baze te nr te procesve & d. gantt
		if (length <= 4) {
			frame.setSize(750, 480); 
			
			if (lengthRrGantt > 11) {
				frame.setSize(850, 480);
			}
		}
		else if (length > 4 && length <= 6) {
			frame.setSize(800, 550);
			
			if (lengthRrGantt > 11) {
				frame.setSize(900, 550);
			}
		}
		else if (length > 6 && length <= 9) {
			frame.setSize(850, 600);
			
			if (lengthRrGantt > 11) {
				frame.setSize(950, 600);
			}
		}
		else {
			frame.setSize(900, 750);
			
			if (lengthRrGantt > 11) {
				frame.setSize(1100, 600);
			}
		}
	}
	
	//funx per rregullimin e paneleve te grupuara
	public void nestPanels() {
		p1.setLayout(new BorderLayout(10, 15));
		p1.add(titleLabel, BorderLayout.NORTH);
		p1.add(p2, BorderLayout.CENTER);
		p1.add(timeLabel, BorderLayout.SOUTH);
		
		p3.setLayout(new BorderLayout(10, 15));
		p3.add(ganttLabel, BorderLayout.NORTH);
		p3.add(p4, BorderLayout.CENTER);
		
		p4.setLayout(new BorderLayout(0, 0));
		p4.add(p5, BorderLayout.NORTH);
		p4.add(p6, BorderLayout.SOUTH);
		p3.add(p8, BorderLayout.SOUTH);
		
		p7.setLayout(new BorderLayout(10, 15));
		p7.add(p1, BorderLayout.NORTH);
		p7.add(p3, BorderLayout.SOUTH);
		
		p8.setLayout(new GridLayout(1, 2));
		p8.add(tryButton);
		p8.add(doneButton);
	} 
	
	public void actionPerformed(ActionEvent e) {
		//kur klikohet butoni per te provuar perseri ose mbaroi
		//mbyll dritaren aktuale
		frame.dispose();
		
		//nqs klikohet per te provuar perseri, hap dritaren qe merr te dhenat
		if(e.getSource() == tryButton) {
			Selection selection = new Selection();
			}
	}
}


