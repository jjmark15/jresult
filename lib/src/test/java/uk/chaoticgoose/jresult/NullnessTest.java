package uk.chaoticgoose.jresult;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_TESTS;
import static com.tngtech.archunit.lang.SimpleConditionEvent.satisfied;
import static com.tngtech.archunit.lang.conditions.ArchConditions.not;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

public class NullnessTest {
    private static final JavaClasses CLASSES = new ClassFileImporter()
        .withImportOption(DO_NOT_INCLUDE_TESTS)
        .importPackages(NullnessTest.class.getPackageName());

    private static final ArchCondition<JavaMethod> beAnnotatedWithNullable = new ArchCondition<>("be annotated with @Nullable") {
        @Override
        public void check(JavaMethod method, ConditionEvents events) {
            var isAnnotated = method.reflect().getAnnotatedReturnType().getAnnotation(Nullable.class) != null;

            if (isAnnotated) {
                String message = "Method %s is annotated with @Nullable".formatted(method.getFullName());
                events.add(satisfied(method, message));
            }
        }
    };

    @Test
    void nullableReturnMethodsMustFollowNamingConvention() {
        methods()
            .that().arePublic()
            .and().areDeclaredInClassesThat().areNotRecords()
            .and().haveNameNotEndingWith("OrNull")
            .should(not(beAnnotatedWithNullable))
            .check(CLASSES);
    }
}
