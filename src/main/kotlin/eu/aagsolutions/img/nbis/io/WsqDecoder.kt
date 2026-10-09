/*
 * Kotlin adaptation of the WSQ reconstruction algorithm from JNBIS.
 * Original author: M. H. Shamsi (2007), https://github.com/mhshams/jnbis
 * Licensed under the Apache License, Version 2.0. See META-INF/LICENSE-WSQ.
 * Modified in 2026: translated to Kotlin, added validation and PNG conversion.
 */
package eu.aagsolutions.img.nbis.io

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
    )

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
                throw IllegalArgumentException("ERROR: getCTableWSQ : Invalid table defined : " + marker)
            }
        }
        require(token.pointer == segmentEnd) { "Invalid WSQ table length" }
    }

    private fun getCComment(token: WsqHelper.Token) {
        token.readBytes(token.readShort() - 2)
    }

    private fun getCTransformTable(token: WsqHelper.Token) {
        token.readShort()
        token.tableDTT.hisz = token.readByte()
        token.tableDTT.losz = token.readByte()
        require(token.tableDTT.hisz > 0 && token.tableDTT.losz > 0) { "Empty WSQ wavelet filter" }
        token.tableDTT.hifilt = FloatArray(token.tableDTT.hisz)
        token.tableDTT.lofilt = FloatArray(token.tableDTT.losz)
        var aSize: Int
        if ((token.tableDTT.hisz % 2) != 0) {
            aSize = ((token.tableDTT.hisz + 1) / 2)
        } else {
            aSize = (token.tableDTT.hisz / 2)
        }
        val aLofilt: FloatArray = FloatArray(aSize)
        aSize--
        run {
            var cnt = 0
            while (cnt <= aSize) {
                val sign = token.readByte()
                var scale = token.readByte()
                val shrtDat: Long = token.readInt()
                aLofilt[cnt] = shrtDat.toFloat()
                while (scale > 0) {
                    aLofilt[cnt] = (aLofilt[cnt] / 10.0).toFloat()
                    scale--
                }
                if (sign != 0) {
                    aLofilt[cnt] *= -1.0f
                }
                if ((token.tableDTT.hisz % 2) != 0) {
                    token.tableDTT.hifilt[(cnt + aSize)] = (intSign(cnt) * aLofilt[cnt])
                    if (cnt > 0) {
                        token.tableDTT.hifilt[(aSize - cnt)] = token.tableDTT.hifilt[(cnt + aSize)]
                    }
                } else {
                    token.tableDTT.hifilt[((cnt + aSize) + 1)] = (intSign(cnt) * aLofilt[cnt])
                    token.tableDTT.hifilt[(aSize - cnt)] = (-1 * token.tableDTT.hifilt[((cnt + aSize) + 1)])
                }
                cnt++
            }
        }
        if ((token.tableDTT.losz % 2) != 0) {
            aSize = ((token.tableDTT.losz + 1) / 2)
        } else {
            aSize = (token.tableDTT.losz / 2)
        }
        val aHifilt: FloatArray = FloatArray(aSize)
        aSize--
        run {
            var cnt: Int = 0
            while (cnt <= aSize) {
                val sign: Int = token.readByte()
                var scale: Int = token.readByte()
                val shrtDat: Long = token.readInt()
                aHifilt[cnt] = shrtDat.toFloat()
                while (scale > 0) {
                    aHifilt[cnt] = (aHifilt[cnt] / 10.0).toFloat()
                    scale--
                }
                if (sign != 0) {
                    aHifilt[cnt] *= -1.0f
                }
                if ((token.tableDTT.losz % 2) != 0) {
                    token.tableDTT.lofilt[(cnt + aSize)] = (intSign(cnt) * aHifilt[cnt])
                    if (cnt > 0) {
                        token.tableDTT.lofilt[(aSize - cnt)] = token.tableDTT.lofilt[(cnt + aSize)]
                    }
                } else {
                    token.tableDTT.lofilt[((cnt + aSize) + 1)] = (intSign(cnt + 1) * aHifilt[cnt])
                    token.tableDTT.lofilt[(aSize - cnt)] = token.tableDTT.lofilt[((cnt + aSize) + 1)]
                }
                cnt++
            }
        }
        token.tableDTT.lodef = 1
        token.tableDTT.hidef = 1
    }

    private fun getCQuantizationTable(token: WsqHelper.Token) {
        token.readShort()
        var scale: Int = token.readByte()
        var shrtDat: Int = token.readShort()
        token.tableDQT.binCenter = shrtDat.toFloat()
        while (scale > 0) {
            token.tableDQT.binCenter = (token.tableDQT.binCenter / 10.0).toFloat()
            scale--
        }
        run {
            var cnt: Int = 0
            while (cnt < WsqHelper.MAX_SUBBANDS) {
                scale = token.readByte()
                shrtDat = token.readShort()
                token.tableDQT.qBin[cnt] = shrtDat.toFloat()
                while (scale > 0) {
                    token.tableDQT.qBin[cnt] = (token.tableDQT.qBin[cnt] / 10.0).toFloat()
                    scale--
                }
                scale = token.readByte()
                shrtDat = token.readShort()
                token.tableDQT.zBin[cnt] = shrtDat.toFloat()
                while (scale > 0) {
                    token.tableDQT.zBin[cnt] = (token.tableDQT.zBin[cnt] / 10.0).toFloat()
                    scale--
                }
                cnt++
            }
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
        val huffmanTable: WsqHelper.HuffmanTable = WsqHelper.HuffmanTable()
        if (readTableLen) {
            huffmanTable.tableLen = token.readShort()
            huffmanTable.bytesLeft = (huffmanTable.tableLen - 2)
            bytesLeft = huffmanTable.bytesLeft
        } else {
            huffmanTable.bytesLeft = bytesLeft
        }
        if (bytesLeft <= 0) {
            throw IllegalArgumentException("ERROR : getCHuffmanTable : no huffman table bytes remaining")
        }
        huffmanTable.tableId = token.readByte()
        require(huffmanTable.tableId < WsqHelper.MAX_DHT_TABLES) { "Invalid WSQ Huffman table ID" }
        huffmanTable.bytesLeft--
        huffmanTable.huffbits = IntArray(WsqHelper.MAX_HUFFBITS)
        var numHufvals: Int = 0
        run {
            var i: Int = 0
            while (i < WsqHelper.MAX_HUFFBITS) {
                huffmanTable.huffbits[i] = token.readByte()
                numHufvals += huffmanTable.huffbits[i]
                i++
            }
        }
        var availableCodes = 1
        for (count in huffmanTable.huffbits) {
            availableCodes = availableCodes * 2 - count
            require(availableCodes >= 0) { "Oversubscribed WSQ Huffman table" }
        }
        huffmanTable.bytesLeft -= WsqHelper.MAX_HUFFBITS
        if (numHufvals > maxHuffcounts) {
            throw IllegalArgumentException("ERROR : getCHuffmanTable : numHufvals is larger than MAX_HUFFCOUNTS")
        }
        huffmanTable.huffvalues = IntArray(maxHuffcounts + 1)
        run {
            var i: Int = 0
            while (i < numHufvals) {
                huffmanTable.huffvalues[i] = token.readByte()
                i++
            }
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
        var lenx: Int = 0
        var lenx2: Int = 0
        var leny: Int = 0
        var leny2: Int = 0
        token.wtree = Array(wtreelen) { WsqHelper.WaveletTree() }
        run {
            var i: Int = 0
            while (i < wtreelen) {
                token.wtree[i] = WsqHelper.WaveletTree()
                token.wtree[i].invrw = 0
                token.wtree[i].invcl = 0
                i++
            }
        }
        token.wtree[2].invrw = 1
        token.wtree[4].invrw = 1
        token.wtree[7].invrw = 1
        token.wtree[9].invrw = 1
        token.wtree[11].invrw = 1
        token.wtree[13].invrw = 1
        token.wtree[16].invrw = 1
        token.wtree[18].invrw = 1
        token.wtree[3].invcl = 1
        token.wtree[5].invcl = 1
        token.wtree[8].invcl = 1
        token.wtree[9].invcl = 1
        token.wtree[12].invcl = 1
        token.wtree[13].invcl = 1
        token.wtree[17].invcl = 1
        token.wtree[18].invcl = 1
        wtree4(token, 0, 1, width, height, 0, 0, 1)
        if ((token.wtree[1].lenx % 2) == 0) {
            lenx = (token.wtree[1].lenx / 2)
            lenx2 = lenx
        } else {
            lenx = ((token.wtree[1].lenx + 1) / 2)
            lenx2 = (lenx - 1)
        }
        if ((token.wtree[1].leny % 2) == 0) {
            leny = (token.wtree[1].leny / 2)
            leny2 = leny
        } else {
            leny = ((token.wtree[1].leny + 1) / 2)
            leny2 = (leny - 1)
        }
        wtree4(token, 4, 6, lenx2, leny, lenx, 0, 0)
        wtree4(token, 5, 10, lenx, leny2, 0, leny, 0)
        wtree4(token, 14, 15, lenx, leny, 0, 0, 0)
        token.wtree[19].x = 0
        token.wtree[19].y = 0
        if ((token.wtree[15].lenx % 2) == 0) {
            token.wtree[19].lenx = (token.wtree[15].lenx / 2)
        } else {
            token.wtree[19].lenx = ((token.wtree[15].lenx + 1) / 2)
        }
        if ((token.wtree[15].leny % 2) == 0) {
            token.wtree[19].leny = (token.wtree[15].leny / 2)
        } else {
            token.wtree[19].leny = ((token.wtree[15].leny + 1) / 2)
        }
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
        var evenx: Int = 0
        var eveny: Int = 0
        var p1: Int = 0
        var p2: Int = 0
        p1 = start1
        p2 = start2
        evenx = (lenx % 2)
        eveny = (leny % 2)
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
        token.qtree = Array(qtreelen) { WsqHelper.QuantTree() }
        run {
            var i: Int = 0
            while (i < token.qtree.size) {
                token.qtree[i] = WsqHelper.QuantTree()
                i++
            }
        }
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
        var tempx: Int = 0
        var temp2x: Int = 0
        var tempy: Int = 0
        var temp2y: Int = 0
        var evenx: Int = 0
        var eveny: Int = 0
        var p: Int = 0
        p = start
        evenx = (lenx % 2)
        eveny = (leny % 2)
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
        var evenx: Int = 0
        var eveny: Int = 0
        var p: Int = 0
        p = start
        evenx = (lenx % 2)
        eveny = (leny % 2)
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
        val qdata: IntArray = IntArray(size)
        val maxcode: IntArray = IntArray(WsqHelper.MAX_HUFFBITS + 1)
        val mincode: IntArray = IntArray(WsqHelper.MAX_HUFFBITS + 1)
        val valptr: IntArray = IntArray(WsqHelper.MAX_HUFFBITS + 1)
        val marker: WsqHelper.IntRef = WsqHelper.IntRef(getCMarkerWSQ(token, WsqHelper.TBLS_N_SOB))
        val bitCount: WsqHelper.IntRef = WsqHelper.IntRef(0)
        val nextByte: WsqHelper.IntRef = WsqHelper.IntRef(0)
        var hufftableId: Int = 0
        var ip: Int = 0
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
        lateinit var huffcodeTable: Array<WsqHelper.HuffCode>
        var numberOfCodes: Int = 1
        huffcodeTable = Array(maxHuffcounts + 1) { WsqHelper.HuffCode() }
        var tempSize: Int = 0
        run {
            var codeSize: Int = 1
            while (codeSize <= WsqHelper.MAX_HUFFBITS) {
                while (numberOfCodes <= huffbits[(codeSize - 1)]) {
                    huffcodeTable[tempSize] = WsqHelper.HuffCode()
                    huffcodeTable[tempSize].size = codeSize
                    tempSize++
                    numberOfCodes++
                }
                numberOfCodes = 1
                codeSize++
            }
        }
        huffcodeTable[tempSize] = WsqHelper.HuffCode()
        huffcodeTable[tempSize].size = 0
        return huffcodeTable
    }

    private fun buildHuffcodes(huffcodeTable: Array<WsqHelper.HuffCode>) {
        var tempCode: Int = 0
        var pointer: Int = 0
        var tempSize: Int = huffcodeTable[0].size
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
        run {
            var i: Int = 0
            while (i <= WsqHelper.MAX_HUFFBITS) {
                maxcode[i] = 0
                mincode[i] = 0
                valptr[i] = 0
                i++
            }
        }
        var i2: Int = 0
        run {
            var i: Int = 1
            while (i <= WsqHelper.MAX_HUFFBITS) {
                if (huffbits[(i - 1)] == 0) {
                    maxcode[i] = -1
                    i++
                    continue
                }
                valptr[i] = i2
                mincode[i] = huffcodeTable[i2].code
                i2 = ((i2 + huffbits[(i - 1)]) - 1)
                maxcode[i] = huffcodeTable[i2].code
                i2++
                i++
            }
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
        var code: Int = getCNextbitsWSQ(token, marker, bitCount, 1, nextByte)
        if (marker.value != 0) {
            return -1
        }
        var inx: Int = 0
        run {
            inx = 1
            while (code > maxcode[inx]) {
                val tbits: Int = getCNextbitsWSQ(token, marker, bitCount, 1, nextByte)
                code = ((code shl 1) + tbits)
                if (marker.value != 0) {
                    return -1
                }
                inx++
            }
        }
        val inx2: Int = ((valptr[inx] + code) - mincode[inx])
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
        var bits: Int = 0
        var tbits: Int = 0
        var bitsNeeded: Int = 0
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
                for (column in 0 until region.lenx) {
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
        if (token.tableDTT.lodef != 1) {
            throw IllegalArgumentException("ERROR: wsq_reconstruct : Lopass filter coefficients not defined")
        }
        if (token.tableDTT.hidef != 1) {
            throw IllegalArgumentException("ERROR: wsq_reconstruct : Hipass filter coefficients not defined")
        }
        val numPix: Int = (width * height)
        val fdataTemp: FloatArray = FloatArray(numPix)
        run {
            var node: Int = (WsqHelper.W_TREELEN - 1)
            while (node >= 0) {
                val fdataBse: Int = ((token.wtree[node].y * width) + token.wtree[node].x)
                joinLets(
                    fdataTemp,
                    fdata,
                    0,
                    fdataBse,
                    token.wtree[node].lenx,
                    token.wtree[node].leny,
                    1,
                    width,
                    token.tableDTT.hifilt,
                    token.tableDTT.hisz,
                    token.tableDTT.lofilt,
                    token.tableDTT.losz,
                    token.wtree[node].invcl,
                )
                joinLets(
                    fdata,
                    fdataTemp,
                    fdataBse,
                    0,
                    token.wtree[node].leny,
                    token.wtree[node].lenx,
                    width,
                    1,
                    token.tableDTT.hifilt,
                    token.tableDTT.hisz,
                    token.tableDTT.lofilt,
                    token.tableDTT.losz,
                    token.wtree[node].invrw,
                )
                node--
            }
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
        var lp0: Int = 0
        var lp1: Int = 0
        var hp0: Int = 0
        var hp1: Int = 0
        var lopass: Int = 0
        var hipass: Int = 0
        var limg: Int = 0
        var himg: Int = 0
        var pix: Int = 0
        var clRw: Int = 0
        var i: Int = 0
        var daEv: Int = 0
        var loc: Int = 0
        var hoc: Int = 0
        var hlen: Int = 0
        var llen: Int = 0
        var nstr: Int = 0
        var pstr: Int = 0
        var tap: Int = 0
        var fiEv: Int = 0
        var olle: Int = 0
        var ohle: Int = 0
        var olre: Int = 0
        var ohre: Int = 0
        var lle: Int = 0
        var lle2: Int = 0
        var lre: Int = 0
        var lre2: Int = 0
        var hle: Int = 0
        var hle2: Int = 0
        var hre: Int = 0
        var hre2: Int = 0
        var lpx: Int = 0
        var lspx: Int = 0
        var lpxstr: Int = 0
        var lspxstr: Int = 0
        var lstap: Int = 0
        var lotap: Int = 0
        var hpx: Int = 0
        var hspx: Int = 0
        var hpxstr: Int = 0
        var hspxstr: Int = 0
        var hstap: Int = 0
        var hotap: Int = 0
        var asym: Int = 0
        var fhre: Int = 0
        var ofhre: Int = 0
        var ssfac: Float = 0.0f
        var osfac: Float = 0.0f
        var sfac: Float = 0.0f
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
            run {
                i = 0
                while (i < hsz) {
                    hi[i] *= -1.0f
                    i++
                }
            }
        }
        run {
            clRw = 0
            while (clRw < len1) {
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
                run {
                    pix = 0
                    while (pix < hlen) {
                        run {
                            tap = lstap
                            while (tap >= 0) {
                                lle = lle2
                                lre = lre2
                                lpx = lspx
                                lpxstr = lspxstr
                                newdata[limg] = (olddata[lpx] * lo[tap])
                                run {
                                    i = (tap + 2)
                                    while (i < lsz) {
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
                                        i += 2
                                    }
                                }
                                limg += stride
                                tap--
                            }
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
                        run {
                            tap = hstap
                            while (tap >= 0) {
                                hle = hle2
                                hre = hre2
                                hpx = hspx
                                hpxstr = hspxstr
                                fhre = ofhre
                                sfac = osfac
                                run {
                                    i = tap
                                    while (i < hsz) {
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
                                                if ((asym != 0) && (daEv != 0)) {
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
                                        newdata[himg] += ((olddata[hpx] * hi[i]) * sfac)
                                        hpx += hpxstr
                                        i += 2
                                    }
                                }
                                himg += stride
                                tap--
                            }
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
                        pix++
                    }
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
                run {
                    tap = 1
                    while (tap >= lstap) {
                        lle = lle2
                        lre = lre2
                        lpx = lspx
                        lpxstr = lspxstr
                        newdata[limg] = (olddata[lpx] * lo[tap])
                        run {
                            i = (tap + 2)
                            while (i < lsz) {
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
                                i += 2
                            }
                        }
                        limg += stride
                        tap--
                    }
                }
                if (daEv != 0) {
                    if (hotap != 0) {
                        hstap = 1
                    } else {
                        hstap = 0
                    }
                    if (hsz == 2) {
                        hspx -= hspxstr
                        fhre = 1
                    }
                } else {
                    if (hotap != 0) {
                        hstap = 2
                    } else {
                        hstap = 1
                    }
                }
                run {
                    tap = 1
                    while (tap >= hstap) {
                        hle = hle2
                        hre = hre2
                        hpx = hspx
                        hpxstr = hspxstr
                        sfac = osfac
                        if (hsz != 2) {
                            fhre = ofhre
                        }
                        run {
                            i = tap
                            while (i < hsz) {
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
                                        if ((asym != 0) && (daEv != 0)) {
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
                                newdata[himg] += ((olddata[hpx] * hi[i]) * sfac)
                                hpx += hpxstr
                                i += 2
                            }
                        }
                        himg += stride
                        tap--
                    }
                }
                clRw++
            }
        }
        if (fiEv == 0) {
            run {
                i = 0
                while (i < hsz) {
                    hi[i] *= -1.0f
                    i++
                }
            }
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
