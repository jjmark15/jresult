package uk.chaoticgoose.jresult;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.core.importer.ImportOption.Predefined.DO_NOT_INCLUDE_TESTS;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ArchitectureTest {
    private static final JavaClasses CLASSES = new ClassFileImporter().withImportOption(DO_NOT_INCLUDE_TESTS).importPackages("uk.chaoticgoose.jresult");

    @Test
    void baseClassesShouldNotBePublic() {
        classes().that().haveSimpleNameStartingWith("Base").should().notBePublic().check(CLASSES);
    }

    @Test
    void throwingResultClassesMustBeNamedThrowing() {
        classes().that().areAssignableTo(ThrowingResult.class).should().haveSimpleNameStartingWith("Throwing").check(CLASSES);
    }
}
