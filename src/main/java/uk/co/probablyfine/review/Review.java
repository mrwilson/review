package uk.co.probablyfine.review;

public @interface Review {
    String lastReviewed();

    String reviewIn() default "2 weeks";
}
