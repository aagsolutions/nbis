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

package eu.aagsolutions.img.nbis.validators.records

import eu.aagsolutions.img.nbis.calculators.Calculators.fromRecordsMapToListOfEntries
import eu.aagsolutions.img.nbis.calculators.Calculators.fromTextTolistOfEntries
import eu.aagsolutions.img.nbis.model.NistEntry
import eu.aagsolutions.img.nbis.model.NistFile
import eu.aagsolutions.img.nbis.model.enums.records.TransactionInformationFields

/**
 * Base validator for the Transaction Information Record (Type 10).
 */
class TransactionInformationRecordValidator {
    /**
     * Validates the CNT field of the Transaction Information Record.
     *
     * @param nistContent The NIST file to validate.
     * @return `true` if the CNT field is valid, `false` otherwise.
     */
    fun validateCNTField(nistContent: NistFile): Boolean {
        val cntFiled =
            nistContent
                .getTransactionInformationRecord()
                .getFieldText(TransactionInformationFields.CNT) ?: return false
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
        return entriesFromCnt.equals(entriesFromNistFile)
    }
}
