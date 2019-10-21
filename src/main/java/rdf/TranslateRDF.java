package rdf;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.jena.atlas.lib.Timer;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.util.FileManager;

import shacl.ConstraintCollection;

public class TranslateRDF {
	
	private static HashMap<String, String> hashmap;
	
	public static void toFacts(ConstraintCollection cstr, String filename, Model rdfGraph)
	{
		 String[] strings = cstr.toRules().split("\n"); 
		 HashMap<String, Integer> toExtract = new HashMap<String, Integer>();
		 int i = 0;
		 String res = "res";
		 String lit = "lit";
		 String num = "int";
		
		 
		 
		 for (String string : strings){ 
			 string = string.trim(); 
			 if (string.startsWith("%")) { 
				 List<String> atoms = getAllMatches(string, "<([^<]*)>"); 
				 for (String atom : atoms) {
					//System.out.println(atom); // extract predicate name and arity String
					  String predName = atom.split("\\/")[0].replace("<", ""); //
					  //System.out.println(predName);
					  int arity = Integer.parseInt(atom.split("\\/")[1].replace(">", "")); //
					 // System.out.println(arity);
					  toExtract.put(predName, arity); 
					  } 
				 }
		 }
		 
		 
		 BufferedWriter bw = null; 
		 try{ 
			 bw = new BufferedWriter(new FileWriter(filename, true)); 
			 
			 
			 for ( String predName : toExtract.keySet()) {
			    
				 int arity = toExtract.get(predName); 
				// System.out.println(predName);
				 if (arity == 1) { 
					 String queryString = "SELECT ?x \n WHERE { ?x a <http://dbpedia.org/ontology/"+predName+"> }";
					// System.out.println(queryString); 
					 QueryExecution qexec = QueryExecutionFactory.create(queryString, rdfGraph); 
					 ResultSet results = qexec.execSelect() ;
					 List <QuerySolution> solutions = ResultSetFormatter.toList(results);
					 for (QuerySolution sol : solutions) { 
						 
						 RDFNode node = sol.get("x");
						 String constant = "";
						 if (node.isURIResource()) {
							 String[] aux = sol.get("x").toString().split("/"); 
						 	 constant = aux[aux.length-1];
						 	 constant = constant.replaceAll("[^a-zA-Z0-9]", "");
						 	 if (constant.isEmpty() || Character.isDigit(constant.charAt(0)) || !Character.isDefined(constant.charAt(0))) {
						 		 constant = "res"+i+constant;
						 		 i++;
						 	 }
						 	
						 }
						 if (node.isLiteral())
						 {
							constant = lit+sol.get("x").toString().split("\\^")[0].replaceAll("[^a-zA-Z0-9]", "");
						 }
						 if (node.isAnon())
						 {
							 constant = "anon"+i+constant;
							 i++;
						 }
						if (constant.length()>0 && Character.isUpperCase(constant.charAt(0)))
					 	 {
					 		constant = Character.toLowerCase(constant.charAt(0)) + constant.substring(1);
					 	 }
						
							 String fact =predName+"("+constant+"). \n"; 
							
							 bw.write(fact); 
						
						 }
							
				 }
				 
				 if (arity == 2)
				 {
					 String queryString = "SELECT ?x ?y \n WHERE { ?x <http://dbpedia.org/ontology/"+predName+">  ?y }"; 
					
					 QueryExecution qexec = QueryExecutionFactory.create(queryString, rdfGraph);
					 ResultSet results = qexec.execSelect() ;
					 List <QuerySolution> solutions = ResultSetFormatter.toList(results);
					 for (QuerySolution sol : solutions) { 
						
						 String constant1 = "";
						 String constant2 = "";
						 RDFNode node1 = sol.get("x");
						 //System.out.println(node1.toString());
						 
						 RDFNode node2 = sol.get("y");
						 //System.out.println(node2.toString());
						 if (node1.isURIResource())
						 {
							 String[] aux = sol.get("x").toString().split("/"); 
						 	 constant1 = aux[aux.length-1];
						 	 constant1 = constant1.replaceAll("[^a-zA-Z0-9]", "");
						 	if (constant1.isEmpty() || Character.isDigit(constant1.charAt(0)) || !Character.isDefined(constant1.charAt(0))) {
						 		 constant1 = "res"+i+constant1;
						 		 i++;
						 	 }
						 }
						  if (node1.isLiteral())
						 {
							  constant1 = lit+sol.get("x").toString().split("\\^")[0].replaceAll("[^a-zA-Z0-9]", "");
							 
						 }
						  if (node1.isAnon())
						  {
							  constant1 = "anon"+i;
							  i++;
						  }
						 
						 if (node2.isURIResource())
						 {
							 String[] aux = sol.get("y").toString().split("/"); 
						 	 constant2 = aux[aux.length-1];
						 	 constant2 = constant2.replaceAll("[^a-zA-Z0-9]", "");
						 	if  (constant2.isEmpty() || Character.isDigit(constant2.charAt(0)) || !Character.isDefined(constant2.charAt(0))) {
						 		 constant2 = "res"+i+constant2;
						 		 i++;
						 	 }
						 } 
						 
						 if (node2.isLiteral())
						 {
							 constant2 = lit+sol.get("x").toString().split("\\^")[0].replaceAll("[^a-zA-Z0-9]", "");
						 }
						 if (node2.isAnon())
						 {
							 constant2 = "anon"+i;
							 i++;
						 }
						
						 if (constant1.length()>0 && Character.isUpperCase(constant1.charAt(0)))
					 	 {
					 		constant1 = Character.toLowerCase(constant1.charAt(0)) + constant1.substring(1);
					 	 }
						 if (constant2.length()>0 && Character.isUpperCase(constant2.charAt(0)))
					 	 {
					 		constant2 = Character.toLowerCase(constant2.charAt(0)) + constant2.substring(1);
					 	 }
						 //if (!constant1.isEmpty() && !constant2.isEmpty()) {
							 String fact = predName+"("+constant1+","+constant2+"). \n";
							// System.out.println(fact); 
							// System.out.println("***** ");
							 bw.write(fact); 
							// }
						 }
					 
				 }
			 }
		 }catch(IOException e)
		 {
			 e.printStackTrace();
		 }finally {
			 try {
				 bw.close();
			 }catch (IOException e)
			 {
				 e.printStackTrace();
			 }
		 }
				
	}
	
	 public static List<String> getAllMatches(String text, String regex) {
		 List<String> matches = new ArrayList<String>(); 
		 Matcher m = Pattern.compile("(?=(" + regex + "))").matcher(text); 
		 while (m.find()) {
			 matches.add(m.group(1)); } 
		 return matches; 
		 }
	
	public static void main (String[] args)
	{
		

		if (args.length != 4)
		{
			System.out.println("USE <ex.shacl> <target> <programFile> <outputFactsFile> <tbdFolder>");
			return ;
		}
		
		
		 String tdb_folder = args[4];
		
		Dataset dataset = TDBFactory.createDataset(tdb_folder);
		
		Model tdb = dataset.getDefaultModel();
		
		
		
		
		// read and transform SHACL constraints
		ConstraintCollection full = null;
		try {

            String contents = new String(Files.readAllBytes(Paths.get(args[0])));

            full = ConstraintCollection.constructFromText(contents);

            
            // construct the program file, take target as input
            BufferedWriter bf = null;
            try {
            	bf = new BufferedWriter(new FileWriter(args[2]));
            	bf.write(full.toRules()+"\n"+args[1]+"\n");
            }catch (Exception e) {
            	e.printStackTrace();
			}finally
            {
				try {
					bf.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
            }
            //System.out.println();
            
            //measure the time to extract facts
            Timer t1 = new Timer();
			t1.startTimer();
            toFacts(full,args[3],tdb);
            t1.endTimer();
			System.out.println(" Time(s) to extract facts: "+(t1.getTimeInterval()/1000.0));

        } catch (Exception e) {
            System.out.println(e);
        }

		
		dataset.close();
	}

}
