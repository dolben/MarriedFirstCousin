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

public class MarriedFirstCousin {

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
        for ( Family family : parser.gedcom.families.values() ) {
            Family husbandsFamily = parentsFamily(family.husband);
            Family wifesFamily    = parentsFamily(family.wife);
            if ( husbandsFamily != null && wifesFamily != null &&
                 husbandsFamily != wifesFamily ) {
                /*
                 *  The husband and wife are first cousins if
                 *  they have a pair of grandparents in common.
                 */
                Family husbandsFathers = parentsFamily(husbandsFamily.husband);
                Family husbandsMothers = parentsFamily(husbandsFamily.wife);
                Family wifesFathers    = parentsFamily(wifesFamily.husband);
                Family wifesMothers    = parentsFamily(wifesFamily.wife);
                if ( ( husbandsFathers != null && wifesFathers != null &&
                       husbandsFathers         == wifesFathers            ) ||
                     ( husbandsFathers != null && wifesMothers != null &&
                       husbandsFathers         == wifesMothers            ) ||
                     ( husbandsMothers != null && wifesFathers != null &&
                       husbandsMothers         == wifesFathers            ) ||
                     ( husbandsMothers != null && wifesMothers != null &&
                       husbandsMothers         == wifesMothers            ) ) {
                    System.out.println(
                        family.husband.names.get(0).basic +
                        " married first cousin " +
                        family.wife.names.get(0).basic );
                }
            }
        }
    }

}
