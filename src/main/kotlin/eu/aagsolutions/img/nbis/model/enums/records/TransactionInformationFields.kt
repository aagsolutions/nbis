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
 * Represents transaction-related metadata used in NIST logical record types.
 *
 * The `TransactionInformation` enum defines the structure for various transaction fields,
 * describing their attributes such as unique identifier, standardized code, description,
 * and the implementation class for managing the field value data. Each field type is associated
 * with a record type and conforms to the `FieldType` interface.
 *
 * @property recordType The type of the record that the field belongs to, represented as a string.
 * @property id A unique integer identifier associated with the transaction field.
 * @property code A standardized code associated with the field.
 * @property description A human-readable description providing additional context or purpose of the field.
 * @property typeClass The specific class type that implements the `Field` interface for this field.
 *
 * @constructor Creates a `TransactionInformation` field by initializing its attributes either directly
 * or inheriting them from another `FieldType`.
 *
 * @param parentEnum The parent `FieldType` from which the field attributes will be inherited.
 * @param id The unique identifier of the field.
 * @param code The standardized code of the field.
 * @param description The description of the field.
 * @param typeClass The class that implements the `Field` interface for the field.
 */
@Suppress("MagicNumber")
enum class TransactionInformationFields : FieldType {
    LEN(DefaultFields.LEN),
    VER(2, "VER", "Version number", TextField::class),
    CNT(3, "CNT", "File content", TextField::class),
    TOT(4, "TOT", "Transaction type", TextField::class),
    DAT(5, "DAT", "Date", TextField::class),
    PRY(6, "PRY", "Priority", TextField::class),
    DAI(7, "DAI", "Destination agency identifier", TextField::class),
    ORI(8, "ORI", "Originating agency identifier", TextField::class),
    TCN(9, "TCN", "Control number", TextField::class),
    TCR(10, "TCR", "Transaction control reference", TextField::class),
    NSR(11, "NSR", "Native scanning resolution", TextField::class),
    NTR(12, "NTR", "Nominal transmitting resolution", TextField::class),
    DOM(13, "DOM", "Domain name", TextField::class),
    GMT(14, "GMT", "Greenwich Mean Time", TextField::class),
    DCS(15, "DCS", "Directory of character sets", TextField::class),
    APS(16, "APS", "Application profile specifications", TextField::class),
    ANM(17, "ANM", "Agency names", TextField::class),
    GNS(18, "GNS", "Geographic name set", TextField::class),
    ;

    override val recordType: String = "Transaction Information Record"
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
