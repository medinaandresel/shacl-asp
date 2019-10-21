# shacl-asp
Prototype translation from SHACL constraints into answer set programming (ASP).

# Building

I used Maven. After a successful build the following file should appear:

```./target/shacl-to-asp-1.0-SNAPSHOT.jar```

# Running the translation


You should make sure that DLV is in your PATH variable, so that we can run the 'dlv' command.

You should make sure taht './bin' is in your PATH variable, so that we can run 'shacl-to-asp' and 'shacl-validate'.

The first program 'shacl-to-asp' executes a translation from SHACL constraints in an abstract syntax into a set of ASP rules. 

You can run the translation by

```shacl-to-asp constraint_file```

Here constraint_file is a file with  constraints in the abstract syntax.

You can test some example(s) by running

```shacl-to-asp ex1.shacl```

# Running the validator

One can use 'shacl-validate' to validate a graph against some constraints and targets. Currently the classical brave ASP semantics is used.
 
```shacl-validate ex1.shacl ex1.graph ex1.target```

The SHACL constraint file contains constrains as described below.

The graph file is just a set of facts in the DLV syntax.

The target file is just a comma-separated list of ground atoms in the DLV syntax.


# Abstract Syntax

Each constraint is of the form

```ShapeName :- ShapeExpression;```

'ShapeExpression' must obey the following grammar:

```
ShapeExpression ::= NodeType | (ShapeExpression or ShapeExpression) | 
                    (ShapeExpression and ShapeExpression) |
                    (not ShapeExpression) | 
                    (PathExpression only ShapeExpression) | 
                    (PathExpression some ShapeExpression)

PathExpression ::= PropertyName | 
                  (PathExpression union PathExpression) | 
                  (PathExpression conc PathExpression) | 
                  PathExpression*
```

Here 'ShapeName', 'NodeType' and 'PropertyName' must be alphanumeretic.

