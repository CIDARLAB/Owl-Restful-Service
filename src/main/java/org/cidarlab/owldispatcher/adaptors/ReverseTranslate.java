package org.cidarlab.owldispatcher.adaptors;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by dmitry on 5/1/16.
 */
public class ReverseTranslate {

    // this assumes you are using google's Guava library (Prashant seems to be using it). If not you can create this map
    // using add method on HashMap.
    private static final Map<Character, String> CODONS_TO_AMINO_ACIDS = ImmutableMap.<Character, String>builder()
            .put('I', "ATT")
            .put('L', "CTT")
            .put('V', "GTT")
            .put('F', "TTT")
            .put('M', "ATG")
            .put('C', "TGT")
            .put('A', "GCT")
            .put('G', "GGT")
            .put('P', "CCT")
            .put('T', "ACT")
            .put('S', "TCT")
            .put('Y', "TAT")
            .put('W', "TGG")
            .put('Q', "CAA")
            .put('N', "AAT")
            .put('H', "CAT")
            .put('E', "GAA")
            .put('D', "GAT")
            .put('K', "AAA")
            .put('R', "CGT")
            .build();


    // this method is using java 8 FP elements
    public static String translate(final String protein) {
        Stream<Character> streamOfProtein = protein.chars().mapToObj(i -> (char)i);

        // first let's see if our protein is not bogus...
        List<Character> unknownProteinCodes = protein.chars()
                .mapToObj(i -> (char) i)
                .filter(p -> !CODONS_TO_AMINO_ACIDS.containsKey(p))
                .collect(Collectors.toList());
        if (unknownProteinCodes.size() > 0) throw new RuntimeException("Protein contains unrecognized elements: " + unknownProteinCodes);

        // now we can translate it...
        return protein.chars()
                .mapToObj(i -> (char)i)
                .map(CODONS_TO_AMINO_ACIDS::get)
                .collect(Collectors.joining(""));

    }

    // this method is using java 8 FP elements
    public static String translateJava7Style(final String protein) {

        String result = "";
        String badElements = "";
        for (char ch: protein.toCharArray()) {
            if (CODONS_TO_AMINO_ACIDS.containsKey(ch)) {
                result += CODONS_TO_AMINO_ACIDS.get(ch);
            } else {
                badElements += ch;
            }
        }

        if (badElements.length() > 0) throw new RuntimeException("Protein contains unrecognized elements: "
                + badElements);

        return result;
    }

    public static void main(String[] args) {
        String p = "MTYSRSNITLALLANICAFFLWSLATLIFNALSTIDNLQVLAFRIIFSM";

        System.out.println(translate(p));

        System.out.println(translateJava7Style(p));
    }

}
