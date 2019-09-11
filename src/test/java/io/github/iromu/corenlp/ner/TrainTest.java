package io.github.iromu.corenlp.ner;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class TrainTest {
    private CRFClassifier crfClassifier = CRFClassifier.getClassifierNoExceptions("ner.model.ser.gz");
    private String input;
    private String expected;


    public TrainTest(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Iterable<Object> dataset() {
        return Arrays.asList(new Object[][]{
                {"My name is Ivan", "0,0,0,NAME"},
                {"My name is Joe", "0,0,0,NAME"},
                {"My name is Paul", "0,0,0,NAME"},
                {"My name is Paula", "0,0,0,NAME"}
        });
    }

    @Test
    public void classify() {
        List<List<CoreLabel>> classify = crfClassifier.classify(input.toLowerCase());
        List<String> labels = new ArrayList<>();
        List<Double> prob = new ArrayList<>();
        for (CoreLabel o : classify.get(0)) {
            labels.add(o.getString(CoreAnnotations.AnswerAnnotation.class));
            prob.add(o.get(CoreAnnotations.AnswerProbAnnotation.class));
        }

        System.out.println(classify.toString());
        System.out.println(labels.toString());
        System.out.println(prob.toString());

        assertThat(String.join(",",labels)).isEqualTo(expected);
    }
}
