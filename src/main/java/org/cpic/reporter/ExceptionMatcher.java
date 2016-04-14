package org.cpic.reporter;

import java.util.Set;
import java.util.TreeSet;

import org.cpic.reporter.resultsJSON.Gene;
import org.cpic.util.HaplotypeNameComparator;

public class ExceptionMatcher {

    /**
     * FIXME: here I make the assumption of three different rule types
     * this should be drivin in config or some other place that makes handleing
     * matching easy without having to edit code 
     */
    public boolean test(Gene gene, String rule_name) {
        boolean finalTest = false;
        boolean geneTest = false;
        boolean hapTest = false;
        boolean dipTest = false;
        
        String cleaned_rule = rule_name.replaceAll("\\s", "");
        String[] parts = cleaned_rule.split(",");
        for( String part : parts){
           String[] type = part.split("=");
           if( type[0].equals("gene") ){
               geneTest = true; // just assume this is true for now, should double check that gene strings match              
           } else if( type[0].equals("hap") ){
               if( type[1].replaceAll("'", "").isEmpty()){
                   hapTest = true;
               } else {
                   String cleanHap = type[1].replaceAll("'", "");
                   hapTest = testForHapCall(cleanHap, gene.getDips() );
               }
                   
           } else if( type[0].equals("dip") ) {
               if( type[1].replaceAll("'", "").isEmpty()){
                   dipTest = true;
               } else {
                   String cleanDip = type[1].replaceAll("'", "");
                   dipTest = testForDipCall( cleanDip, gene.getDips() );
               }
                   
           }
        }
        if( geneTest && hapTest && dipTest ){
            finalTest = true;
        }
        
        return finalTest;
    }

    private boolean testForDipCall(String cleanDip, Set<String> dips) {
        boolean test = false;
        for( String dip : dips) {
            if( HaplotypeNameComparator.getComparator().compare(cleanDip, dip) == 0 ){
                test = true;
                break; 
            }
        }
        return test;
    }

    private boolean testForHapCall(String cleanHap, Set<String> dips) {
        boolean test = false;
        for( String dip : dips) {
            String[] parts = dip.split("/");
            if( parts[0].equals(cleanHap) || parts[1].equals(cleanHap)){
                test = true;
                break;
            }
        }
        return test;
    }

}
