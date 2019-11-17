package uk.co.probablyfine.review;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

import java.time.LocalDate;
import org.junit.Test;

public class ReviewProcessorTest {

    private final ReviewProcessor processor = new ReviewProcessor();
    private final String className = ReviewProcessorTest.class.getSimpleName();

    @Test
    public void shouldLogIfCodeIsDueForReview() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        StringBuilder builder = new StringBuilder();

        processor.checkElementForReview(className, yesterday.toString(), builder::append);

        assertThat(builder.toString(), is("Code due for review: ReviewProcessorTest"));
    }
}
