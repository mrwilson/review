package uk.co.probablyfine.review;

import static java.time.LocalDate.now;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes("uk.co.probablyfine.review.Review")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class ReviewProcessor extends AbstractProcessor {

    private final Consumer<String> writer;

    public ReviewProcessor() {
        this(System.out::println);
    }

    public ReviewProcessor(Consumer<String> writer) {
        this.writer = writer;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv.getElementsAnnotatedWith(Review.class).forEach(this::processElement);

        return false;
    }

    private void processElement(Element element) {
        String className = element.getSimpleName().toString();
        String lastReviewed = element.getAnnotation(Review.class).lastReviewed();

        checkElementForReview(className, lastReviewed);
    }

    void checkElementForReview(String className, String lastReviewed) {
        LocalDate reviewDate = LocalDate.parse(lastReviewed);

        if (reviewDate.isBefore(now().minusWeeks(2))) {
            writer.accept(String.format("Code due for review: %s", className));
        }
    }
}
