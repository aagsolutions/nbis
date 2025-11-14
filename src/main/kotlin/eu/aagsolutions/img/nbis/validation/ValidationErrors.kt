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

import eu.aagsolutions.img.nbis.model.enums.RecordType

enum class ValidationErrors(
    val message: String,
    val recordType: RecordType,
) {
    STD_ERR_CNT_CONTENT_RT1("The content field is not valid", RecordType.RT1),
    STD_ERR_CNT_MISSING_RT1("The content field is missing", RecordType.RT1),
    STD_ERR_NSR_WITH_RT4_INVALID_FORMAT_RT1(
        "NSR is having invalid format invalid, it should be in the format of ^\\d{2}\\.\\d{2}$",
        RecordType.RT1,
    ),
    STD_ERR_NSR_NO_RT4_INVALID_FORMAT_RT1("NSR expected value is 00.00", RecordType.RT1),
    STD_ERR_NTR_WITH_RT4_INVALID_FORMAT_RT1(
        "NTR is having invalid format invalid, it should be in the format of ^\\d{2}\\.\\d{2}$",
        RecordType.RT1,
    ),
    STD_ERR_NTR_NO_RT4_INVALID_FORMAT_RT1("NTR expected value is 00.00", RecordType.RT1),
    STD_ERR_VER_INVALID_FORMAT_RT1("Invalid version provided", RecordType.RT1),
}
