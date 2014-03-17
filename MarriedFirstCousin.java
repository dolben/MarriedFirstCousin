/**
 *  Prints a list of first cousins who married
 *  from the GEDCOM whose path is given in the command line
 *
 *  by Hank Dolben http://dolben.org/
 *
 *  See http://gedcom4j.org/
 *
 *  Can be built and run as follows:
 *
 *    $ # this source file, gedcom4j.jar, and some.ged in the working directory
 *    $ export CLASSPATH=".:gedcom4j.jar"
 *    $ javac MarriedFirstCousin.java
 *    $ java MarriedFirstCousin some.ged
 */
import java.io.IOException;
import java.util.List;

import org.gedcom4j.model.Gedcom;
import org.gedcom4j.model.Individual;
import org.gedcom4j.model.Family;
import org.gedcom4j.parser.GedcomParser;
import org.gedcom4j.parser.GedcomParserException;
import org.gedcom4j.relationship.Relationship;
import org.gedcom4j.relationship.RelationshipCalculator;
import org.gedcom4j.relationship.RelationshipName;

public class Married1stCousin {

    /**
     *  Returns the (first) family where the given individual is a child,
     *          null when that family is not in the GEDCOM
     */
    private static Family parentsFamily( Individual child ) {
        return child != null && child.familiesWhereChild.size() != 0 ?
                child.familiesWhereChild.get(0).family : null;
    }
    
    public static void main( String arg[] )
        throws IOException, GedcomParserException {
        GedcomParser parser = new GedcomParser();
        parser.load(arg[0]);
        RelationshipCalculator calculator = new RelationshipCalculator();
        for ( Family family : parser.gedcom.families.values() ) {
            if ( family.husband != null && family.wife != null ) {
                calculator.calculateRelationships(family.husband,family.wife,false);
                for ( Relationship relationship : calculator.relationshipsFound ) {
                    if ( relationship.toString() == "FIRST_COUSIN" ) {
                        System.out.println(
                            family.husband.names.get(0).basic +
                            " married first cousin " +
                            family.wife.names.get(0).basic );
                    } 
                }
            }
        }
    }

}
