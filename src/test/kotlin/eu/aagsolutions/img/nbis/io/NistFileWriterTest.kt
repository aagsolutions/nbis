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

package eu.aagsolutions.img.nbis.io

import io.kotest.matchers.equality.shouldBeEqualUsingFields
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.test.Test

class NistFileWriterTest {
    @Test
    fun `it should write successful a nist file`() {
        val url = NistFileReader::class.java.getResource("/references/type-14-amp-nqm-utf8.an2")
        val nistContent =
            NistFileReader(url!!.openStream()).use { file ->
                file.read()
            }

        NistFileWriter(FileOutputStream("output.nist")).use { writer -> writer.write(nistContent) }

        val nistContent2 =
            NistFileReader(Files.newInputStream(Path("output.nist"), StandardOpenOption.READ)).use { file ->
                file.read()
            }
        nistContent2 shouldBeEqualUsingFields nistContent
    }

    @Test
    fun `it should write successful a nist file type 3`() {
        val url = NistFileReader::class.java.getResource("/references/type-3.an2")
        val nistContent =
            NistFileReader(url!!.openStream()).use { file ->
                file.read()
            }
        NistFileWriter(FileOutputStream("output.nist")).use { writer -> writer.write(nistContent) }

        val nistContent2 =
            NistFileReader(Files.newInputStream(Path("output.nist"), StandardOpenOption.READ)).use { file ->
                file.read()
            }
        nistContent2 shouldBeEqualUsingFields nistContent
    }

    @Test
    fun `it should write successful a nist file type 10`() {
        val url = NistFileReader::class.java.getResource("/references/type-10-sap10.an2")
        val nistContent =
            NistFileReader(url!!.openStream()).use { file ->
                file.read()
            }
        NistFileWriter(FileOutputStream("output.nist")).use { writer -> writer.write(nistContent) }

        val nistContent2 =
            NistFileReader(Files.newInputStream(Path("output.nist"), StandardOpenOption.READ)).use { file ->
                file.read()
            }
        nistContent2 shouldBeEqualUsingFields nistContent
    }
}
