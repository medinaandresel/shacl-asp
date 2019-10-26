package shacl;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args )
    {

        if (args.length != 1) {
            System.out.println("A file name for the SHACL constraints is expected as argument! ");
            return;
        }

        try {

            String contents = new String(Files.readAllBytes(Paths.get(args[0])));

            ConstraintCollection full = ConstraintCollection.constructFromText(contents);

            System.out.println(full.toRules());

        } catch (Exception e) {
            System.out.println(e);
        }




    }
}
