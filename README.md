# review

Java annotation and compile-time processor to mark when code needs to be re-reviewed.

```java
@Review(lastReviewed = "2019-12-12", reviewIn = "2 weeks")
class ToBeReviewed {
        
}
```

```groovy
dependencies {
    testCompileOnly 'uk.co.probablyfine:review:1.0-SNAPSHOT'
}
```

```bash
$ ./gradlew clean compileJava

> Task :compileJava
Code due for review: ToBeReviewed
```