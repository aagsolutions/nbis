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

package eu.aagsolutions.img.nbis.validation

import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_CGA_RT10
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_CSP_RT10
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_DATA_RT10
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_HLL_RT10
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_IDC_RT10
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_IMT_RT10
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_LEN_RT10
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_PHD_RT10
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_SLC_RT10
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_SRC_RT10_U
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_THPS_RT10
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_TVPS_RT10
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_VLL_RT10
import eu.aagsolutions.img.nbis.model.enums.reference.CompressionAlgorithm
import eu.aagsolutions.img.nbis.model.enums.reference.FacialSMTImageType
import eu.aagsolutions.img.nbis.model.records.FacialAndSMTImageRecord

/** Basic Type 10 validation for records containing an embedded image. */
@Suppress("MagicNumber")
class FacialAndSMTImageRecordValidator {
    fun validate(record: FacialAndSMTImageRecord): List<StandardNistValidatorError> {
        val checks = RecordChecks(record)
        with(checks) {
            check(STD_ERR_LEN_RT10) { it.matches(Regex("[0-9]{1,10}")) && it.toLong() > 0 }
            number(STD_ERR_IDC_RT10, 0..99)
            check(STD_ERR_IMT_RT10) { value -> FacialSMTImageType.entries.any { it.code == value } }
            check(STD_ERR_SRC_RT10_U) { it.isNotBlank() && it.none { char -> char.isISOControl() } }
            date(STD_ERR_PHD_RT10)
            number(STD_ERR_HLL_RT10, 1..99999)
            number(STD_ERR_VLL_RT10, 1..99999)
            number(STD_ERR_SLC_RT10, 0..2)
            number(STD_ERR_THPS_RT10, 1..99999)
            number(STD_ERR_TVPS_RT10, 1..99999)
            check(STD_ERR_CGA_RT10) { value -> CompressionAlgorithm.entries.any { it.code == value } }
            check(STD_ERR_CSP_RT10) { it in setOf("UNK", "GRAY", "RGB", "SRGB", "YCC", "SYCC") }
            image(STD_ERR_DATA_RT10)
        }
        return checks.errors.toList()
    }
}
