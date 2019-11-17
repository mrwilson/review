package uk.co.probablyfine.review;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.time.LocalDate;
import org.junit.Test;

public class ReviewProcessorTest {

    private final StringBuilder stringBuilder = new StringBuilder();
    private final ReviewProcessor processor = new ReviewProcessor(stringBuilder::append);
    private final String className = ReviewProcessorTest.class.getSimpleName();

    @Test
    public void shouldDoNothingIfReviewNotNeeded() {
        LocalDate oneDayBeforeThreshold = LocalDate.now().minusWeeks(2).plusDays(1);

        processor.checkElementForReview(
                className, reviewAnnotation(oneDayBeforeThreshold.toString()));

        assertThat(stringBuilder.length(), is(0));
    }

    @Test
    public void shouldLogIfCodeIsDueForReview() {
        LocalDate defaultThreshold = LocalDate.now().minusWeeks(2).minusDays(1);

        processor.checkElementForReview(className, reviewAnnotation(defaultThreshold.toString()));

        assertThat(stringBuilder.toString(), is("Code due for review: ReviewProcessorTest"));
    }

    @Test
    public void shouldLogIfCodeIsDueForReview_withCustomReviewIn_day() {
        LocalDate beforeThreshold = LocalDate.now().minusDays(2);

        Review annotation = reviewAnnotation(beforeThreshold.toString(), "1 day");

        processor.checkElementForReview(className, annotation);

        assertThat(stringBuilder.toString(), is("Code due for review: ReviewProcessorTest"));
    }

    @Test
    public void shouldLogIfCodeIsDueForReview_withCustomReviewIn_days() {
        LocalDate beforeThreshold = LocalDate.now().minusDays(2);

        Review annotation = reviewAnnotation(beforeThreshold.toString(), "1 days");

        processor.checkElementForReview(className, annotation);

        assertThat(stringBuilder.toString(), is("Code due for review: ReviewProcessorTest"));
    }

    @Test
    public void shouldLogIfCodeIsDueForReview_withCustomReviewIn_week() {
        LocalDate beforeThreshold = LocalDate.now().minusWeeks(2);

        Review annotation = reviewAnnotation(beforeThreshold.toString(), "1 week");

        processor.checkElementForReview(className, annotation);

        assertThat(stringBuilder.toString(), is("Code due for review: ReviewProcessorTest"));
    }

    @Test
    public void shouldLogIfCodeIsDueForReview_withCustomReviewIn_weeks() {
        LocalDate beforeThreshold = LocalDate.now().minusWeeks(4);

        Review annotation = reviewAnnotation(beforeThreshold.toString(), "3 weeks");

        processor.checkElementForReview(className, annotation);

        assertThat(stringBuilder.toString(), is("Code due for review: ReviewProcessorTest"));
    }

    @Test
    public void shouldLogIfCodeIsDueForReview_withCustomReview_garbage() {
        LocalDate beforeThreshold = LocalDate.now();

        Review annotation = reviewAnnotation(beforeThreshold.toString(), "woogly boogly");

        try {
            processor.checkElementForReview(className, annotation);
            fail("Expecting exception");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("Could not parse reviewIn threshold: woogly boogly"));
        }
    }

    private Review reviewAnnotation(String toString) {
        return reviewAnnotation(toString, "2 weeks");
    }

    private Review reviewAnnotation(String lastReviewed, String reviewIn) {
        return new Review() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Review.class;
            }

            @Override
            public String lastReviewed() {
                return lastReviewed;
            }

            @Override
            public String reviewIn() {
                return reviewIn;
            }
        };
    }
}
