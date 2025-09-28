# NIST Library
![Build&Test](https://github.com/atdi/nbis/actions/workflows/main.yml/badge.svg) [![GitHub license](https://img.shields.io/badge/license-MIT-green.svg?style=flat)](https://opensource.org/licenses/MIT)

Kotlin implementation of NIST Biometric Image Software (NBIS).
This library is used to read and write NIST records and can be used with any JVM language.
## About
NBIS is a library, implemented in Kotlin, to extract, decode, build and write NIST compressed files.
NIST Biometric Image Software information can be found [here](http://www.nist.gov/itl/iad/ig/nbis.cfm).

## NIST Records Types

| Record Type | Description                                                              | Read | Write | Build | Validate |
|-------------|--------------------------------------------------------------------------|------|-------|-------|----------|
| 1 | Transaction Information Record                                           | Y    | Y     | Y     | N        |
| 2 | User-defined descriptive text (Interpol version)                         | Y    | Y     | Y     | N        |
| 3 | Low-resolution grayscale fingerprint image (deprecated in some versions) | Y    | Y     | Y     | N        |
| 4 | High-resolution grayscale fingerprint image                              | Y    | Y     | Y     | N        |
| 5 | Low-resolution binary fingerprint image (deprecated)                     | Y    | Y     | Y     | N        |
| 6 | High-resolution binary fingerprint image (deprecated)                    | Y    | Y     | Y     | N        |
| 7 | User-defined image                                                       | Y    | Y     | Y     | N        |
| 8 | Signature image                                                          | Y    | Y     | Y     | N        |
| 9 | Minutiae data (for fingerprints)                                         | Y    | Y     | Y     | N        |
| 10 | Facial and SMT (Scar, Mark, and Tattoo) image                            | Y    | Y     | Y     | N        |
| 11 | Forensic and investigatory voice data                                    | Y    | Y     | Y     | N        |
| 12 | Forensic dental and oral data                                            | Y    | Y     | Y     | N        |
| 13 | Variable-resolution latent friction ridge image (latent prints)          | Y    | Y     | Y     | N        |
| 14 | Variable-resolution fingerprint image                                    | Y    | Y     | Y     | N        |
| 15 | Palm print image                                                         | Y    | Y     | Y     | N        |
| 16 | User-defined variable-resolution test image                              | Y    | Y     | Y     | N        |
| 17 | Iris image                                                               | Y    | Y     | Y     | N        |
| 18 | DNA data                                                                 | N    | N     | N     | N        |
| 19 | Plantar (footprint) image                                                | N    | N     | N     | N        |
| 20 | Original image record (a reference record for other image types)         | N    | N     | N     | N        |