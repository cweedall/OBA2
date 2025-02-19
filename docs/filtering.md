Some ontologies contain numerous classes. However, you can be interested in a subgroup.
OBA can filter the classes.

The following example is selecting two classes:

- http://dbpedia.org/ontology/Genre
- http://dbpedia.org/ontology/Band

```yaml
### For more information about the section. Go to the official documentation
openapi:
  openapi: 3.0.1
  info:
    description: This is the API of the DBpedia Ontology
    title: DBpedia
    version: v1.3.0
  externalDocs:
    description: DBpedia
    url: https://w3id.org/okn/o/sdm
  servers:
    - url: https://dbpedia.oba.isi.edu/v1.3.0
    - url: http://localhost:8080/v1.3.0

## Ontologies
### List of ontologies
ontologies:
  - https://gist.githubusercontent.com/mosoriob/cec147b24bd241295584dfcc21c21b93/raw/b6fa41ddf93212d967f35da20278f54d2ae2d40d/gistfile1.txt

## SPARQL information
endpoint:
  url: http://endpoint.mint.isi.edu/modelCatalog-1.2.0
  prefix: https://w3id.org/okn/i/mint
  graph_base: http://ontosoft.isi.edu:3030/modelCatalog-1.2.0/data/

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

The result is available at: [DBPedia Music](https://app.swaggerhub.com/apis/mosoriob/dbpedia-music/v1.3.0)

### Following references

If you inspect the properties of a Band, you can see the a Band has one or more locationCity. However, a locationCity is a object then you don't have information about the object.

```yaml
components:
  schemas:
    Band:
      properties:
        locationCity:
          items:
            type: object
          nullable: true
          type: array
```

The option `follow_references` enables OBA to follow the references when generating the OpenAPI spec.

For comparison, let's enable the option for the previous example.

Assume the following information is true for the ontology:

- A city has 423 properties.
- One property is `leaderName` and a `leaderName` is Person reference.
- A person has 285 properties.
- (this is **a lot** of properties to process)

!!! warning
For large ontologies, we don't recommend using the `follow_references` option because the result can be too heavy/large.

```yaml
components:
  schemas:
    Band:
      locationCity:
        items:
          $ref: "#/components/schemas/City"
        nullable: true
        type: array
    City:
      properties:
        cityType:
          items:
            type: string
          nullable: true
          type: array
        irishName:
          items:
            type: string
          nullable: true
          type: array
        reffBourgmestre:
          items:
            $ref: "#/components/schemas/Person"
          nullable: true
          type: array
        communityIsoCode:
          items:
            type: string
          nullable: true
          type: array
        leaderName:
          items:
            $ref: "#/components/schemas/Person"
          nullable: true
          type: array
    Person:
      properties:
        parent:
          items:
            $ref: "#/components/schemas/Person"
          nullable: true
          type: array
        viafId:
          items:
            type: string
          nullable: true
          type: array
        competitionTitle:
          items:
            $ref: "#/components/schemas/SportsEvent"
          nullable: true
          type: array
        artPatron:
          items:
            $ref: "#/components/schemas/Artist"
          nullable: true
          type: array
        hairColour:
          items:
            type: string
          nullable: true
          type: array
        ...
        ...etc..
        ...
        finalProperty:
          items:
            type: string
          nullable: true
          type: array
```

#### Using inheritance references

Enabling option `use_inheritance_references` only works if `follow_references` is also enabled (which it is by default - see above). Whereas `follow_references` allows `$ref` to be used to reference other schemas, `use_inheritance_references` allows references to parent/super classes to be used via the `allOf:` option for a class schema. This option can reduce the size of the OpenAPI spec at the cost of added OpenAPI parser/UI processing time.

For example, lets say that a `Doctor` is a subclass of a `Person`. The `Doctor` has one property `licenseDetails` and inherits all other properties from `Person`. If `use_inheritance_references` is disabled, then its schema will look like:

```yaml
components:
  schemas:
    Doctor:
      properties:
        licenseDetails:
          items:
            type: object
          nullable: true
          type: array
        firstPersonProperty:
          items:
            type: string
          nullable: true
          type: array
        ...
        ...etc..
        ...
        finalPersonProperty:
          items:
            type: string
          nullable: true
          type: array
```

Enabling option `use_inheritance_references` allows the `Person` schema to be referenced completely, so that the `Person`-specific properties only appear on the `Person` schema. The result looks like:

```yaml
components:
  schemas:
    Doctor:
      allOf:
        - type: object
        - $ref: "#components/schemas/Person"
      properties:
        licenseDetails:
          items:
            type: object
          nullable: true
          type: array
    Person:
      properties:
        firstPersonProperty:
          items:
            type: string
          nullable: true
          type: array
        ...
        ...etc..
        ...
        finalPersonProperty:
          items:
            type: string
          nullable: true
          type: array
```

In some cases, the subclass inherits a property and then has additional restrictions for the inherited property that do not exist on the parent/super class. In these situations, the entire property _will_ be copied to the subclass with all of the parent/super class property information _and_ the subclass's restrictions.

### Including default schema descriptions

It is generally good practice to include a high-level description for a schema. By default, a placeholder description is included with the text `Description not available`. For example:

```yaml
components:
  schemas:
    YourClass:
      description: Description not available
      properties: {}
      type: object
```

The option `default_descriptions` allows you to disable the default description for a schema (i.e. if there is no description/comment defined for an entity/class in the ontology). By setting the `default_descriptions` value to `false`, the above example becomes:

```yaml
components:
  schemas:
    YourClass:
      properties: {}
      type: object
```

### Including default schema properties

You may wish to include common properties for each even if not defined for the entity/class. Currently, the default properites that are added to each schema are `description`, `id`, `label`, and `type`. Additional default properties are included (`eventDateTime`, `quantity`, `isBool`) which are meant to provide examples are other common data types. For example:

```yaml
components:
  schemas:
    YourClass:
      properties:
        propertyA:
          type: string
        propertyB:
          type: integer
        eventDateTime:
          description: a date/time of the resource
          format: date-time
          nullable: true
          type: string
        quantity:
          description: a number quantity of the resource
          nullable: true
          type: number
        isBool:
          description: a boolean indicator of the resource
          nullable: true
          type: boolean
        description:
          description: small description
          nullable: true
          type: string
        label:
          description: short description of the resource
          nullable: true
          type: string
        id:
          description: identifier
          format: int32
          nullable: false
          type: integer
        type:
          description: type(s) of the resource
          items:
            type: string
          nullable: true
          type: array
      type: object
```

The option `default_properties` allows you to disable the default properties for a schema. If one or more of the properties are defined for the class, however, the property will still be included in the OpenAPI YAML specification. By setting the `default_properties` value to `false`, the above example becomes:

```yaml
components:
  schemas:
    YourClass:
      properties:
        propertyA:
          type: string
        propertyB:
          type: integer
      type: object
```

### Generating property types as array of items uniformly vs. non-array when cardinality/restrictions warrant it

When a property can have multiple items (e.g. a list of names), the property schema will always generate `type: array` with an `items:` key and sub-item types (e.g. `type: string`). However, in cases where the property is restricted by cardinality, the API spec can be generated so that the property is not an array but a single type (e.g. `type: string`). These cases include cardinality where the property is exactly 1 or is a maximum of 1.

The default is to treat everything as an array, such as:

```yaml
components:
  schemas:
    YourClass:
      properties:
        propertyA:
          maxItems: 1
          minItems: 1
          items:
            type: string
          type: array
        propertyB:
          maxItems: 3
          items:
            type: string
          type: array
      type: object
```

The option `always_generate_arrays` allows you to disable the generating all properties with an array of items, if the class restrictions warrant it (e.g. there is exactly one property value allowed). By setting the `always_generate_arrays` value to `false`, the above example becomes:

```yaml
components:
  schemas:
    YourClass:
      properties:
        propertyA:
          type: array
        propertyB:
          maxItems: 3
          items:
            type: string
          type: array
      type: object
```

### Generating list of required property/properties for a class, when cardinality/restrictions warrant it

When a property is known to be required based on class restrictions (e.g. exactly 1 or a minimum >= 1), OpenAPI specification supports a `required` key directly under the property. By default, OBA does not generate this required list of properties (for each class), for example:

```yaml
components:
  schemas:
    YourClass:
      properties:
        propertyA:
          maxItems: 1
          minItems: 1
          items:
            type: string
          type: array
        propertyB:
          maxItems: 3
          items:
            type: string
          type: array
      type: object
```

The option `required_properties_from_cardinality` allows you to generate this list based on class restrictions (e.g. there is exactly one property value or there is a minimum of 1 or more of the property value). By setting the `required_properties_from_cardinality` value to `true`, the above example becomes:

```yaml
components:
  schemas:
    YourClass:
      properties:
        propertyA:
          maxItems: 1
          minItems: 1
          items:
            type: string
          type: array
        propertyB:
          maxItems: 3
          items:
            type: string
          type: array
      type: object
      required:
        - propertyA
```

### Singularization/pluralization of property names, when property is a non-array or array, respectively.

#### NOTE: singularization and pluralization _only_ work for English currently.

API naming conventions recommend that a property with a single value, regardless of nullability, have be spelled as a singular noun. Conversely, if the property requires or allows an array of values, the property name should be spelled as a plural noun. For example, a `color` property should only have one value as a `string` type while a `colors` property should allow an array of multiple values as a `string` type.

There may be cases where the ontology maintains naming conventions which differ from API naming conventions. Or, there may be cases where the property name in the ontology was mistyped, or perhaps changed from singular to plura or vice-versa. In either case, enabling the `fix_singular_plural_property_names` option will compare the property name against the possible/required number of values and rename the property if it does not match. For example, if the ontology specifies a `colors` property but the axiom dictates that the `colors` property have exactly one item (i.e. minimum cardinality of 1 and maximum cardinality of 1), then the property will be renamed to `color` and output a warning message that the change occurred.

By default, OBA does not generate the property names, for example:

```yaml
components:
  schemas:
    YourClass:
      properties:
        colors:
          items:
            maxItems: 1
            minItems: 1
            type: string
          type: array
        moreThanOneThing:
          minItems: 3
          items:
            type: string
          type: array
      type: object
```

By setting the `fix_singular_plural_property_names` value to `true`, the above example becomes:

```yaml
components:
  schemas:
    YourClass:
      properties:
        color:
          maxItems: 1
          minItems: 1
          items:
            type: string
          type: array
        moreThanOneThings:
          minItems: 3
          items:
            type: string
          type: array
      type: object
```
