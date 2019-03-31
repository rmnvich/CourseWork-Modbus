package rmnvich.apps.coursework.modbus.data.utils;

public class ControlSumCalculator {

    private int m_nInitValue;
    private int m_nPolynom;
    private boolean m_bTableCalced;
    private int[] m_vuTable;
    private boolean m_bDirectInsert;
    private boolean m_bReverseInsert;

    public ControlSumCalculator() {
        int nPolynom = 0x8005;
        int nInitValue = 0xffff;
        m_vuTable = new int[256];
        m_bTableCalced = false;
        setPolynom(nPolynom, nInitValue, true, false);
    }

    public int calculateControlSum(byte[] pData, int fromIndex, int nLength) {
        initTable();
        int crc = m_nInitValue;
        if (!m_bReverseInsert) {
            if (!m_bDirectInsert)
                for (int i = 0; i < 2; i++)
                    crc = (HIBYTE(crc) & 0xFF) ^ m_vuTable[LOBYTE(crc) & 0xFF];
            while (nLength > 0) {
                nLength--;
                int acc = (LOBYTE(crc) ^ pData[fromIndex]) & 0xFF;
                crc = (HIBYTE(crc) & 0xFF) ^ m_vuTable[acc];
                fromIndex++;
            }
        } else {
            if (!m_bDirectInsert)
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

    private void initTable() {
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
            m_vuTable[i] = calculateControlSum(buff, 0, 1, polynom, nPrev, m_bReverseInsert);
        }
        m_bTableCalced = true;
    }

    private byte LOBYTE(int val) {
        return (byte) (val & 0xFF);
    }

    private byte HIBYTE(int val) {
        return (byte) ((val >> 8) & 0xFF);
    }

    private void setPolynom(int nPolynom, int nInitValue, boolean bDirectInsert, boolean bReverseInsert) {
        nPolynom = nPolynom & 0xffff;
        nInitValue = nInitValue & 0xffff;
        m_nInitValue = nInitValue;
        m_nPolynom = nPolynom;
        m_bTableCalced = false;
        m_bDirectInsert = bDirectInsert;
        m_bReverseInsert = bReverseInsert;
    }

    private int makeWord(byte loByte, byte hiByte) {
        return ((hiByte & 0xFF) << 8) | (loByte & 0xFF);
    }

    private int calculateControlSum(byte[] pBuff, int fromIndex, int nLength, int nPolynom, int nCRCPrev, boolean bReverseInsert) {
        nPolynom &= 0xFFFF;
        nCRCPrev &= 0xFFFF;
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
            int reg = makeWord(HIBYTE(nCRCPrev), LOBYTE(nCRCPrev));
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
}
