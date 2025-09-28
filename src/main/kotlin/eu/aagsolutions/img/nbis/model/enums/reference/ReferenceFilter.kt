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

package eu.aagsolutions.img.nbis.model.enums.reference

import eu.aagsolutions.img.nbis.model.enums.Standard

/**
 * Helper class for working with NIST reference enums.
 *
 * Provides utility functions for filtering and searching reference enums
 * based on their standard compatibility and codes.
 */
object ReferenceFilter {
    /**
     * Finds a reference enum by its code.
     *
     * @param values Array of reference enum values to search in.
     * @param code The code to search for.
     * @return The matching reference enum, or null if not found.
     */
    inline fun <reified R : StandardReference> findByCode(
        values: Array<R>,
        code: String,
    ): R? = values.find { it.code == code }

    /**
     * Finds all reference enum values that are allowed for the specified standard.
     *
     * @param values Array of reference enum values to filter.
     * @param standard The NIST standard to check compatibility against.
     * @return List of reference enums that are allowed for the given standard.
     */
    inline fun <reified R : StandardReference> findValuesAllowedByStandard(
        values: Array<R>,
        standard: Standard,
    ): List<R> = values.filter { it.isAllowedForStandard(standard) }

    /**
     * Finds all codes of reference enums that are allowed for the specified standard.
     *
     * @param values List of reference enum values to filter.
     * @param standard The NIST standard to check compatibility against.
     * @return List of codes from reference enums that are allowed for the given standard.
     */
    inline fun <reified R : StandardReference> findCodesAllowedByStandard(
        values: List<R>,
        standard: Standard,
    ): List<String> =
        values
            .filter { it.isAllowedForStandard(standard) }
            .map { it.code }

    /**
     * Finds all codes of reference enums that are allowed for the specified standard.
     *
     * @param values Array of reference enum values to filter.
     * @param standard The NIST standard to check compatibility against.
     * @return List of codes from reference enums that are allowed for the given standard.
     */
    inline fun <reified R : StandardReference> findCodesAllowedByStandard(
        values: Array<R>,
        standard: Standard,
    ): List<String> = findCodesAllowedByStandard(values.toList(), standard)
}
