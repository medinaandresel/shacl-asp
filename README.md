# Extended Version

The extended version can be found here: https://anonymousfiles.io/nxhY0JeR/


# Building

We used Maven and Java 1.8 and Jena TDB and ARQ for reading and tansforming RDF to ASP facts. To build run ```maven clean install```

After a successful build the following files should appear:

```./target/shacl-to-asp.jar```

```./target/rdf-to-asp.jar```

```./target/valiate.jar```


# Running the translation

To obtain the ASP encoding of the SHACL constraints use: ```java -jar shacl-to-asp.jar constraint_file```.
Here constraint_file is a file with  constraints in the abstract syntax (see below).


# Running the RDF to ASP facts translation

We have used Jena TDB as data triples store to load each DBPedia dataset. To extract the ASP facts relevant to the SHACL constraints, run 

```java -jar rdf-to-asp.jar constraints_file target programOutputFile factsOutputFile tdbFolder```


Again constraints_file is the SHACL constrains file (in abstract syntax) and target is the string denoting the target of interest: ```MovieShape(X)?```. 
This will generate also the ASP program and put it into programOutputFile, while the targeted RDF triples are transformed into ASP facts and put into factsOutputFile. The ```tdbFolder```denotes the folder that Jena is using to store and read the RDF model. 


# Running the validator 

To run the validator (tested only on MAC OS), make sure the 'dlv.bin' is in the same folder as 'validate.jar' and run 

```java -jar validator.jar ASP_program_file ASP_facts_file sm|wf```

Option "sm" or "wf" is required to select the 2-valued (sm), respectively the 3-valued (wf) semantics. A file ```out.txt``` is then generated that contains the output of the solver. If errors occur, a file ```err.txt``` is generated. 

For the 2-valued semantics, a goal (e.g., MovieShape(X)?) in the ASP_program_file is required, while for the 3-valued (well-founded) semantics, no goal is required. 

Alternatively, run the DLV solver ```dlv -brave -silent ASP_program_file ASP_facts_file```. For the 3-valued semantics, use 
```dlv -wf -silent ASP_program_file ASP_facts_file```. 


# Abstract Syntax

Each constraint is of the form

```ShapeName :- ShapeExpression;```

'ShapeExpression' must obey the following grammar:

```
ShapeExpression ::= NodeType | (ShapeExpression OR ShapeExpression) | 
                    (ShapeExpression AND ShapeExpression) |
                    (NOT ShapeExpression) | 
                    (MIN n PathExpression)
                    (PathExpression ONLY ShapeExpression) | 
                    (PathExpression SOME ShapeExpression)

PathExpression ::= PropertyName | 
                  (PathExpression UNION PathExpression) | 
                  (PathExpression CONC PathExpression) | 
                  PathExpression*
```

Here 'ShapeName', 'NodeType' and 'PropertyName' must be alphanumeretic.

