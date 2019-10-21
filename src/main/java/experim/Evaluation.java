package experim;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.jena.atlas.lib.Timer;
import org.apache.jena.query.Dataset;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;

import rdf.TranslateRDF;
import shacl.ConstraintCollection;

public class Evaluation {
	
	public static void evalSMB(String aspProgram, String aspInstances)
	{
		String resFilesSMB = "resSMB_"+aspProgram+"_"+aspInstances+".txt";
		System.out.println("------ Stable Models Brave: ");

		Runtime rt = Runtime.getRuntime();
		
		try {
			Timer t1 = new Timer();
			t1.startTimer();
			ProcessBuilder builder = new ProcessBuilder("./examples/dlv.bin", "-brave", "-silent", aspProgram, aspInstances);
			builder.redirectOutput(new File("out.txt"));
			builder.redirectError(new File("err.txt"));
			Process pr = builder.start();
			//Process pr = rt.exec("./examples/dlv.bin -brave -silent "+aspProgram+" "+aspInstances+" &> "
			//		+resFilesSMB);
			
			pr.waitFor();
			t1.endTimer();
			System.out.println("  Time(s): "+(t1.getTimeInterval()/1000.0));
			pr.destroy();
			Process pr2 = rt.exec("wc -w < out.txt");
			BufferedReader in = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println("  #validated targets: "+line);
			}
			pr2.waitFor();
			pr2.destroy();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		System.out.println("--------------------");
	}

	public static void evalSMC(String aspProgram, String aspInstances)
	{
		String resFilesSMC = "resSMC_"+aspProgram+"_"+aspInstances+".txt";
		System.out.println("------ Stable Models Cautious: ");
		Runtime rt = Runtime.getRuntime();
		try {
			Timer t1 = new Timer();
			t1.startTimer();
			Process pr = rt.exec("./examples/dlv.bin -cautious -silent "+aspProgram+" "+aspInstances+" > "
					+resFilesSMC);
			pr.waitFor();
			t1.endTimer();
			System.out.println("  Time(s): "+(t1.getTimeInterval()/1000.0));
			pr.destroy();
			Process pr2 = rt.exec("wc -l < "+resFilesSMC);
			BufferedReader in = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println("  #validated targets: "+line);
			}
			pr2.waitFor();
			pr2.destroy();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("------------------- ");
	}
	
	
	public static void evalWFB(String aspProgram, String aspInstances)
	{
		//String resFilesWFB = "resWFB_"+aspProgram+"_"+aspInstances+".txt";
		System.out.println("------ Well-founded Models Brave: ");
		Runtime rt = Runtime.getRuntime();
		try {
			Timer t1 = new Timer();
			ProcessBuilder builder = new ProcessBuilder("./examples/dlv.bin", "-wf", "-silent", aspProgram, aspInstances);
			builder.redirectOutput(new File("out.txt"));
			builder.redirectError(new File("err.txt"));
			Process pr = builder.start();
			t1.startTimer();
			//Process pr = rt.exec("./examples/dlv.bin -wf -silent "+aspProgram+" "+aspInstances+" > "
			//		+resFilesWFB);
			pr.waitFor();
			t1.endTimer();
			System.out.println("  Time(s): "+(t1.getTimeInterval()/1000.0));
			pr.destroy();
			Process pr2 = rt.exec("grep -o MovieShape out.txt | wc -l");
			pr2.waitFor();
			BufferedReader in = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println("  #validated targets: "+line);
			}
			
			pr2.destroy();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("------------------- ");
	}
	
	public static void evalWFC(String aspProgram, String aspInstances)
	{
		String resFilesWFC = "resWFC_"+aspProgram+"_"+aspInstances+".txt";
		System.out.println("------ Well-founded Cautious: ");
		Runtime rt = Runtime.getRuntime();
		try {
			Timer t1 = new Timer();
			t1.startTimer();
			Process pr = rt.exec("./examples/dlv.bin -wf -cautious -silent "+aspProgram+" "+aspInstances+" > "
					+resFilesWFC);
			pr.waitFor();
			t1.endTimer();
			System.out.println("  Time(s): "+(t1.getTimeInterval()/1000.0));
			pr.destroy();
			Process pr2 = rt.exec("wc -l < "+resFilesWFC);
			BufferedReader in = new BufferedReader(new InputStreamReader(pr2.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println("  #validated targets: "+line);
			}
			pr2.waitFor();
			pr2.destroy();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("------------------- ");
		
	}
	
	public static void main (String[] args)
	{
		

		//String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
		//System.out.println(encodedString);
		
		String aspProgram = args[0];
		String aspInstances = args[1];
		
		
		
		
		
		System.out.println("------ Program: "+aspProgram);
		System.out.println("------ Instance: "+aspInstances);
		File file = new File(aspInstances);
		if (file.exists())
		{
			System.out.println("ASP file size: "+(file.getTotalSpace()/(1024*1024)));
			
		}else
		{
			System.out.println("Err: file does not exist");
			
		}
		
		// evalSMB(aspProgram, aspInstances);
		//evalSMC(aspProgram, aspInstances);
		 evalWFB(aspProgram, aspInstances);
		//evalWFC(aspProgram, aspInstances);
	
	}

}
