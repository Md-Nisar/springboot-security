
---

# Forbidden APIs Checker Setup

This document outlines the setup and usage of the **Forbidden APIs Checker** plugin in the project. It ensures that any usage of forbidden APIs, deprecated methods, and unsafe practices is detected during the build process.

## Plugin Setup

### 1. **Add the Forbidden APIs Checker Plugin to `pom.xml`**

To enforce restrictions on certain APIs, the Forbidden APIs Checker plugin is used during the Maven build process. Below is the configuration for adding the plugin to your `pom.xml`:

```xml
<plugin>
    <groupId>de.thetaphi</groupId>
    <artifactId>forbiddenapis</artifactId>
    <version>3.4</version>
    <executions>
        <execution>
            <phase>verify</phase>
            <goals>
                <goal>check</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <bundledSignatures>
            <bundledSignature>jdk-unsafe</bundledSignature>
            <bundledSignature>jdk-deprecated</bundledSignature>
            <bundledSignature>jdk-non-portable</bundledSignature>
            <bundledSignature>jdk-internal</bundledSignature>
            <bundledSignature>jdk-system-out</bundledSignature>
            <bundledSignature>jdk-reflection</bundledSignature>
        </bundledSignatures>
        <failOnMissingClasses>true</failOnMissingClasses>
    </configuration>
</plugin>
```

### 2. **Configuration Explanation**

- **Plugin Version**: The plugin version `3.4` is specified to ensure compatibility.
- **Execution Phase**: The plugin is configured to run during the `verify` phase of the Maven lifecycle, which ensures checks are done before the build is completed.
- **Bundled Signatures**: The plugin comes with pre-configured signatures to block unsafe, deprecated, non-portable, internal, and reflection-based API usage. These are specified as `bundledSignature` entries.
- **Fail on Missing Classes**: The `failOnMissingClasses` option is set to `true`, meaning the build will fail if any forbidden APIs are used.

### 3. **Bundled Signatures**

The following bundled signatures are included by default:

- **`jdk-unsafe`**: Blocks usage of unsafe Java classes, such as `sun.misc.Unsafe`.
- **`jdk-deprecated`**: Blocks deprecated Java methods and classes.
- **`jdk-non-portable`**: Blocks usage of platform-dependent, non-portable classes.
- **`jdk-internal`**: Blocks usage of internal, non-public Java APIs.
- **`jdk-system-out`**: Blocks usage of `System.out` and `System.err`.
- **`jdk-reflection`**: Blocks unsafe usage of reflection APIs.

### 4. **Adding Custom Signatures**

You can add custom forbidden API rules for your project by specifying additional signatures. For example, to forbid usage of classes in a legacy package:

```xml
<configuration>
    <bundledSignatures>
        <!-- Include bundled signatures here -->
        <bundledSignature>jdk-unsafe</bundledSignature>
        <bundledSignature>jdk-deprecated</bundledSignature>
    </bundledSignatures>

    <signatures>
        <!-- Block specific legacy classes -->
        <signature>com.example.legacy.*</signature>
        <signature>com.example.utils.LegacyClass</signature>
    </signatures>

    <failOnMissingClasses>true</failOnMissingClasses>
</configuration>
```

### 5. **Running the Check**

To run the Forbidden APIs check, use the following Maven command:

```bash
mvn verify
```

This will trigger the check, and if any forbidden APIs are found in the code, the build will fail, providing details of which forbidden classes or methods were used.

### 6. **Expected Output**

If forbidden APIs are found, the build output will show an error message with the details, such as:

```text
Forbidden API detected:
  -> java.lang.System#out
  -> com.example.utils.LegacyClass#deprecatedMethod()
  -> com.example.legacy.OldClass
```

This helps maintain code quality by ensuring that unsafe or deprecated APIs are not used.

## Conclusion

By using the Forbidden APIs Checker plugin, you ensure that your codebase remains clean and does not rely on unsafe, deprecated, or non-portable APIs. You can customize the configuration to enforce additional rules specific to your project.

---
