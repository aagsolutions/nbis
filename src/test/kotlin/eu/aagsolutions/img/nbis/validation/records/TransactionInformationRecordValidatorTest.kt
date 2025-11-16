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

import eu.aagsolutions.img.nbis.io.NistFileReader
import eu.aagsolutions.img.nbis.io.NistFileReaderTest
import eu.aagsolutions.img.nbis.validation.ValidationError
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.use

class TransactionInformationRecordValidatorTest {
    @Test
    fun `it should validate successfully type 14 nist file`() {
        val url = NistFileReaderTest::class.java.getResource("/references/type-14-amp-nqm-utf8.an2")
        val nistContent = NistFileReader(url!!.openStream()).use { reader -> reader.read() }
        val errors = mutableListOf<ValidationError>()
        TransactionInformationRecordValidator(errors, nistContent)
            .validate()
        assertTrue { errors.isEmpty() }
    }

    @Test
    fun `it should validate successfully type 10 nist file`() {
        val url = NistFileReaderTest::class.java.getResource("/references/type-10-14-17-piv-index-iris.an2")
        val nistContent = NistFileReader(url!!.openStream()).use { reader -> reader.read() }
        val errors = mutableListOf<ValidationError>()
        TransactionInformationRecordValidator(errors, nistContent)
            .validate()
        assertTrue { errors.isEmpty() }
    }

    @Test
    fun `it should validate successfully type 4 nist file`() {
        val url = NistFileReaderTest::class.java.getResource("/references/type-4-slaps.an2")
        val nistContent = NistFileReader(url!!.openStream()).use { reader -> reader.read() }
        val errors = mutableListOf<ValidationError>()
        TransactionInformationRecordValidator(errors, nistContent)
            .validate()
        assertTrue { errors.isEmpty() }
    }

    @Test
    fun `it should fail validation`() {
        val url = NistFileReaderTest::class.java.getResource("/invalid/fail-dom-field.nist")
        val nistContent = NistFileReader(url!!.openStream()).use { reader -> reader.read() }
        val errors = mutableListOf<ValidationError>()
        TransactionInformationRecordValidator(errors, nistContent)
            .validate()
        assertEquals(2, errors.size)
    }
}
