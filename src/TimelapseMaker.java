
import java.io.*;
import java.util.Scanner;

import org.jcodec.api.JCodecException;

public class TimelapseMaker {
	private String folderPath ,outputFile;
	private int fps;
	private Util util;
	private Scanner sc = new Scanner(System.in);

	public TimelapseMaker() throws IOException, JCodecException, InterruptedException {
		util = new Util();
		welcome();
		getData();
		makeTimelapse(folderPath, outputFile, fps);
		finish();
	}

	private void makeTimelapse(String folder, String output, int fps) throws IOException, JCodecException, InterruptedException {
		System.out.println("================STARTED================"); 
		if(util.fileExists(output)) 
			while(!util.overwriteFile()) {
				System.out.print("Path: ");
				folderPath = sc.nextLine();
			}				
		util.toVideo(folder, output, fps);
		System.out.println("================DONE================");
	}

	private void getData() {
		System.out.print("Path: ");
		folderPath = sc.nextLine();
		System.out.print("FPS: ");
		fps = Integer.parseInt(sc.nextLine());
		System.out.print("Output file: ");
		outputFile = sc.nextLine();
		System.out.println();
	}

	private void finish() {
		System.out.println();
		System.out.println("Your timelape have been saved to "+ outputFile +".");
		System.out.println("Total video time: "+util.getVideoLength(folderPath));
		System.out.println("Video size: "+util.getVideoSize(new File(outputFile)));
		System.out.println("Enjoy!");
		System.out.println();
		System.out.println("- Mattias Jönsson");
	}

	private void welcome() {
		System.out.println("========== Welcome to TimelapseMaker v1.0 ========== 	by Mattias Jönsson");
		System.out.println();
		System.out.println("This program will encode every images in a");
		System.out.println("folder and make it into a timeplapse.");
		System.out.println();
		System.out.println("Please enter the filepath to the folder that");
		System.out.println("contains the images you want to make timelapse of");
		System.out.println("aswell as the fps and the outputpath.");
		System.out.println();
		System.out.println("REMEMBER: All images must be the same width and height.");
		System.out.println();
	}

	public static void main(String[] args){
		try {
			new TimelapseMaker();
		} catch (IOException | JCodecException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
