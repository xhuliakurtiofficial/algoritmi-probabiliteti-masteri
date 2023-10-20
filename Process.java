//klase per ruajtjen e te dhenave te nje procesi
public class Process {
	public String name;
	public int right, arrival, burst, priority;
	
	Process() {
	}
	
	Process(String name, int right) {
		this.name = name;
		this.right = right;
	}
	
	Process(String name, int arrival, int burst) {
		this.name = name;
		this.arrival = arrival;
		this.burst = burst;
	}
	
	public String getName() {
		return name;
	}
	
}
