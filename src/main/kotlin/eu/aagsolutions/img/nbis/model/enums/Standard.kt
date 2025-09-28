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

package eu.aagsolutions.img.nbis.model.enums

/**
 * Represents a set of standardized NIST formats with corresponding codes and labels.
 * The enum constants define different versions of the ANSI/NIST-ITL standards.
 * The standards have to be kept in order.
 *
 * @property label A descriptive label for the standard.
 * @property code A string representation of the code associated with the standard.
 */
enum class Standard(
    val label: String,
    val code: String,
) {
    ANSI_NIST_ITL_2000("ANSI/NIST-ITL 1-2000", "0300"),
    ANSI_NIST_ITL_2007("ANSI/NIST-ITL 1-2007", "0400"),
    ANSI_NIST_ITL_2011("ANSI/NIST-ITL 1-2011", "0500"),
    ANSI_NIST_ITL_2013("ANSI/NIST-ITL 1-2011 Update 2013", "0501"),
    ANSI_NIST_ITL_2015("ANSI/NIST-ITL 1-2011 Update 2015", "0502"),
    ;

    /**
     * Provides utility functions for interacting with the StandardEnum.
     */
    companion object {
        /**
         * Finds a StandardEnum entry that matches the given code.
         *
         * @param code The code to search for within the StandardEnum entries.
         * @return The matching StandardEnum entry if found, or null if no match is found.
         */
        fun findByCode(code: String): Standard? = entries.find { it.code == code }
    }

    /**
     * Determines if the current standard is within the inclusive range defined by the given lower and upper standards.
     *
     * @param lowerStandard The lower bound of the standard range (inclusive).
     * @param upperStandard The upper bound of the standard range (exclusive).
     * @return true if the current standard is within the specified range; otherwise, false.
     */
    fun isBetweenStandards(
        lowerStandard: Standard,
        upperStandard: Standard,
    ): Boolean = (this.ordinal >= lowerStandard.ordinal) && isPriorTo(upperStandard)

    /**
     * Determines if the current enum value is prior to the specified enum value.
     *
     * @param standardEnum The enum value to compare against. If null, this method returns true.
     * @return true if this enum value has a lower ordinal than the specified enum value, or if the specified enum value is null.
     */
    fun isPriorTo(standardEnum: Standard?): Boolean = standardEnum == null || this.ordinal < standardEnum.ordinal
}
