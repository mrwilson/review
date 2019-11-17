package uk.co.probablyfine.review;

import static java.time.LocalDate.now;

import java.time.LocalDate;
import java.util.function.Consumer;

public class ReviewProcessor {

    void checkElementForReview(String className, String lastReviewed, Consumer<String> writer) {
        LocalDate reviewDate = LocalDate.parse(lastReviewed);

        if (reviewDate.isBefore(now())) {

            writer.accept(String.format("Code due for review: %s", className));
        }
    }
}
