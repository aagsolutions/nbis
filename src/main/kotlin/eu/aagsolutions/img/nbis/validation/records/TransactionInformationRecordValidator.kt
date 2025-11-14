/*
 * Copyright (c) 2025 Aurel Avramescu.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the “Software”), to deal
 * in the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package eu.aagsolutions.img.nbis.validation.records

import eu.aagsolutions.img.nbis.calculators.Calculators.fromRecordsMapToListOfEntries
import eu.aagsolutions.img.nbis.calculators.Calculators.fromTextTolistOfEntries
import eu.aagsolutions.img.nbis.model.NistEntry
import eu.aagsolutions.img.nbis.model.NistFile
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.enums.Standard
import eu.aagsolutions.img.nbis.model.enums.records.TransactionInformationFields
import eu.aagsolutions.img.nbis.validation.ValidationError
import eu.aagsolutions.img.nbis.validation.ValidationErrors
import eu.aagsolutions.img.nbis.validation.predicates.StringPredicates

/**
 * Base validator for the Transaction Information Record (Type 10).
 */
class TransactionInformationRecordValidator(
    val errors: MutableList<ValidationError>,
    val nistContent: NistFile,
) {
    /**
     * Validates the CNT field of the Transaction Information Record.
     *
     * @return `this` validator instance for method chaining
     */
    fun validateCNTField(): TransactionInformationRecordValidator {
        val cntFiled =
            nistContent
                .getTransactionInformationRecord()
                .getFieldText(TransactionInformationFields.CNT)
        if (cntFiled != null) {
            val entriesFromCnt =
                fromTextTolistOfEntries(cntFiled)
                    .map {
                        NistEntry(it.key.toInt(), it.value.toInt())
                    }.sortedBy { it.key }
            val entriesFromNistFile =
                fromRecordsMapToListOfEntries(nistContent.records)
                    .map {
                        NistEntry(it.key.toInt(), it.value.toInt())
                    }.sortedBy { it.key }
            if (entriesFromCnt != entriesFromNistFile) {
                errors.add(
                    ValidationError(
                        RecordType.RT1,
                        TransactionInformationFields.CNT,
                        ValidationErrors.STD_ERR_CNT_CONTENT_RT1.message,
                        cntFiled,
                    ),
                )
            }
        } else {
            errors.add(
                ValidationError(
                    RecordType.RT1,
                    TransactionInformationFields.CNT,
                    ValidationErrors.STD_ERR_CNT_MISSING_RT1.message,
                    "",
                ),
            )
        }
        return this
    }

    /**
     * Validates the special resolution fields of the Transaction Information Record.
     * These fields are validated using a regular expression.
     *
     * @return `this` validator instance for method chaining
     */
    fun validateSpecialResolutionFields(): TransactionInformationRecordValidator {
        val transactionInformationRecord = nistContent.getTransactionInformationRecord()
        val nsrField = transactionInformationRecord.getFieldText(TransactionInformationFields.NSR) ?: ""
        val ntrField = transactionInformationRecord.getFieldText(TransactionInformationFields.NTR) ?: ""

        if (nistContent.getHighResolutionGrayscaleFingerprintRecords().isNotEmpty()) {
            if (StringPredicates
                    .stringMatches(Regex("^\\d{2}\\.\\d{2}$"))(nsrField)
                    .not()
            ) {
                errors.add(
                    ValidationError(
                        RecordType.RT1,
                        TransactionInformationFields.CNT,
                        ValidationErrors.STD_ERR_NSR_WITH_RT4_INVALID_FORMAT_RT1.message,
                        nsrField,
                    ),
                )
            }

            if (StringPredicates
                    .stringMatches(Regex("^\\d{2}\\.\\d{2}$"))(ntrField)
                    .not()
            ) {
                errors.add(
                    ValidationError(
                        RecordType.RT1,
                        TransactionInformationFields.CNT,
                        ValidationErrors.STD_ERR_NTR_WITH_RT4_INVALID_FORMAT_RT1.message,
                        ntrField,
                    ),
                )
            }
        } else {
            if (StringPredicates
                    .stringMatches(Regex("^00.00\$"))(nsrField)
                    .not()
            ) {
                errors.add(
                    ValidationError(
                        RecordType.RT1,
                        TransactionInformationFields.CNT,
                        ValidationErrors.STD_ERR_NSR_NO_RT4_INVALID_FORMAT_RT1.message,
                        nsrField,
                    ),
                )
            }

            if (StringPredicates
                    .stringMatches(Regex("^00.00\$"))(ntrField)
                    .not()
            ) {
                errors.add(
                    ValidationError(
                        RecordType.RT1,
                        TransactionInformationFields.CNT,
                        ValidationErrors.STD_ERR_NTR_NO_RT4_INVALID_FORMAT_RT1.message,
                        ntrField,
                    ),
                )
            }
        }
        return this
    }

    /**
     * Validates the version field of the Transaction Information Record.
     *
     * @return `this` validator instance for method chaining
     */
    fun validateVersionField(): TransactionInformationRecordValidator {
        val versionField = nistContent.getTransactionInformationRecord().getFieldText(TransactionInformationFields.VER) ?: ""
        if (Standard.findByCode(versionField) == null) {
            errors.add(
                ValidationError(
                    RecordType.RT1,
                    TransactionInformationFields.VER,
                    ValidationErrors.STD_ERR_VER_INVALID_FORMAT_RT1.message,
                    versionField,
                ),
            )
        }
        return this
    }
}
