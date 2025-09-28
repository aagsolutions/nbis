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

package eu.aagsolutions.img.nbis.model.enums.records

import eu.aagsolutions.img.nbis.model.fields.Field
import eu.aagsolutions.img.nbis.model.fields.TextField
import kotlin.reflect.KClass

/**
 * Represents user-defined descriptive text fields in a NIST record structure.
 *
 * The `UserDefinedText` enum derives from the `FieldType` interface and defines
 * specific field types that represent user-defined descriptive text information.
 * Each enum entry is linked to a corresponding default field related to this purpose.
 *
 * @property recordType The type of record associated with user-defined text fields.
 * @property id A unique identifier for the field, inherited from the parent `FieldType`.
 * @property code The standardized code representing the specific field.
 * @property description A descriptive text providing additional context about the field.
 * @property typeClass The class type implementing the `Field` interface for this field.
 */
enum class UserDefinedTextFields : FieldType {
    LEN(DefaultFields.LEN),
    IDC(DefaultFields.IDC),
    SYS(id = 3, code = "SYS", description = "System information (deprecated)", typeClass = TextField::class),
    DAR(id = 4, code = "DAR", description = "Date of record", typeClass = TextField::class),
    DLU(id = 5, code = "DLU", description = "Date of last update", typeClass = TextField::class),
    SCT(id = 6, code = "SCT", description = "Send copy to (deprecated)", typeClass = TextField::class),
    CNO(id = 7, code = "CNO", description = "Case number/Case reference", typeClass = TextField::class),
    SQN(id = 8, code = "SQN", description = "Sequence number/Evidence identifier", typeClass = TextField::class),
    MID(id = 9, code = "MID", description = "Latent identifier", typeClass = TextField::class),
    CRN(id = 10, code = "CRN", description = "Criminal reference number", typeClass = TextField::class),
    ORN(id = 11, code = "CRN", description = "Other reference number/Business reference number", typeClass = TextField::class),
    MN1(id = 12, code = "MN1", description = "Miscellaneous identification number 1", typeClass = TextField::class),
    MN2(id = 13, code = "MN2", description = "Miscellaneous identification number 2", typeClass = TextField::class),
    MN3(id = 14, code = "MN3", description = "Miscellaneous identification number 3", typeClass = TextField::class),
    MN4(id = 15, code = "MN4", description = "Miscellaneous identification number 4", typeClass = TextField::class),
    MN5(id = 16, code = "MN5", description = "Miscellaneous identification number 5", typeClass = TextField::class),
    FNU(id = 17, code = "FNU", description = "Finger number (deprecated)", typeClass = TextField::class),
    FIB(id = 18, code = "FIB", description = "Fingerprint identification byte (deprecated)", typeClass = TextField::class),
    DPR(id = 19, code = "DPR", description = "Date fingerprinted/Date and time fingerprinted", typeClass = TextField::class),
    TOF(id = 20, code = "TOF", description = "Time of fingerprinting (deprecated)", typeClass = TextField::class),
    RFP(id = 21, code = "RFP", description = "Reason fingerprinted/Offence description code", typeClass = TextField::class),
    POA(id = 22, code = "POA", description = "Place of arrest", typeClass = TextField::class),
    ;

    override val recordType: String = "User-defined descriptive text"
    override val id: Int
    override val code: String
    override val description: String
    override val typeClass: KClass<out Field<*>>

    constructor(parentEnum: FieldType) : this(
        parentEnum.id,
        parentEnum.code,
        parentEnum.description,
        parentEnum.typeClass,
    )

    constructor(
        id: Int,
        code: String,
        description: String,
        typeClass: KClass<out Field<*>>,
    ) {
        this.id = id
        this.code = code
        this.description = description
        this.typeClass = typeClass
    }
}
