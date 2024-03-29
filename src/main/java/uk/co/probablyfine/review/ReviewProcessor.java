package uk.co.probablyfine.review;

import static java.lang.Integer.parseInt;
import static java.time.LocalDate.now;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAmount;
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

        checkElementForReview(className, element.getAnnotation(Review.class));
    }

    void checkElementForReview(String className, Review annotation) {
        LocalDate reviewDate = LocalDate.parse(annotation.lastReviewed());

        TemporalAmount threshold = thresholdFrom(annotation.reviewIn());

        if (reviewDate.isBefore(now().minus(threshold))) {
            writer.accept(String.format("Code due for review: %s", className));
        }
    }

    private TemporalAmount thresholdFrom(String reviewIn) {
        String[] amountAndPeriod = reviewIn.split(" ");

        String period = amountAndPeriod[1];

        switch (period) {
            case "day":
            case "days":
                return Period.ofDays(parseInt(amountAndPeriod[0]));
            case "week":
            case "weeks":
                return Period.ofWeeks(parseInt(amountAndPeriod[0]));
            case "month":
            case "months":
                return Period.ofMonths(parseInt(amountAndPeriod[0]));
            default:
                throw new RuntimeException("Could not parse reviewIn threshold: " + reviewIn);
        }
    }
}
