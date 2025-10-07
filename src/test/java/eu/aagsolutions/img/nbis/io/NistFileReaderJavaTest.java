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

package eu.aagsolutions.img.nbis.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NistFileReaderJavaTest {

    @Test
    void itShouldEncodeDecodeSuccessBase64() throws IOException {
        var url = NistFileReaderJavaTest.class.getResource("/base64/application.nist");
        try (NistFileReader reader = new NistFileReader(url.openStream())) {
            var base64 = reader.readToBase64();
            var nistFile = NistFileReader.Companion.decode(Base64.getDecoder().decode(base64));
            nistFile.getTransactionInformationRecord().getFields().forEach((idx, f) -> System.out.println(f.getData()));
            nistFile.getUserDefinedDescriptionTextRecords().forEach(System.out::println);
            assertNotNull(nistFile.getUserDefinedDescriptionTextRecords());
        }
    }

    @Test
    void itShouldDecodeSuccessBase64() throws IOException {
        var path = NistFileReaderJavaTest.class.getResource("/base64/base64content.txt").getPath();
        String base64 = Files.readString(Paths.get(path));
        var bytes = Base64.getDecoder().decode(base64);
        var nistFile = NistFileReader.Companion.decode(Base64.getDecoder().decode(bytes));
        nistFile.getTransactionInformationRecord().getFields().forEach((idx, f) -> System.out.println(f.getData()));
        nistFile.getUserDefinedDescriptionTextRecords().forEach(System.out::println);
        assertNotNull(nistFile.getUserDefinedDescriptionTextRecords());
    }
}
