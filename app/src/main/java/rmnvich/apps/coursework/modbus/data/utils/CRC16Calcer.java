package rmnvich.apps.coursework.modbus.data.utils;

public class CRC16Calcer {
    private int m_nInitValue;
    private int m_nPolynom;
    private boolean m_bTableCalced;
    private int[] m_vuTable;
    private boolean m_bDirectInsert;
    private boolean m_bReverseInsert;

    private void InitTable() {
        if (m_bTableCalced)
            return;
        int polynom;
        if (m_bReverseInsert)
            polynom = m_nPolynom;
        else {
            polynom = 0;
            for (int i = 0x8000; i != 0; i >>= 1) {
                polynom >>= 1;
                if (0 != (m_nPolynom & i))
                    polynom |= 0x8000;
            }
        }
        byte[] buff = new byte[1];
        buff[0] = 0;
        for (int i = 0; i < 0x100; i++) {
            int nPrev = i + 0x0000;
            m_vuTable[i] = _CalcCRC(buff, 0, 1, polynom, nPrev, m_bReverseInsert);
        }
        m_bTableCalced = true;
    }

    public CRC16Calcer() {
        int nPolynom = 0x8005;
        int nInitValue = 0xffff;
        m_vuTable = new int[256];
        m_bTableCalced = false;
        SetPolynom(nPolynom, nInitValue, true, false);
    }

    public CRC16Calcer(int nPolynom /*= 0x8005*/, int nInitValue /*= 0xFFFF*/) {
        nPolynom = nPolynom & 0xffff;
        nInitValue = nInitValue & 0xffff;
        m_vuTable = new int[256];
        m_bTableCalced = false;
        SetPolynom(nPolynom, nInitValue, true, false);
    }

    public CRC16Calcer(int nPolynom /*= 0x8005*/, int nInitValue /*= 0xFFFF*/, boolean bDirectInsert /*= true*/, boolean bReverseInsert /*= false*/) // ïîëèíîì 1 + x^2 + x^15 + x^16
    {
        nPolynom = nPolynom & 0xffff;
        nInitValue = nInitValue & 0xffff;
        m_vuTable = new int[256];
        m_bTableCalced = false;
        SetPolynom(nPolynom, nInitValue, bDirectInsert, bReverseInsert);
    }

    private byte LOBYTE(int val) {
        return (byte) (val & 0xFF);
    }

    private byte HIBYTE(int val) {
        return (byte) ((val >> 8) & 0xFF);
    }

    public int CalcCRC(byte[] pData, int fromIndex, int nLength) {
        InitTable();
        //String log = "";
        //for (int i = 0; i < nLength; i++)
        //	log += " " + Integer.toHexString(pData[fromIndex + i] & 0xFF);

        int crc = m_nInitValue;
        if (!m_bReverseInsert) {
            if (!m_bDirectInsert) // åñëè ïîðÿäîê íå ïðÿìîé òî ýòî ïðîñòî àíàëîãè÷íî âñòàâêå â íà÷àëå äâóõ íóëåâûõ áàéò
                for (int i = 0; i < 2; i++)
                    crc = (HIBYTE(crc) & 0xFF) ^ m_vuTable[LOBYTE(crc) & 0xFF];
            while (nLength > 0) {
                nLength--;
                int acc = (LOBYTE(crc) ^ pData[fromIndex]) & 0xFF;
                crc = (HIBYTE(crc) & 0xFF) ^ m_vuTable[acc];
                fromIndex++;
            }
        } else {
            if (!m_bDirectInsert) // åñëè ïîðÿäîê íå ïðÿìîé òî ýòî ïðîñòî àíàëîãè÷íî âñòàâêå â íà÷àëå äâóõ íóëåâûõ áàéò
                for (int i = 0; i < 2; i++)
                    crc = ((LOBYTE(crc) & 0xFF) << 8) ^ m_vuTable[HIBYTE(crc) & 0xFF];
            while (nLength > 0) {
                nLength--;
                crc = ((LOBYTE(crc) & 0xFF) << 8) ^ m_vuTable[(HIBYTE(crc) ^ pData[fromIndex]) & 0xFF];
                fromIndex++;
            }
        }
        return crc;
    }

    public void SetPolynom() {
        SetPolynom(0x8005, 0xFFFF, true, false);
    }

    public void SetPolynom(int nPolynom /*= 0x8005*/, int nInitValue /*= 0xFFFF*/) {
        SetPolynom(nPolynom, nInitValue, true, false);
    }

    public void SetPolynom(int nPolynom /*= 0x8005*/, int nInitValue /*= 0xFFFF*/, boolean bDirectInsert /*= true*/, boolean bReverseInsert /*= false*/) {
        nPolynom = nPolynom & 0xffff;
        nInitValue = nInitValue & 0xffff;
        m_nInitValue = nInitValue;
        m_nPolynom = nPolynom;
        m_bTableCalced = false;
        m_bDirectInsert = bDirectInsert;
        m_bReverseInsert = bReverseInsert;
    }

    private int MAKEWORD(byte loByte, byte hiByte) {
        return ((hiByte & 0xFF) << 8) | (loByte & 0xFF);
    }

    private int _CalcCRC(byte[] pBuff, int fromIndex, int nLength, int nPolynom, int nCRCPrev, boolean bReverseInsert) {
        nPolynom &= 0xFFFF;
        nCRCPrev &= 0xFFFF;
        // const USHORT polynom_base = (1 << 0) | (1 << 2) | (1<<15); // 1 + x^2 + x^15 + x^16 = 0x8005
        if (!bReverseInsert) {
            int reg = nCRCPrev;
            for (; nLength > 0; fromIndex++) {
                nLength--;
                reg ^= (pBuff[fromIndex] & 0xFF);
                for (int i = 0; i < 8; i++) {
                    if ((reg & 1) != 0)
                        reg = (reg >> 1) ^ nPolynom;
                    else
                        reg >>= 1;
                }
            }
            return reg;
        } else {
            int reg = MAKEWORD(HIBYTE(nCRCPrev), LOBYTE(nCRCPrev));
            for (; nLength > 0; fromIndex++) {
                nLength--;
                reg ^= (pBuff[fromIndex] & 0xFF) << 8;
                for (int i = 0; i < 8; i++) {
                    if ((reg & 0x8000) != 0)
                        reg = (reg << 1) ^ nPolynom;
                    else
                        reg <<= 1;
                }
            }
            reg = reg & 0xFFFF;
            return reg;
        }
    }

    public boolean CheckCRC(byte[] pData, int fromIndex, int nLength) {
        if (nLength < 2)
            return false;
        if (nLength == 2)
            return MAKEWORD(pData[fromIndex + 0], pData[fromIndex + 1]) == m_nInitValue;
        int crc = CalcCRC(pData, fromIndex, nLength - 2);
        int foundCRC = MAKEWORD(pData[fromIndex + nLength - 2], pData[fromIndex + nLength - 1]);
        return crc == foundCRC;
    }

/*
	String hexToAscii(String s) throws IllegalArgumentException
	{
		  int n = s.length();
		  StringBuilder sb = new StringBuilder(n / 2);
		  for (int i = 0; i < n; i += 2)
		  {
		    char a = s.charAt(i);
		    char b = s.charAt(i + 1);
		    sb.append((char) ((hexToInt(a) << 4) | hexToInt(b)));
		  }
		  return sb.toString();
	}

	static int hexToInt(char ch)
	{
		  if ('a' <= ch && ch <= 'f') { return ch - 'a' + 10; }
		  if ('A' <= ch && ch <= 'F') { return ch - 'A' + 10; }
		  if ('0' <= ch && ch <= '9') { return ch - '0'; }
		  throw new IllegalArgumentException(String.valueOf(ch));
	}*/
}
