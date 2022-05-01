import java.util.concurrent.TimeUnit;

public class OneShot {
	public static void main(String[] args) throws InterruptedException {
		int[] numResources = {8,7,8};
		//int[] numResources = {3,10,7};
		
		MultithreadProcess p1 = new MultithreadProcess("Process 1", 3, 6, 7, 10, numResources);
		p1.start();
		MultithreadProcess p2 = new MultithreadProcess("Process 2", 1, 5, 6, 3, numResources);
		p2.start();
		MultithreadProcess p3 = new MultithreadProcess("Process 3", 4, 2, 2, 5, numResources);
		p3.start();
		MultithreadProcess p4 = new MultithreadProcess("Process 4", 6, 3, 3, 6, numResources);
		p4.start();
		MultithreadProcess p5 = new MultithreadProcess("Process 5", 2, 1, 5, 8, numResources);
		p5.start();
		
		while(true) {
			TimeUnit.SECONDS.sleep(1);
			System.out.println("Available Resources:| " + numResources[0] + " | " + numResources[1] + " | " + numResources[2]+ " | ");
			System.out.println("------------------------------------------------");
			System.out.println("                       CURRENT       REQUIRED");
			System.out.println("Process 1:          | " + p1.getCurrentResourceA() + " | " + p1.getCurrentResourceB() 
					+ " | " + p1.getCurrentResourceC() + " |" + "  | " + p1.getRequiredResourceA() 
					+ " | " + p1.getRequiredResourceB() + " | " + p1.getRequiredResourceC() + " | ");
			System.out.println("------------------------------------------------");
			System.out.println("Process 2:          | " + p2.getCurrentResourceA() + " | " + p2.getCurrentResourceB() 
					+ " | " + p2.getCurrentResourceC() + " |" + "  | " + p2.getRequiredResourceA() 
						+ " | " + p2.getRequiredResourceB() + " | " + p2.getRequiredResourceC() + " | ");
			System.out.println("------------------------------------------------");
			System.out.println("Process 3:          | " + p3.getCurrentResourceA() + " | " + p3.getCurrentResourceB() 
					+ " | " + p3.getCurrentResourceC() + " |" + "  | " + p3.getRequiredResourceA() 
						+ " | " + p3.getRequiredResourceB() + " | " + p3.getRequiredResourceC() + " | ");
			System.out.println("------------------------------------------------");
			System.out.println("Process 4:          | " + p4.getCurrentResourceA() + " | " + p4.getCurrentResourceB() 
					+ " | " + p4.getCurrentResourceC() + " |" + "  | " + p4.getRequiredResourceA() 
						+ " | " + p4.getRequiredResourceB() + " | " + p4.getRequiredResourceC() + " | ");
			System.out.println("------------------------------------------------");
			System.out.println("Process 5:          | " + p5.getCurrentResourceA() + " | " + p5.getCurrentResourceB() 
					+ " | " + p5.getCurrentResourceC() + " |" + "  | " + p5.getRequiredResourceA() 
						+ " | " + p5.getRequiredResourceB() + " | " + p5.getRequiredResourceC() + " | ");
			System.out.println("------------------------------------------------");
			p1.printStatus();
			p2.printStatus();
			p3.printStatus();
			p4.printStatus();
			p5.printStatus();
			System.out.println();
		}
	}
}
