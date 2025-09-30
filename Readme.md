# NIST Library

![Build&Test](https://github.com/aagsolutions/nbis/actions/workflows/main.yml/badge.svg) [![Maven Central](https://img.shields.io/maven-central/v/eu.aagsolutions.img/nbis)](https://central.sonatype.com/artifact/eu.aagsolutions.img/nbis/0.3.3) [![GitHub license](https://img.shields.io/badge/license-MIT-green.svg?style=flat)](https://opensource.org/licenses/MIT) [![CodeQL Scan Status](https://github.com/aagsolutions/nbis/actions/workflows/github-code-scanning/codeql/badge.svg)](https://github.com/aagsolutions/nbis/actions/workflows/github-code-scanning/codeql)

A Kotlin implementation of NIST Biometric Image Software (NBIS) for reading, writing, and building NIST records. Compatible with any JVM language.

## About

NBIS is a library implemented in Kotlin to extract, decode, build and write NIST compressed files. For more information about NIST Biometric Image Software, visit the [official NIST page](http://www.nist.gov/itl/iad/ig/nbis.cfm).

## Features

- ✅ **Read** NIST files from various input sources
- ✅ **Write** NIST files to output streams
- ✅ **Build** NIST files programmatically
- ✅ Support for record types 1-17
- ✅ JVM language compatibility (Java, Kotlin, Scala, etc.)
- ✅ Comprehensive field access and manipulation

## NIST Record Types Support

| Record Type | Description | Read | Write | Build |
|-------------|-------------|------|-------|-------|
| 1 | Transaction Information Record | ✅ | ✅ | ✅ |
| 2 | User-defined descriptive text | ✅ | ✅ | ✅ |
| 3 | Low-resolution grayscale fingerprint image | ✅ | ✅ | ✅ |
| 4 | High-resolution grayscale fingerprint image | ✅ | ✅ | ✅ |
| 5 | Low-resolution binary fingerprint image | ✅ | ✅ | ✅ |
| 6 | High-resolution binary fingerprint image | ✅ | ✅ | ✅ |
| 7 | User-defined image | ✅ | ✅ | ✅ |
| 8 | Signature image | ✅ | ✅ | ✅ |
| 9 | Minutiae data (for fingerprints) | ✅ | ✅ | ✅ |
| 10 | Facial and SMT image | ✅ | ✅ | ✅ |
| 11 | Forensic and investigatory voice data | ✅ | ✅ | ✅ |
| 12 | Forensic dental and oral data | ✅ | ✅ | ✅ |
| 13 | Variable-resolution latent friction ridge image | ✅ | ✅ | ✅ |
| 14 | Variable-resolution fingerprint image | ✅ | ✅ | ✅ |
| 15 | Palm print image | ✅ | ✅ | ✅ |
| 16 | User-defined variable-resolution test image | ✅ | ✅ | ✅ |
| 17 | Iris image | ✅ | ✅ | ✅ |
| 18 | DNA data | ❌ | ❌ | ❌ |
| 19 | Plantar (footprint) image | ❌ | ❌ | ❌ |
| 20 | Original image record | ❌ | ❌ | ❌ |

## Installation

### Maven
```xml
<dependency>
    <groupId>eu.aagsolutions.img</groupId>
    <artifactId>nbis</artifactId>
    <version>0.3.3</version>
</dependency>
```

### Gradle (Groovy DSL)

```groovy
implementation 'eu.aagsolutions.img:nbis:0.3.3'
```

### Gradle (Kotlin DSL)

```kotlin
implementation("eu.aagsolutions.img:nbis:0.3.3")
```

## Quick Start

### Reading a NIST File

#### Kotlin
```kotlin
val nistFile = NistFileReader(inputStream).use { reader -> reader.read() }
val transactionInformationRecord = nistFile.getTransactionInformationRecord()
```
#### Java
```java
import eu.aagsolutions.img.nbis.io.NistFileReader;
import java.io.FileInputStream;
        
try (NistFileReader reader = new NistFileReader(inputStream)) {
    NistFile nistFile = reader.read();
    TransactionInformationRecord transactionInformationRecord = nistFile.getTransactionInformationRecord();
} catch (IOException e) {
    // Handle the potential IOException from NistFileReader or its operations
}
```
### Writing a NIST File
#### Kotlin
```kotlin
NistFileWriter(FileOutputStream("output.nist")).use { writer -> writer.write(nistFile) }
```
#### Java
```java
import eu.aagsolutions.img.nbis.io.NistFileWriter;
import eu.aagsolutions.img.nbis.io.NistFile;
import java.io.FileOutputStream;
import java.io.IOException;

NistFile nistFile = new NistFile();
try (FileOutputStream fos = new FileOutputStream("output.nist");
     NistFileWriter writer = new NistFileWriter(fos)) {
    writer.write(nistFile);
} catch (IOException e) {
    // Handle file I/O errors
}
```

## API Documentation

### Core Classes

#### `NistFile`
The main container for NIST records.

**Key Methods:**
- `getTransactionInformationRecord()` - Get the required Type 1 record
- `getRecordsByType(RecordType)` - Get all records of a specific type
- `getRecordByTypeAndIdc(RecordType, Int)` - Get a specific record by type and IDC
- `getAllRecords()` - Get all records in the file

#### `NistFileReader`
Reads NIST files from input streams.

#### `NistFileWriter`
Writes NIST files to output streams.

#### `NistFileBuilder`
Programmatically builds NIST files.

**Usage:**

```kotlin 
val builder = NistFileBuilder()
    .withTransactionInformationRecord(informationRecord)
    .withUserDefinedDescriptionTextRecords(userDefinedTextRecords)
val nistFile = builder.build()
```

### Working with Records

#### Accessing Field Data
```kotlin 
// Get field as text 
val idcValue = record.getFieldText(DefaultFields.IDC)
// Get binary data (for image records) 
val imageData = record.getBinaryData()
// Get specific field object 
val field = record.getField(DefaultFields.LEN)
```

#### Record Types

All record types extend `BaseRecord` and provide type-specific functionality:

- `TransactionInformationRecord` - Type 1 records
- `UserDefinedTextRecord` - Type 2 records
- `HighResolutionGrayscaleFingerprintRecord` - Type 4 records
- `MinutiaeDataRecord` - Type 9 records
- And more...

### Field Types

The library supports different field types:

- **`TextField`** - Text-based fields
- **`ImageField`** - Binary image data fields

## Complete Examples

### Example 1: Reading and Analyzing a NIST File
```kotlin
fun analyzeNistFile(filePath: String) { 
    val nistFile = NistFileReader(FileInputStream(filePath)).use { it.read() }
    // Print file information
    val transaction = nistFile.getTransactionInformationRecord()
    println("NIST File Analysis:")
    println("- Version: ${transaction.getFieldText(TransactionInformationFields.VER)}")
    println("- Agency: ${transaction.getFieldText(TransactionInformationFields.ORI)}")

// Count records by type
    val recordCounts = nistFile.getAllRecords()
        .groupBy { it.recordId }
        .mapValues { it.value.size }

    println("\nRecord Counts:")
    recordCounts.forEach { (type, count) ->
        println("- Type $type: $count records")
    }

// Analyze fingerprint records
    val fingerprintRecords = nistFile.getRecordsByType(RecordType.RT4)
    println("\nFingerprint Records: ${fingerprintRecords.size}")
    fingerprintRecords.forEach { record ->
        val idc = record.getFieldText(DefaultFields.IDC)
        println("- IDC $idc")
    }
}
```
### Example 2: Building a NIST File from Scratch

```kotlin
val nistFile = NistFileBuilder()
        .withTransactionInformationRecord(transactionInformationRecord)
        .withUserDefinedDescriptionTextRecords(userDefinedText as List<UserDefinedTextRecord>)
        .withFacialAndSmtImageRecords(imageRecords)
        .build()
```
## Requirements
* Java: 21 or higher
* Kotlin: 2.1 or higher

## Inspiration and Credits
This project was inspired by the need for a robust and efficient library for handling NIST biometric image files. The inspiration comes from the original NBIS library, which is written in C and has been widely used in the biometric community. 
The goal of this project is to provide a modern, Kotlin-based alternative that is easier to use and maintain.

The project also draws inspiration from the work of other developers who have contributed to the open-source community with similar libraries. By leveraging their work and building upon it, we aim to create a high-quality, reliable, and efficient library for working with NIST biometric image files:
* [JNBIS](https://github.com/mhshams/jnbis)
* [NIST4J](https://github.com/nist4j/nist4j)
