package com.modestoneto.pedidos;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * Transforma a regra de dependência da arquitetura hexagonal em teste:
 * o domínio não depende de nada, a aplicação depende só do domínio e os
 * adaptadores não dependem um do outro.
 */
@AnalyzeClasses(packages = "com.modestoneto.pedidos", importOptions = ArchitectureTest.OnlyProductionClasses.class)
class ArchitectureTest {

	/**
	 * Analisa só o código de produção (target/classes). O filtro pronto
	 * ImportOption.DoNotIncludeTests excluía todas as classes neste projeto, então
	 * este filtro próprio tira apenas a pasta de classes de teste.
	 */
	static class OnlyProductionClasses implements ImportOption {

		@Override
		public boolean includes(Location location) {
			return !location.contains("/test-classes/");
		}
	}

	@ArchTest
	static final ArchRule dominioNaoDependeDeAplicacaoNemDeAdaptadores = noClasses()
			.that().resideInAPackage("..domain..")
			.should().dependOnClassesThat().resideInAnyPackage("..application..", "..adapter..", "..config..");

	@ArchTest
	static final ArchRule dominioNaoDependeDeFrameworks = noClasses()
			.that().resideInAPackage("..domain..")
			.should().dependOnClassesThat().resideInAnyPackage(
					"org.springframework..",
					"jakarta.persistence..",
					"jakarta.validation..",
					"com.fasterxml.jackson..",
					"tools.jackson..",
					"io.swagger..",
					"org.hibernate..");

	@ArchTest
	static final ArchRule aplicacaoNaoDependeDeAdaptadores = noClasses()
			.that().resideInAPackage("..application..")
			.should().dependOnClassesThat().resideInAnyPackage("..adapter..", "..config..");

	@ArchTest
	static final ArchRule aplicacaoNaoDependeDeJpaNemDeJsonNemDeSwagger = noClasses()
			.that().resideInAPackage("..application..")
			.should().dependOnClassesThat().resideInAnyPackage(
					"jakarta.persistence..",
					"org.springframework.data..",
					"com.fasterxml.jackson..",
					"tools.jackson..",
					"io.swagger..");

	@ArchTest
	static final ArchRule adaptadorWebNaoDependeDoAdaptadorDePersistencia = noClasses()
			.that().resideInAPackage("..adapter.in..")
			.should().dependOnClassesThat().resideInAPackage("..adapter.out..");

	@ArchTest
	static final ArchRule adaptadorDePersistenciaNaoDependeDoAdaptadorWeb = noClasses()
			.that().resideInAPackage("..adapter.out..")
			.should().dependOnClassesThat().resideInAPackage("..adapter.in..");

	@ArchTest
	static final ArchRule controllersUsamSomentePortasDeEntrada = noClasses()
			.that().resideInAPackage("..adapter.in.web..")
			.should().dependOnClassesThat().resideInAnyPackage("..application.service..", "..application.port.out..");
}
