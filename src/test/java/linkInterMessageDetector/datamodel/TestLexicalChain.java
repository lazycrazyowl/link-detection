package linkInterMessageDetector.datamodel;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestLexicalChain {

    @Test
    public void test() {
        LexicalChain lc1 = new LexicalChain();
        lc1.add("père");
        lc1.add("noël");
        lc1.add("santa");
        lc1.add("sapin");
        lc1.add("neige");
        lc1.add("vinasse");

        LexicalChain lc2 = new LexicalChain();
        lc2.add("père");
        lc2.add("fouettard");
        lc2.add("martinet");
        lc2.add("enfant");
        lc2.add("vinasse");

        System.out.printf(
                "Comparing %s with %s gives %f which stands for %s \n",
                lc1,
                lc2,
                lc1.compare(lc2),
                lc1.isSimilar(lc2) ? "similar" : "notSimilar");
    }
}
