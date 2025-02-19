# Configuration File

OBA supports configuring your documentation builds with a YAML file.

Below is an example YAML file which may require some changes for your project's configuration:

You can find examples in [GitHub](https://github.com/cweedall/OBA2/tree/master/examples)

!!! info
If you experience any issues when using OBA, or if you would like us to support additional exciting features, please open an issue on our [GitHub repository](https://github.com/cweedall/OBA2/issues).

```yaml
#Name of the project
name: dbpedia_music

## OpenAPI Section
### Name, version and URL of the OpenAPI
### For more information about the section. Go to the official documentation
openapi:
  openapi: 3.0.1
  info:
    description: This is the API of the DBpedia Ontology
    title: DBpedia
    version: v1.3.0
  externalDocs:
    description: DBpedia
    url: http://dbpedia.org/
  servers:
    - url: https://dbpedia.dbpedia.oba.isi.edu/v1.3.0
    - url: http://localhost:8080/v1.3.0

## Ontologies
### List of ontologies
ontologies:
  - https://tinyurl.com/dbpediaoba

## SPARQL information
endpoint:
  url: http://dbpedia.org/sparql
  prefix: http://dbpedia.org/resource

## Configuration settings related to OpenAPI's "path" section
path_config:
  ## Creates an empty "path" object within the specification file.
  ## !!IMPORTANT!! overrides all of the "enable" options below.
  disable_all_paths: true
  ## Filter the paths by operation types
  enable_get_paths: true
  enable_post_paths: true
  enable_delete_paths: true
  enable_put_paths: true
  ## For endpoint path names, use "kebab-case" case (all lowercase and separate words with a dash/hyphen).
  ## Synonyms for "kebab-case" include: caterpillar-case, param-case, dash-case, hyphen-case, lisp-case, spinal-case and css-case
  use_kebab_case_paths: false

## Select the classes to add in the API
classes:
  - http://dbpedia.org/ontology/Genre
  - http://dbpedia.org/ontology/Band

## OpenAPI processing
### Configuration details for property annotations from the ontology.
property_annotations:
  ### Define an annotation property which indicates a boolean value whether the object/data property is read-only.
  read_only_flag_name: isReadOnly
  ### Define an annotation property which indicates a boolean value whether the object/data property is write-only.
  write_only_flag_name: isWriteOnly
  ### Define an annotation property which indicates a value (e.g. string, boolean, integer) which should be used for the "example" value for a schema's property.
  example_value_name: exampleValue

## REFERENCES
### Enable/disable schema references.  This can be recursive and cause multiple depths/levels of reference.
follow_references: false
### Enable/disable use of references for class/schema inheritance.
use_inheritance_references: false

## Enable/disable generation of a default description for each schema
default_descriptions: true

## Enable/disable generation of default properties (description, id, label, and type) for each schema
default_properties: true

## Enable/disable generation of arrays for property type(s) all the time, regardless of cardinality / restrictions
always_generate_arrays: true

## Enable/disable generation of list of required properties for a schema, if the the cardinality indicates it is required (e.g. exactly 1)
required_properties_from_cardinality: false

## Enable/disable automatic schema property singularization/pluralization based on current spelling and whether the property is an array or not.
fix_singular_plural_property_names: false
```

## Supported settings

### name

The name of OpenAPI

| Field         | Value  |
| ------------- | ------ |
| **Required:** | `true` |

Example:

```yaml
name: dbpedia_music
```

### output_dir

The output directory of the OpenApi specification files, relative to the root of the project.

| Field         | Value     |
| ------------- | --------- |
| **Required:** | `false`   |
| **Default:**  | `outputs` |

Example:

```yaml
output_dir: outputs
```

Te generate the OpenAPI spec files as JSON, use the `generate_json_file` flag set to `true`.

| Field         | Value     |
| ------------- | --------- |
| **Required:** | `false`   |
| **Type:**     | `Boolean` |
| **Default:**  | `False`   |

```yaml
generate_json_file: true
```

### OpenAPI

Basic information of API using OpenAPI Spec.
More info: [OpenAPI Base file](https://swagger.io/docs/specification/basic-structure/)

| Field         | Value     |
| ------------- | --------- |
| **Required:** | `true`    |
| **Type:**     | `OpenAPI` |

Example:

```yaml
openapi:
  openapi: 3.0.1
  info:
    description: This is the API of the DBpedia Ontology
    title: DBpedia
    version: v1.3.0
  externalDocs:
    description: DBpedia
    url: http://dbpedia.org/
  servers:
    - url: https://dbpedia.dbpedia.oba.isi.edu/v1.3.0
    - url: http://localhost:8080/v1.3.0
```

### Path-related configuration settings

The `path_config` property contains sub-properties which are relevant to the configuration and display settings for the `path` section of the OpenAPI specification.

#### disable_all_paths

Creates an empty `path` section by disabling all paths from displaying. Note that, if true, this will override the other path-related configuration settings (i.e. `enable_get_paths`, `enable_post_paths`, `enable_delete_paths`, `enable_put_paths`).

| Field         | Value     |
| ------------- | --------- |
| **Required:** | `false`   |
| **Type:**     | `boolean` |
| **Default:**  | `false`   |

#### enable_get_paths

Enable the GET method for the paths

| Field         | Value     |
| ------------- | --------- |
| **Required:** | `false`   |
| **Type:**     | `boolean` |
| **Default:**  | `true`    |

#### enable_post_paths:

Enable the POST method for the paths

| Field         | Value     |
| ------------- | --------- |
| **Required:** | `false`   |
| **Type:**     | `boolean` |
| **Default:**  | `false`   |

#### enable_delete_paths

Enable the DELETE method for the paths

| Field         | Value     |
| ------------- | --------- |
| **Required:** | `false`   |
| **Type:**     | `boolean` |
| **Default:**  | `false`   |

#### enable_put_paths

Enable the PUT method for the paths

| Field         | Value     |
| ------------- | --------- |
| **Required:** | `false`   |
| **Type:**     | `boolean` |
| **Default:**  | `false`   |

#### use_kebab_case_paths

For endpoint path names, use "kebab-case" case (all lowercase and separate words with a dash/hyphen). Synonyms for "kebab-case" include: caterpillar-case, param-case, dash-case, hyphen-case, lisp-case, spinal-case and css-case.

| Field         | Value     |
| ------------- | --------- |
| **Required:** | `false`   |
| **Type:**     | `Boolean` |
| **Default:**  | `False`   |

For more information, go to [filtering classes](filtering.md#use_kebab_case_paths)

```yaml
use_kebab_case_paths: true
```

### endpoint

Example

```yaml
endpoint:
  url: http://dbpedia.org/sparql
  prefix: http://dbpedia.org/resource
  # Add the GRAPH clause. Enable it when you are using authentication.
  # OBA uses a graph to store the user contents on a personal namespace.
  # For DBpedia, dont use it.
  graph: http://endpoint.mint.isi.edu/modelCatalog-1.4.0/data/
```

### endpoint.url

The url of the SPARQL Endpoint

| Field         | Value  |
| ------------- | ------ |
| **Required:** | `true` |
| **Type:**     | `url`  |

Example:

```yaml
url: http://dbpedia.org/sparql
```

### endpoint.prefix

The prefix of the SPARQL Endpoint.
This is useful when you create a new resource.

| Field         | Value  |
| ------------- | ------ |
| **Required:** | `true` |
| **Type:**     | `url`  |

Example:

```yaml
prefix: http://dbpedia.org/resource
```

### endpoint.graph_base

OBA uses a graph to store the user contents on a personal namespace.

| Field         | Value  |
| ------------- | ------ |
| **Required:** | `true` |
| **Type:**     | `url`  |

Example:

```yaml
graph_base: http://ontosoft.isi.edu:3030/modelCatalog-1.2.0/data/
```

## ontologies

Example:

```yaml
ontologies:
  - https://tinyurl.com/dbpediaoba
```

| Field         | Value          |
| ------------- | -------------- |
| **Required:** | `true`         |
| **Type:**     | `List[string]` |

## custom_queries_directory

| Field         | Value        |
| ------------- | ------------ |
| **Required:** | `false`      |
| **Type:**     | `List[Path]` |

[Go to how to add custom queries](adding_custom_queries.md) for more information

## filtering

Some ontologies contain numerous classes. However, you can be interested in a subgroup. OBA can filter the classes.

### classes

| Field         | Value       |
| ------------- | ----------- |
| **Required:** | `false`     |
| **Type:**     | `List[URI]` |

```yaml
classes:
  - http://dbpedia.org/ontology/Genre
  - http://dbpedia.org/ontology/Band
```

### follow_references

| Field         | Value     |
| ------------- | --------- |
| **Required:** | `false`   |
| **Type:**     | `Boolean` |
| **Default:**  | `True`    |

For more information, go to [filtering classes](filtering.md#following-references)

```yaml
follow_references: false
```

### use_inheritance_references

| Field         | Value     |
| ------------- | --------- |
| **Required:** | `false`   |
| **Type:**     | `Boolean` |
| **Default:**  | `False`   |

For more information, go to [filtering classes](filtering.md#use_inheritance_references)

```yaml
use_inheritance_references: false
```

### default_descriptions

Enable/disable generation of a default description for each schema.

| Field         | Value     |
| ------------- | --------- |
| **Required:** | `false`   |
| **Type:**     | `Boolean` |
| **Default:**  | `True`    |

For more information, go to [filtering classes](filtering.md#default_descriptions)

```yaml
default_descriptions: false
```

### default_properties

Enable/disable generation of default properties (description, id, label, and type) for each schema.

| Field         | Value     |
| ------------- | --------- |
| **Required:** | `false`   |
| **Type:**     | `Boolean` |
| **Default:**  | `True`    |

For more information, go to [filtering classes](filtering.md#default_properties)

```yaml
default_properties: false
```

### always_generate_arrays

Enable/disable generation of arrays for property type(s) all the time, regardless of cardinality / restrictions.

| Field         | Value     |
| ------------- | --------- |
| **Required:** | `false`   |
| **Type:**     | `Boolean` |
| **Default:**  | `True`    |

For more information, go to [filtering classes](filtering.md#always_generate_arrays)

```yaml
always_generate_arrays: true
```

### required_properties_from_cardinality

Enable/disable generation of list of required properties for a schema, if the the cardinality indicates it is required (e.g. exactly 1).

| Field         | Value     |
| ------------- | --------- |
| **Required:** | `false`   |
| **Type:**     | `Boolean` |
| **Default:**  | `False`   |

For more information, go to [filtering classes](filtering.md#required_properties_from_cardinality)

```yaml
required_properties_from_cardinality: true
```

### fix_singular_plural_property_names

Enable/disable automatic schema property singularization/pluralization based on current spelling and whether the property is an array or not.

| Field         | Value     |
| ------------- | --------- |
| **Required:** | `false`   |
| **Type:**     | `Boolean` |
| **Default:**  | `False`   |

For more information, go to [filtering classes](filtering.md#fix_singular_plural_property_names)

```yaml
fix_singular_plural_property_names: true
```

## auth

Add login to the API and add security to the following methods: `POST`, `PUT` and `DELETE`

### provider

| Field         | Value  |
| ------------- | ------ |
| **Required:** | `true` |
| **Type:**     | `str`  |

The providers supported:

- Firebase

## Providers

### firebase

You can use firebase to login

```
firebase:
  key: google_key
```

### firebase.key

To authenticate a service account and authorize it to access Firebase services, you must generate a private key file.

| Field         | Value  |
| ------------- | ------ |
| **Required:** | `true` |
| **Type:**     | `str`  |

```
firebase:
  key: google_key
```

## OpenAPI processing

The ontology can be customized to include details relevant for the OpenAPI specification and how the specification should be configured. These are things that cannot necessarily be determined directly from the ontology, such as whether a data property should be read-only or write-only.

### property_annotations

Example

```yaml
property_annotations:
  read_only_flag_name: isReadOnly
  write_only_flag_name: isWriteOnly
  example_value_name: exampleValue
```

### property_annotations.read_only_flag_name

The annotation property name which indicates whether a property is read-only or not. This indicates whether it appears _only_ in GET operations.

| Field         | Value    |
| ------------- | -------- |
| **Required:** | `false`  |
| **Type:**     | `string` |

Example:

```yaml
read_only_flag_name: isReadOnly
```

### property_annotations.write_only_flag_name

The annotation property name which indicates whether a property is write-only or not. This indicates whether it appears _only_ in PUT/POST operations.

| Field         | Value    |
| ------------- | -------- |
| **Required:** | `false`  |
| **Type:**     | `string` |

Example:

```yaml
write_only_flag_name: isWriteOnly
```

### property_annotations.example_value_name

The annotation property name which specifies a specific value to be used for the `example` in the OpenAPI schema's property.

| Field         | Value    |
| ------------- | -------- |
| **Required:** | `false`  |
| **Type:**     | `string` |

Example:

```yaml
example_value_name: exampleValue
```
