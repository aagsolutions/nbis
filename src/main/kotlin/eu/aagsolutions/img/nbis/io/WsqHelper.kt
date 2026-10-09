/*
 * Kotlin adaptation of JNBIS WSQ structures by M. H. Shamsi (2007).
 * Licensed under the Apache License, Version 2.0. See META-INF/LICENSE-WSQ.
 * Modified in 2026: translated to Kotlin and added bounds-checked reads.
 */
package eu.aagsolutions.img.nbis.io

@Suppress("MagicNumber")
internal object WsqHelper {
    val bitMasks = intArrayOf(0x00, 0x01, 0x03, 0x07, 0x0f, 0x1f, 0x3f, 0x7f, 0xff)
    const val MAX_DHT_TABLES = 8
    const val MAX_HUFFBITS = 16
    const val MAX_HUFFCOUNTS_WSQ = 256
    const val W_TREELEN = 20
    const val Q_TREELEN = 64
    const val SOI_WSQ = 0xffa0
    const val EOI_WSQ = 0xffa1
    const val SOF_WSQ = 0xffa2
    const val SOB_WSQ = 0xffa3
    const val DTT_WSQ = 0xffa4
    const val DQT_WSQ = 0xffa5
    const val DHT_WSQ = 0xffa6
    const val DRT_WSQ = 0xffa7
    const val COM_WSQ = 0xffa8
    const val STRT_SUBBAND_2 = 19
    const val STRT_SUBBAND_3 = 52
    const val MAX_SUBBANDS = 64
    const val NUM_SUBBANDS = 60
    const val STRT_SUBBAND_DEL = NUM_SUBBANDS
    const val STRT_SIZE_REGION_2 = 4
    const val STRT_SIZE_REGION_3 = 51
    const val ANY_WSQ = 0xffff
    const val TBLS_N_SOF = 2
    const val TBLS_N_SOB = (TBLS_N_SOF + 2)

    class WaveletTree {
        var x: Int = 0
        var y: Int = 0
        var lenx: Int = 0
        var leny: Int = 0
        var invrw: Int = 0
        var invcl: Int = 0
    }

    class TableDTT {
        var lofilt: FloatArray = FloatArray(0)
        var hifilt: FloatArray = FloatArray(0)
        var losz: Int = 0
        var hisz: Int = 0
        var lodef: Int = 0
        var hidef: Int = 0
    }

    class HuffCode {
        var size: Int = 0
        var code: Int = 0
    }

    class HeaderFrm {
        var black: Int = 0
        var white: Int = 0
        var width: Int = 0
        var height: Int = 0
        var mShift: Float = 0.0f
        var rScale: Float = 0.0f
        var wsqEncoder: Int = 0
        var software: Int = 0
    }

    class HuffmanTable {
        var tableLen: Int = 0
        var bytesLeft: Int = 0
        var tableId: Int = 0
        var huffbits: IntArray = IntArray(0)
        var huffvalues: IntArray = IntArray(0)
    }

    class TableDHT {
        var tabdef: Int = 0
        var huffbits: IntArray = IntArray(MAX_HUFFBITS)
        var huffvalues: IntArray = IntArray((MAX_HUFFCOUNTS_WSQ + 1))
    }

    class QuantizationTable {
        var binCenter: Float = 0.0f
        var qBin: FloatArray = FloatArray(MAX_SUBBANDS)
        var zBin: FloatArray = FloatArray(MAX_SUBBANDS)
        var dqtDef: Int = 0
    }

    class QuantTree {
        var x: Int = 0
        var y: Int = 0
        var lenx: Int = 0
        var leny: Int = 0
    }

    class IntRef(
        var value: Int,
    )

    class Token(
        val buffer: ByteArray,
    ) {
        var pointer = 0
        val tableDTT = TableDTT()
        val tableDQT = QuantizationTable()
        val tableDHT = Array(MAX_DHT_TABLES) { TableDHT() }
        var wtree = emptyArray<WaveletTree>()
        var qtree = emptyArray<QuantTree>()

        fun segmentEnd(): Int {
            val start = pointer
            val length = readShort()
            pointer = start
            require(length >= 2 && length <= buffer.size - start) { "Invalid WSQ segment length" }
            return start + length
        }

        fun readByte(): Int {
            require(pointer < buffer.size) { "Truncated WSQ data at byte $pointer" }
            return buffer[pointer++].toInt() and 255
        }

        fun readShort(): Int = (readByte() shl 8) or readByte()

        fun readInt(): Long = (readShort().toLong() shl 16) or readShort().toLong()

        fun readBytes(size: Int): ByteArray {
            require(size >= 0 && size <= buffer.size - pointer) { "Invalid WSQ segment length" }
            return buffer.copyOfRange(pointer, pointer + size).also { pointer += size }
        }
    }
}
