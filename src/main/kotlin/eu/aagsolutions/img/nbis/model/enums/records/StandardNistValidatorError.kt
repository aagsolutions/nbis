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

/**
 * Represents a standard NIST validation error.
 *
 * This class encapsulates details of an error encountered during the validation
 * of a NIST record, including a descriptive message, the type of the field where
 * the error occurred, and an optional error code.
 *
 * @property message Describes the details of the validation error.
 * @property fieldTypeEnum The specific type of the field in which the error occurred,
 * identified using the `FieldType` enumeration.
 * @property code An optional code that provides additional standardized context about the error.
 */
enum class StandardNistValidatorError(
    val message: String,
    val fieldTypeEnum: FieldType,
) {
    STD_ERR_MISSING_STANDARD("NIST standard invalid", TransactionInformationFields.VER),
    STD_ERR_UNIMPLEMENTED_STANDARD("NIST standard not implemented", TransactionInformationFields.VER),
    STD_ERR_MISSING_RT("Record Type is missing for this NIST standard", TransactionInformationFields.TOT),
    STD_ERR_BAD_RT("Record Type is not conform with the NIST standard", TransactionInformationFields.TOT),
    STD_ERR_FORBIDDEN_RT("Record type not allowed for this NIST standard", TransactionInformationFields.TOT),
    STD_ERR_LEN("The expected format is : 1 to 10 digits", TransactionInformationFields.LEN),
    STD_ERR_IDC("The expected value is between 0 and 99", UserDefinedTextFields.IDC),
    STD_ERR_SRC("SRC is unlimited in size and is \"U\" character type", VariableResolutionFingerprintImageFields.SRC),
    STD_ERR_SRC_36("The field should contain maximum 36 AN-characters", VariableResolutionFingerprintImageFields.SRC),
    STD_ERR_DMM("DMM should be present in reference", VariableResolutionFingerprintImageFields.DMM),

    // Errors for Record Type 1
    STD_ERR_VER_RT1("Invalid field \"VER\"", TransactionInformationFields.VER),
    STD_ERR_CNT_CONTENT_RT1("The content is not valid", TransactionInformationFields.CNT),
    STD_ERR_CNT_FORMAT_RT1("The field is mandatory", TransactionInformationFields.CNT),
    STD_ERR_TOT_RT1("Invalid field \"TOT\". Does not match regex", TransactionInformationFields.TOT),
    STD_ERR_DAI_RT1(
        "Invalid format for field \"DAI\". Should match : At least one character, and only characters from type A, N and S",
        TransactionInformationFields.DAI,
    ),
    STD_ERR_ORI_RT1(
        "Invalid format for field \"ORI\". Should match : At least one character, and only characters from type A, N and S",
        TransactionInformationFields.ORI,
    ),
    STD_ERR_DAT_RT1("The expected format is : YYYYMMDD", TransactionInformationFields.DAT),
    STD_ERR_DOM_RT1(
        "Invalid format for field \"DOM\". Should contain domain, with optionally a version",
        TransactionInformationFields.DOM,
    ),
    STD_ERR_PRY_RT1("The expected value is : between 0 and 2", TransactionInformationFields.PRY),
    STD_ERR_TCN_RT1(
        "Invalid format for field \"TCN\". Should match : At least one character, and only characters from type A, N and S",
        TransactionInformationFields.TCN,
    ),
    STD_ERR_NSR_NO_RT4_RT1("The expected value is : 00.00", TransactionInformationFields.NSR),
    STD_ERR_NSR_WITH_RT4_RT1("The expected value should match XX.XX", TransactionInformationFields.NSR),
    STD_ERR_NTR_NO_RT4_RT1("The expected value is : 00.00", TransactionInformationFields.NTR),
    STD_ERR_NTR_WITH_RT4_RT1("The expected value should match XX.XX", TransactionInformationFields.NTR),
    STD_ERR_GMT_RT1("The expected format is : YYYYMMDDHHMMSSZ", TransactionInformationFields.GMT),
    STD_ERR_DCS_RT1("The expected format is : CSI<US>CSN<US>CSV", TransactionInformationFields.DCS),
    STD_ERR_ANM_DAN_RT1(
        "The expected format is : DAN<US>OAN, incorrect value for DAN",
        TransactionInformationFields.ANM,
    ),
    STD_ERR_ANM_OAN_RT1(
        "The expected format is : DAN<US>OAN, incorrect value for OAN",
        TransactionInformationFields.ANM,
    ),
    STD_ERR_GNS_RT1("Allowed values are ISO and GENC", TransactionInformationFields.GNS),

    // Errors for Record Type 3
    STD_ERR_LEN_RT3("The expected format is : 1 to 10 digits", LowResolutionGrayscaleFingerprintImageFields.LEN),
    STD_ERR_IDC_RT3("The expected value is between 0 and 99", LowResolutionGrayscaleFingerprintImageFields.IDC),
    STD_ERR_IMP_RT3(
        "IMP value is mandatory and should be one of Impression Type allowed values",
        LowResolutionGrayscaleFingerprintImageFields.IMP,
    ),
    STD_ERR_FGP_RT3(
        "FGP is mandatory and should be a list of subfields containing friction ridge positions",
        LowResolutionGrayscaleFingerprintImageFields.FGP,
    ),
    STD_ERR_ISR_RT3(
        "Is mandatory and should be a numerical field between 0 and 1",
        LowResolutionGrayscaleFingerprintImageFields.ISR,
    ),
    STD_ERR_HLL_RT3(
        "Is mandatory and should be a numerical field between 10 and 65535",
        LowResolutionGrayscaleFingerprintImageFields.HLL,
    ),
    STD_ERR_VLL_RT3(
        "Is mandatory and should be a numerical field between 10 and 65535",
        LowResolutionGrayscaleFingerprintImageFields.VLL,
    ),
    STD_ERR_GCA_RT3(
        "GCA/CGA Is mandatory, and should be one of Compression Algorithms",
        LowResolutionGrayscaleFingerprintImageFields.GCA,
    ),
    STD_ERR_DATA_RT3(
        "DATA value is mandatory and should be one of Impression Type allowed values",
        LowResolutionGrayscaleFingerprintImageFields.DATA,
    ),

    // Errors for Record Type 4
    STD_ERR_LEN_RT4("The expected format is : 1 to 10 digits", HighResolutionGrayscaleFingerprintImageFields.LEN),
    STD_ERR_IDC_RT4("The expected value is between 0 and 99", HighResolutionGrayscaleFingerprintImageFields.IDC),
    STD_ERR_IMP_RT4(
        "IMP value is mandatory and should be one of Impression Type allowed values",
        HighResolutionGrayscaleFingerprintImageFields.IMP,
    ),
    STD_ERR_FGP_RT4(
        "FGP is mandatory and should be a list of subfields containing friction ridge positions",
        HighResolutionGrayscaleFingerprintImageFields.FGP,
    ),
    STD_ERR_ISR_RT4(
        "Is mandatory and should be a numerical field between 0 and 1",
        HighResolutionGrayscaleFingerprintImageFields.ISR,
    ),
    STD_ERR_HLL_RT4(
        "Is mandatory and should be a numerical field between 10 and 65535",
        HighResolutionGrayscaleFingerprintImageFields.HLL,
    ),
    STD_ERR_VLL_RT4(
        "Is mandatory and should be a numerical field between 10 and 65535",
        HighResolutionGrayscaleFingerprintImageFields.VLL,
    ),
    STD_ERR_GCA_RT4(
        "GCA/BCA Is mandatory, and should be one of Compression Algorithms",
        HighResolutionGrayscaleFingerprintImageFields.GCA,
    ),
    STD_ERR_DATA_RT4(
        "DATA value is mandatory and should be one of Impression Type allowed values",
        HighResolutionGrayscaleFingerprintImageFields.DATA,
    ),

    // Errors for Record Type 5
    STD_ERR_LEN_RT5("The expected format is : 1 to 10 digits", LowResolutionBinaryFingerprintImageFields.LEN),
    STD_ERR_IDC_RT5("The expected value is between 0 and 99", LowResolutionBinaryFingerprintImageFields.IDC),
    STD_ERR_IMP_RT5(
        "IMP value is mandatory and should be one of Impression Type allowed values",
        LowResolutionBinaryFingerprintImageFields.IMP,
    ),
    STD_ERR_FGP_RT5(
        "FGP is mandatory and should be a list of subfields containing friction ridge positions",
        LowResolutionBinaryFingerprintImageFields.FGP,
    ),
    STD_ERR_ISR_RT5(
        "Is mandatory and should be a numerical field between 0 and 1",
        LowResolutionBinaryFingerprintImageFields.ISR,
    ),
    STD_ERR_HLL_RT5(
        "Is mandatory and should be a numerical field between 10 and 65535",
        LowResolutionBinaryFingerprintImageFields.HLL,
    ),
    STD_ERR_VLL_RT5(
        "Is mandatory and should be a numerical field between 10 and 65535",
        LowResolutionBinaryFingerprintImageFields.VLL,
    ),
    STD_ERR_GCA_RT5(
        "GCA/CGA Is mandatory, and should be one of Compression Algorithms",
        LowResolutionBinaryFingerprintImageFields.GCA,
    ),
    STD_ERR_DATA_RT5(
        "DATA value is mandatory and should be one of Impression Type allowed values",
        LowResolutionBinaryFingerprintImageFields.DATA,
    ),

    // Errors for Record Type 6
    STD_ERR_LEN_RT6("The expected format is : 1 to 10 digits", HighResolutionBinaryFingerprintImageFields.LEN),
    STD_ERR_IDC_RT6("The expected value is between 0 and 99", HighResolutionBinaryFingerprintImageFields.IDC),
    STD_ERR_IMP_RT6(
        "IMP value is mandatory and should be one of Impression Type allowed values",
        HighResolutionBinaryFingerprintImageFields.IMP,
    ),
    STD_ERR_FGP_RT6(
        "FGP is mandatory and should be a list of subfields containing friction ridge positions",
        HighResolutionBinaryFingerprintImageFields.FGP,
    ),
    STD_ERR_ISR_RT6(
        "Is mandatory and should be a numerical field between 0 and 1",
        HighResolutionBinaryFingerprintImageFields.ISR,
    ),
    STD_ERR_HLL_RT6(
        "Is mandatory and should be a numerical field between 10 and 65535",
        HighResolutionBinaryFingerprintImageFields.HLL,
    ),
    STD_ERR_VLL_RT6(
        "Is mandatory and should be a numerical field between 10 and 65535",
        HighResolutionBinaryFingerprintImageFields.VLL,
    ),
    STD_ERR_GCA_RT6(
        "GCA/CGA Is mandatory, and should be one of Compression Algorithms",
        HighResolutionBinaryFingerprintImageFields.GCA,
    ),
    STD_ERR_DATA_RT6(
        "DATA value is mandatory and should be one of Impression Type allowed values",
        HighResolutionBinaryFingerprintImageFields.DATA,
    ),

    // Errors for Record Type 10
    STD_ERR_LEN_RT10("The expected format is : 1 to 10 digits", FacialAndSMTImageFields.LEN),
    STD_ERR_IDC_RT10("The expected value is between 0 and 99", FacialAndSMTImageFields.IDC),
    STD_ERR_IDC_RT10_0("IDC cannot starting with '0'", FacialAndSMTImageFields.IDC),
    STD_ERR_IMT_RT10(
        "IMT value is mandatory and should be one of Faciale or SMT allowed values",
        FacialAndSMTImageFields.IMT,
    ),
    STD_ERR_SRC_RT10("Is Mandatory field with size between 10 and 36", FacialAndSMTImageFields.SRC),
    STD_ERR_SRC_RT10_U("Is Mandatory field with Unicode", FacialAndSMTImageFields.SRC),
    STD_ERR_PHD_RT10("Is Mandatory date field", FacialAndSMTImageFields.PHD),
    STD_ERR_HLL_RT10("Is mandatory and value should be between 10 and 99999", FacialAndSMTImageFields.HLL),
    STD_ERR_VLL_RT10("Is mandatory and value should be between 10 and 99999", FacialAndSMTImageFields.VLL),
    STD_ERR_SLC_RT10("Is mandatory, and value should be 0, 1 or 2", FacialAndSMTImageFields.SLC),
    STD_ERR_HPS_RT10(
        "Is mandatory and value should be between 10 and 99999",
        FacialAndSMTImageFields.HPS_LEGACY,
    ),
    STD_ERR_VPS_RT10(
        "Is mandatory and value should be between 10 and 99999",
        FacialAndSMTImageFields.VPS_LEGACY,
    ),
    STD_ERR_THPS_RT10("Is mandatory and value should be between 10 and 99999", FacialAndSMTImageFields.THPS),
    STD_ERR_TVPS_RT10("Is mandatory and value should be between 10 and 99999", FacialAndSMTImageFields.TVPS),
    STD_ERR_CGA_RT10("Is mandatory, and should be one of Compression Algorithms", FacialAndSMTImageFields.CGA),
    STD_ERR_CSP_RT10("Is mandatory, and should be one of allowed values", FacialAndSMTImageFields.CSP),
    STD_ERR_FIP_RT10("Is optional, but must be a list of values", FacialAndSMTImageFields.FIP),
    STD_ERR_FIP_RT10_1("Is optional, but 1 < LHC < HLL and LHC < RHC < HLL ", FacialAndSMTImageFields.FIP),
    STD_ERR_FIP_RT10_2("Is optional, but 1 < TVC < VLL and TVC < BVC < VLL", FacialAndSMTImageFields.FIP),
    STD_ERR_FPFI_RT10("Is optional, but must contains subfields", FacialAndSMTImageFields.FPFI),
    STD_ERR_SAP_RT10("Is mandatory, and should be one of allowed values", FacialAndSMTImageFields.SAP),
    STD_ERR_SMT_RT10("Is mandatory, and should be one of allowed values", FacialAndSMTImageFields.SMT),
    STD_ERR_SMT_RT10_FORMAT("Is optional but must be one of allowed values", FacialAndSMTImageFields.SMT),
    STD_ERR_SHPS_RT10(
        "Is optional but must be a numerical field between 10 and 99999",
        FacialAndSMTImageFields.SHPS,
    ),
    STD_ERR_SVPS_RT10(
        "Is optional but must be a numerical field between 10 and 99999",
        FacialAndSMTImageFields.SVPS,
    ),
    STD_ERR_DIST_RT10("Is optional but must contains subfields", FacialAndSMTImageFields.DIST),
    STD_ERR_DIST_RT10_IMT_MUST_BE_FACE(
        "Is optional but can only be used if IMT is FACE",
        FacialAndSMTImageFields.DIST,
    ),
    STD_ERR_LAF_RT10("Is optional but must contains subfields", FacialAndSMTImageFields.LAF),
    STD_ERR_POS_RT10("Is optional but must be in collection (F, R, L, A, D)", FacialAndSMTImageFields.POS),
    STD_ERR_POA_RT10(
        "Is optional but must be a numerical field between -180 and 180",
        FacialAndSMTImageFields.POA,
    ),
    STD_ERR_PXS_LEGACY_RT10(
        "Is optional but must be in collection (GLASSES, HAT, SCARF, PHYSICAL, OTHER)",
        FacialAndSMTImageFields.PXS_LEGACY,
    ),
    STD_ERR_PXS_LEGACY_RT10_DEPRECATED("Is deprecated", FacialAndSMTImageFields.PXS_LEGACY),
    STD_ERR_PAS_RT10(
        "Is optional but must be in collection of acquisition source types codes",
        FacialAndSMTImageFields.PAS,
    ),
    STD_ERR_SQS_RT10("Is optional but must be a list quality score", FacialAndSMTImageFields.SQS),
    STD_ERR_SPA_RT10("Is optional but must be a list separated with US", FacialAndSMTImageFields.SPA),
    STD_ERR_SXS_RT10("Is mandatory if SAP>=40 and must be a list in collection", FacialAndSMTImageFields.SXS),
    STD_ERR_SEC_RT10("Is mandatory if SAP>=40 and must be a value of collection", FacialAndSMTImageFields.SEC),
    STD_ERR_SHC_RT10("Is mandatory if SAP>=40 and must be a value of collection", FacialAndSMTImageFields.SHC),
    STD_ERR_FFP_RT10("Is optional but must be a list of points", FacialAndSMTImageFields.FFP),
    STD_ERR_DMM_RT10(
        "Is optional but must be an alphanumeric with length from 8 to 11",
        FacialAndSMTImageFields.DMM,
    ),
    STD_ERR_TMC_RT10(
        "Is optional but must be an numeric with length from 1 to 3",
        FacialAndSMTImageFields.TMC,
    ),
    STD_ERR_3DF_RT10("Is optional but must contains subfields", FacialAndSMTImageFields.THREEDF),
    STD_ERR_FEC_RT10("Is optional but must contains subfields", FacialAndSMTImageFields.FEC),
    STD_ERR_COM_RT10("Is optional but must be a unicode with length 1 to 126", FacialAndSMTImageFields.FEC),

    STD_ERR_SMS_RT10(
        "Is optional but must be a pair of numbers between 1 to 999",
        FacialAndSMTImageFields.SMS,
    ),
    STD_ERR_SMD_RT10("Is optional but must be a list of items", FacialAndSMTImageFields.SMD),
    STD_ERR_COL_RT10("Is optional but must be a list of colors", FacialAndSMTImageFields.COL),
    STD_ERR_ITX_RT10("Is optional but must be a list of transform", FacialAndSMTImageFields.ITX),
    STD_ERR_OCC_RT10("Is optional but must be a list of items", FacialAndSMTImageFields.OCC),
    STD_ERR_SUB_RT10("Is optional but must be a list of items", FacialAndSMTImageFields.SUB),
    STD_ERR_PID_RT10("Is optional but must be a repeated list of items", FacialAndSMTImageFields.PID),
    STD_ERR_CID_RT10("Is optional but must be a unique list of items", FacialAndSMTImageFields.CID),
    STD_ERR_VID_RT10("Is optional but must be a unique list of items", FacialAndSMTImageFields.VID),
    STD_ERR_RSP_RT10("Is optional but must be a unique list of items", FacialAndSMTImageFields.RSP),
    STD_ERR_CON_RT10("Is optional but unicode string", FacialAndSMTImageFields.CON),
    STD_ERR_ANN_RT10("Is optional but must be a list of items", FacialAndSMTImageFields.ANN),
    STD_ERR_DUI_RT10("Is optional but must ANS field starting with M or P", FacialAndSMTImageFields.DUI),
    STD_ERR_MMS_RT10("Is optional but must be a list of items", FacialAndSMTImageFields.MMS),
    STD_ERR_T2C_RT10("Is optional but must be a unique list of items", FacialAndSMTImageFields.T2C),
    STD_ERR_EFR_RT10(
        "Is optional but must be a unicode string with length 1,200",
        FacialAndSMTImageFields.EFR,
    ),
    STD_ERR_SAN_RT10(
        "Is optional but must be a unicode string with length 1,125",
        FacialAndSMTImageFields.SAN,
    ),
    STD_ERR_ASC_RT10("Is optional but must be a list of items", FacialAndSMTImageFields.ASC),
    STD_ERR_HAS_RT10("Is optional but must be a hexa string with length 64", FacialAndSMTImageFields.HAS),
    STD_ERR_SOR_RT10("Is optional but must be a list of items", FacialAndSMTImageFields.SOR),
    STD_ERR_GEO_RT10("Is optional but must be a unique list of items", FacialAndSMTImageFields.GEO),
    STD_ERR_DATA_RT10("Image is mandatory", FacialAndSMTImageFields.DATA),

    // Errors for Record Type 13
    STD_ERR_LEN_RT13("The expected format is : 1 to 10 digits", LatentImageFields.LEN),
    STD_ERR_LCD_RT13("The expected format is : YYYYMMDD", LatentImageFields.LCD),
    STD_ERR_IMP_MANDATORY_RT13(
        "IMP value is mandatory and should be one of Impression Type allowed values",
        LatentImageFields.IMP,
    ),
    STD_ERR_FGP_RT13(
        "FGP is mandatory and should be a list of subfields containing friction ridge positions",
        LatentImageFields.FGP,
    ),
    STD_ERR_HLL_MANDATORY_RT13(
        "Is mandatory and should be a numerical field between 1 and 99999",
        LatentImageFields.HLL,
    ),
    STD_ERR_VLL_MANDATORY_RT13(
        "Is mandatory and should be a numerical field between 1 and 99999",
        LatentImageFields.VLL,
    ),
    STD_ERR_SLC_MANDATORY_RT13("Is mandatory, and value should be 0, 1 or 2", LatentImageFields.SLC),
    STD_ERR_THPS_MANDATORY_RT13(
        "Is mandatory, and value should be a positive integer",
        LatentImageFields.THPS,
    ),
    STD_ERR_TVPS_MANDATORY_RT13(
        "Is mandatory, and value should be a positive integer",
        LatentImageFields.TVPS,
    ),
    STD_ERR_CGA_MANDATORY_RT13(
        "Is mandatory, and should be one of Compression Algorithms",
        LatentImageFields.CGA,
    ),
    STD_ERR_BPX_MANDATORY_RT13("Is mandatory numerical field between 8 and 99", LatentImageFields.BPX),
    STD_ERR_SHPS_O_RT13(
        "SHPS value is optional and, if filled, it should be a positive integer with maximum 5 characters",
        LatentImageFields.SHPS,
    ),
    STD_ERR_SVPS_O_RT13(
        "SVPS value is optional and, if filled, it should be a positive integer with maximum 5 characters",
        LatentImageFields.SVPS,
    ),
    STD_ERR_COM_RT13("Should contains max 126 characters", LatentImageFields.COM),
    STD_ERR_PPC_RT13("Should be present only if FGP = 19", LatentImageFields.PPC),
    STD_ERR_LQM_RT13(
        "Should be a list with each with format : FRMP<US>QVU<US>QAV<US>QAP",
        LatentImageFields.LQM,
    ),
    STD_ERR_DATA_RT13(
        "DATA value is mandatory and should be one of Impression Type allowed values",
        LatentImageFields.DATA,
    ),

    // Errors for Record Type 14
    STD_ERR_IMP_MANDATORY_RT14(
        "IMP value is mandatory and should be one of Impression Type allowed values",
        VariableResolutionFingerprintImageFields.IMP,
    ),
    STD_ERR_IMP_NOT_ALLOWED_RT14(
        "IMP value is optional and, if filled, it should be one of Impression Type allowed values",
        VariableResolutionFingerprintImageFields.IMP,
    ),
    STD_ERR_FCD_RT14("The expected format is : YYYYMMDD", VariableResolutionFingerprintImageFields.FCD),
    STD_ERR_HLL_RT14(
        "Present only if there is an image, and value should be between 10 and 99999",
        VariableResolutionFingerprintImageFields.HLL,
    ),
    STD_ERR_HLL_MANDATORY_RT14(
        "Is mandatory and should be a numerical field between 1 and 99999",
        VariableResolutionFingerprintImageFields.HLL,
    ),
    STD_ERR_VLL_RT14(
        "Present only if there is an image, and value should be between 10 and 99999",
        VariableResolutionFingerprintImageFields.VLL,
    ),
    STD_ERR_VLL_MANDATORY_RT14(
        "Is mandatory and should be a numerical field between 1 and 99999",
        VariableResolutionFingerprintImageFields.VLL,
    ),
    STD_ERR_SLC_RT14(
        "Present only if there is an image, and value should be 0, 1 or 2",
        VariableResolutionFingerprintImageFields.SLC,
    ),
    STD_ERR_SLC_MANDATORY_RT14("Is mandatory, and value should be 0, 1 or 2", VariableResolutionFingerprintImageFields.SLC),
    STD_ERR_THPS_RT14(
        "Present only if there is an image, and value should be a positive integer",
        VariableResolutionFingerprintImageFields.THPS,
    ),
    STD_ERR_TVPS_RT14(
        "Present only if there is an image, and value should be a positive integer",
        VariableResolutionFingerprintImageFields.TVPS,
    ),
    STD_ERR_THPS_MANDATORY_RT14(
        "Is mandatory, and value should be a positive integer",
        VariableResolutionFingerprintImageFields.THPS,
    ),
    STD_ERR_TVPS_MANDATORY_RT14(
        "Is mandatory, and value should be a positive integer",
        VariableResolutionFingerprintImageFields.TVPS,
    ),
    STD_ERR_SLC_COHERENCE_RT14(
        "With value 1 or 2, THPS and TVPS should be equals",
        VariableResolutionFingerprintImageFields.SLC,
    ),
    STD_ERR_CGA_RT14(
        "Present only if there is an image, and should be one of Compression Algorithms",
        VariableResolutionFingerprintImageFields.CGA,
    ),
    STD_ERR_BPX_RT14(
        "Present only if there is an image, and should be between 8 and 99",
        VariableResolutionFingerprintImageFields.BPX,
    ),
    STD_ERR_CGA_MANDATORY_RT14(
        "Is mandatory, and should be one of Compression Algorithms",
        VariableResolutionFingerprintImageFields.CGA,
    ),
    STD_ERR_BPX_MANDATORY_RT14("Is mandatory numerical field", VariableResolutionFingerprintImageFields.BPX),
    STD_ERR_FGP_RT14(
        "FGP is mandatory and should be a list of subfields containing friction ridge positions",
        VariableResolutionFingerprintImageFields.FGP,
    ),
    STD_ERR_FGP_ONE_ALLOWED_RT14("Only one subfield is allowed", VariableResolutionFingerprintImageFields.FGP),
    STD_ERR_PPD_RT14("Should be present only if FGP = 19", VariableResolutionFingerprintImageFields.PPD),
    STD_ERR_PPC_RT14("Should be present only if FGP = 19", VariableResolutionFingerprintImageFields.PPC),
    STD_ERR_SHPS_O_RT14(
        "SHPS value is optional and, if filled, it should be a positive integer with maximum 5 characters",
        VariableResolutionFingerprintImageFields.SHPS,
    ),
    STD_ERR_SHPS_NOT_ALLOWED_RT14("Should only be present if there is an image", VariableResolutionFingerprintImageFields.SHPS),
    STD_ERR_SVPS_O_RT14(
        "SVPS value is optional and, if filled, it should be a positive integer with maximum 5 characters",
        VariableResolutionFingerprintImageFields.SVPS,
    ),
    STD_ERR_SVPS_NOT_ALLOWED_RT14("Should only be present if there is an image", VariableResolutionFingerprintImageFields.SVPS),
    STD_ERR_AMP_RT14(
        "Should be a list with unique element with format : FRAP<US>ABC",
        VariableResolutionFingerprintImageFields.AMP,
    ),
    STD_ERR_COM_RT14("Should contains max 126 characters", VariableResolutionFingerprintImageFields.COM),
    STD_ERR_NQM_RT14(
        "Should be a list with each element at format : FRNP<US>IQS with ISQ = [1,5] or 254 or 255",
        VariableResolutionFingerprintImageFields.NQM,
    ),
    STD_ERR_SEG_NOT_ALLOWED_RT14(
        "Should be not be present for this type of Friction Ridge Position",
        VariableResolutionFingerprintImageFields.SEG,
    ),
    STD_ERR_SEG_INVALID_RT14(
        "Should be multiple subfields with format : FRSP<US>LHC<US>RHC<US>TVC<US>BVC",
        VariableResolutionFingerprintImageFields.SEG,
    ),
    STD_ERR_SEQ_5_ITEMS_RT14("Should be a list with each containing 5 items", VariableResolutionFingerprintImageFields.SEG),
    STD_ERR_FQM_RT14(
        "Should be a list with each element at format : FRMP<US>QVU<US>QAV<US>QAP",
        VariableResolutionFingerprintImageFields.FQM,
    ),
    STD_ERR_SQM_RT14(
        "Should be a list with each with format : FRQP<US>QVU<US>QAV<US>QAP",
        VariableResolutionFingerprintImageFields.SQM,
    ),
    STD_ERR_SQM_UNALLOWED_FRQP_RT14(
        "FRQP Should be in the set of either the FRSP or FRAS values contained in this record",
        VariableResolutionFingerprintImageFields.SQM,
    ),
    STD_ERR_ASEG_RT14(
        "Should be a list with each with format : FRAS<US>NOP{<US>HPO<US>VPO}",
        VariableResolutionFingerprintImageFields.ASEG,
    ),
    STD_ERR_SCF_RT14("Should be an integer between 1 and 255", VariableResolutionFingerprintImageFields.SCF),
    STD_ERR_SIF_RT14("Should be 'Y' if present", VariableResolutionFingerprintImageFields.SIF),
    STD_ERR_FAP_RT14("Should be in referenced values", VariableResolutionFingerprintImageFields.FAP),
    STD_ERR_SUB_RT14("Should be with format : SSC<US>SBSC<US>SBCC", VariableResolutionFingerprintImageFields.SUB),
    STD_ERR_CON_RT14("Should contains maximum 1000 ANS characters", VariableResolutionFingerprintImageFields.CON),
    ;

    val code: String = name

    fun getFieldName(): String = fieldTypeEnum.code
}
