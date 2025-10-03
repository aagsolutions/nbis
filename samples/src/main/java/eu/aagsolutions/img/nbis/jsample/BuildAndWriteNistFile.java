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

import eu.aagsolutions.img.nbis.io.NistFileWriter;
import eu.aagsolutions.img.nbis.model.NistFileBuilder;
import eu.aagsolutions.img.nbis.model.builders.FacialAndSMTImageRecordBuilder;
import eu.aagsolutions.img.nbis.model.builders.TransactionInformationRecordBuilder;
import eu.aagsolutions.img.nbis.model.builders.UserDefinedTextRecordBuilder;
import eu.aagsolutions.img.nbis.model.enums.reference.Color;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static eu.aagsolutions.img.nbis.calculators.CalculatorsKt.generateAgencyTCN;

public class BuildAndWriteNistFile {

    public static void main(String[] args) throws IOException {
        var currentGmtDate = LocalDateTime.now(ZoneOffset.UTC);
        var transactionInformationRecord =
                new TransactionInformationRecordBuilder()
                        .withDateField(currentGmtDate.toLocalDate())
                        .withGmtDateTimeField(currentGmtDate)
                        .withVersionNumberField("0100")
                        .withAgencyNamesField("0101")
                        .withTransactionControlNumberField(generateAgencyTCN("INTERPOOL", 1000))
                        .calculateFields(true)
                        .build();
        var userDefinedTextRecord =
                new UserDefinedTextRecordBuilder()
                        .withInformationDesignationCharField("01")
                        .withMiscellaneousIdentificationNumber1("RO109323243")
                        .calculateFields(true)
                        .build();
        var url = BuildAndWriteNistFile.class.getResource("/mugshot-1024x1024.jpg");

        try (InputStream out = url.openStream()) {
            var faceImage = out.readAllBytes();
            var facialAndSMTImageRecord =
                    new FacialAndSMTImageRecordBuilder()
                            .withInformationDesignationCharField("01")
                            .withImageDataField(faceImage)
                            .withScaleUnitsField("1")
                            .withColorSpaceField(Color.MULTI.getCode())
                            .calculateFields(true)
                            .build();
            var nistFile = new NistFileBuilder()
                            .withTransactionInformationRecord(transactionInformationRecord)
                            .withUserDefinedDescriptionTextRecords(userDefinedTextRecord)
                            .withFacialAndSmtImageRecords(facialAndSMTImageRecord)
                            .build();
            try (NistFileWriter fileWriter = new NistFileWriter(new FileOutputStream("output_jpeg.nist"))) {
                fileWriter.write(nistFile);
            }
        }


    }
}
