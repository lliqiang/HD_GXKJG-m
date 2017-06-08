package com.hengda.smart.common.dialog;

import android.view.View;

import static com.nineoldandroids.animation.ObjectAnimator.ofFloat;

/**
 * Copyright 2014 litao
 * https://github.com/sd6352051/NiftyDialogEffects
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class SlideTop extends BaseEffects {

    @Override
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(
                ofFloat(view, "translationY", -300, 0).setDuration(mDuration),
                ofFloat(view, "alpha", 0, 1).setDuration(mDuration * 3 / 2)
        );
    }
}
