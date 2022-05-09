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
	private boolean canRun;
	private boolean isHoldingResources;
	private int stage;
	private int numRunned;
	
	static Semaphore readWrite = new Semaphore(1);
	
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
		this.canRun = false;
		this.isHoldingResources = false;
		this.stage = 0;
		this.numRunned = 0;
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
	
	public boolean getCanRun() {
		return canRun;
	}
	
	public int getNumRunned() {
		return numRunned;
	}
	
	public boolean isHoldingResources() {
		return isHoldingResources;
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
	
	public void setCanRun(boolean status) {
		this.canRun = status;
	}
	
	public void setIsHoldingResources(boolean status) {
		this.isHoldingResources = status;
	}
	
	public void setNumRunned(int num) {
		this.numRunned = num;
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
		System.out.println();
		//System.out.println("   STAGE: " + getStage());
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
	
	public void incrementNumRunned() {
		setNumRunned(getNumRunned() + 1);
	}
	
	public void releaseResources() {
		numResources[0] += getCurrentResourceA();
		numResources[1] += getCurrentResourceB();
		numResources[2] += getCurrentResourceC();
		setCurrentResourceA(0);
		setCurrentResourceB(0);
		setCurrentResourceC(0);
		
		// ***** difference *****
		setIsHoldingResources(false);
		setCanRun(false);
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
		
		// ***** difference *****
		setIsHoldingResources(true);
		setCanRun(false);
		
		setStatus(2);
	}
	
	public void addResources() {
		numResources[0] -= getRequiredResourceA() - getCurrentResourceA();
		numResources[1] -= getRequiredResourceB() - getCurrentResourceB();
		numResources[2] -= getRequiredResourceC() - getCurrentResourceC();
		setCurrentResourceA(getRequiredResourceA());
		setCurrentResourceB(getRequiredResourceB());
		setCurrentResourceC(getRequiredResourceC());
		setStatus(1);
		setCanRun(true);
		incrementNumRunned();
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
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			
			try {
				readWrite.acquire();
				try {
					// once permission is received, check to see if there are enough
					// resources to run
					if (checkAvailability()) {
						// if there are enough resources to run obtain the resources
						addResources();
					}
					
					// ***** DIFFERENCE *****
					// process is currently not holding resources
					// has permission to hold resources for one cycle
					else if (!isHoldingResources()){
						holdingResources();
					}
					
					// ***** DIFFERENCE *****
					// process is currently holding resources
					// AND there are not enough resources for the process to run
					// therefore, the process must release its resources
					else {
						releaseResources();
					}
				}
				// release mutex lock
				finally {
					readWrite.release();
				}
			} catch (InterruptedException e) {
			}
			
			if (getCanRun()) {
				// set status of process to run -> (1)
				setStatus(1);
				try {
					// run the resource depending on its burst time
					Thread.sleep(1000 * getBurstTime());
				} catch (InterruptedException e) {
				}
				
				// once process is done running, it must release allocated resources
				// attempt to obtain read/write permission
				try {
					readWrite.acquire();
					// once permission is obtain, release allocated resources
					try {
						releaseResources();
					}
					// release permission
					finally {
						readWrite.release();
					}
				} catch (InterruptedException e) {
				}
				
				// small buffer to prevent starvation
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
				
				// set process status to waiting -> (0)
				setStatus(0);
			}
		}
	}
}

