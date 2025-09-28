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
 * Represents different character types with associated descriptions, validations,
 * and allowed character sets.
 *
 * @property description A description of the character type.
 * @property regexpValidation A regular expression for validating the character type, if applicable.
 * @property allowedCharacters The set of characters allowed for this type, if defined.
 */
enum class CharacterType(
    val description: String,
    val regexpValidation: String?,
    val allowedCharacters: Set<Char>?,
) {
    A("Alphabetic", null, AllowedCharacterTypes.ALLOWED_A),
    AN("Alphanumeric and numeric", null, AllowedCharacterTypes.ALLOWED_AN),
    ANS("Alphanumeric and special characters", null, AllowedCharacterTypes.ALLOWED_ANS),
    AS("Alphabetic and special characters", null, AllowedCharacterTypes.ALLOWED_AS),
    B("Binary", null, null),
    BASE64("base64", "^[A-Za-z]+$", null),
    H("Hexadecimal", null, AllowedCharacterTypes.ALLOWED_H),
    N("Numeric", null, AllowedCharacterTypes.ALLOWED_N),
    S("Numeric and special characters", null, AllowedCharacterTypes.ALLOWED_S),
    NS("Numeric and special characters", null, AllowedCharacterTypes.ALLOWED_NS),
    U("Unicode", "^[\\s\\S&&[^\u001E\u001F\u001D\u001C\u0002\u0003]]+$", null),
    ;

    /**
     * Defines and organizes sets of allowed characters for various character types.
     *
     * This object aggregates predefined sets of characters organized into distinct character groups. These groupings
     * include alphabetic characters, numeric characters, special characters, hexadecimal characters, and combinations
     * of these categories. The predefined sets are immutable once initialized.
     *
     * The structure is intended for use cases that require strictly defined character types or combinations of them,
     * such as input validation, data standardization, or conformance to specific encoding rules in standards.
     *
     * @property ALLOWED_N A set containing numeric characters (digits 0-9).
     * @property ALLOWED_A A set containing alphabetic characters (both uppercase and lowercase letters).
     * @property ALLOWED_S A set containing special characters (such as punctuation and symbols).
     * @property ALLOWED_H A set containing characters valid for hexadecimal values (digits 0-9 and letters A-F).
     * @property ALLOWED_AN A set containing alphanumeric characters (a combination of alphabetic and numeric characters).
     * @property ALLOWED_AS A set containing alphabetic and special characters.
     * @property ALLOWED_NS A set containing numeric and special characters.
     * @property ALLOWED_ANS A set containing alphanumeric characters and special characters.
     */
    object AllowedCharacterTypes {
        val ALLOWED_N: Set<Char>
        val ALLOWED_A: Set<Char>
        val ALLOWED_S: Set<Char>
        val ALLOWED_H: Set<Char>
        val ALLOWED_AN: Set<Char>
        val ALLOWED_AS: Set<Char>
        val ALLOWED_NS: Set<Char>
        val ALLOWED_ANS: Set<Char>

        init {
            val allowedN = mutableSetOf<Char>()
            val allowedA = mutableSetOf<Char>()
            val allowedS = mutableSetOf<Char>()
            val allowedH = mutableSetOf<Char>()
            val allowedAN = mutableSetOf<Char>()
            val allowedAS = mutableSetOf<Char>()
            val allowedNS = mutableSetOf<Char>()
            val allowedANS = mutableSetOf<Char>()

            for (c in 'a'..'z') {
                allowedA.add(c)
            }

            for (c in 'A'..'Z') {
                allowedA.add(c)
            }

            for (c in '0'..'9') {
                allowedN.add(c)
            }

            val specials = " !\"#$%&'()*+,-./:;<=>?@[\\^_`{|}~"
            for (c in specials) {
                allowedS.add(c)
            }

            for (c in 'A'..'F') {
                allowedH.add(c)
            }
            allowedH.addAll(allowedN)

            allowedAN.addAll(allowedA)
            allowedAN.addAll(allowedN)

            allowedAS.addAll(allowedA)
            allowedAS.addAll(allowedS)

            allowedNS.addAll(allowedN)
            allowedNS.addAll(allowedS)

            allowedANS.addAll(allowedA)
            allowedANS.addAll(allowedN)
            allowedANS.addAll(allowedS)

            ALLOWED_N = allowedN.toSet()
            ALLOWED_A = allowedA.toSet()
            ALLOWED_S = allowedS.toSet()
            ALLOWED_H = allowedH.toSet()
            ALLOWED_AN = allowedAN.toSet()
            ALLOWED_AS = allowedAS.toSet()
            ALLOWED_NS = allowedNS.toSet()
            ALLOWED_ANS = allowedANS.toSet()
        }
    }
}
