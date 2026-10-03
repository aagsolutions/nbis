package eu.aagsolutions.img.nbis.validation

import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_CGA_RT10
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_CNT_CONTENT_RT1
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_DATA_RT10
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_DATA_RT14
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_HLL_RT10
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_IDC_RT10
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_IMT_RT10
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_PHD_RT10
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_SHPS_O_RT14
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_SIF_RT14
import eu.aagsolutions.img.nbis.model.enums.records.StandardNistValidatorError.STD_ERR_SLC_RT10
import eu.aagsolutions.img.nbis.model.fields.Field
import eu.aagsolutions.img.nbis.model.fields.ImageField
import eu.aagsolutions.img.nbis.model.fields.TextField
import eu.aagsolutions.img.nbis.model.records.FacialAndSMTImageRecord
import eu.aagsolutions.img.nbis.model.records.TransactionInformationRecord
import eu.aagsolutions.img.nbis.model.records.VariableResolutionFingerprintRecord
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RecordValidatorsTest {
    private fun textFields(vararg values: Pair<Int, String>): Map<Int, Field<*>> = values.associate { it.first to TextField(it.second) }

    private fun imageFields(): Map<Int, Field<*>> =
        textFields(
            1 to "100",
            2 to "00",
            4 to "Agency",
            5 to "20240229",
            6 to "100",
            7 to "100",
            8 to "1",
            9 to "500",
            10 to "500",
            11 to "NONE",
        ) + (999 to ImageField(byteArrayOf(1)))

    @Test
    fun `valid records have no errors`() {
        val transaction =
            TransactionInformationRecord(
                textFields(
                    1 to "100",
                    2 to "0502",
                    3 to "1\u001f0",
                    4 to "TEST",
                    5 to "20240229",
                    7 to "Agency",
                    8 to "Agency",
                    9 to "123",
                    11 to "00.00",
                    12 to "00.00",
                ),
            )
        assertEquals(emptyList(), TransactionInformationRecordValidator().validate(transaction))
        val face = FacialAndSMTImageRecord(imageFields() + textFields(3 to "FACE", 12 to "RGB"))
        assertEquals(emptyList(), FacialAndSMTImageRecordValidator().validate(face))
        val fingerprint = VariableResolutionFingerprintRecord(imageFields() + textFields(3 to "0", 12 to "8", 13 to "1"))
        assertEquals(emptyList(), VariableResolutionFingerprintRecordValidator().validate(fingerprint))
    }

    @Test
    fun `missing fields return multiple errors`() {
        assertTrue(TransactionInformationRecordValidator().validate(TransactionInformationRecord(emptyMap())).size > 5)
        assertTrue(FacialAndSMTImageRecordValidator().validate(FacialAndSMTImageRecord(emptyMap())).contains(STD_ERR_DATA_RT10))
        assertTrue(
            VariableResolutionFingerprintRecordValidator()
                .validate(VariableResolutionFingerprintRecord(emptyMap()))
                .contains(STD_ERR_DATA_RT14),
        )
    }

    @Test
    fun `bad dates numbers references and field types are reported without throwing`() {
        val fields =
            imageFields() +
                textFields(
                    3 to "INVALID",
                    5 to "20230229",
                    6 to "9999999999999999",
                    8 to "3",
                    11 to "BAD",
                    12 to "RGB",
                ) + (2 to ImageField(byteArrayOf(1))) + (999 to TextField("wrong type"))
        val errors = FacialAndSMTImageRecordValidator().validate(FacialAndSMTImageRecord(fields))
        assertTrue(
            errors.containsAll(
                listOf(
                    STD_ERR_IMT_RT10,
                    STD_ERR_PHD_RT10,
                    STD_ERR_HLL_RT10,
                    STD_ERR_SLC_RT10,
                    STD_ERR_CGA_RT10,
                    STD_ERR_IDC_RT10,
                    STD_ERR_DATA_RT10,
                ),
            ),
        )
    }

    @Test
    fun `optional fields are validated when supplied`() {
        val fields = imageFields() + textFields(3 to "0", 12 to "8", 13 to "1", 16 to "0", 27 to "N")
        val errors = VariableResolutionFingerprintRecordValidator().validate(VariableResolutionFingerprintRecord(fields))
        assertEquals(listOf(STD_ERR_SHPS_O_RT14, STD_ERR_SIF_RT14), errors)
    }

    @Test
    fun `CNT count and item structure are checked`() {
        val validator = TransactionInformationRecordValidator()
        for (content in listOf("1\u001f2\u001e14\u001f0", "1\u001f1\u001e14\u001f100", "1\u001f1\u001e14")) {
            assertTrue(validator.validate(TransactionInformationRecord(textFields(3 to content))).contains(STD_ERR_CNT_CONTENT_RT1))
        }
        assertTrue(
            !validator
                .validate(TransactionInformationRecord(textFields(3 to "1\u001f1\u001e14\u001f00")))
                .contains(STD_ERR_CNT_CONTENT_RT1),
        )
    }
}
