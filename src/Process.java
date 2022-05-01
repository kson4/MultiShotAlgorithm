import java.util.concurrent.TimeUnit;

public class Process extends Thread {
	private int requiredResourceA;
	private int requiredResourceB;
	private int requiredResourceC;
	private int burstTime;
	
	public Process(int requiredResourceA, int requiredResourceB, int requiredResourceC, int burstTime) {
		this.requiredResourceA = requiredResourceA;
		this.requiredResourceB = requiredResourceB;
		this.requiredResourceC = requiredResourceC;
		this.burstTime = burstTime;
	}
	
	public int getRequiredResourceA() {
		return requiredResourceA;
	}
	
	public int getRequiredResourceB() {
		return requiredResourceB;
	}
	
	public int getRequiredResourceC() {
		return requiredResourceC;
	}
	
	public int getBurstTime() {
		return burstTime;
	}
	
	public void setRequiredResourceA(int requiredResourceA) {
		this.requiredResourceA = requiredResourceA;
	}
	
	public void setRequiredResourceB(int requiredResourceB) {
		this.requiredResourceB = requiredResourceB;
	}
	
	public void setRequiredResourceC(int requiredResourceC) {
		this.requiredResourceC = requiredResourceC;
	}
	
	public void setBurstTime(int burstTime) {
		this.burstTime = burstTime;
	}
	
	public void run(int[] array) {
		for (int i = 0; i < 5; i++) {
			System.out.println(i);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void oneShot(int[] array) throws InterruptedException {
		if (array[0] >= getRequiredResourceA() &&
			array[1] >= getRequiredResourceB() &&
			array[2] >= getRequiredResourceC()) {
			array[0] -= getRequiredResourceA();
			array[1] -= getRequiredResourceB();
			array[2] -= getRequiredResourceC();
			TimeUnit.SECONDS.sleep(getBurstTime());
			System.out.println("Process finished. Returning resources.");
			array[0] += getRequiredResourceA();
			array[1] += getRequiredResourceB();
			array[2] += getRequiredResourceC();
		}
		else {
			TimeUnit.SECONDS.sleep(1);
			System.out.println("Currently waiting for resources.");
		}
	}
}
