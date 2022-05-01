import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class MultithreadProcess extends Thread {
	private String name;
	private int requiredResourceA;
	private int requiredResourceB;
	private int requiredResourceC;
	private int currentResourceA;
	private int currentResourceB;
	private int currentResourceC;
	private int burstTime;
	private int status;
	private int[] numResources;
	
	static Semaphore semaphoreA = new Semaphore(1);
	static Semaphore semaphoreB = new Semaphore(1);
	static Semaphore semaphoreC = new Semaphore(1);
	
	public MultithreadProcess(String name, int requiredResourceA, int requiredResourceB, 
							  int requiredResourceC, int burstTime, int[] numResources) {
		this.name = name;
		this.requiredResourceA = requiredResourceA;
		this.requiredResourceB = requiredResourceB;
		this.requiredResourceC = requiredResourceC;
		this.currentResourceA = 0;
		this.currentResourceB = 0;
		this.currentResourceC = 0;
		this.burstTime = burstTime;
		this.status = 0;
		this.numResources = numResources;
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
	
	public int getCurrentResourceA() {
		return currentResourceA;
	}
	
	public int getCurrentResourceB() {
		return currentResourceB;
	}
	
	public int getCurrentResourceC() {
		return currentResourceC;
	}
	
	public int getBurstTime() {
		return burstTime;
	}
	
	public int getStatus() {
		return status;
	}
	
	public String _getName() {
		return name;
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
	
	public void setCurrentResourceA(int currentResourceA) {
		this.currentResourceA = currentResourceA;
	}
	
	public void setCurrentResourceB(int currentResourceB) {
		this.currentResourceB = currentResourceB;
	}
	
	public void setCurrentResourceC(int currentResourceC) {
		this.currentResourceC = currentResourceC;
	}
	
	public void setBurstTime(int burstTime) {
		this.burstTime = burstTime;
	}
	
	public void setStatus(int status) {
		// 0 is waiting
		// 1 is running
		// 2 is requesting
		this.status = status;
	}
	
	public void _setName(String name) {
		this.name = name;
	}
	
	public void printStatus() {
		if (getStatus() == 0) {
			System.out.println(_getName() + " Status: Waiting...");
		}
		else if (getStatus() == 1) {
			System.out.println(_getName() + " Status: Running");
		}
		else if (getStatus() == 2) {
			System.out.println(_getName() + " Status: Holding Resources~");
		}
	}
	
	public void runProcess() {
		numResources[0] -= getRequiredResourceA() - getCurrentResourceA();
		numResources[1] -= getRequiredResourceB() - getCurrentResourceB();
		numResources[2] -= getRequiredResourceC() - getCurrentResourceC();
		setStatus(1);
		setCurrentResourceA(getRequiredResourceA());
		setCurrentResourceB(getRequiredResourceB());
		setCurrentResourceC(getRequiredResourceC());
		try {
			Thread.sleep(1000 * getBurstTime());
		} catch (InterruptedException e) {
		}

		numResources[0] += getCurrentResourceA();
		numResources[1] += getCurrentResourceB();
		numResources[2] += getCurrentResourceC();
		setCurrentResourceA(0);
		setCurrentResourceB(0);
		setCurrentResourceC(0);
		setStatus(0);
	}
	
	public void releaseResources() {
		numResources[0] += getCurrentResourceA();
		numResources[1] += getCurrentResourceB();
		numResources[2] += getCurrentResourceC();
		setCurrentResourceA(0);
		setCurrentResourceB(0);
		setCurrentResourceC(0);
		setStatus(0);
	}
	
	public void holdingResources() {
		int temp;
		if (numResources[0] <= getRequiredResourceA() - getCurrentResourceA()) {
			temp = numResources[0];
			numResources[0] = 0;
			setCurrentResourceA(temp);
		}
		if (numResources[1] <= getRequiredResourceB() - getCurrentResourceB()) {
			temp = numResources[1];
			numResources[1] = 0;
			setCurrentResourceA(temp);
		}
		if (numResources[2] <= getRequiredResourceC() - getCurrentResourceC()) {
			temp = numResources[2];
			numResources[2] = 0;
			setCurrentResourceC(temp);
		}
		setStatus(2);
	}
	
	public void run() {
		try {
			Thread.sleep(1000 * getBurstTime());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (getStatus() == 0 &&
					numResources[0] >= getRequiredResourceA() - getCurrentResourceA() &&
					numResources[1] >= getRequiredResourceB() - getCurrentResourceB() &&
					numResources[2] >= getRequiredResourceC() - getCurrentResourceC()) {
				runProcess();
			}
			// waiting
			else if (getStatus() == 0) {
				holdingResources();
			}
			else if (getStatus() == 2 &&
					(numResources[0] >= getRequiredResourceA() - getCurrentResourceA() &&
					 numResources[1] >= getRequiredResourceB() - getCurrentResourceB() &&
					 numResources[2] >= getRequiredResourceC() - getCurrentResourceC())) {
				runProcess();
			}
			else if (getStatus() == 2 &&
					(numResources[0] < getRequiredResourceA() - getCurrentResourceA() ||
					 numResources[1] < getRequiredResourceB() - getCurrentResourceB() ||
					 numResources[2] < getRequiredResourceC() - getCurrentResourceC())) {
				releaseResources();
			}
		}
	}
}

