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
 * NIST reference enum for friction ridge position codes.
 *
 * Defines all the standard friction ridge positions used in NIST format
 * for fingers, palms, and plantar surfaces according to various NIST standards.
 */
enum class FingerprintFrictionRidgePosition(
    override val code: String,
    val type: FrictionRidgeType?,
    val description: String,
    override val createdFromStandard: Standard,
    override val deprecatedFromStandard: Standard?,
) : StandardReference {
    UNKNOWN_FINGER("0", FrictionRidgeType.FINGER, "Unknown finger", Standard.ANSI_NIST_ITL_2000, null),
    RIGHT_THUMB("1", FrictionRidgeType.FINGER, "Right thumb", Standard.ANSI_NIST_ITL_2000, null),
    RIGHT_INDEX("2", FrictionRidgeType.FINGER, "Right index finger", Standard.ANSI_NIST_ITL_2000, null),
    RIGHT_MIDDLE("3", FrictionRidgeType.FINGER, "Right middle finger", Standard.ANSI_NIST_ITL_2000, null),
    RIGHT_RING("4", FrictionRidgeType.FINGER, "Right ring finger", Standard.ANSI_NIST_ITL_2000, null),
    RIGHT_LITTLE("5", FrictionRidgeType.FINGER, "Right little finger", Standard.ANSI_NIST_ITL_2000, null),
    LEFT_THUMB("6", FrictionRidgeType.FINGER, "Left thumb", Standard.ANSI_NIST_ITL_2000, null),
    LEFT_INDEX("7", FrictionRidgeType.FINGER, "Left index finger", Standard.ANSI_NIST_ITL_2000, null),
    LEFT_MIDDLE("8", FrictionRidgeType.FINGER, "Left middle finger", Standard.ANSI_NIST_ITL_2000, null),
    LEFT_RING("9", FrictionRidgeType.FINGER, "Left ring finger", Standard.ANSI_NIST_ITL_2000, null),
    LEFT_LITTLE("10", FrictionRidgeType.FINGER, "Left little finger", Standard.ANSI_NIST_ITL_2000, null),
    PLAIN_RIGHT_THUMB("11", FrictionRidgeType.FINGER, "Plain right thumb", Standard.ANSI_NIST_ITL_2000, null),
    PLAIN_LEFT_THUMB("12", FrictionRidgeType.FINGER, "Plain left thumb", Standard.ANSI_NIST_ITL_2000, null),
    PLAIN_RIGHT_FOUR_FINGERS(
        "13",
        FrictionRidgeType.FINGERS_COMBINATION,
        "Plain right four fingers",
        Standard.ANSI_NIST_ITL_2000,
        null,
    ),
    PLAIN_LEFT_FOUR_FINGERS(
        "14",
        FrictionRidgeType.FINGERS_COMBINATION,
        "Plain left four fingers",
        Standard.ANSI_NIST_ITL_2000,
        null,
    ),
    LEFT_AND_RIGHT_THUMBS(
        "15",
        FrictionRidgeType.FINGERS_COMBINATION,
        "Left & right thumbs",
        Standard.ANSI_NIST_ITL_2007,
        null,
    ),
    RIGHT_EXTRA_DIGIT("16", FrictionRidgeType.FINGER, "Right extra digit", Standard.ANSI_NIST_ITL_2011, null),
    LEFT_EXTRA_DIGIT("17", FrictionRidgeType.FINGER, "Left extra digit", Standard.ANSI_NIST_ITL_2011, null),
    UNKNOWN("18", null, "Unknown friction ridge", Standard.ANSI_NIST_ITL_2011, null),
    EJI_OR_TIPS("19", FrictionRidgeType.FINGERS_COMBINATION, "EJI or tip", Standard.ANSI_NIST_ITL_2007, null),
    UNKNOWN_PALM("20", FrictionRidgeType.PALM, "Unknown palm", Standard.ANSI_NIST_ITL_2000, null),
    RIGHT_FULL_PALM("21", FrictionRidgeType.PALM, "Right Full Palm", Standard.ANSI_NIST_ITL_2000, null),
    RIGHT_WRITER_PALM("22", FrictionRidgeType.PALM, "Right Writer's Palm", Standard.ANSI_NIST_ITL_2000, null),
    LEFT_FULL_PALM("23", FrictionRidgeType.PALM, "Left Full Palm", Standard.ANSI_NIST_ITL_2000, null),
    LEFT_WRITER_PALM("24", FrictionRidgeType.PALM, "Left Writer's palm", Standard.ANSI_NIST_ITL_2000, null),
    RIGHT_LOWER_PALM("25", FrictionRidgeType.PALM, "Right lower palm", Standard.ANSI_NIST_ITL_2000, null),
    RIGHT_UPPER_PALM("26", FrictionRidgeType.PALM, "Right upper palm", Standard.ANSI_NIST_ITL_2000, null),
    LEFT_LOWER_PALM("27", FrictionRidgeType.PALM, "Left lower palm", Standard.ANSI_NIST_ITL_2000, null),
    LEFT_UPPER_PALM("28", FrictionRidgeType.PALM, "Left upper palm", Standard.ANSI_NIST_ITL_2000, null),
    RIGHT_OTHER_PALM("29", FrictionRidgeType.PALM, "Right other palm", Standard.ANSI_NIST_ITL_2000, null),
    LEFT_OTHER_PALM("30", FrictionRidgeType.PALM, "Left other palm", Standard.ANSI_NIST_ITL_2000, null),
    RIGHT_INTERDIGITAL_PALM(
        "31",
        FrictionRidgeType.PALM,
        "Right Interdigital palm",
        Standard.ANSI_NIST_ITL_2007,
        null,
    ),
    RIGHT_THENAR_PALM("32", FrictionRidgeType.PALM, "Right Thenar palm", Standard.ANSI_NIST_ITL_2007, null),
    RIGHT_HYPOTHENAR_PALM(
        "33",
        FrictionRidgeType.PALM,
        "Right Hypothenar palm",
        Standard.ANSI_NIST_ITL_2007,
        null,
    ),
    LEFT_INTERDIGITAL_PALM(
        "34",
        FrictionRidgeType.PALM,
        "Left Interdigital palm",
        Standard.ANSI_NIST_ITL_2007,
        null,
    ),
    LEFT_THENAR_PALM("35", FrictionRidgeType.PALM, "Left Thenar palm", Standard.ANSI_NIST_ITL_2007, null),
    LEFT_HYPOTHENAR_PALM(
        "36",
        FrictionRidgeType.PALM,
        "Left Hypothenar palm",
        Standard.ANSI_NIST_ITL_2007,
        null,
    ),
    RIGHT_GRASP_PALM("37", FrictionRidgeType.PALM, "Right grasp palm", Standard.ANSI_NIST_ITL_2011, null),
    LEFT_GRASP_PALM("38", FrictionRidgeType.PALM, "Left grasp palm", Standard.ANSI_NIST_ITL_2011, null),
    RIGHT_CARPAL_DELTA_PALM(
        "81",
        FrictionRidgeType.PALM,
        "Right carpal delta area",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    LEFT_CARPAL_DELTA_PALM(
        "82",
        FrictionRidgeType.PALM,
        "Left carpal delta area",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    RIGHT_ROLLED_FULL_PALM(
        "83",
        FrictionRidgeType.PALM,
        "Right full palm, including writer's palm - hand is rolled",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    LEFT_ROLLED_FULL_PALM(
        "84",
        FrictionRidgeType.PALM,
        "Left full palm, including writer's palm - hand is rolled",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    RIGHT_WRIST_BRACELET(
        "85",
        FrictionRidgeType.PALM,
        "Right wrist bracelet",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    LEFT_WRIST_BRACELET(
        "86",
        FrictionRidgeType.PALM,
        "Left wrist bracelet",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    UNKNOWN_SOLE("60", FrictionRidgeType.PLANTAR, "Unknown sole", Standard.ANSI_NIST_ITL_2011, null),
    RIGHT_FOOT("61", FrictionRidgeType.PLANTAR, "Sole – right foot", Standard.ANSI_NIST_ITL_2011, null),
    LEFT_FOOT("62", FrictionRidgeType.PLANTAR, "Sole – left foot", Standard.ANSI_NIST_ITL_2011, null),
    UNKNOWN_TOE("63", FrictionRidgeType.PLANTAR, "Unknown toe", Standard.ANSI_NIST_ITL_2011, null),
    RIGHT_BIG_TOE("64", FrictionRidgeType.PLANTAR, "Right big toe", Standard.ANSI_NIST_ITL_2011, null),
    RIGHT_SECOND_TOE("65", FrictionRidgeType.PLANTAR, "Right second toe", Standard.ANSI_NIST_ITL_2011, null),
    RIGHT_MIDDLE_TOE("66", FrictionRidgeType.PLANTAR, "Right middle toe", Standard.ANSI_NIST_ITL_2011, null),
    RIGHT_FOURTH_TOE("67", FrictionRidgeType.PLANTAR, "Right fourth toe", Standard.ANSI_NIST_ITL_2011, null),
    RIGHT_LITTLE_TOE("68", FrictionRidgeType.PLANTAR, "Right little toe", Standard.ANSI_NIST_ITL_2011, null),
    LEFT_BIG_TOE("69", FrictionRidgeType.PLANTAR, "Left big toe", Standard.ANSI_NIST_ITL_2011, null),
    LEFT_SECOND_TOE("70", FrictionRidgeType.PLANTAR, "Left second toe", Standard.ANSI_NIST_ITL_2011, null),
    LEFT_MIDDLE_TOE("71", FrictionRidgeType.PLANTAR, "Left middle toe", Standard.ANSI_NIST_ITL_2011, null),
    LEFT_FOURTH_TOE("72", FrictionRidgeType.PLANTAR, "Left fourth toe", Standard.ANSI_NIST_ITL_2011, null),
    LEFT_LITTLE_TOE("73", FrictionRidgeType.PLANTAR, "Left little toe", Standard.ANSI_NIST_ITL_2011, null),
    FRONT_RIGHT_FOOT(
        "74",
        FrictionRidgeType.PLANTAR,
        "Front / ball of right foot",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    BACK_RIGHT_FOOT(
        "75",
        FrictionRidgeType.PLANTAR,
        "Back / heel of right foot",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    FRONT_LEFT_FOOT(
        "76",
        FrictionRidgeType.PLANTAR,
        "Front / ball of left foot",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    BACK_LEFT_FOOT(
        "77",
        FrictionRidgeType.PLANTAR,
        "Back / ball of left foot",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    RIGHT_MIDDLE_FOOT(
        "78",
        FrictionRidgeType.PLANTAR,
        "Right middle of foot ( arch and/or outside (fibular hypothenar) areas of the feet",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    LEFT_MIDDLE_FOOT(
        "79",
        FrictionRidgeType.PLANTAR,
        "Left middle of foot ( arch and/or outside (fibular hypothenar) areas of the feet",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    RIGHT_INDEX_MIDDLE(
        "40",
        FrictionRidgeType.FINGERS_COMBINATION,
        "Right index/middle",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    RIGHT_MIDDLE_RING(
        "41",
        FrictionRidgeType.FINGERS_COMBINATION,
        "Right middle/ring",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    RIGHT_RING_LITTLE(
        "42",
        FrictionRidgeType.FINGERS_COMBINATION,
        "Right ring/little",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    LEFT_INDEX_MIDDLE(
        "43",
        FrictionRidgeType.FINGERS_COMBINATION,
        "Left index/middle",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    LEFT_MIDDLE_RING(
        "44",
        FrictionRidgeType.FINGERS_COMBINATION,
        "Left middle/ring",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    LEFT_RING_LITTLE(
        "45",
        FrictionRidgeType.FINGERS_COMBINATION,
        "Left ring/little",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    RIGHT_LEFT_INDEX(
        "46",
        FrictionRidgeType.FINGERS_COMBINATION,
        "Right index and left index",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    RIGHT_INDEX_MIDDLE_RING(
        "47",
        FrictionRidgeType.FINGERS_COMBINATION,
        "Right index/middle/ring",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    RIGHT_MIDDLE_RING_LITTLE(
        "48",
        FrictionRidgeType.FINGERS_COMBINATION,
        "Right middle/ring/little",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    LEFT_INDEX_MIDDLE_RING(
        "49",
        FrictionRidgeType.FINGERS_COMBINATION,
        "Left index/middle/ring",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    LEFT_MIDDLE_RING_LITTLE(
        "50",
        FrictionRidgeType.FINGERS_COMBINATION,
        "Left middle/ring/little",
        Standard.ANSI_NIST_ITL_2011,
        null,
    ),
    RIGHT_4_FINGERTIPS(
        "51",
        FrictionRidgeType.FINGERS_COMBINATION,
        "Fingertips (4 fingers simultaneously – no thumb – right hand - plain) ",
        Standard.ANSI_NIST_ITL_2013,
        null,
    ),
    LEFT_4_FINGERTIPS(
        "52",
        FrictionRidgeType.FINGERS_COMBINATION,
        "Fingertips (4 fingers simultaneously – no thumb – left hand - plain) ",
        Standard.ANSI_NIST_ITL_2013,
        null,
    ),
    RIGHT_5_FINGERTIPS(
        "53",
        FrictionRidgeType.FINGERS_COMBINATION,
        "Fingertips (4 fingers and thumb simultaneously – right hand - plain)",
        Standard.ANSI_NIST_ITL_2013,
        null,
    ),
    LEFT_5_FINGERTIPS(
        "54",
        FrictionRidgeType.FINGERS_COMBINATION,
        "Fingertips (4 fingers and thumb simultaneously – left hand - plain) ",
        Standard.ANSI_NIST_ITL_2013,
        null,
    ),
    ;

    /**
     * Enum defining the types of friction ridge areas.
     */
    enum class FrictionRidgeType {
        FINGER,
        FINGERS_COMBINATION,
        PALM,
        PLANTAR,
    }

    companion object {
        /**
         * List used for validators. Some attributes are allowed to specify only on unitary fingers.
         */
        val TEN_FINGERS =
            listOf(
                UNKNOWN_FINGER,
                RIGHT_THUMB,
                RIGHT_INDEX,
                RIGHT_MIDDLE,
                RIGHT_RING,
                RIGHT_LITTLE,
                LEFT_THUMB,
                LEFT_INDEX,
                LEFT_MIDDLE,
                LEFT_RING,
                LEFT_LITTLE,
                RIGHT_EXTRA_DIGIT,
                LEFT_EXTRA_DIGIT,
            )

        val FINGERS_AND_PALMS =
            listOf(
                UNKNOWN_FINGER,
                RIGHT_THUMB,
                RIGHT_INDEX,
                RIGHT_MIDDLE,
                RIGHT_RING,
                RIGHT_LITTLE,
                LEFT_THUMB,
                LEFT_INDEX,
                LEFT_MIDDLE,
                LEFT_RING,
                LEFT_LITTLE,
                RIGHT_EXTRA_DIGIT,
                LEFT_EXTRA_DIGIT,
                UNKNOWN_PALM,
                RIGHT_FULL_PALM,
                RIGHT_WRITER_PALM,
                LEFT_FULL_PALM,
                LEFT_WRITER_PALM,
                RIGHT_LOWER_PALM,
                RIGHT_UPPER_PALM,
                LEFT_LOWER_PALM,
                LEFT_UPPER_PALM,
                RIGHT_OTHER_PALM,
                LEFT_OTHER_PALM,
                RIGHT_INTERDIGITAL_PALM,
                RIGHT_THENAR_PALM,
                RIGHT_HYPOTHENAR_PALM,
                LEFT_INTERDIGITAL_PALM,
                LEFT_THENAR_PALM,
                LEFT_HYPOTHENAR_PALM,
            )

        val FINGERS_PALMS_AND_COMBINATION = entries.toList()
    }
}
