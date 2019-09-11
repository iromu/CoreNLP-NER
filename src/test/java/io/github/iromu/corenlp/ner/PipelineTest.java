package io.github.iromu.corenlp.ner;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class PipelineTest {

    private final String input;
    private final String expected;

    public PipelineTest(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Iterable<Object> dataset() {
        return Arrays.asList(new Object[][]{
                {"My name is Ivan", "0,0,0,NAME"},
                {"My name is Joe", "0,0,0,NAME"},
                {"My name is Paul", "0,0,0,NAME"},
                {"My name is Paula", "0,0,0,NAME"},
                {"Assign task to Ivan", "ACTION,ENTITY,0,NAME"},
                {"Assign task to Robert", "ACTION,ENTITY,0,NAME"}
        });
    }

    @Test
    public void annotators() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        props.setProperty("coref.algorithm", "neural");
        props.setProperty("ner.model", "ner.model.ser.gz");
        props.setProperty("ner.useSUTime", "false");
        props.setProperty("ner.applyNumericClassifiers", "false");
        props.setProperty("ner.applyFineGrained", "false");
        Document doc = new Document(props, input);
        for (Sentence sent : doc.sentences()) { // Will iterate over two sentences
            System.out.println("The words of the sentence '" + sent + "' is " + sent.words());
            System.out.println("The lemmas of the sentence '" + sent + "' is " + sent.lemmas());
            System.out.println("The posTags of the sentence '" + sent + "' is " + sent.posTags());
            System.out.println("The mentions of the sentence '" + sent + "' is " + sent.mentions());
            System.out.println("The nerTags of the sentence '" + sent + "' is " + sent.nerTags());
            System.out.println("The parse of the sentence '" + sent + "' is " + sent.parse());
            System.out.println();

            assertThat(String.join(",", sent.nerTags())).isEqualTo(expected);
        }
    }

}
