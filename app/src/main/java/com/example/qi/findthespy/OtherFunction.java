package com.example.qi.findthespy;

import java.util.Random;

/**
 * Created by sinmo on 2016/1/1.
 */
public class OtherFunction {
    public int createRandomNum(int upper) {
        Random rand = new Random();
        int randNum = rand.nextInt(upper);
        return randNum;
    }
}

