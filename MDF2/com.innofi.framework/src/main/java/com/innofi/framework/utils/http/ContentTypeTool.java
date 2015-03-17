package com.innofi.framework.utils.http;

import java.util.HashMap;
import java.util.Map;

public class ContentTypeTool {

    /**
     * MicrosoftWord 微软 Word doc格式文件
     */
    public static final String CONTENT_TYPE_DOC = "doc";

    /**
     * MicrosoftWord 微软 Word docx格式文件
     */
    public static final String CONTENT_TYPE_DOCX = "docx";

    /**
     * MicrosoftWord 微软 Excel xls格式文件
     */
    public static final String CONTENT_TYPE_XLS = "xls";

    /**
     * MicrosoftWord 微软 Excel xlsx格式文件
     */
    public static final String CONTENT_TYPE_XLSX = "xlsx";

    /**
     * MicrosoftWord 微软 PowerPoint ppt格式文件
     */
    public static final String CONTENT_TYPE_PPT = "ppt";

    /**
     * MicrosoftWord 微软 PowerPoint pptx格式文件
     */
    public static final String CONTENT_TYPE_PPTX = "pptx";

    /**
     * MicrosoftWord 微软 PowerPoint pps格式文件
     */
    public static final String CONTENT_TYPE_PPS = "pps";

    /**
     * gif格式文件
     */
    public static final String CONTENT_TYPE_GIF = "gif";

    /**
     * jpg格式文件
     */
    public static final String CONTENT_TYPE_JPG = "jpg";

    /**
     * HTML格式文件
     */
    public static final String CONTENT_TYPE_HTML = "html";

    /**
     * 未知格式文件
     */
    public static final String CONTENT_TYPE_UNKNOW = "unknow";

    /**
     * Adobe PDF格式文件
     */
    public static final String CONTENT_TYPE_PDF = "pdf";

    /**
     * WMA格式文件
     */
    public static final String CONTENT_TYPE_WMA = "wma";

    /**
     * BMP格式文件
     */
    public static final String CONTENT_TYPE_BMP = "bmp";

    /**
     * RTF格式文件
     */
    public static final String CONTENT_TYPE_RTF = "rtf";

    /**
     * RFC格式文件
     */
    public static final String CONTENT_TYPE_RFC = "rfc";

    /**
     * MHT格式文件
     */
    public static final String CONTENT_TYPE_MHT = "mht";

    /**
     * MHTML格式文件
     */
    public static final String CONTENT_TYPE_MHTML = "mhtml";

    /**
     * TXT格式文件
     */
    public static final String CONTENT_TYPE_TXT = "txt";

    /**
     * 323格式文件
     */
    public static final String CONTENT_TYPE_323 = "323";

    /**
     * acx格式文件
     */
    public static final String CONTENT_TYPE_ACX = "acx";

    /**
     * ai格式文件
     */
    public static final String CONTENT_TYPE_AI = "ai";

    /**
     * aif格式文件
     */
    public static final String CONTENT_TYPE_AIF = "aif";

    /**
     * aifc格式文件
     */
    public static final String CONTENT_TYPE_AIFC = "aifc";

    /**
     * aiff格式文件
     */
    public static final String CONTENT_TYPE_AIFF = "aiff";

    /**
     * asf格式文件
     */
    public static final String CONTENT_TYPE_ASF = "asf";

    /**
     * asr格式文件
     */
    public static final String CONTENT_TYPE_ASR = "asr";

    /**
     * asx格式文件
     */
    public static final String CONTENT_TYPE_ASX = "asx";

    /**
     * au格式文件
     */
    public static final String CONTENT_TYPE_AU = "au";

    /**
     * avi格式文件
     */
    public static final String CONTENT_TYPE_AVI = "avi";

    /**
     * axs格式文件
     */
    public static final String CONTENT_TYPE_AXS = "axs";

    /**
     * bas格式文件
     */
    public static final String CONTENT_TYPE_BAS = "bas";

    /**
     * bcpio格式文件
     */
    public static final String CONTENT_TYPE_BCPIO = "bcpio";

    /**
     * bin格式文件
     */
    public static final String CONTENT_TYPE_BIN = "bin";

    /**
     * c格式文件
     */
    public static final String CONTENT_TYPE_C = "c";

    /**
     * cat格式文件
     */
    public static final String CONTENT_TYPE_CAT = "cat";

    /**
     * cdf格式文件
     */
    public static final String CONTENT_TYPE_CDF = "cdf";

    /**
     * cer格式文件
     */
    public static final String CONTENT_TYPE_CER = "cer";

    /**
     * class格式文件
     */
    public static final String CONTENT_TYPE_CLASS = "class";

    /**
     * clp格式文件
     */
    public static final String CONTENT_TYPE_CLP = "clp";

    /**
     * cmx格式文件
     */
    public static final String CONTENT_TYPE_CMX = "cmx";

    /**
     * cod格式文件
     */
    public static final String CONTENT_TYPE_COD = "cod";

    /**
     * cpio格式文件
     */
    public static final String CONTENT_TYPE_CPIO = "cpio";

    /**
     * crd格式文件
     */
    public static final String CONTENT_TYPE_CRD = "crd";

    /**
     * crl格式文件
     */
    public static final String CONTENT_TYPE_CRL = "crl";

    /**
     * crt格式文件
     */
    public static final String CONTENT_TYPE_CRT = "crt";

    /**
     * csh格式文件
     */
    public static final String CONTENT_TYPE_CSH = "csh";

    /**
     * css格式文件
     */
    public static final String CONTENT_TYPE_CSS = "css";

    /**
     * dcr格式文件
     */
    public static final String CONTENT_TYPE_DCR = "dcr";

    /**
     * der格式文件
     */
    public static final String CONTENT_TYPE_DER = "der";

    /**
     * dir格式文件
     */
    public static final String CONTENT_TYPE_DIR = "dir";

    /**
     * dll格式文件
     */
    public static final String CONTENT_TYPE_DLL = "dll";

    /**
     * dms格式文件
     */
    public static final String CONTENT_TYPE_DMS = "dms";

    /**
     * dot格式文件
     */
    public static final String CONTENT_TYPE_DOT = "dot";

    /**
     * dvi格式文件
     */
    public static final String CONTENT_TYPE_DVI = "dvi";

    /**
     * dxr格式文件
     */
    public static final String CONTENT_TYPE_DXR = "dxr";

    /**
     * eps格式文件
     */
    public static final String CONTENT_TYPE_EPS = "eps";

    /**
     * etx格式文件
     */
    public static final String CONTENT_TYPE_ETX = "etx";

    /**
     * evy格式文件
     */
    public static final String CONTENT_TYPE_EVY = "evy";

    /**
     * exe格式文件
     */
    public static final String CONTENT_TYPE_EXE = "exe";

    /**
     * fif格式文件
     */
    public static final String CONTENT_TYPE_FIF = "fif";

    /**
     * flr格式文件
     */
    public static final String CONTENT_TYPE_FLR = "flr";

    /**
     * gtar格式文件
     */
    public static final String CONTENT_TYPE_GTAR = "gtar";

    /**
     * gz格式文件
     */
    public static final String CONTENT_TYPE_GZ = "gz";

    /**
     * h格式文件
     */
    public static final String CONTENT_TYPE_H = "h";

    /**
     * hdf格式文件
     */
    public static final String CONTENT_TYPE_HDF = "hdf";

    /**
     * hlp格式文件
     */
    public static final String CONTENT_TYPE_HLP = "hlp";

    /**
     * hqx格式文件
     */
    public static final String CONTENT_TYPE_HQX = "hqx";

    /**
     * hta格式文件
     */
    public static final String CONTENT_TYPE_HTA = "hta";

    /**
     * htc格式文件
     */
    public static final String CONTENT_TYPE_HTC = "htc";

    /**
     * htm格式文件
     */
    public static final String CONTENT_TYPE_HTM = "htm";

    /**
     * htt格式文件
     */
    public static final String CONTENT_TYPE_HTT = "htt";

    /**
     * ico格式文件
     */
    public static final String CONTENT_TYPE_ICO = "ico";

    /**
     * ief格式文件
     */
    public static final String CONTENT_TYPE_IEF = "ief";

    /**
     * iii格式文件
     */
    public static final String CONTENT_TYPE_III = "iii";

    /**
     * ins格式文件
     */
    public static final String CONTENT_TYPE_INS = "ins";

    /**
     * isp格式文件
     */
    public static final String CONTENT_TYPE_ISP = "isp";

    /**
     * jfif格式文件
     */
    public static final String CONTENT_TYPE_JFIF = "jfif";

    /**
     * jpe格式文件
     */
    public static final String CONTENT_TYPE_JPE = "jpe";

    /**
     * jpeg格式文件
     */
    public static final String CONTENT_TYPE_JPEG = "jpeg";

    /**
     * js格式文件
     */
    public static final String CONTENT_TYPE_JS = "js";

    /**
     * latex格式文件
     */
    public static final String CONTENT_TYPE_LATEX = "latex";

    /**
     * lha格式文件
     */
    public static final String CONTENT_TYPE_LHA = "lha";

    /**
     * lsf格式文件
     */
    public static final String CONTENT_TYPE_LSF = "lsf";

    /**
     * lsx格式文件
     */
    public static final String CONTENT_TYPE_LSX = "lsx";

    /**
     * lzh格式文件
     */
    public static final String CONTENT_TYPE_LZH = "lzh";

    /**
     * m13格式文件
     */
    public static final String CONTENT_TYPE_M13 = "m13";

    /**
     * m14格式文件
     */
    public static final String CONTENT_TYPE_M14 = "m14";

    /**
     * m3u格式文件
     */
    public static final String CONTENT_TYPE_M3U = "m3u";

    /**
     * man格式文件
     */
    public static final String CONTENT_TYPE_MAN = "man";

    /**
     * mdb格式文件
     */
    public static final String CONTENT_TYPE_MDB = "mdb";

    /**
     * me格式文件
     */
    public static final String CONTENT_TYPE_ME = "me";

    /**
     * mid格式文件
     */
    public static final String CONTENT_TYPE_MID = "mid";

    /**
     * mny格式文件
     */
    public static final String CONTENT_TYPE_MNY = "mny";

    /**
     * mov格式文件
     */
    public static final String CONTENT_TYPE_MOV = "mov";

    /**
     * movie格式文件
     */
    public static final String CONTENT_TYPE_MOVIE = "movie";

    /**
     * mp2格式文件
     */
    public static final String CONTENT_TYPE_MP2 = "mp2";

    /**
     * mp3格式文件
     */
    public static final String CONTENT_TYPE_MP3 = "mp3";

    /**
     * mpa格式文件
     */
    public static final String CONTENT_TYPE_MPA = "mpa";

    /**
     * mpe格式文件
     */
    public static final String CONTENT_TYPE_MPE = "mpe";

    /**
     * mpeg格式文件
     */
    public static final String CONTENT_TYPE_MPEG = "mpeg";

    /**
     * mpg格式文件
     */
    public static final String CONTENT_TYPE_MPG = "mpg";

    /**
     * mpp格式文件
     */
    public static final String CONTENT_TYPE_MPP = "mpp";

    /**
     * mpv2格式文件
     */
    public static final String CONTENT_TYPE_MPV2 = "mpv2";

    /**
     * ms格式文件
     */
    public static final String CONTENT_TYPE_MS = "ms";

    /**
     * mvb格式文件
     */
    public static final String CONTENT_TYPE_MVB = "mvb";

    /**
     * nws格式文件
     */
    public static final String CONTENT_TYPE_NWS = "nws";

    /**
     * oda格式文件
     */
    public static final String CONTENT_TYPE_ODA = "oda";

    /**
     * p10格式文件
     */
    public static final String CONTENT_TYPE_P10 = "p10";

    /**
     * p12格式文件
     */
    public static final String CONTENT_TYPE_P12 = "p12";

    /**
     * p7b格式文件
     */
    public static final String CONTENT_TYPE_P7B = "p7b";

    /**
     * p7c格式文件
     */
    public static final String CONTENT_TYPE_P7C = "p7c";

    /**
     * p7m格式文件
     */
    public static final String CONTENT_TYPE_P7M = "p7m";

    /**
     * p7r格式文件
     */
    public static final String CONTENT_TYPE_P7R = "p7r";

    /**
     * p7s格式文件
     */
    public static final String CONTENT_TYPE_P7S = "p7s";

    /**
     * pbm格式文件
     */
    public static final String CONTENT_TYPE_PBM = "pbm";

    /**
     * pfx格式文件
     */
    public static final String CONTENT_TYPE_PFX = "pfx";

    /**
     * pgm格式文件
     */
    public static final String CONTENT_TYPE_PGM = "pgm";

    /**
     * pko格式文件
     */
    public static final String CONTENT_TYPE_PKO = "pko";

    /**
     * pma格式文件
     */
    public static final String CONTENT_TYPE_PMA = "pma";

    /**
     * pmc格式文件
     */
    public static final String CONTENT_TYPE_PMC = "pmc";

    /**
     * pml格式文件
     */
    public static final String CONTENT_TYPE_PML = "pml";

    /**
     * pmr格式文件
     */
    public static final String CONTENT_TYPE_PMR = "pmr";

    /**
     * pmw格式文件
     */
    public static final String CONTENT_TYPE_PMW = "pmw";

    /**
     * pnm格式文件
     */
    public static final String CONTENT_TYPE_PNM = "pnm";

    /**
     * pot格式文件
     */
    public static final String CONTENT_TYPE_POT = "pot";

    /**
     * ppm格式文件
     */
    public static final String CONTENT_TYPE_PPM = "ppm";

    /**
     * prf格式文件
     */
    public static final String CONTENT_TYPE_PRF = "prf";

    /**
     * ps格式文件
     */
    public static final String CONTENT_TYPE_PS = "ps";

    /**
     * pub格式文件
     */
    public static final String CONTENT_TYPE_PUB = "pub";

    /**
     * qt格式文件
     */
    public static final String CONTENT_TYPE_QT = "qt";

    /**
     * ra格式文件
     */
    public static final String CONTENT_TYPE_RA = "ra";

    /**
     * ram格式文件
     */
    public static final String CONTENT_TYPE_RAM = "ram";

    /**
     * ras格式文件
     */
    public static final String CONTENT_TYPE_RAS = "ras";

    /**
     * rgb格式文件
     */
    public static final String CONTENT_TYPE_RGB = "rgb";

    /**
     * rmi格式文件
     */
    public static final String CONTENT_TYPE_RMI = "rmi";

    /**
     * roff格式文件
     */
    public static final String CONTENT_TYPE_ROFF = "roff";

    /**
     * rtx格式文件
     */
    public static final String CONTENT_TYPE_RTX = "rtx";

    /**
     * scd格式文件
     */
    public static final String CONTENT_TYPE_SCD = "scd";

    /**
     * sct格式文件
     */
    public static final String CONTENT_TYPE_SCT = "sct";

    /**
     * setpay格式文件
     */
    public static final String CONTENT_TYPE_SETPAY = "setpay";

    /**
     * setreg格式文件
     */
    public static final String CONTENT_TYPE_SETREG = "setreg";

    /**
     * sh格式文件
     */
    public static final String CONTENT_TYPE_SH = "sh";

    /**
     * shar格式文件
     */
    public static final String CONTENT_TYPE_SHAR = "shar";

    /**
     * sit格式文件
     */
    public static final String CONTENT_TYPE_SIT = "sit";

    /**
     * snd格式文件
     */
    public static final String CONTENT_TYPE_SND = "snd";

    /**
     * spc格式文件
     */
    public static final String CONTENT_TYPE_SPC = "spc";

    /**
     * spl格式文件
     */
    public static final String CONTENT_TYPE_SPL = "spl";

    /**
     * src格式文件
     */
    public static final String CONTENT_TYPE_SRC = "src";

    /**
     * sst格式文件
     */
    public static final String CONTENT_TYPE_SST = "sst";

    /**
     * stl格式文件
     */
    public static final String CONTENT_TYPE_STL = "stl";

    /**
     * stm格式文件
     */
    public static final String CONTENT_TYPE_STM = "stm";

    /**
     * svg格式文件
     */
    public static final String CONTENT_TYPE_SVG = "svg";

    /**
     * sv4cpio格式文件
     */
    public static final String CONTENT_TYPE_SV4CPIO = "sv4cpio";

    /**
     * sv4crc格式文件
     */
    public static final String CONTENT_TYPE_SV4CRC = "sv4crc";

    /**
     * swf格式文件
     */
    public static final String CONTENT_TYPE_SWF = "swf";

    /**
     * t格式文件
     */
    public static final String CONTENT_TYPE_T = "t";

    /**
     * tar格式文件
     */
    public static final String CONTENT_TYPE_TAR = "tar";

    /**
     * tcl格式文件
     */
    public static final String CONTENT_TYPE_TCL = "tcl";

    /**
     * tex格式文件
     */
    public static final String CONTENT_TYPE_TEX = "tex";

    /**
     * texi格式文件
     */
    public static final String CONTENT_TYPE_TEXI = "texi";

    /**
     * texinfo格式文件
     */
    public static final String CONTENT_TYPE_TEXINFO = "texinfo";

    /**
     * tgz格式文件
     */
    public static final String CONTENT_TYPE_TGZ = "tgz";

    /**
     * tif格式文件
     */
    public static final String CONTENT_TYPE_TIF = "tif";

    /**
     * tiff格式文件
     */
    public static final String CONTENT_TYPE_TIFF = "tiff";

    /**
     * tr格式文件
     */
    public static final String CONTENT_TYPE_TR = "tr";

    /**
     * trm格式文件
     */
    public static final String CONTENT_TYPE_TRM = "trm";

    /**
     * tsv格式文件
     */
    public static final String CONTENT_TYPE_TSV = "tsv";

    /**
     * uls格式文件
     */
    public static final String CONTENT_TYPE_ULS = "uls";

    /**
     * ustar格式文件
     */
    public static final String CONTENT_TYPE_USTAR = "ustar";

    /**
     * vcf格式文件
     */
    public static final String CONTENT_TYPE_VCF = "vcf";

    /**
     * vrml格式文件
     */
    public static final String CONTENT_TYPE_VRML = "vrml";

    /**
     * wav格式文件
     */
    public static final String CONTENT_TYPE_WAV = "wav";

    /**
     * wcm格式文件
     */
    public static final String CONTENT_TYPE_WCM = "wcm";

    /**
     * wdb格式文件
     */
    public static final String CONTENT_TYPE_WDB = "wdb";

    /**
     * wks格式文件
     */
    public static final String CONTENT_TYPE_WKS = "wks";

    /**
     * wmf格式文件
     */
    public static final String CONTENT_TYPE_WMF = "wmf";

    /**
     * wps格式文件
     */
    public static final String CONTENT_TYPE_WPS = "wps";

    /**
     * wri格式文件
     */
    public static final String CONTENT_TYPE_WRI = "wri";

    /**
     * wrl格式文件
     */
    public static final String CONTENT_TYPE_WRL = "wrl";

    /**
     * wrz格式文件
     */
    public static final String CONTENT_TYPE_WRZ = "wrz";

    /**
     * xaf格式文件
     */
    public static final String CONTENT_TYPE_XAF = "xaf";

    /**
     * xbm格式文件
     */
    public static final String CONTENT_TYPE_XBM = "xbm";

    /**
     * xla格式文件
     */
    public static final String CONTENT_TYPE_XLA = "xla";

    /**
     * xlc格式文件
     */
    public static final String CONTENT_TYPE_XLC = "xlc";

    /**
     * xlm格式文件
     */
    public static final String CONTENT_TYPE_XLM = "xlm";

    /**
     * xlt格式文件
     */
    public static final String CONTENT_TYPE_XLT = "xlt";

    /**
     * xlw格式文件
     */
    public static final String CONTENT_TYPE_XLW = "xlw";

    /**
     * xof格式文件
     */
    public static final String CONTENT_TYPE_XOF = "xof";

    /**
     * xpm格式文件
     */
    public static final String CONTENT_TYPE_XPM = "xpm";

    /**
     * xwd格式文件
     */
    public static final String CONTENT_TYPE_XWD = "xwd";

    /**
     * z格式文件
     */
    public static final String CONTENT_TYPE_Z = "z";

    /**
     * zip格式文件
     */
    public static final String CONTENT_TYPE_ZIP = "zip";


    private static Map dict = new HashMap();

    static {
        dict.put(CONTENT_TYPE_ACX, "application/internet-property-stream");
        dict.put(CONTENT_TYPE_AI, "application/postscript");
        dict.put(CONTENT_TYPE_AIF, "audio/x-aiff");
        dict.put(CONTENT_TYPE_AIFC, "audio/x-aiff");
        dict.put(CONTENT_TYPE_AIFF, "audio/x-aiff");
        dict.put(CONTENT_TYPE_ASF, "video/x-ms-asf");
        dict.put(CONTENT_TYPE_ASR, "video/x-ms-asf");
        dict.put(CONTENT_TYPE_ASX, "video/x-ms-asf");
        dict.put(CONTENT_TYPE_AU, "audio/basic");
        dict.put(CONTENT_TYPE_AVI, "video/x-msvideo");
        dict.put(CONTENT_TYPE_AXS, "application/olescript");
        dict.put(CONTENT_TYPE_BAS, "text/plain");
        dict.put(CONTENT_TYPE_BCPIO, "application/x-bcpio");
        dict.put(CONTENT_TYPE_BIN, "application/octet-stream");
        dict.put(CONTENT_TYPE_BMP, "image/bmp");
        dict.put(CONTENT_TYPE_C, "text/plain");
        dict.put(CONTENT_TYPE_CAT, "application/vnd.ms-pkiseccat");
        dict.put(CONTENT_TYPE_CDF, "application/x-cdf");
        dict.put(CONTENT_TYPE_CER, "application/x-x509-ca-cert");
        dict.put(CONTENT_TYPE_CLASS, "application/octet-stream");
        dict.put(CONTENT_TYPE_CLP, "application/x-msclip");
        dict.put(CONTENT_TYPE_CMX, "image/x-cmx");
        dict.put(CONTENT_TYPE_COD, "image/cis-cod");
        dict.put(CONTENT_TYPE_CPIO, "application/x-cpio");
        dict.put(CONTENT_TYPE_CRD, "application/x-mscardfile");
        dict.put(CONTENT_TYPE_CRL, "application/pkix-crl");
        dict.put(CONTENT_TYPE_CRT, "application/x-x509-ca-cert");
        dict.put(CONTENT_TYPE_CSH, "application/x-csh");
        dict.put(CONTENT_TYPE_CSS, "text/css");
        dict.put(CONTENT_TYPE_DCR, "application/x-director");
        dict.put(CONTENT_TYPE_DER, "application/x-x509-ca-cert");
        dict.put(CONTENT_TYPE_DIR, "application/x-director");
        dict.put(CONTENT_TYPE_DLL, "application/x-msdownload");
        dict.put(CONTENT_TYPE_DMS, "application/octet-stream");
        dict.put(CONTENT_TYPE_DOC, "application/msword");
        dict.put(CONTENT_TYPE_DOT, "application/msword");
        dict.put(CONTENT_TYPE_DVI, "application/x-dvi");
        dict.put(CONTENT_TYPE_DXR, "application/x-director");
        dict.put(CONTENT_TYPE_EPS, "application/postscript");
        dict.put(CONTENT_TYPE_ETX, "text/x-setext");
        dict.put(CONTENT_TYPE_EVY, "application/envoy");
        dict.put(CONTENT_TYPE_EXE, "application/octet-stream");
        dict.put(CONTENT_TYPE_FIF, "application/fractals");
        dict.put(CONTENT_TYPE_FLR, "x-world/x-vrml");
        dict.put(CONTENT_TYPE_GIF, "image/gif");
        dict.put(CONTENT_TYPE_GTAR, "application/x-gtar");
        dict.put(CONTENT_TYPE_GZ, "application/x-gzip");
        dict.put(CONTENT_TYPE_H, "text/plain");
        dict.put(CONTENT_TYPE_HDF, "application/x-hdf");
        dict.put(CONTENT_TYPE_HLP, "application/winhlp");
        dict.put(CONTENT_TYPE_HQX, "application/mac-binhex40");
        dict.put(CONTENT_TYPE_HTA, "application/hta");
        dict.put(CONTENT_TYPE_HTC, "text/x-component");
        dict.put(CONTENT_TYPE_HTM, "text/html");
        dict.put(CONTENT_TYPE_HTML, "text/html");
        dict.put(CONTENT_TYPE_HTT, "text/webviewhtml");
        dict.put(CONTENT_TYPE_ICO, "image/x-icon");
        dict.put(CONTENT_TYPE_IEF, "image/ief");
        dict.put(CONTENT_TYPE_III, "application/x-iphone");
        dict.put(CONTENT_TYPE_INS, "application/x-internet-signup");
        dict.put(CONTENT_TYPE_ISP, "application/x-internet-signup");
        dict.put(CONTENT_TYPE_JFIF, "image/pipeg");
        dict.put(CONTENT_TYPE_JPE, "image/jpeg");
        dict.put(CONTENT_TYPE_JPEG, "image/jpeg");
        dict.put(CONTENT_TYPE_JPG, "image/jpeg");
        dict.put(CONTENT_TYPE_JS, "application/x-javascript");
        dict.put(CONTENT_TYPE_LATEX, "application/x-latex");
        dict.put(CONTENT_TYPE_LHA, "application/octet-stream");
        dict.put(CONTENT_TYPE_LSF, "video/x-la-asf");
        dict.put(CONTENT_TYPE_LSX, "video/x-la-asf");
        dict.put(CONTENT_TYPE_LZH, "application/octet-stream");
        dict.put(CONTENT_TYPE_M13, "application/x-msmediaview");
        dict.put(CONTENT_TYPE_M14, "application/x-msmediaview");
        dict.put(CONTENT_TYPE_M3U, "audio/x-mpegurl");
        dict.put(CONTENT_TYPE_MAN, "application/x-troff-man");
        dict.put(CONTENT_TYPE_MDB, "application/x-msaccess");
        dict.put(CONTENT_TYPE_ME, "application/x-troff-me");
        dict.put(CONTENT_TYPE_MHT, "message/rfc822");
        dict.put(CONTENT_TYPE_MHTML, "message/rfc822");
        dict.put(CONTENT_TYPE_MID, "audio/mid");
        dict.put(CONTENT_TYPE_MNY, "application/x-msmoney");
        dict.put(CONTENT_TYPE_MOV, "video/quicktime");
        dict.put(CONTENT_TYPE_MOVIE, "video/x-sgi-movie");
        dict.put(CONTENT_TYPE_MP2, "video/mpeg");
        dict.put(CONTENT_TYPE_MP3, "audio/mpeg");
        dict.put(CONTENT_TYPE_MPA, "video/mpeg");
        dict.put(CONTENT_TYPE_MPE, "video/mpeg");
        dict.put(CONTENT_TYPE_MPEG, "video/mpeg");
        dict.put(CONTENT_TYPE_MPG, "video/mpeg");
        dict.put(CONTENT_TYPE_MPP, "application/vnd.ms-project");
        dict.put(CONTENT_TYPE_MPV2, "video/mpeg");
        dict.put(CONTENT_TYPE_MS, "application/x-troff-ms");
        dict.put(CONTENT_TYPE_MVB, "application/x-msmediaview");
        dict.put(CONTENT_TYPE_NWS, "message/rfc822");
        dict.put(CONTENT_TYPE_ODA, "application/oda");
        dict.put(CONTENT_TYPE_P10, "application/pkcs10");
        dict.put(CONTENT_TYPE_P12, "application/x-pkcs12");
        dict.put(CONTENT_TYPE_P7B, "application/x-pkcs7-certificates");
        dict.put(CONTENT_TYPE_P7C, "application/x-pkcs7-mime");
        dict.put(CONTENT_TYPE_P7M, "application/x-pkcs7-mime");
        dict.put(CONTENT_TYPE_P7R, "application/x-pkcs7-certreqresp");
        dict.put(CONTENT_TYPE_P7S, "application/x-pkcs7-signature");
        dict.put(CONTENT_TYPE_PBM, "image/x-portable-bitmap");
        dict.put(CONTENT_TYPE_PDF, "application/pdf");
        dict.put(CONTENT_TYPE_PFX, "application/x-pkcs12");
        dict.put(CONTENT_TYPE_PGM, "image/x-portable-graymap");
        dict.put(CONTENT_TYPE_PKO, "application/ynd.ms-pkipko");
        dict.put(CONTENT_TYPE_PMA, "application/x-perfmon");
        dict.put(CONTENT_TYPE_PMC, "application/x-perfmon");
        dict.put(CONTENT_TYPE_PML, "application/x-perfmon");
        dict.put(CONTENT_TYPE_PMR, "application/x-perfmon");
        dict.put(CONTENT_TYPE_PMW, "application/x-perfmon");
        dict.put(CONTENT_TYPE_PNM, "image/x-portable-anymap");
        dict.put(CONTENT_TYPE_POT, "");
        dict.put(CONTENT_TYPE_PPM, "image/x-portable-pixmap");
        dict.put(CONTENT_TYPE_PPS, "application/vnd.ms-powerpoint");
        dict.put(CONTENT_TYPE_PPT, "application/vnd.ms-powerpoint");
        dict.put(CONTENT_TYPE_PRF, "application/pics-rules");
        dict.put(CONTENT_TYPE_PS, "application/postscript");
        dict.put(CONTENT_TYPE_PUB, "application/x-mspublisher");
        dict.put(CONTENT_TYPE_QT, "video/quicktime");
        dict.put(CONTENT_TYPE_RA, "audio/x-pn-realaudio");
        dict.put(CONTENT_TYPE_RAM, "audio/x-pn-realaudio");
        dict.put(CONTENT_TYPE_RAS, "image/x-cmu-raster");
        dict.put(CONTENT_TYPE_RGB, "image/x-rgb");
        dict.put(CONTENT_TYPE_RMI, "audio/mid");
        dict.put(CONTENT_TYPE_ROFF, "application/x-troff");
        dict.put(CONTENT_TYPE_RTF, "application/rtf");
        dict.put(CONTENT_TYPE_RTX, "text/richtext");
        dict.put(CONTENT_TYPE_SCD, "application/x-msschedule");
        dict.put(CONTENT_TYPE_SCT, "text/scriptlet");
        dict.put(CONTENT_TYPE_SETPAY, "application/set-payment-initiation");
        dict.put(CONTENT_TYPE_SETREG, "application/set-registration-initiation");
        dict.put(CONTENT_TYPE_SH, "application/x-sh");
        dict.put(CONTENT_TYPE_SHAR, "application/x-shar");
        dict.put(CONTENT_TYPE_SIT, "application/x-stuffit");
        dict.put(CONTENT_TYPE_SND, "audio/basic");
        dict.put(CONTENT_TYPE_SPC, "application/x-pkcs7-certificates");
        dict.put(CONTENT_TYPE_SPL, "application/futuresplash");
        dict.put(CONTENT_TYPE_SRC, "application/x-wais-source");
        dict.put(CONTENT_TYPE_SST, "application/vnd.ms-pkicertstore");
        dict.put(CONTENT_TYPE_STL, "application/vnd.ms-pkistl");
        dict.put(CONTENT_TYPE_STM, "text/html");
        dict.put(CONTENT_TYPE_SVG, "image/svg+xml");
        dict.put(CONTENT_TYPE_SV4CPIO, "application/x-sv4cpio");
        dict.put(CONTENT_TYPE_SV4CRC, "application/x-sv4crc");
        dict.put(CONTENT_TYPE_SWF, "application/x-shockwave-flash");
        dict.put(CONTENT_TYPE_T, "application/x-troff");
        dict.put(CONTENT_TYPE_TAR, "application/x-tar");
        dict.put(CONTENT_TYPE_TCL, "application/x-tcl");
        dict.put(CONTENT_TYPE_TEX, "application/x-tex");
        dict.put(CONTENT_TYPE_TEXI, "application/x-texinfo");
        dict.put(CONTENT_TYPE_TEXINFO, "application/x-texinfo");
        dict.put(CONTENT_TYPE_TGZ, "application/x-compressed");
        dict.put(CONTENT_TYPE_TIF, "image/tiff");
        dict.put(CONTENT_TYPE_TIFF, "image/tiff");
        dict.put(CONTENT_TYPE_TR, "application/x-troff");
        dict.put(CONTENT_TYPE_TRM, "application/x-msterminal");
        dict.put(CONTENT_TYPE_TSV, "text/tab-separated-values");
        dict.put(CONTENT_TYPE_TXT, "text/plain");
        dict.put(CONTENT_TYPE_ULS, "text/iuls");
        dict.put(CONTENT_TYPE_USTAR, "application/x-ustar");
        dict.put(CONTENT_TYPE_VCF, "text/x-vcard");
        dict.put(CONTENT_TYPE_VRML, "x-world/x-vrml");
        dict.put(CONTENT_TYPE_WAV, "audio/x-wav");
        dict.put(CONTENT_TYPE_WCM, "application/vnd.ms-works");
        dict.put(CONTENT_TYPE_WDB, "application/vnd.ms-works");
        dict.put(CONTENT_TYPE_WKS, "application/vnd.ms-works");
        dict.put(CONTENT_TYPE_WMF, "application/x-msmetafile");
        dict.put(CONTENT_TYPE_WPS, "application/vnd.ms-works");
        dict.put(CONTENT_TYPE_WRI, "application/x-mswrite");
        dict.put(CONTENT_TYPE_WRL, "x-world/x-vrml");
        dict.put(CONTENT_TYPE_WRZ, "x-world/x-vrml");
        dict.put(CONTENT_TYPE_XAF, "x-world/x-vrml");
        dict.put(CONTENT_TYPE_XBM, "image/x-xbitmap");
        dict.put(CONTENT_TYPE_XLA, "application/vnd.ms-excel");
        dict.put(CONTENT_TYPE_XLC, "application/vnd.ms-excel");
        dict.put(CONTENT_TYPE_XLM, "application/vnd.ms-excel");
        dict.put(CONTENT_TYPE_XLS, "application/vnd.ms-excel");
        dict.put(CONTENT_TYPE_XLT, "application/vnd.ms-excel");
        dict.put(CONTENT_TYPE_XLW, "application/vnd.ms-excel");
        dict.put(CONTENT_TYPE_XOF, "x-world/x-vrml");
        dict.put(CONTENT_TYPE_XPM, "image/x-xpixmap");
        dict.put(CONTENT_TYPE_XWD, "image/x-xwindowdump");
        dict.put(CONTENT_TYPE_Z, "application/x-compress");
        dict.put(CONTENT_TYPE_ZIP, "application/zip");
    }

    /**
     * 根据文件扩展名，取得文件对应的contentType
     *
     * @param fileExt 文件扩展名
     * @return 文件contentType字串
     */
    public static String getContentType(String fileExt) {
        String temp = (String) dict.get(fileExt);
        if (temp == null) {
            temp = "";
        }
        return temp;
    }
}