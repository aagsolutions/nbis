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

package eu.aagsolutions.img.nbis.validation.predicates

import eu.aagsolutions.img.nbis.model.enums.CharacterType

object ValidationPredicates {
    fun stringMatches(regex: Regex): (String) -> Boolean =
        { inputString ->
            inputString.matches(regex)
        }

    fun areCharTypeWithMinLength(
        charType: CharacterType,
        minLength: Int,
    ): (List<String>) -> Boolean =
        { inputList ->
            inputList.all {
                isCharTypeWithMinLength(charType, minLength)(it)
            }
        }

    fun isCharTypeWithMinLength(
        charType: CharacterType,
        minLength: Int,
    ): (String) -> Boolean =
        { inputString ->
            if (charType.regexpValidation != null) {
                charType.allowedCharacters!!.containsAll(inputString.toList())
            }
            if (charType.regexpValidation != null) {
                stringMatches(Regex(charType.regexpValidation))(inputString)
            }
            inputString.length >= minLength
        }

    fun isCharTypeWithMinMaxLength(
        charType: CharacterType,
        minLength: Int,
        maxLength: Int,
    ): (String) -> Boolean =
        { inputString ->
            if (charType.regexpValidation != null) {
                inputString.length >= minLength && charType.allowedCharacters!!.containsAll(inputString.toList())
            }
            if (charType.regexpValidation != null) {
                inputString.length >= minLength && stringMatches(Regex(charType.regexpValidation))(inputString)
            }
            inputString.length in minLength..maxLength
        }
}
