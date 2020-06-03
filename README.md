[ ![Download](https://api.bintray.com/packages/krossovochkin/detekt-exposed-api-extensions/com.krossovochkin.detekt:exposed-api/images/download.svg?version=0.1.0) ](https://bintray.com/krossovochkin/detekt-exposed-api-extensions/com.krossovochkin.detekt:exposed-api/0.1.0/link) [ ![](https://img.shields.io/badge/Version-0.1.0-green)](https://bintray.com/krossovochkin/detekt-exposed-api-extensions/com.krossovochkin.detekt:exposed-api/0.1.0/link)

# detekt-exposed-api-extensions
ExposedApi rule extension for https://detekt.github.io/detekt/

### Why

In Kotlin default visibility modifier is `public`.  
This might become an issue if one develops a library, where `public` should be only for public API, and everything else should be private or internal.  
But it is so easy to forget to put `internal` modifier, so automation help is appreciated.  
[Detekt](https://detekt.github.io/detekt/) is a static analyzer for Kotlin.  
In this repository there is an extension plugin with `ExposedApi` rule.

Top-level class, object, function or property is considered exposed if it has public (or default) visibility modifier and is not marked explicitly as public API available for clients.

### Installation

```
dependencies {
    detektPlugins 'com.krossovochkin.detekt:exposed-api:x.x.x'
}
```

### Configuration

```
library:
  ExposedApi:
    active: true
    ignoreAnnotated: ["PublicApi"]
    ignorePathRegex: [".*detekt.*"]
```

- `active` - boolean value whether to enable rule
- `ignoreAnnotated` - suppress annotated with given annotations places
- `ignorePathRegex` - suppress some files (like tests or application modules) by given regex
