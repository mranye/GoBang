package com.androiddemo.wuziqi.simplegobang;



public interface OnGameOverListener {
    /**
     * @param win true,白赢; false,黑赢
     */
    void onGameWin(boolean win);

    /**
     * 和局
     */
    void onGameHe();
}
