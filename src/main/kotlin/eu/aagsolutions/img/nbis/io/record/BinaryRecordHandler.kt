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

package eu.aagsolutions.img.nbis.io.record

import eu.aagsolutions.img.nbis.datakit.Converters.longToBytes
import eu.aagsolutions.img.nbis.exceptions.NistException
import eu.aagsolutions.img.nbis.model.enums.RecordType
import eu.aagsolutions.img.nbis.model.enums.records.FieldType
import eu.aagsolutions.img.nbis.model.fields.ImageField
import eu.aagsolutions.img.nbis.model.records.BaseRecord
import java.io.OutputStream

abstract class BinaryRecordHandler(
    recordType: RecordType,
) : RecordHandler(recordType) {
    protected fun writeOneByteToken(
        outputStream: OutputStream,
        nistRecord: BaseRecord,
        field: FieldType,
    ) {
        val intVal: Int = nistRecord.getFieldText(field)?.toIntOrNull() ?: EMPTY_INT
        outputStream.write(intVal)
    }

    @Suppress("MagicNumber")
    protected fun writeTwoBytesToken(
        outputStream: OutputStream,
        record: BaseRecord,
        field: FieldType,
    ) {
        val buffer =
            record
                .getFieldText(field)
                ?.toIntOrNull()
                ?.let { longToBytes(it.toLong(), 2) }
                ?: EMPTY_TWO_BYTES
        outputStream.write(buffer)
    }

    @Suppress("MagicNumber")
    protected fun writeFourBytesToken(
        outputStream: OutputStream,
        record: BaseRecord,
        field: FieldType,
    ) {
        val buffer =
            record
                .getFieldText(field)
                ?.toIntOrNull()
                ?.let { longToBytes(it.toLong(), 4) }
                ?: EMPTY_FOUR_BYTES
        outputStream.write(buffer)
    }

    protected fun writeImageToken(
        outputStream: OutputStream,
        record: BaseRecord,
        field: FieldType,
    ) {
        if (field.typeClass != ImageField::class) {
            throw NistException("Field ${field.code} is not of type ImageField")
        }
        val byteArrays =
            record.getFieldImage(field)
                ?: throw NistException("Missing ${field.code} field")
        outputStream.write(byteArrays)
    }
}
