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

package eu.aagsolutions.img.nbis.calculators

import eu.aagsolutions.img.nbis.model.fields.Field

/**
 * Represents a contract for calculating the logical record length of a NIST record
 * based on its identifier and the collection of its fields. This interface
 * should be implemented by classes that provide specific record length
 * calculation strategies for different types of records (e.g., text-based or binary-based).
 */
interface LogicalRecordLengthCalculator {
    /**
     * Calculates a logical record length for a NIST record based on its identifier and a collection of fields.
     *
     * @param recordId the identifier of the record for which the length is being calculated
     * @param fields a map of field identifiers to their corresponding field objects, representing the fields of the record
     * @return a field representing the calculated record length as a string
     */
    fun calculate(
        recordId: Int,
        fields: Map<Int, Field<*>>,
    ): Field<String>
}
