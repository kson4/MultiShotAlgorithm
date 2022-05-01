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
		
//		if (numResources[0] >= getRequiredResourceA() - getCurrentResourceA()) {
//			temp = getRequiredResourceA() - getCurrentResourceA();
//			numResources[0] = numResources[0] - temp;
//			setCurrentResourceA(temp);
//		}
//		if (numResources[1] >= getRequiredResourceB() - getCurrentResourceB()) {
//			temp = getRequiredResourceB() - getCurrentResourceB();
//			numResources[1] = numResources[1] - temp;
//			setCurrentResourceA(temp);
//		}
//		if (numResources[2] >= getRequiredResourceC() - getCurrentResourceC()) {
//			temp = getRequiredResourceC() - getCurrentResourceC();
//			numResources[2] = numResources[2] - temp;
//			setCurrentResourceC(temp);
//		}
		setStatus(2);
	}
	
	public boolean checkAvailability() {
		return (numResources[0] >= (getRequiredResourceA() - getCurrentResourceA()) &&
					numResources[1] >= (getRequiredResourceB() - getCurrentResourceB()) &&
					numResources[2] >= (getRequiredResourceC() - getCurrentResourceC()));
	}
	
	public void run() {
//		try {
//			Thread.sleep(1000 * getBurstTime());
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		while(true) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			stage = 1;
			//System.out.println("running avail: " + semaphoreRunning.availablePermits());
			//System.out.println("holding avail: " + semaphoreHolding.availablePermits());
			if (checkAvailability()) {
				stage = 2;
				try {
					semaphoreRunning.acquire();
					try {
						if (checkAvailability())
							runProcess();
					} finally {
						semaphoreRunning.release();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stage = 3;
				
				//runProcess();
			}
			// waiting
			else if ((numResources[0] <= getRequiredResourceA() - getCurrentResourceA() ||
						numResources[1] <= getRequiredResourceB() - getCurrentResourceB() ||
						numResources[2] <= getRequiredResourceC() - getCurrentResourceC())) {
				setStatus(3);
				stage = 4;
				try {
					semaphoreHolding.acquire();
					try {
						holdingResources();
					} finally {
						semaphoreHolding.release();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stage = 5;
				
			}
			stage = 6;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			stage = 7;
			if (checkAvailability()) {
				try {
					stage = 8;
					semaphoreRunning.acquire();
					stage = 9;
					try {
						stage = 10;
						if (checkAvailability()) {
							stage = 11;
							runProcess();
						}
						else {
							stage = 12;
							try {
								semaphoreRelease.acquire();
								try {
									releaseResources();
								} finally {
									semaphoreRelease.release();
								}
							} catch (InterruptedException e) {
							}
						}
					} finally {
						stage = 13;
						semaphoreRunning.release();
					}
					stage = 14;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					stage = 15;
					e.printStackTrace();
				}
			}
			else {
				try {
					semaphoreRelease.acquire();
					try {
						releaseResources();
					} finally {
						semaphoreRelease.release();
					}
				} catch (InterruptedException e) {
				}
			}
			stage = 16;
//			if (getStatus() == 2 &&
//						(numResources[0] >= (getRequiredResourceA() - getCurrentResourceA()) &&
//						numResources[1] >= (getRequiredResourceB() - getCurrentResourceB()) &&
//						numResources[2] >= (getRequiredResourceC() - getCurrentResourceC()))){
//				stage = 8;
//				try {
//					semaphoreRunning.acquire();
//					try {
//						if (checkAvailability())
//							runProcess();
//						else
//							releaseResources();
//					} finally {
//						semaphoreRunning.release();
//					}
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				stage = 9;
//				
//				//releaseResources();
//			}
//			else
//				releaseResources();
//			stage = 10;
		}
	}
}

