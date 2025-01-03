package edu.isi.oba.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class YamlConfig {
	private final Map<CONFIG_FLAG, Boolean> configFlags =
			new HashMap<>() {
				{
					put(CONFIG_FLAG.ALWAYS_GENERATE_ARRAYS, true);
					put(CONFIG_FLAG.DEFAULT_DESCRIPTIONS, true);
					put(CONFIG_FLAG.DEFAULT_PROPERTIES, true);
					put(CONFIG_FLAG.FIX_SINGULAR_PLURAL_PROPERTY_NAMES, false);
					put(CONFIG_FLAG.FOLLOW_REFERENCES, true);
					put(CONFIG_FLAG.GENERATE_JSON_FILE, false);
					put(CONFIG_FLAG.PATH_DELETE, false);
					put(CONFIG_FLAG.PATH_GET, true);
					put(CONFIG_FLAG.PATH_PATCH, false);
					put(CONFIG_FLAG.PATH_POST, false);
					put(CONFIG_FLAG.PATH_PUT, false);
					put(CONFIG_FLAG.REQUIRED_PROPERTIES_FROM_CARDINALITY, false);
					put(CONFIG_FLAG.USE_INHERITANCE_REFERENCES, false);
					put(CONFIG_FLAG.USE_KEBAB_CASE_PATHS, false);
				}
			};

	String DEFAULT_OUTPUT_DIRECTORY = "outputs";
	String DEFAULT_PROJECT_NAME = "default_project";
	public OpenAPI openapi;
	public String output_dir = DEFAULT_OUTPUT_DIRECTORY;
	public String name = DEFAULT_PROJECT_NAME;
	public Set<String> paths;
	public Set<String> ontologies;
	private EndpointConfig endpoint;
	private AuthConfig auth;
	public FirebaseConfig firebase;
	public Map<String, List<RelationConfig>> relations;
	private LinkedHashMap<String, PathItem> custom_paths = null;
	public Set<String> classes;
	public PropertyAnnotationConfig property_annotations;

	public Boolean getEnable_get_paths() {
		return this.configFlags.get(CONFIG_FLAG.PATH_GET);
	}

	public void setEnable_get_paths(Boolean enable_get_paths) {
		this.configFlags.put(CONFIG_FLAG.PATH_GET, enable_get_paths);
	}

	public Boolean getEnable_patch_paths() {
		return this.configFlags.get(CONFIG_FLAG.PATH_PATCH);
	}

	public void setEnable_patch_paths(Boolean enable_patch_paths) {
		this.configFlags.put(CONFIG_FLAG.PATH_PATCH, enable_patch_paths);
	}

	public Boolean getEnable_post_paths() {
		return this.configFlags.get(CONFIG_FLAG.PATH_POST);
	}

	public void setEnable_post_paths(Boolean enable_post_paths) {
		this.configFlags.put(CONFIG_FLAG.PATH_POST, enable_post_paths);
	}

	public Boolean getEnable_put_paths() {
		return this.configFlags.get(CONFIG_FLAG.PATH_PUT);
	}

	public void setEnable_put_paths(Boolean enable_put_paths) {
		this.configFlags.put(CONFIG_FLAG.PATH_PUT, enable_put_paths);
	}

	public Boolean getEnable_delete_paths() {
		return this.configFlags.get(CONFIG_FLAG.PATH_DELETE);
	}

	public void setEnable_delete_paths(Boolean enable_delete_paths) {
		this.configFlags.put(CONFIG_FLAG.PATH_DELETE, enable_delete_paths);
	}

	public String getOutput_dir() {
		return this.output_dir;
	}

	public void setOutput_dir(String output_dir) {
		this.output_dir = output_dir;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getPaths() {
		return this.paths;
	}

	public void setPaths(Set<String> paths) {
		this.paths = paths;
	}

	public Set<String> getOntologies() {
		return this.ontologies;
	}

	public void setOntologies(Set<String> ontologies) {
		this.ontologies = ontologies;
	}

	public EndpointConfig getEndpoint() {
		return this.endpoint;
	}

	public void setEndpoint(EndpointConfig endpoint) {
		this.endpoint = endpoint;
	}

	public FirebaseConfig getFirebase() {
		return this.firebase;
	}

	public void setFirebase(FirebaseConfig firebase) {
		this.firebase = firebase;
	}

	public Map<String, List<RelationConfig>> getRelations() {
		return this.relations;
	}

	public void setRelations(Map<String, List<RelationConfig>> relations) {
		this.relations = relations;
	}

	public LinkedHashMap<String, PathItem> getCustom_paths() {
		return this.custom_paths;
	}

	public void setCustom_paths(LinkedHashMap<String, PathItem> custom_paths) {
		this.custom_paths = custom_paths;
	}

	public OpenAPI getOpenapi() {
		return this.openapi;
	}

	public void setOpenapi(OpenAPI openapi) {
		this.openapi = openapi;
	}

	public Set<String> getClasses() {
		return this.classes;
	}

	public void setClasses(Set<String> classes) {
		this.classes = classes;
	}

	public Boolean getAlways_generate_arrays() {
		return this.configFlags.get(CONFIG_FLAG.ALWAYS_GENERATE_ARRAYS);
	}

	public void setAlways_generate_arrays(Boolean always_generate_arrays) {
		this.configFlags.put(CONFIG_FLAG.ALWAYS_GENERATE_ARRAYS, always_generate_arrays);
	}

	public Boolean getFix_singular_plural_property_names() {
		return this.configFlags.get(CONFIG_FLAG.FIX_SINGULAR_PLURAL_PROPERTY_NAMES);
	}

	public void setFix_singular_plural_property_names(Boolean fix_singular_plural_property_names) {
		this.configFlags.put(
				CONFIG_FLAG.FIX_SINGULAR_PLURAL_PROPERTY_NAMES, fix_singular_plural_property_names);
	}

	public Boolean getFollow_references() {
		return this.configFlags.get(CONFIG_FLAG.FOLLOW_REFERENCES);
	}

	public void setFollow_references(Boolean follow_references) {
		this.configFlags.put(CONFIG_FLAG.FOLLOW_REFERENCES, follow_references);
	}

	public Boolean getUse_inheritance_references() {
		return this.configFlags.get(CONFIG_FLAG.USE_INHERITANCE_REFERENCES);
	}

	public void setUse_inheritance_references(Boolean use_inheritance_references) {
		this.configFlags.put(CONFIG_FLAG.USE_INHERITANCE_REFERENCES, use_inheritance_references);
	}

	public Boolean getDefault_descriptions() {
		return this.configFlags.get(CONFIG_FLAG.DEFAULT_DESCRIPTIONS);
	}

	public void setDefault_descriptions(Boolean default_descriptions) {
		this.configFlags.put(CONFIG_FLAG.DEFAULT_DESCRIPTIONS, default_descriptions);
	}

	public Boolean getDefault_properties() {
		return this.configFlags.get(CONFIG_FLAG.DEFAULT_PROPERTIES);
	}

	public void setDefault_properties(Boolean default_properties) {
		this.configFlags.put(CONFIG_FLAG.DEFAULT_PROPERTIES, default_properties);
	}

	public Boolean getRequired_properties_from_cardinality() {
		return this.configFlags.get(CONFIG_FLAG.REQUIRED_PROPERTIES_FROM_CARDINALITY);
	}

	public void setRequired_properties_from_cardinality(
			Boolean required_properties_from_cardinality) {
		this.configFlags.put(
				CONFIG_FLAG.REQUIRED_PROPERTIES_FROM_CARDINALITY, required_properties_from_cardinality);
	}

	public Boolean getUse_kebab_case_paths() {
		return this.configFlags.get(CONFIG_FLAG.USE_KEBAB_CASE_PATHS);
	}

	public void setUse_kebab_case_paths(Boolean use_kebab_case_paths) {
		this.configFlags.put(CONFIG_FLAG.USE_KEBAB_CASE_PATHS, use_kebab_case_paths);
	}

	public Boolean getGenerate_json_file() {
		return this.configFlags.get(CONFIG_FLAG.GENERATE_JSON_FILE);
	}

	public void setGenerate_json_file(Boolean generate_json_file) {
		this.configFlags.put(CONFIG_FLAG.GENERATE_JSON_FILE, generate_json_file);
	}

	public AuthConfig getAuth() {
		return this.auth;
	}

	public void setAuth(AuthConfig auth) {
		this.auth = auth;
	}

	/**
	 * The property annotations may be null (because it doesn't exist in the config file). We wrap it
	 * within an {@link Optional} for determining whether a value exists.
	 *
	 * @return a {@link PropertyAnnotationConfig} parameterized {@link Optional}
	 */
	public Optional<PropertyAnnotationConfig> getProperty_annotations() {
		if (this.property_annotations != null) {
			return Optional.ofNullable(this.property_annotations);
		} else {
			return Optional.empty();
		}
	}

	public void setProperty_annotations(PropertyAnnotationConfig property_annotations) {
		this.property_annotations = property_annotations;
	}

	/**
	 * Get the value of a particular configuration flag.
	 *
	 * @param {flag} the configuration flag name
	 * @return The flag's value (true/false/null).
	 */
	public Boolean getConfigFlagValue(CONFIG_FLAG flag) {
		return this.configFlags.get(flag);
	}

	/**
	 * Get map of all config flags and their values.
	 *
	 * @return Map of CONFIG_FLAGs and their Boolean value (true/false/null).
	 */
	public Map<CONFIG_FLAG, Boolean> getConfigFlags() {
		return this.configFlags;
	}
}
