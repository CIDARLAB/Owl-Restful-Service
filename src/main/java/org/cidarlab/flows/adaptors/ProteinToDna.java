/*
START execution

CREATE Hash Map
    - Amino acids represent Key of the Hash
    - Codons represent the Hash

READ the Protein sequence
    Start Reverse Translation
      For each letter, do the substitution of a letter to a 3-letter codon.
      Write nucleotide sequence
      Loop
        read next letter
        Aino acid to Codons
        append nucleotide sequence
        End loop
    End reverse Translation

PRINT nucleotide sequence

END execution
*/

import java.util.HashMap;
import java.util.Map;

public class ReverseTranslate{
  // Protein String to reverse-translate:
  private static String PROTEIN = "MTYSRSNITLALLANICAFFLWSLATLIFNALSTIDNLQVLAFRIIFSMM";

  // Map to store the amino acids to codons sequences
  private static final Map<String,String> REVERSE_TRANSLATION = new HashMap<String,String>();

  // Codons to translate
  private static final String[] CODONS = { "ATT", "CTT", "GTT", "TTT", "ATG", "TGT", "GCT",
                                           "GGT", "CCT", "ACT", "TCT", "TAT", "TGG", "CAA",
                                           "AAT", "CAT", "GAA", "GAT", "AAA", "CGT"
  };

  // Amino acids in Map
  private static final String[] AMINO_ACIDS = { "I", "L", "V", "F", "M", "C", "A",
                                                "G", "P", "T", "S", "Y", "W", "Q",
                                                "N", "H", "E", "D", "K", "R",
  };

  // Initialize the Map
  private static void init() {
    for(int i=0; i<AMINO_ACIDS.length; i++)
      REVERSE_TRANSLATION.put(AMINO_ACIDS[i], CODONS[i]);
  }

  public static void main(String[] argv) {
    init();
    StringBuffer nucleotideSequence = new StringBuffer();
    int i = 0;
    while(i<PROTEIN.length()) {
      nucleotideSequence.append(REVERSE_TRANSLATION.get(PROTEIN.substring(i)));
      i++;
    }
    System.out.println("Reverse-translated sequence: " + nucleotideSequence);
  }
}
