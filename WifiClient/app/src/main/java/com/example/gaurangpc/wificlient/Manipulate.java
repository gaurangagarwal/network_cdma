package com.example.gaurangpc.wificlient;

public class Manipulate {
    byte[] byteArr;
    byte pseudoNum;
    byte reversePseudoNum;

    public Manipulate(byte byteArr[], byte pseudoNum, byte reversePseudoNum) { // -127 to 127
        this.byteArr = byteArr;
        this.pseudoNum = pseudoNum;
        this.reversePseudoNum = reversePseudoNum;
    }

    public byte[] decoding(byte[] encodedArr) {
        byte[] decodeArr = new byte[encodedArr.length/8];
        for (int i = 0; i < encodedArr.length; i+=8) {
            byte temp=0;
            for (int j = i; j < i+8; j++) {
                //System.out.println(encodedArr[j]);
                float sum = 0;
                for (int k = 0; k < 8; k++) {
                    short bit1 = (short) ((encodedArr[j] << k % Byte.SIZE & 0x80) == 0 ? -1 : 1);
                    short bit2 = (short) ((pseudoNum << k % Byte.SIZE & 0x80) == 0 ? -1 : 1);
                    //System.out.println(bit1+"    "+bit2);
                    sum = sum  + bit1*bit2;

                }
                sum/=8;
                double randomNum=Math.random()%100;
                if (sum==1) {
                    temp = (byte) (temp +  1);
                } else if (randomNum>50) {
                    temp = (byte) (temp +  1);
                }

                if (j!=i+7)
                    temp = (byte) (temp*2);
            }
            decodeArr[i/8] = temp;
        }
        return  decodeArr;
    }

    public byte[] encoding() {
        byte[] encodedArr = new byte[8*byteArr.length];
        int index = 0;
        for( int i = 0; i < Byte.SIZE * byteArr.length; i++ ) {
            short singleBit = (short) ((byteArr[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? 0 : 1);
            if (singleBit == 1) {
                encodedArr[index++] = pseudoNum;
            } else { // 0
                encodedArr[index++] = reversePseudoNum;
            }
        }
        return  encodedArr;
    }

}

