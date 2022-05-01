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
	
	static Semaphore semaphoreHolding = new Semaphore(1);
	static Semaphore semaphoreRunning = new Semaphore(1);
	static Semaphore semaphoreRelease = new Semaphore(1);
	
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
//		try {
//			Thread.sleep(1000 * getBurstTime());
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		while(true) {
			stage = 1;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			// enough resources to run!
			stage = 2;
			if (checkAvailability()) {
				try {
					stage = 3;
					semaphoreRunning.acquire();
					try {
						stage = 3;
						if (checkAvailability())
							runProcess();
					} finally {
						stage = 4;
						semaphoreRunning.release();
					}
				} catch (InterruptedException e) {
					stage = 5;
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// not enough resources to run
			// check to see if there are some resources to briefly hold
			else if (notFullyAvailable()) {
				setStatus(3);
				stage = 6;
				try {
					stage = 7;
					semaphoreHolding.acquire();
					try {
						stage = 8;
						holdingResources();
					} finally {
						stage = 9;
						semaphoreHolding.release();
					}
				} catch (InterruptedException e) {
					stage = 10;
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// currently holding resources
			// check to see if there are enough resources to run
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if (getStatus() == 2 && checkAvailability()) {
				try {
					stage = 11;
					semaphoreRunning.acquire();
					try {
						// enough resources to run!
						if (checkAvailability()) {
							stage = 12;
							runProcess();
						}
						// not enough resources to run
						// release
						else {
							try {
								stage = 14;
								semaphoreRelease.acquire();
								try {
									stage = 15;
									releaseResources();
								} finally {
									stage = 16;
									semaphoreRelease.release();
								}
							} catch (InterruptedException e) {
							}
						}
					} finally {
						stage = 17;
						semaphoreRunning.release();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// release all resources
			else {
				try {
					stage = 18;
					semaphoreRelease.acquire();
					try {
						stage = 19;
						releaseResources();
					} finally {
						stage = 20;
						semaphoreRelease.release();
					}
				} catch (InterruptedException e) {
				}
			}
		}
	}
}

