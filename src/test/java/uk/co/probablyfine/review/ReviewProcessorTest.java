package uk.co.probablyfine.review;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.time.LocalDate;
import org.junit.Test;

public class ReviewProcessorTest {

    private final StringBuilder stringBuilder = new StringBuilder();
    private final ReviewProcessor processor = new ReviewProcessor(stringBuilder::append);
    private final String className = ReviewProcessorTest.class.getSimpleName();

    @Test
    public void shouldDoNothingIfReviewNotNeeded() {
        LocalDate oneDayBeforeDefaultThreshold = LocalDate.now().minusWeeks(2).plusDays(1);

        processor.checkElementForReview(className, oneDayBeforeDefaultThreshold.toString());

        assertThat(stringBuilder.length(), is(0));
    }

    @Test
    public void shouldLogIfCodeIsDueForReview() {
        LocalDate defaultThreshold = LocalDate.now().minusWeeks(2).minusDays(1);

        processor.checkElementForReview(className, defaultThreshold.toString());

        assertThat(stringBuilder.toString(), is("Code due for review: ReviewProcessorTest"));
    }
}
