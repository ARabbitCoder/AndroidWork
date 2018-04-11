/*
 * Copyright (C) 2018 CoorChice <icechen_@outlook.com>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * <p>
 * Last modified 18-3-29 下午2:57
 */

package com.ayearn.ui.superview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * @author liujingwei
 * @DESC
 * @time 2018-3-29 14:57
 */

public class TvSuperTextView extends SuperTextView{
    public TvSuperTextView(Context context) {
        super(context);
        initFocusAble();
    }

    public TvSuperTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFocusAble();
    }

    public TvSuperTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFocusAble();
    }

    public TvSuperTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initFocusAble();

    }

    private void initFocusAble(){
        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if(focused){
            doFocusAdjust(focused);
            startAnim();
        }else{
            doFocusAdjust(focused);
            stopAnim();
        }
    }

    private void doFocusAdjust(boolean havefocus){
        for (int i = 0; i < adjusterList.size(); i++) {
            Adjuster adjuster = adjusterList.get(i);
            adjuster.onFocusChange(this,havefocus);
        }
    }
}
