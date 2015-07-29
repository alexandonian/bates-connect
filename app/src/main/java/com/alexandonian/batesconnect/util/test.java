package com.alexandonian.batesconnect.util;

import com.alexandonian.batesconnect.data.InfoContract;

/**
 * Created by Administrator on 7/26/2015.
 */
public class test {

    public static void main(String[] args) {

        String test = InfoContract.COLUMN_MONTH + "=? AND ";
        System.out.println(test);
    }
}
