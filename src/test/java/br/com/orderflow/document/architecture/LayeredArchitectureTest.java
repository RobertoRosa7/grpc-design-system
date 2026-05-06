package br.com.orderflow.document.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Testes de arquitetura por camada.
 */
@AnalyzeClasses(packages = "br.com.orderflow.document")
class LayeredArchitectureTest {

  @ArchTest
  static final ArchRule application_nao_importa_infra = noClasses()
      .that().resideInAPackage("..application..")
      .should().dependOnClassesThat().resideInAPackage("..infra..");

  @ArchTest
  static final ArchRule infra_nao_importa_application = noClasses()
      .that().resideInAPackage("..infra..")
      .should().dependOnClassesThat().resideInAPackage("..application..");

  @ArchTest
  static final ArchRule domain_nao_importa_application = noClasses()
      .that().resideInAPackage("..domain..")
      .should().dependOnClassesThat().resideInAPackage("..application..");
}
