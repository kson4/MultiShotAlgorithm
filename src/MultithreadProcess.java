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
	private int stage;
	
	static Semaphore read = new Semaphore(1);
	static Semaphore write = new Semaphore(1);
	
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
		this.stage = 0;
	}
	
	public int getStage() {
		return stage;
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
			System.out.print(_getName() + " Status: Waiting...");
		}
		else if (getStatus() == 1) {
			System.out.print(_getName() + " Status: Running");
		}
		else if (getStatus() == 2) {
			System.out.print(_getName() + " Status: Holding Resources~");
		}
		else if (getStatus() == 3) {
			System.out.print(_getName() + " Status: Attemping to Hold Resources");
		}
		System.out.println("   STAGE: " + getStage());
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
	
	public void runProcess2() {
		setStatus(1);
		try {
			Thread.sleep(1000 * getBurstTime());
		} catch (InterruptedException e) {
		}
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
		if (numResources[0] != 0) {
			if (numResources[0] >= getRequiredResourceA()) {
				setCurrentResourceA(getRequiredResourceA());
				numResources[0] -= getRequiredResourceA();
			}
			else {
				temp = numResources[0];
				numResources[0] = 0;
				setCurrentResourceA(temp);
			}
		}
		
		if (numResources[1] != 0) {
			if (numResources[1] >= getRequiredResourceB()) {
				setCurrentResourceB(getRequiredResourceB());
				numResources[1] -= getRequiredResourceB();
			}
			else {
				temp = numResources[1];
				numResources[1] = 0;
				setCurrentResourceB(temp);
			}
		}
		
		if (numResources[2] != 0) {
			if (numResources[2] >= getRequiredResourceC()) {
				setCurrentResourceC(getRequiredResourceC());
				numResources[2] -= getRequiredResourceC();
			}
			else {
				temp = numResources[2];
				numResources[2] = 0;
				setCurrentResourceC(temp);
			}
		}
		setStatus(2);
	}
	
	public void addResources() {
		numResources[0] -= getRequiredResourceA() - getCurrentResourceA();
		numResources[1] -= getRequiredResourceB() - getCurrentResourceB();
		numResources[2] -= getRequiredResourceC() - getCurrentResourceC();
		setCurrentResourceA(getRequiredResourceA());
		setCurrentResourceB(getRequiredResourceB());
		setCurrentResourceC(getRequiredResourceC());
	}
	
	public boolean checkAvailability() {
		return (numResources[0] >= (getRequiredResourceA() - getCurrentResourceA()) &&
					numResources[1] >= (getRequiredResourceB() - getCurrentResourceB()) &&
					numResources[2] >= (getRequiredResourceC() - getCurrentResourceC()));
	}
	
	public boolean notFullyAvailable() {
		return (numResources[0] <= (getRequiredResourceA() - getCurrentResourceA()) ||
					numResources[1] <= (getRequiredResourceB() - getCurrentResourceB()) ||
					numResources[2] <= (getRequiredResourceC() - getCurrentResourceC()));
	}
	
	public void run() {
		try {
			Thread.sleep(1000 * getBurstTime());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(true) {
			stage = 1;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (checkAvailability()) {
				stage = 2;
				try {
					stage = 3;
					write.acquire();
					try {
						stage = 4;
						if (checkAvailability()) {
							addResources();
							setStatus(1);
						}
					} finally {
						stage = 5;
						write.release();
					}
				} catch (InterruptedException e) { 
				}
				stage = 6;
				if (getStatus() == 1) {
					stage = 7;
					runProcess2();
					try {
						stage = 8;
						write.acquire();
						try {
							stage = 9;
							releaseResources();
						} finally {
							stage = 10;
							write.release();
						}
					} catch (InterruptedException e) { 
					}
				}
			}
			else if (getStatus() == 0 && notFullyAvailable()){
				stage = 11;
				try {
					stage = 12;
					write.acquire();
					try {
						stage = 13;
						holdingResources();
					} finally {
						stage = 14;
						write.release();
					}
				} catch (InterruptedException e) { 
				}
			}
			else {
				try {
					stage = 15;
					write.acquire();
					try {
						stage = 16;
						releaseResources();
					} finally {
						stage = 17;
						write.release();
					}
				} catch (InterruptedException e) { 
				}
			}
		}
	}
}

