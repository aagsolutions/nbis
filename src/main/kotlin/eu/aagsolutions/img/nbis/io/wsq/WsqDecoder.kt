/*
 * Kotlin adaptation of the WSQ reconstruction algorithm from JNBIS.
 * Original author: M. H. Shamsi (2007), https://github.com/mhshams/jnbis
 * Licensed under the Apache License, Version 2.0. See META-INF/LICENSE-WSQ.
 * Modified in 2026: translated to Kotlin, added validation and PNG conversion.
 */
package eu.aagsolutions.img.nbis.io.wsq

// Keep the standard's tree layout and filter synthesis together for auditability.
@Suppress(
    "MagicNumber",
    "LargeClass",
    "LongMethod",
    "TooManyFunctions",
    "CyclomaticComplexMethod",
    "NestedBlockDepth",
    "LoopWithTooManyJumpStatements",
    "ReturnCount",
)
internal class WsqDecoder {
    private companion object {
        // Bound allocations for untrusted biometric input (integer and float work buffers).
        const val MAX_PIXELS = 16_777_216L
    }

    data class DecodedImage(
        val pixels: ByteArray,
        val width: Int,
        val height: Int,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as DecodedImage

            if (width != other.width) return false
            if (height != other.height) return false
            if (!pixels.contentEquals(other.pixels)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = width
            result = 31 * result + height
            result = 31 * result + pixels.contentHashCode()
            return result
        }
    }

    fun decode(data: ByteArray): DecodedImage {
        val token: WsqHelper.Token = WsqHelper.Token(data)
        getCMarkerWSQ(token, WsqHelper.SOI_WSQ)
        var marker: Int = getCMarkerWSQ(token, WsqHelper.TBLS_N_SOF)
        while (marker != WsqHelper.SOF_WSQ) {
            getCTableWSQ(token, marker)
            marker = getCMarkerWSQ(token, WsqHelper.TBLS_N_SOF)
        }
        val frmHeaderWSQ: WsqHelper.HeaderFrm = getCFrameHeaderWSQ(token)
        val width = frmHeaderWSQ.width
        val height = frmHeaderWSQ.height
        require(width >= 2 && height >= 2 && width.toLong() * height <= MAX_PIXELS) {
            "Invalid or unsupported WSQ dimensions: ${width}x$height"
        }
        require(frmHeaderWSQ.rScale > 0) { "Invalid WSQ reconstruction scale" }
        // WSQ partitions the wavelet plane into 64 regions; 60 carry quantized samples.
        buildWSQTrees(token, width, height)
        val qdata: IntArray = huffmanDecodeDataMem(token, (width * height))
        val fdata: FloatArray = unquantize(token, qdata, width, height)
        wsqReconstruct(token, fdata, width, height)
        val cdata: ByteArray = convertImage2Byte(fdata, width, height, frmHeaderWSQ.mShift, frmHeaderWSQ.rScale)
        return DecodedImage(cdata, width, height)
    }

    private fun intSign(power: Int): Int = if (power % 2 == 0) 1 else -1

    private fun getCMarkerWSQ(
        token: WsqHelper.Token,
        type: Int,
    ): Int {
        val marker = token.readShort()
        val valid =
            when (type) {
                WsqHelper.SOI_WSQ -> marker == WsqHelper.SOI_WSQ
                WsqHelper.TBLS_N_SOF, WsqHelper.TBLS_N_SOB -> {
                    marker == WsqHelper.DTT_WSQ ||
                        marker == WsqHelper.DQT_WSQ ||
                        marker == WsqHelper.DHT_WSQ ||
                        marker == WsqHelper.COM_WSQ ||
                        marker == (if (type == WsqHelper.TBLS_N_SOF) WsqHelper.SOF_WSQ else WsqHelper.SOB_WSQ)
                }
                else -> false
            }
        require(valid) { "Unexpected WSQ marker: ${marker.toString(16)}" }
        return marker
    }

    private fun getCTableWSQ(
        token: WsqHelper.Token,
        marker: Int,
    ) {
        val segmentEnd = token.segmentEnd()
        when (marker) {
            WsqHelper.DTT_WSQ -> {
                getCTransformTable(token)
            }
            WsqHelper.DQT_WSQ -> {
                getCQuantizationTable(token)
            }
            WsqHelper.DHT_WSQ -> {
                getCHuffmanTableWSQ(token)
            }
            WsqHelper.COM_WSQ -> {
                getCComment(token)
            }
            else -> {
                throw IllegalArgumentException("ERROR: getCTableWSQ : Invalid table defined : $marker")
            }
        }
        require(token.pointer == segmentEnd) { "Invalid WSQ table length" }
    }

    private fun getCComment(token: WsqHelper.Token) {
        token.readBytes(token.readShort() - 2)
    }

    private fun reconstructWaveletFilterCoefficient(token: WsqHelper.Token): Float {
        val sign = token.readByte()
        val scale = token.readByte()
        val shrtDat = token.readInt()

        var value = shrtDat.toFloat()
        repeat(scale) {
            value /= 10.0f
        }
        if (sign != 0) {
            value *= -1.0f
        }
        return value
    }

    private fun getCTransformTable(token: WsqHelper.Token) {
        token.readShort()
        token.tableDTT.hisz = token.readByte()
        token.tableDTT.losz = token.readByte()
        require(token.tableDTT.hisz > 0 && token.tableDTT.losz > 0) { "Empty WSQ wavelet filter" }

        token.tableDTT.hifilt = FloatArray(token.tableDTT.hisz)
        token.tableDTT.lofilt = FloatArray(token.tableDTT.losz)

        var aSize =
            if (token.tableDTT.hisz % 2 != 0) {
                (token.tableDTT.hisz + 1) / 2
            } else {
                token.tableDTT.hisz / 2
            }

        val aLofilt = FloatArray(aSize)
        aSize--
        for (cnt in 0..aSize) {
            aLofilt[cnt] = reconstructWaveletFilterCoefficient(token)

            if (token.tableDTT.hisz % 2 != 0) {
                token.tableDTT.hifilt[cnt + aSize] = intSign(cnt) * aLofilt[cnt]
                if (cnt > 0) {
                    token.tableDTT.hifilt[aSize - cnt] = token.tableDTT.hifilt[cnt + aSize]
                }
            } else {
                val targetIdx = cnt + aSize + 1
                token.tableDTT.hifilt[targetIdx] = intSign(cnt) * aLofilt[cnt]
                token.tableDTT.hifilt[aSize - cnt] = -token.tableDTT.hifilt[targetIdx]
            }
        }

        aSize =
            if (token.tableDTT.losz % 2 != 0) {
                (token.tableDTT.losz + 1) / 2
            } else {
                token.tableDTT.losz / 2
            }

        val aHifilt = FloatArray(aSize)
        aSize--

        for (cnt in 0..aSize) {
            aHifilt[cnt] = reconstructWaveletFilterCoefficient(token)

            if (token.tableDTT.losz % 2 != 0) {
                token.tableDTT.lofilt[cnt + aSize] = intSign(cnt) * aHifilt[cnt]
                if (cnt > 0) {
                    token.tableDTT.lofilt[aSize - cnt] = token.tableDTT.lofilt[cnt + aSize]
                }
            } else {
                val targetIdx = cnt + aSize + 1
                token.tableDTT.lofilt[targetIdx] = intSign(cnt + 1) * aHifilt[cnt]
                token.tableDTT.lofilt[aSize - cnt] = token.tableDTT.lofilt[targetIdx]
            }
        }

        token.tableDTT.lodef = 1
        token.tableDTT.hidef = 1
    }

    private fun getCQuantizationTable(token: WsqHelper.Token) {
        token.readShort()

        // Local helper to read a scaled float value, avoiding repetitive code blocks
        fun readScaledFloat(): Float {
            val scale = token.readByte()
            var value = token.readShort().toFloat()
            repeat(scale) {
                value /= 10.0f
            }
            return value
        }

        token.tableDQT.binCenter = readScaledFloat()

        for (cnt in 0 until WsqHelper.MAX_SUBBANDS) {
            token.tableDQT.qBin[cnt] = readScaledFloat()
            token.tableDQT.zBin[cnt] = readScaledFloat()
        }

        token.tableDQT.dqtDef = 1
    }

    private fun getCHuffmanTableWSQ(token: WsqHelper.Token) {
        val firstHuffmanTable: WsqHelper.HuffmanTable = getCHuffmanTable(token, WsqHelper.MAX_HUFFCOUNTS_WSQ, 0, true)
        var tableId: Int = firstHuffmanTable.tableId
        token.tableDHT[tableId].huffbits = firstHuffmanTable.huffbits.copyOf()
        token.tableDHT[tableId].huffvalues = firstHuffmanTable.huffvalues.copyOf()
        token.tableDHT[tableId].tabdef = 1
        var bytesLeft: Int = firstHuffmanTable.bytesLeft
        while (bytesLeft != 0) {
            val huffmantable: WsqHelper.HuffmanTable = getCHuffmanTable(token, WsqHelper.MAX_HUFFCOUNTS_WSQ, bytesLeft, false)
            tableId = huffmantable.tableId
            if (token.tableDHT[tableId].tabdef != 0) {
                throw IllegalArgumentException("ERROR : getCHuffmanTableWSQ : huffman table already defined.")
            }
            token.tableDHT[tableId].huffbits = huffmantable.huffbits.copyOf()
            token.tableDHT[tableId].huffvalues = huffmantable.huffvalues.copyOf()
            token.tableDHT[tableId].tabdef = 1
            bytesLeft = huffmantable.bytesLeft
        }
    }

    private fun getCHuffmanTable(
        token: WsqHelper.Token,
        maxHuffcounts: Int,
        bytesLeftInitial: Int,
        readTableLen: Boolean,
    ): WsqHelper.HuffmanTable {
        var bytesLeft = bytesLeftInitial
        val huffmanTable = WsqHelper.HuffmanTable()

        if (readTableLen) {
            huffmanTable.tableLen = token.readShort()
            huffmanTable.bytesLeft = huffmanTable.tableLen - 2
            bytesLeft = huffmanTable.bytesLeft
        } else {
            huffmanTable.bytesLeft = bytesLeft
        }

        require(bytesLeft > 0) { "ERROR : getCHuffmanTable : no huffman table bytes remaining" }

        huffmanTable.tableId = token.readByte()
        require(huffmanTable.tableId < WsqHelper.MAX_DHT_TABLES) { "Invalid WSQ Huffman table ID" }
        huffmanTable.bytesLeft--

        huffmanTable.huffbits = IntArray(WsqHelper.MAX_HUFFBITS)
        var numHufvals = 0

        for (i in 0 until WsqHelper.MAX_HUFFBITS) {
            huffmanTable.huffbits[i] = token.readByte()
            numHufvals += huffmanTable.huffbits[i]
        }

        var availableCodes = 1
        for (count in huffmanTable.huffbits) {
            availableCodes = availableCodes * 2 - count
            require(availableCodes >= 0) { "Oversubscribed WSQ Huffman table" }
        }

        huffmanTable.bytesLeft -= WsqHelper.MAX_HUFFBITS
        require(numHufvals <= maxHuffcounts) { "ERROR : getCHuffmanTable : numHufvals is larger than MAX_HUFFCOUNTS" }

        huffmanTable.huffvalues = IntArray(maxHuffcounts + 1)
        for (i in 0 until numHufvals) {
            huffmanTable.huffvalues[i] = token.readByte()
        }

        huffmanTable.bytesLeft -= numHufvals
        require(numHufvals > 0 && huffmanTable.bytesLeft >= 0) { "Invalid WSQ Huffman table length" }

        return huffmanTable
    }

    private fun getCFrameHeaderWSQ(token: WsqHelper.Token): WsqHelper.HeaderFrm {
        val headerFrm: WsqHelper.HeaderFrm = WsqHelper.HeaderFrm()
        require(token.readShort() == 17) { "Invalid WSQ frame header length" }
        headerFrm.black = token.readByte()
        headerFrm.white = token.readByte()
        headerFrm.height = token.readShort()
        headerFrm.width = token.readShort()
        var scale: Int = token.readByte()
        var shrtDat: Int = token.readShort()
        headerFrm.mShift = shrtDat.toFloat()
        while (scale > 0) {
            headerFrm.mShift = (headerFrm.mShift / 10.0).toFloat()
            scale--
        }
        scale = token.readByte()
        shrtDat = token.readShort()
        headerFrm.rScale = shrtDat.toFloat()
        while (scale > 0) {
            headerFrm.rScale = (headerFrm.rScale / 10.0).toFloat()
            scale--
        }
        headerFrm.wsqEncoder = token.readByte()
        headerFrm.software = token.readShort()
        return headerFrm
    }

    private fun buildWSQTrees(
        token: WsqHelper.Token,
        width: Int,
        height: Int,
    ) {
        buildWTree(token, WsqHelper.W_TREELEN, width, height)
        buildQTree(token, WsqHelper.Q_TREELEN)
    }

    private fun buildWTree(
        token: WsqHelper.Token,
        wtreelen: Int,
        width: Int,
        height: Int,
    ) {
        // Array factory automatically populates each slot with a new WaveletTree instance
        token.wtree = Array(wtreelen) { WsqHelper.WaveletTree() }

        // Set inverse row flags
        for (i in intArrayOf(2, 4, 7, 9, 11, 13, 16, 18)) {
            token.wtree[i].invrw = 1
        }

        // Set inverse column flags
        for (i in intArrayOf(3, 5, 8, 9, 12, 13, 17, 18)) {
            token.wtree[i].invcl = 1
        }

        wtree4(token, 0, 1, width, height, 0, 0, 1)

        val lenx1Val = token.wtree[1].lenx
        val lenx = if (lenx1Val % 2 == 0) lenx1Val / 2 else (lenx1Val + 1) / 2
        val lenx2 = if (lenx1Val % 2 == 0) lenx.let { it } else lenx - 1

        val leny1Val = token.wtree[1].leny
        val leny = if (leny1Val % 2 == 0) leny1Val / 2 else (leny1Val + 1) / 2
        val leny2 = if (leny1Val % 2 == 0) leny.let { it } else leny - 1

        wtree4(token, 4, 6, lenx2, leny, lenx, 0, 0)
        wtree4(token, 5, 10, lenx, leny2, 0, leny, 0)
        wtree4(token, 14, 15, lenx, leny, 0, 0, 0)

        token.wtree[19].x = 0
        token.wtree[19].y = 0

        val lenx15 = token.wtree[15].lenx
        token.wtree[19].lenx = if (lenx15 % 2 == 0) lenx15 / 2 else (lenx15 + 1) / 2

        val leny15 = token.wtree[15].leny
        token.wtree[19].leny = if (leny15 % 2 == 0) leny15 / 2 else (leny15 + 1) / 2
    }

    private fun wtree4(
        token: WsqHelper.Token,
        start1: Int,
        start2: Int,
        lenx: Int,
        leny: Int,
        x: Int,
        y: Int,
        stop1: Int,
    ) {
        val p1 = start1
        val p2 = start2
        val evenx = (lenx % 2)
        val eveny = (leny % 2)
        token.wtree[p1].x = x
        token.wtree[p1].y = y
        token.wtree[p1].lenx = lenx
        token.wtree[p1].leny = leny
        token.wtree[p2].x = x
        token.wtree[(p2 + 2)].x = x
        token.wtree[p2].y = y
        token.wtree[(p2 + 1)].y = y
        if (evenx == 0) {
            token.wtree[p2].lenx = (lenx / 2)
            token.wtree[(p2 + 1)].lenx = token.wtree[p2].lenx
        } else {
            if (p1 == 4) {
                token.wtree[p2].lenx = ((lenx - 1) / 2)
                token.wtree[(p2 + 1)].lenx = (token.wtree[p2].lenx + 1)
            } else {
                token.wtree[p2].lenx = ((lenx + 1) / 2)
                token.wtree[(p2 + 1)].lenx = (token.wtree[p2].lenx - 1)
            }
        }
        token.wtree[(p2 + 1)].x = (token.wtree[p2].lenx + x)
        if (stop1 == 0) {
            token.wtree[(p2 + 3)].lenx = token.wtree[(p2 + 1)].lenx
            token.wtree[(p2 + 3)].x = token.wtree[(p2 + 1)].x
        }
        token.wtree[(p2 + 2)].lenx = token.wtree[p2].lenx
        if (eveny == 0) {
            token.wtree[p2].leny = (leny / 2)
            token.wtree[(p2 + 2)].leny = token.wtree[p2].leny
        } else {
            if (p1 == 5) {
                token.wtree[p2].leny = ((leny - 1) / 2)
                token.wtree[(p2 + 2)].leny = (token.wtree[p2].leny + 1)
            } else {
                token.wtree[p2].leny = ((leny + 1) / 2)
                token.wtree[(p2 + 2)].leny = (token.wtree[p2].leny - 1)
            }
        }
        token.wtree[(p2 + 2)].y = (token.wtree[p2].leny + y)
        if (stop1 == 0) {
            token.wtree[(p2 + 3)].leny = token.wtree[(p2 + 2)].leny
            token.wtree[(p2 + 3)].y = token.wtree[(p2 + 2)].y
        }
        token.wtree[(p2 + 1)].leny = token.wtree[p2].leny
    }

    private fun buildQTree(
        token: WsqHelper.Token,
        qtreelen: Int,
    ) {
        // Array factory initializes each element automatically, making the manual loop redundant
        token.qtree = Array(qtreelen) { WsqHelper.QuantTree() }

        qtree16(token, 3, token.wtree[14].lenx, token.wtree[14].leny, token.wtree[14].x, token.wtree[14].y, 0, 0)
        qtree16(token, 19, token.wtree[4].lenx, token.wtree[4].leny, token.wtree[4].x, token.wtree[4].y, 0, 1)
        qtree16(token, 48, token.wtree[0].lenx, token.wtree[0].leny, token.wtree[0].x, token.wtree[0].y, 0, 0)
        qtree16(token, 35, token.wtree[5].lenx, token.wtree[5].leny, token.wtree[5].x, token.wtree[5].y, 1, 0)
        qtree4(token, 0, token.wtree[19].lenx, token.wtree[19].leny, token.wtree[19].x, token.wtree[19].y)
    }

    private fun qtree16(
        token: WsqHelper.Token,
        start: Int,
        lenx: Int,
        leny: Int,
        x: Int,
        y: Int,
        rw: Int,
        cl: Int,
    ) {
        var tempx: Int
        var temp2x: Int
        var tempy: Int
        var temp2y: Int
        val p = start
        var evenx: Int = (lenx % 2)
        var eveny: Int = (leny % 2)
        if (evenx == 0) {
            tempx = (lenx / 2)
            temp2x = tempx
        } else {
            if (cl != 0) {
                temp2x = ((lenx + 1) / 2)
                tempx = (temp2x - 1)
            } else {
                tempx = ((lenx + 1) / 2)
                temp2x = (tempx - 1)
            }
        }
        if (eveny == 0) {
            tempy = (leny / 2)
            temp2y = tempy
        } else {
            if (rw != 0) {
                temp2y = ((leny + 1) / 2)
                tempy = (temp2y - 1)
            } else {
                tempy = ((leny + 1) / 2)
                temp2y = (tempy - 1)
            }
        }
        evenx = (tempx % 2)
        eveny = (tempy % 2)
        token.qtree[p].x = x
        token.qtree[(p + 2)].x = x
        token.qtree[p].y = y
        token.qtree[(p + 1)].y = y
        if (evenx == 0) {
            token.qtree[p].lenx = (tempx / 2)
            token.qtree[(p + 1)].lenx = token.qtree[p].lenx
            token.qtree[(p + 2)].lenx = token.qtree[p].lenx
            token.qtree[(p + 3)].lenx = token.qtree[p].lenx
        } else {
            token.qtree[p].lenx = ((tempx + 1) / 2)
            token.qtree[(p + 1)].lenx = (token.qtree[p].lenx - 1)
            token.qtree[(p + 2)].lenx = token.qtree[p].lenx
            token.qtree[(p + 3)].lenx = token.qtree[(p + 1)].lenx
        }
        token.qtree[(p + 1)].x = (x + token.qtree[p].lenx)
        token.qtree[(p + 3)].x = token.qtree[(p + 1)].x
        if (eveny == 0) {
            token.qtree[p].leny = (tempy / 2)
            token.qtree[(p + 1)].leny = token.qtree[p].leny
            token.qtree[(p + 2)].leny = token.qtree[p].leny
            token.qtree[(p + 3)].leny = token.qtree[p].leny
        } else {
            token.qtree[p].leny = ((tempy + 1) / 2)
            token.qtree[(p + 1)].leny = token.qtree[p].leny
            token.qtree[(p + 2)].leny = (token.qtree[p].leny - 1)
            token.qtree[(p + 3)].leny = token.qtree[(p + 2)].leny
        }
        token.qtree[(p + 2)].y = (y + token.qtree[p].leny)
        token.qtree[(p + 3)].y = token.qtree[(p + 2)].y
        evenx = (temp2x % 2)
        token.qtree[(p + 4)].x = (x + tempx)
        token.qtree[(p + 6)].x = token.qtree[(p + 4)].x
        token.qtree[(p + 4)].y = y
        token.qtree[(p + 5)].y = y
        token.qtree[(p + 6)].y = token.qtree[(p + 2)].y
        token.qtree[(p + 7)].y = token.qtree[(p + 2)].y
        token.qtree[(p + 4)].leny = token.qtree[p].leny
        token.qtree[(p + 5)].leny = token.qtree[p].leny
        token.qtree[(p + 6)].leny = token.qtree[(p + 2)].leny
        token.qtree[(p + 7)].leny = token.qtree[(p + 2)].leny
        if (evenx == 0) {
            token.qtree[(p + 4)].lenx = (temp2x / 2)
            token.qtree[(p + 5)].lenx = token.qtree[(p + 4)].lenx
            token.qtree[(p + 6)].lenx = token.qtree[(p + 4)].lenx
            token.qtree[(p + 7)].lenx = token.qtree[(p + 4)].lenx
        } else {
            token.qtree[(p + 5)].lenx = ((temp2x + 1) / 2)
            token.qtree[(p + 4)].lenx = (token.qtree[(p + 5)].lenx - 1)
            token.qtree[(p + 6)].lenx = token.qtree[(p + 4)].lenx
            token.qtree[(p + 7)].lenx = token.qtree[(p + 5)].lenx
        }
        token.qtree[(p + 5)].x = (token.qtree[(p + 4)].x + token.qtree[(p + 4)].lenx)
        token.qtree[(p + 7)].x = token.qtree[(p + 5)].x
        eveny = (temp2y % 2)
        token.qtree[(p + 8)].x = x
        token.qtree[(p + 9)].x = token.qtree[(p + 1)].x
        token.qtree[(p + 10)].x = x
        token.qtree[(p + 11)].x = token.qtree[(p + 1)].x
        token.qtree[(p + 8)].y = (y + tempy)
        token.qtree[(p + 9)].y = token.qtree[(p + 8)].y
        token.qtree[(p + 8)].lenx = token.qtree[p].lenx
        token.qtree[(p + 9)].lenx = token.qtree[(p + 1)].lenx
        token.qtree[(p + 10)].lenx = token.qtree[p].lenx
        token.qtree[(p + 11)].lenx = token.qtree[(p + 1)].lenx
        if (eveny == 0) {
            token.qtree[(p + 8)].leny = (temp2y / 2)
            token.qtree[(p + 9)].leny = token.qtree[(p + 8)].leny
            token.qtree[(p + 10)].leny = token.qtree[(p + 8)].leny
            token.qtree[(p + 11)].leny = token.qtree[(p + 8)].leny
        } else {
            token.qtree[(p + 10)].leny = ((temp2y + 1) / 2)
            token.qtree[(p + 11)].leny = token.qtree[(p + 10)].leny
            token.qtree[(p + 8)].leny = (token.qtree[(p + 10)].leny - 1)
            token.qtree[(p + 9)].leny = token.qtree[(p + 8)].leny
        }
        token.qtree[(p + 10)].y = (token.qtree[(p + 8)].y + token.qtree[(p + 8)].leny)
        token.qtree[(p + 11)].y = token.qtree[(p + 10)].y
        token.qtree[(p + 12)].x = token.qtree[(p + 4)].x
        token.qtree[(p + 13)].x = token.qtree[(p + 5)].x
        token.qtree[(p + 14)].x = token.qtree[(p + 4)].x
        token.qtree[(p + 15)].x = token.qtree[(p + 5)].x
        token.qtree[(p + 12)].y = token.qtree[(p + 8)].y
        token.qtree[(p + 13)].y = token.qtree[(p + 8)].y
        token.qtree[(p + 14)].y = token.qtree[(p + 10)].y
        token.qtree[(p + 15)].y = token.qtree[(p + 10)].y
        token.qtree[(p + 12)].lenx = token.qtree[(p + 4)].lenx
        token.qtree[(p + 13)].lenx = token.qtree[(p + 5)].lenx
        token.qtree[(p + 14)].lenx = token.qtree[(p + 4)].lenx
        token.qtree[(p + 15)].lenx = token.qtree[(p + 5)].lenx
        token.qtree[(p + 12)].leny = token.qtree[(p + 8)].leny
        token.qtree[(p + 13)].leny = token.qtree[(p + 8)].leny
        token.qtree[(p + 14)].leny = token.qtree[(p + 10)].leny
        token.qtree[(p + 15)].leny = token.qtree[(p + 10)].leny
    }

    private fun qtree4(
        token: WsqHelper.Token,
        start: Int,
        lenx: Int,
        leny: Int,
        x: Int,
        y: Int,
    ) {
        val p = start
        val evenx = (lenx % 2)
        val eveny = (leny % 2)
        token.qtree[p].x = x
        token.qtree[(p + 2)].x = x
        token.qtree[p].y = y
        token.qtree[(p + 1)].y = y
        if (evenx == 0) {
            token.qtree[p].lenx = (lenx / 2)
            token.qtree[(p + 1)].lenx = token.qtree[p].lenx
            token.qtree[(p + 2)].lenx = token.qtree[p].lenx
            token.qtree[(p + 3)].lenx = token.qtree[p].lenx
        } else {
            token.qtree[p].lenx = ((lenx + 1) / 2)
            token.qtree[(p + 1)].lenx = (token.qtree[p].lenx - 1)
            token.qtree[(p + 2)].lenx = token.qtree[p].lenx
            token.qtree[(p + 3)].lenx = token.qtree[(p + 1)].lenx
        }
        token.qtree[(p + 1)].x = (x + token.qtree[p].lenx)
        token.qtree[(p + 3)].x = token.qtree[(p + 1)].x
        if (eveny == 0) {
            token.qtree[p].leny = (leny / 2)
            token.qtree[(p + 1)].leny = token.qtree[p].leny
            token.qtree[(p + 2)].leny = token.qtree[p].leny
            token.qtree[(p + 3)].leny = token.qtree[p].leny
        } else {
            token.qtree[p].leny = ((leny + 1) / 2)
            token.qtree[(p + 1)].leny = token.qtree[p].leny
            token.qtree[(p + 2)].leny = (token.qtree[p].leny - 1)
            token.qtree[(p + 3)].leny = token.qtree[(p + 2)].leny
        }
        token.qtree[(p + 2)].y = (y + token.qtree[p].leny)
        token.qtree[(p + 3)].y = token.qtree[(p + 2)].y
    }

    // Symbols 1..100 encode zero runs; 101..106 escape coefficients or longer runs.
    private fun huffmanDecodeDataMem(
        token: WsqHelper.Token,
        size: Int,
    ): IntArray {
        val qdata = IntArray(size)
        val maxcode = IntArray(WsqHelper.MAX_HUFFBITS + 1)
        val mincode = IntArray(WsqHelper.MAX_HUFFBITS + 1)
        val valptr = IntArray(WsqHelper.MAX_HUFFBITS + 1)
        val marker: WsqHelper.IntRef = WsqHelper.IntRef(getCMarkerWSQ(token, WsqHelper.TBLS_N_SOB))
        val bitCount: WsqHelper.IntRef = WsqHelper.IntRef(0)
        val nextByte: WsqHelper.IntRef = WsqHelper.IntRef(0)
        var hufftableId = 0
        var ip = 0
        while (marker.value != WsqHelper.EOI_WSQ) {
            if (marker.value != 0) {
                while (marker.value != WsqHelper.SOB_WSQ) {
                    getCTableWSQ(token, marker.value)
                    marker.value = getCMarkerWSQ(token, WsqHelper.TBLS_N_SOB)
                    if (marker.value == WsqHelper.EOI_WSQ) {
                        break
                    }
                }
                if (marker.value == WsqHelper.EOI_WSQ) {
                    break
                }
                hufftableId = getCBlockHeader(token)
                if (token.tableDHT[hufftableId].tabdef != 1) {
                    throw IllegalArgumentException("ERROR : huffmanDecodeDataMem : huffman table undefined.")
                }
                val hufftable: Array<WsqHelper.HuffCode> =
                    buildHuffsizes(token.tableDHT[hufftableId].huffbits, WsqHelper.MAX_HUFFCOUNTS_WSQ)
                buildHuffcodes(hufftable)
                genDecodeTable(hufftable, maxcode, mincode, valptr, token.tableDHT[hufftableId].huffbits)
                bitCount.value = 0
                marker.value = 0
            }
            val nodeptr: Int =
                decodeDataMem(token, mincode, maxcode, valptr, token.tableDHT[hufftableId].huffvalues, bitCount, marker, nextByte)
            if (nodeptr == -1) {
                continue
            }
            when (nodeptr) {
                in 1..100, 105, 106 -> {
                    val count =
                        when (nodeptr) {
                            105 -> getCNextbitsWSQ(token, marker, bitCount, 8, nextByte)
                            106 -> getCNextbitsWSQ(token, marker, bitCount, 16, nextByte)
                            else -> nodeptr
                        }
                    require(count <= qdata.size - ip) { "WSQ zero run exceeds image size" }
                    // The coefficient buffer is initialized to zero.
                    ip += count
                }
                in 107..254, in 101..104 -> {
                    require(ip < qdata.size) { "Too many WSQ coefficients" }
                    qdata[ip++] =
                        when (nodeptr) {
                            101 -> getCNextbitsWSQ(token, marker, bitCount, 8, nextByte)
                            102 -> -getCNextbitsWSQ(token, marker, bitCount, 8, nextByte)
                            103 -> getCNextbitsWSQ(token, marker, bitCount, 16, nextByte)
                            104 -> -getCNextbitsWSQ(token, marker, bitCount, 16, nextByte)
                            else -> nodeptr - 180
                        }
                }
                else -> throw IllegalArgumentException("Invalid WSQ Huffman symbol: $nodeptr")
            }
        }
        val expected =
            (0 until WsqHelper.NUM_SUBBANDS).sumOf { band ->
                if (token.tableDQT.qBin[band] == 0.0f) 0 else token.qtree[band].lenx * token.qtree[band].leny
            }
        require(token.tableDQT.dqtDef == 1 && ip == expected) { "Incorrect WSQ coefficient count" }
        return qdata
    }

    private fun getCBlockHeader(token: WsqHelper.Token): Int {
        require(token.readShort() == 3) { "Invalid WSQ block header length" }
        return token.readByte().also {
            require(it < WsqHelper.MAX_DHT_TABLES) { "Invalid WSQ Huffman table ID" }
        }
    }

    private fun buildHuffsizes(
        huffbits: IntArray,
        maxHuffcounts: Int,
    ): Array<WsqHelper.HuffCode> {
        val huffcodeTable = Array(maxHuffcounts + 1) { WsqHelper.HuffCode() }
        var tempSize = 0

        for (codeSize in 1..WsqHelper.MAX_HUFFBITS) {
            val count = huffbits[codeSize - 1]
            repeat((1..count).count()) {
                huffcodeTable[tempSize] = WsqHelper.HuffCode()
                huffcodeTable[tempSize].size = codeSize
                tempSize++
            }
        }

        huffcodeTable[tempSize] = WsqHelper.HuffCode()
        huffcodeTable[tempSize].size = 0
        return huffcodeTable
    }

    private fun buildHuffcodes(huffcodeTable: Array<WsqHelper.HuffCode>) {
        var tempCode = 0
        var pointer = 0
        var tempSize = huffcodeTable[0].size
        if (huffcodeTable[pointer].size == 0) {
            return
        }
        do {
            do {
                huffcodeTable[pointer].code = tempCode
                tempCode++
                pointer++
            } while (huffcodeTable[pointer].size == tempSize)
            if (huffcodeTable[pointer].size == 0) {
                return
            }
            do {
                tempCode = (tempCode shl 1)
                tempSize++
            } while (huffcodeTable[pointer].size != tempSize)
        } while (huffcodeTable[pointer].size == tempSize)
    }

    private fun genDecodeTable(
        huffcodeTable: Array<WsqHelper.HuffCode>,
        maxcode: IntArray,
        mincode: IntArray,
        valptr: IntArray,
        huffbits: IntArray,
    ) {
        for (i in 0..WsqHelper.MAX_HUFFBITS) {
            maxcode[i] = 0
            mincode[i] = 0
            valptr[i] = 0
        }

        var i2 = 0
        for (i in 1..WsqHelper.MAX_HUFFBITS) {
            val bits = huffbits[i - 1]
            if (bits == 0) {
                maxcode[i] = -1
                continue
            }

            valptr[i] = i2
            mincode[i] = huffcodeTable[i2].code
            i2 = (i2 + bits) - 1
            maxcode[i] = huffcodeTable[i2].code
            i2++
        }
    }

    private fun decodeDataMem(
        token: WsqHelper.Token,
        mincode: IntArray,
        maxcode: IntArray,
        valptr: IntArray,
        huffvalues: IntArray,
        bitCount: WsqHelper.IntRef,
        marker: WsqHelper.IntRef,
        nextByte: WsqHelper.IntRef,
    ): Int {
        var code = getCNextbitsWSQ(token, marker, bitCount, 1, nextByte)
        if (marker.value != 0) {
            return -1
        }

        var inx = 1
        while (code > maxcode[inx]) {
            val tbits = getCNextbitsWSQ(token, marker, bitCount, 1, nextByte)
            code = (code shl 1) + tbits
            if (marker.value != 0) {
                return -1
            }
            inx++
        }

        val inx2 = (valptr[inx] + code) - mincode[inx]
        return huffvalues[inx2]
    }

    private fun getCNextbitsWSQ(
        token: WsqHelper.Token,
        marker: WsqHelper.IntRef,
        bitCount: WsqHelper.IntRef,
        bitsReq: Int,
        nextByte: WsqHelper.IntRef,
    ): Int {
        if (bitCount.value == 0) {
            nextByte.value = token.readByte()
            bitCount.value = 8
            if (nextByte.value == 0xFF) {
                val code2: Int = token.readByte()
                if ((code2 != 0x00) && (bitsReq == 1)) {
                    marker.value = ((nextByte.value shl 8) or code2)
                    return 1
                }
                if (code2 != 0x00) {
                    throw IllegalArgumentException("ERROR: getCNextbitsWSQ : No stuffed zeros.")
                }
            }
        }
        var bits: Int
        var tbits: Int
        var bitsNeeded: Int
        if (bitsReq <= bitCount.value) {
            bits = ((nextByte.value shr (bitCount.value - bitsReq)) and WsqHelper.bitMasks[bitsReq])
            bitCount.value -= bitsReq
            nextByte.value = (nextByte.value and WsqHelper.bitMasks[bitCount.value])
        } else {
            bitsNeeded = (bitsReq - bitCount.value)
            bits = (nextByte.value shl bitsNeeded)
            bitCount.value = 0
            tbits = getCNextbitsWSQ(token, marker, bitCount, bitsNeeded, nextByte)
            bits = (bits or tbits)
        }
        return bits
    }

    // Restore signed coefficients around each band's dead zone and quantizer bin center.
    private fun unquantize(
        token: WsqHelper.Token,
        sip: IntArray,
        width: Int,
        height: Int,
    ): FloatArray {
        val pixels = FloatArray(width * height)
        require(token.tableDQT.dqtDef == 1) { "Missing WSQ quantization table" }
        val center = token.tableDQT.binCenter
        var source = 0
        for (band in 0 until WsqHelper.NUM_SUBBANDS) {
            val bin = token.tableDQT.qBin[band]
            if (bin == 0.0f) continue
            val zeroBin = token.tableDQT.zBin[band]
            val region = token.qtree[band]
            for (row in 0 until region.leny) {
                var target = (region.y + row) * width + region.x
                repeat(region.lenx) {
                    val coefficient = sip[source++]
                    pixels[target++] =
                        when {
                            coefficient > 0 -> bin * (coefficient - center) + zeroBin / 2.0f
                            coefficient < 0 -> bin * (coefficient + center) - zeroBin / 2.0f
                            else -> 0.0f
                        }
                }
            }
        }
        return pixels
    }

    private fun wsqReconstruct(
        token: WsqHelper.Token,
        fdata: FloatArray,
        width: Int,
        height: Int,
    ) {
        require(token.tableDTT.lodef == 1) { "ERROR: wsq_reconstruct : Lopass filter coefficients not defined" }
        require(token.tableDTT.hidef == 1) { "ERROR: wsq_reconstruct : Hipass filter coefficients not defined" }

        val numPix = width * height
        val fdataTemp = FloatArray(numPix)

        for (node in (WsqHelper.W_TREELEN - 1) downTo 0) {
            val treeNode = token.wtree[node]
            val fdataBse = (treeNode.y * width) + treeNode.x

            joinLets(
                fdataTemp,
                fdata,
                0,
                fdataBse,
                treeNode.lenx,
                treeNode.leny,
                1,
                width,
                token.tableDTT.hifilt,
                token.tableDTT.hisz,
                token.tableDTT.lofilt,
                token.tableDTT.losz,
                treeNode.invcl,
            )
            joinLets(
                fdata,
                fdataTemp,
                fdataBse,
                0,
                treeNode.leny,
                treeNode.lenx,
                width,
                1,
                token.tableDTT.hifilt,
                token.tableDTT.hisz,
                token.tableDTT.lofilt,
                token.tableDTT.losz,
                treeNode.invrw,
            )
        }
    }

    /**
     * Synthesizes scanlines with the file's low/high-pass filters. The boundary
     * reflection and spectral inversion depend on filter and scanline parity.
     * Columns are synthesized first, then rows, from the smallest node outward.
     */
    private fun joinLets(
        newdata: FloatArray,
        olddata: FloatArray,
        newIndex: Int,
        oldIndex: Int,
        len1: Int,
        len2: Int,
        pitch: Int,
        stride: Int,
        hi: FloatArray,
        hsz: Int,
        lo: FloatArray,
        lsz: Int,
        inv: Int,
    ) {
        var lp0: Int
        var lp1: Int
        var hp0: Int
        var hp1: Int
        var lopass: Int
        var hipass: Int
        var limg: Int
        var himg: Int
        var daEv: Int
        var loc: Int
        var hoc: Int
        var hlen: Int
        var llen: Int
        var nstr: Int
        var pstr: Int
        var fiEv: Int
        var olle: Int
        var ohle: Int
        var olre: Int
        var ohre: Int
        var lle: Int
        var lle2: Int
        var lre: Int
        var lre2: Int
        var hle: Int
        var hle2: Int
        var hre: Int
        var hre2: Int
        var lpx: Int
        var lspx: Int
        var lpxstr: Int
        var lspxstr: Int
        var lstap: Int
        var lotap: Int
        var hpx: Int
        var hspx: Int
        var hpxstr: Int
        var hspxstr: Int
        var hstap: Int
        var hotap: Int
        var asym: Int
        var fhre = 0
        var ofhre: Int
        var ssfac: Float
        var osfac: Float
        var sfac: Float
        daEv = (len2 % 2)
        fiEv = (lsz % 2)
        pstr = stride
        nstr = -pstr
        if (daEv != 0) {
            llen = ((len2 + 1) / 2)
            hlen = (llen - 1)
        } else {
            llen = (len2 / 2)
            hlen = llen
        }
        if (fiEv != 0) {
            asym = 0
            ssfac = 1.0f
            ofhre = 0
            loc = ((lsz - 1) / 4)
            hoc = (((hsz + 1) / 4) - 1)
            lotap = (((lsz - 1) / 2) % 2)
            hotap = (((hsz + 1) / 2) % 2)
            if (daEv != 0) {
                olle = 0
                olre = 0
                ohle = 1
                ohre = 1
            } else {
                olle = 0
                olre = 1
                ohle = 1
                ohre = 0
            }
        } else {
            asym = 1
            ssfac = -1.0f
            ofhre = 2
            loc = ((lsz / 4) - 1)
            hoc = ((hsz / 4) - 1)
            lotap = ((lsz / 2) % 2)
            hotap = ((hsz / 2) % 2)
            if (daEv != 0) {
                olle = 1
                olre = 0
                ohle = 1
                ohre = 1
            } else {
                olle = 1
                olre = 1
                ohle = 1
                ohre = 1
            }
            if (loc == -1) {
                loc = 0
                olle = 0
            }
            if (hoc == -1) {
                hoc = 0
                ohle = 0
            }
            for (i in 0 until hsz) {
                hi[i] *= -1.0f
            }
        }
        for (clRw in 0 until len1) {
            limg = (newIndex + (clRw * pitch))
            himg = limg
            newdata[himg] = 0.0f
            newdata[(himg + stride)] = 0.0f
            if (inv != 0) {
                hipass = (oldIndex + (clRw * pitch))
                lopass = (hipass + (stride * hlen))
            } else {
                lopass = (oldIndex + (clRw * pitch))
                hipass = (lopass + (stride * llen))
            }
            lp0 = lopass
            lp1 = (lp0 + ((llen - 1) * stride))
            lspx = (lp0 + (loc * stride))
            lspxstr = nstr
            lstap = lotap
            lle2 = olle
            lre2 = olre
            hp0 = hipass
            hp1 = (hp0 + ((hlen - 1) * stride))
            hspx = (hp0 + (hoc * stride))
            hspxstr = nstr
            hstap = hotap
            hle2 = ohle
            hre2 = ohre
            osfac = ssfac
            repeat(hlen) {
                for (tap in lstap downTo 0) {
                    lle = lle2
                    lre = lre2
                    lpx = lspx
                    lpxstr = lspxstr
                    newdata[limg] = (olddata[lpx] * lo[tap])
                    for (i in (tap + 2) until lsz step 2) {
                        if (lpx == lp0) {
                            if (lle != 0) {
                                lpxstr = 0
                                lle = 0
                            } else {
                                lpxstr = pstr
                            }
                        }
                        if (lpx == lp1) {
                            if (lre != 0) {
                                lpxstr = 0
                                lre = 0
                            } else {
                                lpxstr = nstr
                            }
                        }
                        lpx += lpxstr
                        newdata[limg] += (olddata[lpx] * lo[i])
                    }
                    limg += stride
                }

                if (lspx == lp0) {
                    if (lle2 != 0) {
                        lspxstr = 0
                        lle2 = 0
                    } else {
                        lspxstr = pstr
                    }
                }
                lspx += lspxstr
                lstap = 1
                for (tap in hstap downTo 0) {
                    convolveHighPassTap(
                        newdata = newdata,
                        olddata = olddata,
                        hi = hi,
                        hsz = hsz,
                        himg = himg,
                        tap = tap,
                        hp0 = hp0,
                        hp1 = hp1,
                        pstr = pstr,
                        nstr = nstr,
                        asym = asym,
                        daEv = daEv,
                        hle2 = hle2,
                        hre2 = hre2,
                        hspx = hspx,
                        hspxstr = hspxstr,
                        initialFhre = ofhre,
                        osfac = osfac,
                    )
                    himg += stride
                }
                if (hspx == hp0) {
                    if (hle2 != 0) {
                        hspxstr = 0
                        hle2 = 0
                    } else {
                        hspxstr = pstr
                        osfac = 1.0f
                    }
                }
                hspx += hspxstr
                hstap = 1
            }

            if (daEv != 0) {
                if (lotap != 0) {
                    lstap = 1
                } else {
                    lstap = 0
                }
            } else {
                if (lotap != 0) {
                    lstap = 2
                } else {
                    lstap = 1
                }
            }
            for (tap in 1 downTo lstap) {
                var lle = lle2
                var lre = lre2
                var lpx = lspx
                var lpxstr = lspxstr

                newdata[limg] = olddata[lpx] * lo[tap]
                for (i in (tap + 2) until lsz step 2) {
                    if (lpx == lp0) {
                        if (lle != 0) {
                            lpxstr = 0
                            lle = 0
                        } else {
                            lpxstr = pstr
                        }
                    }
                    if (lpx == lp1) {
                        if (lre != 0) {
                            lpxstr = 0
                            lre = 0
                        } else {
                            lpxstr = nstr
                        }
                    }
                    lpx += lpxstr
                    newdata[limg] += olddata[lpx] * lo[i]
                }
                limg += stride
            }
            if (daEv != 0) {
                hstap =
                    if (hotap != 0) {
                        1
                    } else {
                        0
                    }
                if (hsz == 2) {
                    hspx -= hspxstr
                    fhre = 1
                }
            } else {
                hstap =
                    if (hotap != 0) {
                        2
                    } else {
                        1
                    }
            }
            for (tap in 1 downTo hstap) {
                if (hsz != 2) {
                    fhre = ofhre
                }
                convolveHighPassTap(
                    newdata = newdata,
                    olddata = olddata,
                    hi = hi,
                    hsz = hsz,
                    himg = himg,
                    tap = tap,
                    hp0 = hp0,
                    hp1 = hp1,
                    pstr = pstr,
                    nstr = nstr,
                    asym = asym,
                    daEv = daEv,
                    hle2 = hle2,
                    hre2 = hre2,
                    hspx = hspx,
                    hspxstr = hspxstr,
                    initialFhre = fhre,
                    osfac = osfac,
                )
                himg += stride
            }
        }

        if (fiEv == 0) {
            for (i in 0 until hsz) {
                hi[i] *= -1.0f
            }
        }
    }

    private fun convolveHighPassTap(
        newdata: FloatArray,
        olddata: FloatArray,
        hi: FloatArray,
        hsz: Int,
        himg: Int,
        tap: Int,
        hp0: Int,
        hp1: Int,
        pstr: Int,
        nstr: Int,
        asym: Int,
        daEv: Int,
        hle2: Int,
        hre2: Int,
        hspx: Int,
        hspxstr: Int,
        initialFhre: Int,
        osfac: Float,
    ) {
        var hle = hle2
        var hre = hre2
        var hpx = hspx
        var hpxstr = hspxstr
        var fhre = initialFhre
        var sfac = osfac
        for (i in tap until hsz step 2) {
            if (hpx == hp0) {
                if (hle != 0) {
                    hpxstr = 0
                    hle = 0
                } else {
                    hpxstr = pstr
                    sfac = 1.0f
                }
            }
            if (hpx == hp1) {
                if (hre != 0) {
                    hpxstr = 0
                    hre = 0
                    if (asym != 0 && daEv != 0) {
                        hre = 1
                        fhre--
                        sfac = fhre.toFloat()
                        if (sfac == 0.0f) {
                            hre = 0
                        }
                    }
                } else {
                    hpxstr = nstr
                    if (asym != 0) {
                        sfac = -1.0f
                    }
                }
            }
            newdata[himg] += (olddata[hpx] * hi[i]) * sfac
            hpx += hpxstr
        }
    }

    private fun convertImage2Byte(
        img: FloatArray,
        width: Int,
        height: Int,
        mShift: Float,
        rScale: Float,
    ): ByteArray =
        ByteArray(width * height) { index ->
            // Match WSQ's float reconstruction, rounding and saturation to [0, 255].
            val pixel = ((img[index] * rScale + mShift) + 0.5).toFloat()
            pixel.toInt().coerceIn(0, 255).toByte()
        }
}
