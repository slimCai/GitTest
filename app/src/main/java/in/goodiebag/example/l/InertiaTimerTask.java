// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: braces fieldsfirst space lnc 

package in.goodiebag.example.l;

// Referenced classes of package com.qingchifan.view:
//            LoopView


final class InertiaTimerTask implements Runnable {

    float a;
    final float velocityY;
    final LoopView loopView;

    InertiaTimerTask(LoopView loopview, float velocityY) {
        super();
        loopView = loopview;
        this.velocityY = velocityY;
        a = Integer.MAX_VALUE;
    }

    @Override
    public final void run() {
        if (a == Integer.MAX_VALUE) {
            if (Math.abs(velocityY) > 2000F) {
                if (velocityY > 0.0F) {
                    a = 2000F;
                } else {
                    a = -2000F;
                }
            } else {
                a = velocityY;
            }
        }
        if (Math.abs(a) >= 0.0F && Math.abs(a) <= 20F) {
            loopView.cancelFuture();
            loopView.mHandler.sendEmptyMessage(MessageHandler.WHAT_SMOOTH_SCROLL);
            return;
        }
        int i = (int) ((a * 10F) / 1000F);
        LoopView loopview = loopView;
        loopview.totalScrollX = loopview.totalScrollX - i;
        if (!loopView.isLoop) {
            float itemHeight = loopView.lineSpacingMultiplier * loopView.maxItemWidth;
            if (loopView.totalScrollX <= (int) ((float) (-loopView.initPosition) * itemHeight)) {
                a = 40F;
                loopView.totalScrollX = (int) ((float) (-loopView.initPosition) * itemHeight);
            } else if (loopView.totalScrollX >= (int) ((float) (loopView.items.size() - 1 - loopView.initPosition) * itemHeight)) {
                loopView.totalScrollX = (int) ((float) (loopView.items.size() - 1 - loopView.initPosition) * itemHeight);
                a = -40F;
            }
        }
        if (a < 0.0F) {
            a = a + 20F;
        } else {
            a = a - 20F;
        }
        loopView.mHandler.sendEmptyMessage(MessageHandler.WHAT_INVALIDATE_LOOP_VIEW);
    }
}
