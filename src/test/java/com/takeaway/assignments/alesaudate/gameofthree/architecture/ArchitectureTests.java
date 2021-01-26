package com.takeaway.assignments.alesaudate.gameofthree.architecture;

import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

class ArchitectureTests {

	@Test
	void testIncomingPackageDoesNotHaveAccessToOutcomingPackage() {

		var rule = classes().that().resideInAPackage("..interfaces.incoming..").should().onlyAccessClassesThat()
				.resideOutsideOfPackage("..interfaces.outcoming..");
		checkRule(rule);

	}

	@Test
	void testDomainPackageDoesNotHaveAccessToIncomingPackage() {

		var rule = classes().that().resideInAPackage("..domain..").should().onlyAccessClassesThat()
				.resideOutsideOfPackage("..interfaces.incoming..");
		checkRule(rule);
	}

	@Test
	void testOutcomingPackageDoesNotHaveAccessToIncomingPackageAndDomainPackage() {

		var rule = classes().that().resideInAPackage("..interfaces.outcoming..").should().onlyAccessClassesThat()
				.resideOutsideOfPackage("..interfaces.incoming..").andShould().onlyAccessClassesThat()
				.resideOutsideOfPackage("..domain..");
		checkRule(rule);

	}

	private void checkRule(ArchRule rule) {
		var classes = new ClassFileImporter().importPackages("com.takeaway.assignments.alesaudate.gameofthree");
		rule.check(classes);
	}

}
