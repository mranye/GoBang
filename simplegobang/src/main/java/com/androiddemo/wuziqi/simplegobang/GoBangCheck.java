package com.androiddemo.wuziqi.simplegobang;


import android.graphics.Point;

import java.util.List;

public class GoBangCheck {

    private static int max_count_one_line = GoBangPanel.getMaxCountInLine();

    /**
     * 判断x，y位置的棋子，是否横向有相邻的五个棋子
     */
    public static boolean checkHorizontal(int x,int y,List<Point> points){
        int count = 1;
        //检查左边
        for (int i=1;i<max_count_one_line;i++){
            if(points.contains(new Point(x-i,y))){
                count++;
            }else {
                break;
            }
        }
        if (count == max_count_one_line) return true;//达到5个，则不用进行下面的检查
        //检查右边
        for (int i=1;i<max_count_one_line;i++){
            if(points.contains(new Point(x+i,y))){
                count++;
            }else {
                break;
            }
        }
        if (count == max_count_one_line) return true;//达到5个，则不用进行下面的检查
        return false;
    }

    /**
     * 判断x，y位置的棋子，是否纵向有相邻的五个棋子
     */
    public static boolean checkVertical(int x,int y,List<Point> points){
        int count = 1;
        //检查上边
        for (int i=1;i<max_count_one_line;i++){
            if(points.contains(new Point(x,y-i))){
                count++;
            }else {
                break;
            }
        }
        if (count == max_count_one_line) return true;//达到5个，则不用进行下面的检查
        //检查下边
        for (int i=1;i<max_count_one_line;i++){
            if(points.contains(new Point(x,y+i))){
                count++;
            }else {
                break;
            }
        }
        if (count == max_count_one_line) return true;//达到5个，则不用进行下面的检查
        return false;
    }

    /**
     * 判断x，y位置的棋子，是否左斜向有相邻的五个棋子
     */
    public static boolean checkLeftDiagonal(int x,int y,List<Point> points){
        int count = 1;
        //检查右上
        for (int i=1;i<max_count_one_line;i++){
            if(points.contains(new Point(x-i,y+i))){
                count++;
            }else {
                break;
            }
        }
        if (count == max_count_one_line) return true;//达到5个，则不用进行下面的检查
        //检查左下
        for (int i=1;i<max_count_one_line;i++){
            if(points.contains(new Point(x+i,y-i))){
                count++;
            }else {
                break;
            }
        }
        if (count == max_count_one_line) return true;//达到5个，则不用进行下面的检查
        return false;
    }

    /**
     * 判断x，y位置的棋子，是否右斜向有相邻的五个棋子
     */
    public static boolean checkRightDiagonal(int x,int y,List<Point> points){
        int count = 1;
        //检查左上
        for (int i=1;i<max_count_one_line;i++){
            if(points.contains(new Point(x-i,y-i))){
                count++;
            }else {
                break;
            }
        }
        if (count == max_count_one_line) return true;//达到5个，则不用进行下面的检查
        //检查右下
        for (int i=1;i<max_count_one_line;i++){
            if(points.contains(new Point(x+i,y+i))){
                count++;
            }else {
                break;
            }
        }
        if (count == max_count_one_line) return true;//达到5个，则不用进行下面的检查
        return false;
    }
}
