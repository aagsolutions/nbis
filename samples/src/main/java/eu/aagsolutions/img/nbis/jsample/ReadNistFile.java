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

package eu.aagsolutions.img.nbis.jsample;

import eu.aagsolutions.img.nbis.io.NistFileReader;
import eu.aagsolutions.img.nbis.model.enums.records.FacialAndSMTImageFields;

import java.io.FileOutputStream;
import java.io.IOException;

public class ReadNistFile {

    public static void main(String[] args) throws IOException {
        var url = ReadNistFile.class.getResource("/type-10-tattoo-face-sap20.an2");
        try (NistFileReader reader = new NistFileReader(url.openStream())) {
            var nistFile = reader.read();
            nistFile.getTransactionInformationRecord().getFields().forEach((idx, f) -> System.out.println(f.getData()));
            nistFile.getUserDefinedDescriptionTextRecords().forEach(System.out::println);
            var imgContent = nistFile.getFacialAndSmtImageRecords().getFirst().getFieldImage(FacialAndSMTImageFields.DATA);
            var compressionType = nistFile.getFacialAndSmtImageRecords().getFirst().getFieldText(FacialAndSMTImageFields.CGA);
            System.out.println(compressionType);
            if (compressionType.toUpperCase().contains("JP")) {
                try (FileOutputStream out = new FileOutputStream("jp.jpg")) {
                    out.write(imgContent);
                }
            }
        }

    }
}
