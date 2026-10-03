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
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_BPX_MANDATORY_RT14
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_CGA_MANDATORY_RT14
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_DATA_RT14
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_FCD_RT14
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_FGP_RT14
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_HLL_MANDATORY_RT14
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_IDC
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_IMP_MANDATORY_RT14
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_LEN
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_SCF_RT14
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_SHPS_O_RT14
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_SIF_RT14
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_SLC_MANDATORY_RT14
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_SRC
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_SVPS_O_RT14
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_THPS_MANDATORY_RT14
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_TVPS_MANDATORY_RT14
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_VLL_MANDATORY_RT14
import eu.aagsolutions.img.nbis.model.enums.reference.CompressionAlgorithm
import eu.aagsolutions.img.nbis.model.enums.reference.FingerprintFrictionRidgePosition
import eu.aagsolutions.img.nbis.model.enums.reference.ImpressionType
import eu.aagsolutions.img.nbis.model.records.VariableResolutionFingerprintRecord

/** Basic Type 14 validation for records containing an embedded image. */
@Suppress("MagicNumber")
class VariableResolutionFingerprintRecordValidator {
    fun validate(record: VariableResolutionFingerprintRecord): List<StandardNistValidatorError> {
        val checks = RecordChecks(record)
        with(checks) {
            check(STD_ERR_LEN) { it.matches(Regex("[0-9]{1,10}")) && it.toLong() > 0 }
            number(STD_ERR_IDC, 0..99)
            check(STD_ERR_IMP_MANDATORY_RT14) { value -> ImpressionType.entries.any { it.code == value } }
            check(STD_ERR_SRC) { it.isNotBlank() && it.none { char -> char.isISOControl() } }
            date(STD_ERR_FCD_RT14)
            number(STD_ERR_HLL_MANDATORY_RT14, 1..99999)
            number(STD_ERR_VLL_MANDATORY_RT14, 1..99999)
            number(STD_ERR_SLC_MANDATORY_RT14, 0..2)
            number(STD_ERR_THPS_MANDATORY_RT14, 1..99999)
            number(STD_ERR_TVPS_MANDATORY_RT14, 1..99999)
            check(STD_ERR_CGA_MANDATORY_RT14) { value -> CompressionAlgorithm.entries.any { it.code == value } }
            number(STD_ERR_BPX_MANDATORY_RT14, 1..99)
            check(STD_ERR_FGP_RT14) { value ->
                value.split('\u001e').all { position ->
                    FingerprintFrictionRidgePosition.entries.any {
                        it.code ==
                            position
                    }
                }
            }
            number(STD_ERR_SHPS_O_RT14, 1..99999, optional = true)
            number(STD_ERR_SVPS_O_RT14, 1..99999, optional = true)
            number(STD_ERR_SCF_RT14, 1..255, optional = true)
            check(STD_ERR_SIF_RT14, optional = true) { it == "Y" }
            image(STD_ERR_DATA_RT14)
        }
        return checks.errors.toList()
    }
}
