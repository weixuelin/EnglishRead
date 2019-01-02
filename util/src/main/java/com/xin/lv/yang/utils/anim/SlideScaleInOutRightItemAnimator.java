/*
 * ******************************************************************************
 *   Copyright (c) 2014-2015 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */
package com.xin.lv.yang.utils.anim;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class SlideScaleInOutRightItemAnimator extends BaseItemAnimator {

    private float DEFAULT_SCALE_INITIAL = 0.6f;

    private float mInitialScaleX = DEFAULT_SCALE_INITIAL;
    private float mInitialScaleY = DEFAULT_SCALE_INITIAL;

    private float mEndScaleX = DEFAULT_SCALE_INITIAL;
    private float mEndScaleY = DEFAULT_SCALE_INITIAL;

    private float mOriginalScaleX;
    private float mOriginalScaleY;

    public SlideScaleInOutRightItemAnimator(RecyclerView recyclerView) {
        super(recyclerView);

        setAddDuration(750);
        setRemoveDuration(750);
    }

    protected void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
        final View view = holder.itemView;

        final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
        mRemoveAnimations.add(holder);
        animation.setDuration(getRemoveDuration())
                .scaleX(0)
                .scaleY(0)
                .alpha(0)
                .translationX(+mRecyclerView.getLayoutManager().getWidth())
                .setListener(new VpaListenerAdapter() {

                    @Override
                    public void onAnimationStart(View view) {
                        dispatchRemoveStarting(holder);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        animation.setListener(null);
                        view.setAlpha(1);
                        view.setScaleX(0);
                        view.setScaleY(0);
                        view.setTranslationX(+mRecyclerView.getLayoutManager().getWidth());
                        dispatchRemoveFinished(holder);
                        mRemoveAnimations.remove(holder);
                        dispatchFinishedWhenDone();
                    }
                }).start();

    }

    @Override
    protected void prepareAnimateAdd(RecyclerView.ViewHolder holder) {
        retrieveOriginalScale(holder);
        holder.itemView.setScaleX(0);
        holder.itemView.setScaleY(0);

        holder.itemView.setTranslationX(+mRecyclerView.getLayoutManager().getWidth());
    }


    protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
        final View view = holder.itemView;

        final ViewPropertyAnimatorCompat animation = ViewCompat.animate(view);
        mAddAnimations.add(holder);
        animation.scaleX(1)
                .scaleY(1)
                .translationX(0)
                .alpha(1)
                .setDuration(getAddDuration())
                .setListener(new VpaListenerAdapter() {
                    @Override
                    public void onAnimationStart(View view) {
                        dispatchAddStarting(holder);
                    }

                    @Override
                    public void onAnimationCancel(View view) {
                        view.setAlpha(1);
                        view.setTranslationX(0);
                        view.setScaleX(1);
                        view.setScaleY(1);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        animation.setListener(null);
                        view.setAlpha(1);
                        view.setTranslationX(0);
                        view.setScaleX(1);
                        view.setScaleY(1);
                        dispatchAddFinished(holder);
                        mAddAnimations.remove(holder);
                        dispatchFinishedWhenDone();
                    }
                }).start();

    }

    private void retrieveOriginalScale(RecyclerView.ViewHolder holder) {
        mOriginalScaleX = holder.itemView.getScaleX();
        mOriginalScaleY = holder.itemView.getScaleY();
    }

}
