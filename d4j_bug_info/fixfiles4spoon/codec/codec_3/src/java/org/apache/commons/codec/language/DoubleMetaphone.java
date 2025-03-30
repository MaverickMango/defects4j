package org.apache.commons.codec.language;


public class DoubleMetaphone implements org.apache.commons.codec.StringEncoder {
    private static final java.lang.String VOWELS = "AEIOUY";

    private static final java.lang.String[] SILENT_START = new java.lang.String[]{ "GN", "KN", "PN", "WR", "PS" };

    private static final java.lang.String[] L_R_N_M_B_H_F_V_W_SPACE = new java.lang.String[]{ "L", "R", "N", "M", "B", "H", "F", "V", "W", " " };

    private static final java.lang.String[] ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER = new java.lang.String[]{ "ES", "EP", "EB", "EL", "EY", "IB", "IL", "IN", "IE", "EI", "ER" };

    private static final java.lang.String[] L_T_K_S_N_M_B_Z = new java.lang.String[]{ "L", "T", "K", "S", "N", "M", "B", "Z" };

    protected int maxCodeLen = 4;

    public DoubleMetaphone() {
        super();
    }

    public java.lang.String doubleMetaphone(java.lang.String value) {
        return doubleMetaphone(value, false);
    }

    public java.lang.String doubleMetaphone(java.lang.String value, boolean alternate) {
        value = cleanInput(value);
        if (value == null) {
            return null;
        }
        boolean slavoGermanic = isSlavoGermanic(value);
        int index = (isSilentStart(value)) ? 1 : 0;
        org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult result = new org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult(this.getMaxCodeLen());
        while ((!(result.isComplete())) && (index <= ((value.length()) - 1))) {
            switch (value.charAt(index)) {
                case 'A' :
                case 'E' :
                case 'I' :
                case 'O' :
                case 'U' :
                case 'Y' :
                    index = handleAEIOUY(value, result, index);
                    break;
                case 'B' :
                    result.append('P');
                    index = ((charAt(value, (index + 1))) == 'B') ? index + 2 : index + 1;
                    break;
                case '\u00c7' :
                    result.append('S');
                    index++;
                    break;
                case 'C' :
                    index = handleC(value, result, index);
                    break;
                case 'D' :
                    index = handleD(value, result, index);
                    break;
                case 'F' :
                    result.append('F');
                    index = ((charAt(value, (index + 1))) == 'F') ? index + 2 : index + 1;
                    break;
                case 'G' :
                    index = handleG(value, result, index, slavoGermanic);
                    break;
                case 'H' :
                    index = handleH(value, result, index);
                    break;
                case 'J' :
                    index = handleJ(value, result, index, slavoGermanic);
                    break;
                case 'K' :
                    result.append('K');
                    index = ((charAt(value, (index + 1))) == 'K') ? index + 2 : index + 1;
                    break;
                case 'L' :
                    index = handleL(value, result, index);
                    break;
                case 'M' :
                    result.append('M');
                    index = (conditionM0(value, index)) ? index + 2 : index + 1;
                    break;
                case 'N' :
                    result.append('N');
                    index = ((charAt(value, (index + 1))) == 'N') ? index + 2 : index + 1;
                    break;
                case '\u00d1' :
                    result.append('N');
                    index++;
                    break;
                case 'P' :
                    index = handleP(value, result, index);
                    break;
                case 'Q' :
                    result.append('K');
                    index = ((charAt(value, (index + 1))) == 'Q') ? index + 2 : index + 1;
                    break;
                case 'R' :
                    index = handleR(value, result, index, slavoGermanic);
                    break;
                case 'S' :
                    index = handleS(value, result, index, slavoGermanic);
                    break;
                case 'T' :
                    index = handleT(value, result, index);
                    break;
                case 'V' :
                    result.append('F');
                    index = ((charAt(value, (index + 1))) == 'V') ? index + 2 : index + 1;
                    break;
                case 'W' :
                    index = handleW(value, result, index);
                    break;
                case 'X' :
                    index = handleX(value, result, index);
                    break;
                case 'Z' :
                    index = handleZ(value, result, index, slavoGermanic);
                    break;
                default :
                    index++;
                    break;
            }
        } 
        return alternate ? result.getAlternate() : result.getPrimary();
    }

    public java.lang.Object encode(java.lang.Object obj) throws org.apache.commons.codec.EncoderException {
        if (!(obj instanceof java.lang.String)) {
            throw new org.apache.commons.codec.EncoderException("DoubleMetaphone encode parameter is not of type String");
        }
        return doubleMetaphone(((java.lang.String) (obj)));
    }

    public java.lang.String encode(java.lang.String value) {
        return doubleMetaphone(value);
    }

    public boolean isDoubleMetaphoneEqual(java.lang.String value1, java.lang.String value2) {
        return isDoubleMetaphoneEqual(value1, value2, false);
    }

    public boolean isDoubleMetaphoneEqual(java.lang.String value1, java.lang.String value2, boolean alternate) {
        return doubleMetaphone(value1, alternate).equals(doubleMetaphone(value2, alternate));
    }

    public int getMaxCodeLen() {
        return this.maxCodeLen;
    }

    public void setMaxCodeLen(int maxCodeLen) {
        this.maxCodeLen = maxCodeLen;
    }

    private int handleAEIOUY(java.lang.String value, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult result, int index) {
        if (index == 0) {
            result.append('A');
        }
        return index + 1;
    }

    private int handleC(java.lang.String value, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult result, int index) {
        if (conditionC0(value, index)) {
            result.append('K');
            index += 2;
        }else
            if ((index == 0) && (org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 6, "CAESAR"))) {
                result.append('S');
                index += 2;
            }else
                if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 2, "CH")) {
                    index = handleCH(value, result, index);
                }else
                    if ((org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 2, "CZ")) && (!(org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 2), 4, "WICZ")))) {
                        result.append('S', 'X');
                        index += 2;
                    }else
                        if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 3, "CIA")) {
                            result.append('X');
                            index += 3;
                        }else
                            if ((org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 2, "CC")) && (!((index == 1) && ((charAt(value, 0)) == 'M')))) {
                                return handleCC(value, result, index);
                            }else
                                if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 2, "CK", "CG", "CQ")) {
                                    result.append('K');
                                    index += 2;
                                }else
                                    if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 2, "CI", "CE", "CY")) {
                                        if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 3, "CIO", "CIE", "CIA")) {
                                            result.append('S', 'X');
                                        }else {
                                            result.append('S');
                                        }
                                        index += 2;
                                    }else {
                                        result.append('K');
                                        if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 2, " C", " Q", " G")) {
                                            index += 3;
                                        }else
                                            if ((org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 1, "C", "K", "Q")) && (!(org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 2, "CE", "CI")))) {
                                                index += 2;
                                            }else {
                                                index++;
                                            }

                                    }







        return index;
    }

    private int handleCC(java.lang.String value, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult result, int index) {
        if ((org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 2), 1, "I", "E", "H")) && (!(org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 2), 2, "HU")))) {
            if (((index == 1) && ((charAt(value, (index - 1))) == 'A')) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 1), 5, "UCCEE", "UCCES"))) {
                result.append("KS");
            }else {
                result.append('X');
            }
            index += 3;
        }else {
            result.append('K');
            index += 2;
        }
        return index;
    }

    private int handleCH(java.lang.String value, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult result, int index) {
        if ((index > 0) && (org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 4, "CHAE"))) {
            result.append('K', 'X');
            return index + 2;
        }else
            if (conditionCH0(value, index)) {
                result.append('K');
                return index + 2;
            }else
                if (conditionCH1(value, index)) {
                    result.append('K');
                    return index + 2;
                }else {
                    if (index > 0) {
                        if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, 0, 2, "MC")) {
                            result.append('K');
                        }else {
                            result.append('X', 'K');
                        }
                    }else {
                        result.append('X');
                    }
                    return index + 2;
                }


    }

    private int handleD(java.lang.String value, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult result, int index) {
        if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 2, "DG")) {
            if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 2), 1, "I", "E", "Y")) {
                result.append('J');
                index += 3;
            }else {
                result.append("TK");
                index += 2;
            }
        }else
            if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 2, "DT", "DD")) {
                result.append('T');
                index += 2;
            }else {
                result.append('T');
                index++;
            }

        return index;
    }

    private int handleG(java.lang.String value, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
        if ((charAt(value, (index + 1))) == 'H') {
            index = handleGH(value, result, index);
        }else
            if ((charAt(value, (index + 1))) == 'N') {
                if (((index == 1) && (isVowel(charAt(value, 0)))) && (!slavoGermanic)) {
                    result.append("KN", "N");
                }else
                    if (((!(org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 2), 2, "EY"))) && ((charAt(value, (index + 1))) != 'Y')) && (!slavoGermanic)) {
                        result.append("N", "KN");
                    }else {
                        result.append("KN");
                    }

                index = index + 2;
            }else
                if ((org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 2, "LI")) && (!slavoGermanic)) {
                    result.append("KL", "L");
                    index += 2;
                }else
                    if ((index == 0) && (((charAt(value, (index + 1))) == 'Y') || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 2, org.apache.commons.codec.language.DoubleMetaphone.ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER)))) {
                        result.append('K', 'J');
                        index += 2;
                    }else
                        if (((((org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 2, "ER")) || ((charAt(value, (index + 1))) == 'Y')) && (!(org.apache.commons.codec.language.DoubleMetaphone.contains(value, 0, 6, "DANGER", "RANGER", "MANGER")))) && (!(org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 1), 1, "E", "I")))) && (!(org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 1), 3, "RGY", "OGY")))) {
                            result.append('K', 'J');
                            index += 2;
                        }else
                            if ((org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 1, "E", "I", "Y")) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 1), 4, "AGGI", "OGGI"))) {
                                if (((org.apache.commons.codec.language.DoubleMetaphone.contains(value, 0, 4, "VAN ", "VON ")) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, 0, 3, "SCH"))) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 2, "ET"))) {
                                    result.append('K');
                                }else
                                    if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 3, "IER")) {
                                        result.append('J');
                                    }else {
                                        result.append('J', 'K');
                                    }

                                index += 2;
                            }else
                                if ((charAt(value, (index + 1))) == 'G') {
                                    index += 2;
                                    result.append('K');
                                }else {
                                    index++;
                                    result.append('K');
                                }






        return index;
    }

    private int handleGH(java.lang.String value, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult result, int index) {
        if ((index > 0) && (!(isVowel(charAt(value, (index - 1)))))) {
            result.append('K');
            index += 2;
        }else
            if (index == 0) {
                if ((charAt(value, (index + 2))) == 'I') {
                    result.append('J');
                }else {
                    result.append('K');
                }
                index += 2;
            }else
                if ((((index > 1) && (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 2), 1, "B", "H", "D"))) || ((index > 2) && (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 3), 1, "B", "H", "D")))) || ((index > 3) && (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 4), 1, "B", "H")))) {
                    index += 2;
                }else {
                    if (((index > 2) && ((charAt(value, (index - 1))) == 'U')) && (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 3), 1, "C", "G", "L", "R", "T"))) {
                        result.append('F');
                    }else
                        if ((index > 0) && ((charAt(value, (index - 1))) != 'I')) {
                            result.append('K');
                        }

                    index += 2;
                }


        return index;
    }

    private int handleH(java.lang.String value, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult result, int index) {
        if (((index == 0) || (isVowel(charAt(value, (index - 1))))) && (isVowel(charAt(value, (index + 1))))) {
            result.append('H');
            index += 2;
        }else {
            index++;
        }
        return index;
    }

    private int handleJ(java.lang.String value, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
        if ((org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 4, "JOSE")) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, 0, 4, "SAN "))) {
            if ((((index == 0) && ((charAt(value, (index + 4))) == ' ')) || ((value.length()) == 4)) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, 0, 4, "SAN "))) {
                result.append('H');
            }else {
                result.append('J', 'H');
            }
            index++;
        }else {
            if ((index == 0) && (!(org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 4, "JOSE")))) {
                result.append('J', 'A');
            }else
                if (((isVowel(charAt(value, (index - 1)))) && (!slavoGermanic)) && (((charAt(value, (index + 1))) == 'A') || ((charAt(value, (index + 1))) == 'O'))) {
                    result.append('J', 'H');
                }else
                    if (index == ((value.length()) - 1)) {
                        result.append('J', ' ');
                    }else
                        if ((!(org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 1, org.apache.commons.codec.language.DoubleMetaphone.L_T_K_S_N_M_B_Z))) && (!(org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 1), 1, "S", "K", "L")))) {
                            result.append('J');
                        }



            if ((charAt(value, (index + 1))) == 'J') {
                index += 2;
            }else {
                index++;
            }
        }
        return index;
    }

    private int handleL(java.lang.String value, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult result, int index) {
        if ((charAt(value, (index + 1))) == 'L') {
            if (conditionL0(value, index)) {
                result.appendPrimary('L');
            }else {
                result.append('L');
            }
            index += 2;
        }else {
            index++;
            result.append('L');
        }
        return index;
    }

    private int handleP(java.lang.String value, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult result, int index) {
        if ((charAt(value, (index + 1))) == 'H') {
            result.append('F');
            index += 2;
        }else {
            result.append('P');
            index = (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 1, "P", "B")) ? index + 2 : index + 1;
        }
        return index;
    }

    private int handleR(java.lang.String value, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
        if ((((index == ((value.length()) - 1)) && (!slavoGermanic)) && (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 2), 2, "IE"))) && (!(org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 4), 2, "ME", "MA")))) {
            result.appendAlternate('R');
        }else {
            result.append('R');
        }
        return (charAt(value, (index + 1))) == 'R' ? index + 2 : index + 1;
    }

    private int handleS(java.lang.String value, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
        if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 1), 3, "ISL", "YSL")) {
            index++;
        }else
            if ((index == 0) && (org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 5, "SUGAR"))) {
                result.append('X', 'S');
                index++;
            }else
                if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 2, "SH")) {
                    if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 4, "HEIM", "HOEK", "HOLM", "HOLZ")) {
                        result.append('S');
                    }else {
                        result.append('X');
                    }
                    index += 2;
                }else
                    if ((org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 3, "SIO", "SIA")) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 4, "SIAN"))) {
                        if (slavoGermanic) {
                            result.append('S');
                        }else {
                            result.append('S', 'X');
                        }
                        index += 3;
                    }else
                        if (((index == 0) && (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 1, "M", "N", "L", "W"))) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 1, "Z"))) {
                            result.append('S', 'X');
                            index = (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 1, "Z")) ? index + 2 : index + 1;
                        }else
                            if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 2, "SC")) {
                                index = handleSC(value, result, index);
                            }else {
                                if ((index == ((value.length()) - 1)) && (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 2), 2, "AI", "OI"))) {
                                    result.appendAlternate('S');
                                }else {
                                    result.append('S');
                                }
                                index = (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 1, "S", "Z")) ? index + 2 : index + 1;
                            }





        return index;
    }

    private int handleSC(java.lang.String value, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult result, int index) {
        if ((charAt(value, (index + 2))) == 'H') {
            if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 3), 2, "OO", "ER", "EN", "UY", "ED", "EM")) {
                if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 3), 2, "ER", "EN")) {
                    result.append("X", "SK");
                }else {
                    result.append("SK");
                }
            }else {
                if (((index == 0) && (!(isVowel(charAt(value, 3))))) && ((charAt(value, 3)) != 'W')) {
                    result.append('X', 'S');
                }else {
                    result.append('X');
                }
            }
        }else
            if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 2), 1, "I", "E", "Y")) {
                result.append('S');
            }else {
                result.append("SK");
            }

        return index + 3;
    }

    private int handleT(java.lang.String value, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult result, int index) {
        if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 4, "TION")) {
            result.append('X');
            index += 3;
        }else
            if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 3, "TIA", "TCH")) {
                result.append('X');
                index += 3;
            }else
                if ((org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 2, "TH")) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 3, "TTH"))) {
                    if (((org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 2), 2, "OM", "AM")) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, 0, 4, "VAN ", "VON "))) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, 0, 3, "SCH"))) {
                        result.append('T');
                    }else {
                        result.append('0', 'T');
                    }
                    index += 2;
                }else {
                    result.append('T');
                    index = (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 1, "T", "D")) ? index + 2 : index + 1;
                }


        return index;
    }

    private int handleW(java.lang.String value, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult result, int index) {
        if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 2, "WR")) {
            result.append('R');
            index += 2;
        }else {
            if ((index == 0) && ((isVowel(charAt(value, (index + 1)))) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 2, "WH")))) {
                if (isVowel(charAt(value, (index + 1)))) {
                    result.append('A', 'F');
                }else {
                    result.append('A');
                }
                index++;
            }else
                if ((((index == ((value.length()) - 1)) && (isVowel(charAt(value, (index - 1))))) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 1), 5, "EWSKI", "EWSKY", "OWSKI", "OWSKY"))) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, 0, 3, "SCH"))) {
                    result.appendAlternate('F');
                    index++;
                }else
                    if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 4, "WICZ", "WITZ")) {
                        result.append("TS", "FX");
                        index += 4;
                    }else {
                        index++;
                    }


        }
        return index;
    }

    private int handleX(java.lang.String value, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult result, int index) {
        if (index == 0) {
            result.append('S');
            index++;
        }else {
            if (!((index == ((value.length()) - 1)) && ((org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 3), 3, "IAU", "EAU")) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 2), 2, "AU", "OU"))))) {
                result.append("KS");
            }
            index = (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 1, "C", "X")) ? index + 2 : index + 1;
        }
        return index;
    }

    private int handleZ(java.lang.String value, org.apache.commons.codec.language.DoubleMetaphone.DoubleMetaphoneResult result, int index, boolean slavoGermanic) {
        if ((charAt(value, (index + 1))) == 'H') {
            result.append('J');
            index += 2;
        }else {
            if ((org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 2, "ZO", "ZI", "ZA")) || (slavoGermanic && ((index > 0) && ((charAt(value, (index - 1))) != 'T')))) {
                result.append("S", "TS");
            }else {
                result.append('S');
            }
            index = ((charAt(value, (index + 1))) == 'Z') ? index + 2 : index + 1;
        }
        return index;
    }

    private boolean conditionC0(java.lang.String value, int index) {
        if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, index, 4, "CHIA")) {
            return true;
        }else
            if (index <= 1) {
                return false;
            }else
                if (isVowel(charAt(value, (index - 2)))) {
                    return false;
                }else
                    if (!(org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 1), 3, "ACH"))) {
                        return false;
                    }else {
                        char c = charAt(value, (index + 2));
                        return ((c != 'I') && (c != 'E')) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 2), 6, "BACHER", "MACHER"));
                    }



    }

    private boolean conditionCH0(java.lang.String value, int index) {
        if (index != 0) {
            return false;
        }else
            if ((!(org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 5, "HARAC", "HARIS"))) && (!(org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 1), 3, "HOR", "HYM", "HIA", "HEM")))) {
                return false;
            }else
                if (org.apache.commons.codec.language.DoubleMetaphone.contains(value, 0, 5, "CHORE")) {
                    return false;
                }else {
                    return true;
                }


    }

    private boolean conditionCH1(java.lang.String value, int index) {
        return ((((org.apache.commons.codec.language.DoubleMetaphone.contains(value, 0, 4, "VAN ", "VON ")) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, 0, 3, "SCH"))) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 2), 6, "ORCHES", "ARCHIT", "ORCHID"))) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 2), 1, "T", "S"))) || (((org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 1), 1, "A", "O", "U", "E")) || (index == 0)) && ((org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 2), 1, org.apache.commons.codec.language.DoubleMetaphone.L_R_N_M_B_H_F_V_W_SPACE)) || ((index + 1) == ((value.length()) - 1))));
    }

    private boolean conditionL0(java.lang.String value, int index) {
        if ((index == ((value.length()) - 3)) && (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 1), 4, "ILLO", "ILLA", "ALLE"))) {
            return true;
        }else
            if (((org.apache.commons.codec.language.DoubleMetaphone.contains(value, ((value.length()) - 2), 2, "AS", "OS")) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, ((value.length()) - 1), 1, "A", "O"))) && (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 1), 4, "ALLE"))) {
                return true;
            }else {
                return false;
            }

    }

    private boolean conditionM0(java.lang.String value, int index) {
        if ((charAt(value, (index + 1))) == 'M') {
            return true;
        }
        return (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index - 1), 3, "UMB")) && (((index + 1) == ((value.length()) - 1)) || (org.apache.commons.codec.language.DoubleMetaphone.contains(value, (index + 2), 2, "ER")));
    }

    private boolean isSlavoGermanic(java.lang.String value) {
        return ((((value.indexOf('W')) > (-1)) || ((value.indexOf('K')) > (-1))) || ((value.indexOf("CZ")) > (-1))) || ((value.indexOf("WITZ")) > (-1));
    }

    private boolean isVowel(char ch) {
        return (org.apache.commons.codec.language.DoubleMetaphone.VOWELS.indexOf(ch)) != (-1);
    }

    private boolean isSilentStart(java.lang.String value) {
        boolean result = false;
        for (int i = 0; i < (org.apache.commons.codec.language.DoubleMetaphone.SILENT_START.length); i++) {
            if (value.startsWith(org.apache.commons.codec.language.DoubleMetaphone.SILENT_START[i])) {
                result = true;
                break;
            }
        }
        return result;
    }

    private java.lang.String cleanInput(java.lang.String input) {
        if (input == null) {
            return null;
        }
        input = input.trim();
        if ((input.length()) == 0) {
            return null;
        }
        return input.toUpperCase(java.util.Locale.ENGLISH);
    }

    protected char charAt(java.lang.String value, int index) {
        if ((index < 0) || (index >= (value.length()))) {
            return java.lang.Character.MIN_VALUE;
        }
        return value.charAt(index);
    }

    private static boolean contains(java.lang.String value, int start, int length, java.lang.String criteria) {
        return org.apache.commons.codec.language.DoubleMetaphone.contains(value, start, length, new java.lang.String[]{ criteria });
    }

    private static boolean contains(java.lang.String value, int start, int length, java.lang.String criteria1, java.lang.String criteria2) {
        return org.apache.commons.codec.language.DoubleMetaphone.contains(value, start, length, new java.lang.String[]{ criteria1, criteria2 });
    }

    private static boolean contains(java.lang.String value, int start, int length, java.lang.String criteria1, java.lang.String criteria2, java.lang.String criteria3) {
        return org.apache.commons.codec.language.DoubleMetaphone.contains(value, start, length, new java.lang.String[]{ criteria1, criteria2, criteria3 });
    }

    private static boolean contains(java.lang.String value, int start, int length, java.lang.String criteria1, java.lang.String criteria2, java.lang.String criteria3, java.lang.String criteria4) {
        return org.apache.commons.codec.language.DoubleMetaphone.contains(value, start, length, new java.lang.String[]{ criteria1, criteria2, criteria3, criteria4 });
    }

    private static boolean contains(java.lang.String value, int start, int length, java.lang.String criteria1, java.lang.String criteria2, java.lang.String criteria3, java.lang.String criteria4, java.lang.String criteria5) {
        return org.apache.commons.codec.language.DoubleMetaphone.contains(value, start, length, new java.lang.String[]{ criteria1, criteria2, criteria3, criteria4, criteria5 });
    }

    private static boolean contains(java.lang.String value, int start, int length, java.lang.String criteria1, java.lang.String criteria2, java.lang.String criteria3, java.lang.String criteria4, java.lang.String criteria5, java.lang.String criteria6) {
        return org.apache.commons.codec.language.DoubleMetaphone.contains(value, start, length, new java.lang.String[]{ criteria1, criteria2, criteria3, criteria4, criteria5, criteria6 });
    }

    protected static boolean contains(java.lang.String value, int start, int length, java.lang.String[] criteria) {
        boolean result = false;
        if ((start >= 0) && ((start + length) <= (value.length()))) {
            java.lang.String target = value.substring(start, (start + length));
            for (int i = 0; i < (criteria.length); i++) {
                if (target.equals(criteria[i])) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    public class DoubleMetaphoneResult {
        private java.lang.StringBuffer primary = new java.lang.StringBuffer(getMaxCodeLen());

        private java.lang.StringBuffer alternate = new java.lang.StringBuffer(getMaxCodeLen());

        private int maxLength;

        public DoubleMetaphoneResult(int maxLength) {
            this.maxLength = maxLength;
        }

        public void append(char value) {
            appendPrimary(value);
            appendAlternate(value);
        }

        public void append(char primary, char alternate) {
            appendPrimary(primary);
            appendAlternate(alternate);
        }

        public void appendPrimary(char value) {
            if ((this.primary.length()) < (this.maxLength)) {
                this.primary.append(value);
            }
        }

        public void appendAlternate(char value) {
            if ((this.alternate.length()) < (this.maxLength)) {
                this.alternate.append(value);
            }
        }

        public void append(java.lang.String value) {
            appendPrimary(value);
            appendAlternate(value);
        }

        public void append(java.lang.String primary, java.lang.String alternate) {
            appendPrimary(primary);
            appendAlternate(alternate);
        }

        public void appendPrimary(java.lang.String value) {
            int addChars = (this.maxLength) - (this.primary.length());
            if ((value.length()) <= addChars) {
                this.primary.append(value);
            }else {
                this.primary.append(value.substring(0, addChars));
            }
        }

        public void appendAlternate(java.lang.String value) {
            int addChars = (this.maxLength) - (this.alternate.length());
            if ((value.length()) <= addChars) {
                this.alternate.append(value);
            }else {
                this.alternate.append(value.substring(0, addChars));
            }
        }

        public java.lang.String getPrimary() {
            return this.primary.toString();
        }

        public java.lang.String getAlternate() {
            return this.alternate.toString();
        }

        public boolean isComplete() {
            return ((this.primary.length()) >= (this.maxLength)) && ((this.alternate.length()) >= (this.maxLength));
        }
    }
}

