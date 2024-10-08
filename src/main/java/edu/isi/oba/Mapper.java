package edu.isi.oba;

import static edu.isi.oba.Oba.logger;

import edu.isi.oba.config.CONFIG_FLAG;
import edu.isi.oba.config.YamlConfig;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;

class Mapper {
	private final Map<IRI, String> schemaNames = new HashMap<>(); // URI-names of the schemas
	private final Map<String, Schema> schemas = new HashMap<>();
	private final Paths paths = new Paths();
	private final Set<OWLOntology> ontologies;
	private final Set<OWLClass> allowedClasses = new HashSet<>();
	private final YamlConfig configData;

	private final OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

	/**
	 * Constructor
	 *
	 * @param configData the configuration data
	 * @throws OWLOntologyCreationException
	 * @throws IOException
	 */
	public Mapper(YamlConfig configData) throws OWLOntologyCreationException, IOException {
		this.configData = configData;

		Set<String> configOntologies = this.configData.getOntologies();
		String destinationDir =
				this.configData.getOutput_dir()
						+ File.separator
						+ this.configData
								.getName()
								.replaceAll("[.\\+\\*\\?\\^\\$\\(\\)\\[\\]\\{\\}\\|\\\\\\s]", "_");
		File outputDir = new File(destinationDir);
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}

		// Load the ontology into the manager
		int i = 0;
		Set<String> ontologyPaths = new HashSet<>();
		this.downloadOntologies(configOntologies, destinationDir, i, ontologyPaths);

		// set ontology paths in YAML to the ones we have downloaded
		this.configData.setOntologies(ontologyPaths);
		this.ontologies = this.manager.ontologies().collect(Collectors.toSet());

		// Set the allowed classes for the OpenAPI based on configuration file.  If no restrictions set,
		// all classes are added from each ontology.
		this.allowedClasses.addAll(this.getClassesAllowedByYamlConfig());
	}

	/**
	 * Convenience method for unit testing.
	 *
	 * @return a {@link Set} of {@link OWLOntology}
	 */
	public Set<OWLOntology> getOntologies() {
		return this.ontologies;
	}

	/**
	 * Convenience method for unit testing.
	 *
	 * @return an {@link OWLOntologyManager}
	 */
	public OWLOntologyManager getManager() {
		return this.manager;
	}

	private Schema getSchema(OWLClass cls) {
		logger.info("=======================================================================");
		logger.info("##############################################");
		logger.info("###  Beginning schema mapping for class:");
		logger.info("###\t" + cls);
		logger.info("##############################################");

		// Convert from OWL Class to OpenAPI Schema.
		final var objVisitor = new ObjectVisitor(this.ontologies, this.configData);
		cls.accept(objVisitor);

		final var mappedSchema = objVisitor.getClassSchema();

		// Each time we generate a class's schema, there may be referenced classes that need to be added
		// to the set of allowed classes.
		this.allowedClasses.addAll(objVisitor.getAllReferencedClasses());

		// Create the OpenAPI schema
		logger.info("");
		logger.info("--->  SAVING SCHEMA:  \"" + mappedSchema.getName() + "\"");
		logger.info("=======================================================================");
		logger.info("");
		this.schemas.put(mappedSchema.getName(), mappedSchema);

		return mappedSchema;
	}

	private void downloadOntologies(
			Set<String> configOntologies, String destinationDir, int i, Set<String> ontologyPaths)
			throws OWLOntologyCreationException, IOException {
		for (String ontologyPath : configOntologies) {
			// copy the ontologies used in the destination folder
			final var destinationPath = destinationDir + File.separator + "ontology" + i + ".owl";
			final var ontologyFile = new File(destinationPath);

			// content negotiation + download in case a URI is added
			if (ontologyPath.startsWith("http://") || ontologyPath.startsWith("https://")) {
				// download ontology to local path
				ObaUtils.downloadOntology(ontologyPath, destinationPath);
			} else {
				try {
					// copy to the right folder
					Files.copy(
							new File(ontologyPath).toPath(),
							ontologyFile.toPath(),
							StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException ex) {
					Logger.getLogger(Mapper.class.getName())
							.log(Level.SEVERE, "ERROR while loading file: " + ontologyPath, ex);
					throw ex;
				}
			}

			logger.info("Loaded working ontology file:  " + destinationPath.replace("\\", "/"));
			ontologyPaths.add(destinationPath);

			// Set to silent so missing imports don't make the program fail.
			OWLOntologyLoaderConfiguration loadingConfig = new OWLOntologyLoaderConfiguration();
			loadingConfig =
					loadingConfig.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
			this.manager.loadOntologyFromOntologyDocument(
					new FileDocumentSource(new File(destinationPath)), loadingConfig);
			i++;
		}

		logger.info("");
	}

	/**
	 * Obtain Schemas using the ontology classes The schemas includes all (object and data)
	 * properties.
	 */
	public void createSchemas() {
		final var pathGenerator =
				new PathGenerator(
						this.configData.getConfigFlags(),
						this.configData.getAuth() == null ? false : this.configData.getAuth().getEnable());

		for (OWLOntology ontology : this.ontologies) {
			final var format = ontology.getFormat();
			if (format == null) {
				logger.severe("No ontology format found.  Unable to proceed.");
				System.exit(1);
			} else {
				String defaultOntologyPrefixIRI = format.asPrefixOWLDocumentFormat().getDefaultPrefix();
				if (defaultOntologyPrefixIRI == null) {
					logger.severe("Unable to find the default prefix for the ontology.  Unable to proceed.");
					System.exit(1);
				}

				// Make a copy of the original allowed classes.  Use it for comparison, until this working
				// copy and the allowed classes are equal.
				var workingAllowedClasses = new HashSet<OWLClass>(this.allowedClasses);

				// Add allowed classes to OpenAPI (i.e. remove classes without default ontology
				ontology
						.classesInSignature()
						.filter(
								owlClass -> owlClass.getIRI() != null && workingAllowedClasses.contains(owlClass))
						.forEach(
								(owlClass) -> {
									this.addOwlclassToOpenAPI(
											pathGenerator, ontology, defaultOntologyPrefixIRI, owlClass, true);
								});

				// After allowed classes have been schema-fied, repeat for all the referenced classes.
				// If this is not done, the OpenAPI spec may contain references to schemas which do not
				// exist (because they were not explicitly in the allow list).
				// Looping is done until no new references have been added from the schema-fication process.
				while (!this.allowedClasses.equals(workingAllowedClasses)) {
					workingAllowedClasses.addAll(this.allowedClasses);

					ontology
							.classesInSignature()
							.filter(
									owlClass ->
											workingAllowedClasses.contains(owlClass)
													&& !this.schemas.keySet().contains(owlClass.getIRI().getShortForm()))
							.forEach(
									(owlClass) -> {
										this.addOwlclassToOpenAPI(
												pathGenerator, ontology, defaultOntologyPrefixIRI, owlClass, true);
									});
				}

				// Add all the allowed classes to the map of schema names/IRIs.
				this.setSchemaNames(this.allowedClasses);
			}
		}

		if (this.configData.getAuth().getEnable()) {
			this.addUserPath(pathGenerator);
		}
	}

	private void addUserPath(PathGenerator pathGenerator) {
		// User schema
		final var userSchema = new Schema();
		userSchema.setName("User");
		userSchema.setType("object");
		// Not using setProperties(), because it creates immutability which breaks unit tests.
		userSchema.addProperty("username", new StringSchema());
		userSchema.addProperty("password", new StringSchema());

		this.schemas.put("User", userSchema);

		this.paths.addPathItem("/user/login", pathGenerator.user_login(userSchema.getName()));
	}

	private void addOwlclassToOpenAPI(
			PathGenerator pathGenerator,
			OWLOntology ontology,
			String defaultOntologyPrefixIRI,
			OWLClass cls,
			Boolean isTopLevel) {
		try {
			final var mappedSchema = this.getSchema(cls);

			// Add the OpenAPI paths
			if (isTopLevel && this.getClassesAllowedByYamlConfig().contains(cls)) {
				this.addPath(pathGenerator, mappedSchema, cls.getIRI());
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Could not parse class " + cls.getIRI().toString());
			logger.log(Level.SEVERE, "\n\tdetails:\n" + e);
		}
	}

	/**
	 * Set the {@link Map} of schema names to link {@link IRI} with its (short form) name.
	 *
	 * @param classes a {@link Set} of {@link OWLClass}es from an ontology.
	 */
	private void setSchemaNames(Set<OWLClass> classes) {
		for (OWLClass cls : classes) {
			this.schemaNames.put(cls.getIRI(), cls.getIRI().getShortForm());
		}
	}

	/**
	 * Get a map of IRIs and their short form names for the schemas generated.
	 *
	 * @return a {@link Map} of {@link IRI} keys and short form {@link String} values
	 */
	public Map<IRI, String> getSchemaNames() {
		return this.schemaNames;
	}

	/**
	 * Get a map of names and schemas for each class of the ontology/ies (that are allowed, according to the configuration file).
	 *
	 * @return a {@link Map> of short form name {@link String} keys and their {@link Schema} values
	 */
	public Map<String, Schema> getSchemas() {
		return this.schemas;
	}

	/**
	 * Get all API paths from the OpenAPI spec.
	 *
	 * @return A {@link Paths} object from Swagger's OAS model.
	 */
	public Paths getPaths() {
		return this.paths;
	}

	private void addPath(PathGenerator pathGenerator, Schema mappedSchema, IRI classIRI) {
		// Pluralize the schema name.  Also convert to kebab-case if the configuration specifies it.
		var pluralPathName = "/";
		if (this.configData.getConfigFlagValue(CONFIG_FLAG.USE_KEBAB_CASE_PATHS)) {
			// "kebab-case" -> All lowercase and separate words with a dash/hyphen.
			pluralPathName +=
					ObaUtils.getLowerCasePluralOf(ObaUtils.pascalCaseToKebabCase(mappedSchema.getName()));
		} else {
			// "flatcase" -> This is the current/original version (all lower case, no
			// spaces/dashes/underscores) of endpoint naming.
			pluralPathName += ObaUtils.getLowerCasePluralOf(mappedSchema.getName());
		}

		// Create the plural paths: for example: /models/
		this.paths.addPathItem(
				pluralPathName,
				pathGenerator.generate_plural(mappedSchema.getName(), classIRI.getIRIString()));

		// Create the plural paths: for example: /models/id
		this.paths.addPathItem(
				pluralPathName + "/{id}",
				pathGenerator.generate_singular(mappedSchema.getName(), classIRI.getIRIString()));
	}

	/**
	 * Get set of allowed classes. Returns all classes, if no restrictions in configuration file.
	 *
	 * @return a {@link Set} of {@link OWLClass}es that are allowed by the config file.
	 */
	private Set<OWLClass> getClassesAllowedByYamlConfig() {
		final var allowedClassesByIRI = this.configData.getClasses();
		final var allowedClasses = new HashSet<OWLClass>();

		this.ontologies.forEach(
				(ontology) -> {
					// If the configuration contains no allowed classes, then add all classes from the
					// ontology.
					if (allowedClassesByIRI == null || allowedClassesByIRI.isEmpty()) {
						allowedClasses.addAll(ontology.getClassesInSignature());
					} else {
						ontology
								.classesInSignature()
								.filter(owlClass -> allowedClassesByIRI.contains(owlClass.getIRI().toString()))
								.forEach(
										(allowedClass) -> {
											allowedClasses.add(allowedClass);
										});
					}
				});

		return allowedClasses;
	}
}
