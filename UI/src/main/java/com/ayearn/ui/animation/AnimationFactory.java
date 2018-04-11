package com.ayearn.ui.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;

/**
 * Created by Administrator on 2016-12-8.
 */
public class AnimationFactory {
    public static final int TRANSLATE_X = 0x001;
    public static final int TRANSLATE_Y = 0x002;
    public static final int ANIMATION_XY = 0x003;
    public static final int ANIMATION_CENTER = 0x004;
    public static final int REPEAT_REVERSE = 2;
    public static final int REPEAT_RESTART = 1;
    /**
     * 创建一个以view宽高值为中心的缩放动画
     * @param scale  大于1是放大小于1是缩小
     * @param duration 动画持续时间
     * @param repeatCount 重复次数 小于0是一直重复
     * @param repeatMode 1：重新开始   2：反向开始
     * @return
     */
    public static AnimationSet createScaleAnimationBoundary(float scale,long duration,int repeatCount,int repeatMode) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                Animation.RELATIVE_TO_SELF,scale,         //x开始位置，放大/缩小倍数
                Animation.RELATIVE_TO_SELF,scale,         //y开始位置，放大/缩小倍数
                Animation.RELATIVE_TO_SELF,0,           //x放大/缩小的位置
                Animation.RELATIVE_TO_SELF,0);          //y放大/缩小的位置
        scaleAnimation.setDuration(duration);
        scaleAnimation.setRepeatMode(repeatMode>1?Animation.REVERSE:Animation.RESTART);
        scaleAnimation.setRepeatCount(repeatCount < 0?-1:repeatCount);
        animationSet.addAnimation(scaleAnimation);
        return animationSet;
    }

    /**
     * 创建一个以view中心的缩放动画
     * @param scale  大于1是放大小于1是缩小
     * @param duration 动画持续时间
     * @param repeatCount 重复次数 小于0是一直重复
     * @param repeatMode 1：重新开始   2：反向开始
     * @return
     */
    public static AnimationSet createScaleAnimationCenter(float scale,long duration,int repeatCount,int repeatMode) {
        AnimationSet animationSet = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                Animation.RELATIVE_TO_SELF,scale,         //x开始位置，放大/缩小倍数
                Animation.RELATIVE_TO_SELF,scale,         //y开始位置，放大/缩小倍数
                Animation.RELATIVE_TO_SELF,0.5f,           //x放大/缩小的位置
                Animation.RELATIVE_TO_SELF,0.5f);          //y放大/缩小的位置
        scaleAnimation.setDuration(duration);
        scaleAnimation.setRepeatMode(repeatMode>1?Animation.REVERSE:Animation.RESTART);
        scaleAnimation.setRepeatCount(repeatCount < 0?-1:repeatCount);
        animationSet.addAnimation(scaleAnimation);
        return animationSet;
    }

    /**
     *  生成一个Y轴平移动画
     * @param distance 平移的距离 小于0时为向上平移，大于0时向下平移
     * @param duration 动画持续时间
     * @param duration 动画重复次数 小于0时一直重复
     * @param repeatMode 1：重新开始   2：反向开始
     * @return
     */
    public static AnimationSet createTranslateAnimationY(float distance,long duration,int repeatCount,int repeatMode) {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation
                (       Animation.RELATIVE_TO_SELF,0,   //x起始位置
                        Animation.RELATIVE_TO_SELF,0,   //x终止位置
                        Animation.RELATIVE_TO_SELF,0,   //y起始位置
                        Animation.RELATIVE_TO_SELF,distance);   //y终止位置
        translateAnimation.setRepeatMode(repeatMode>1?Animation.REVERSE:Animation.RESTART);
        translateAnimation.setRepeatCount(repeatCount < 0?-1:repeatCount);
        translateAnimation.setDuration(duration);
        animationSet.addAnimation(translateAnimation);
        return animationSet;
    }

    /**
     *  生成一个Y轴平移动画指定y开始的起点
     * @param distance 平移的距离 小于0时为向上平移，大于0时向下平移
     * @param duration 动画持续时间
     * @param duration 动画重复次数 小于0时一直重复
     * @param repeatMode 1：重新开始   2：反向开始
     * @return
     */
    public static AnimationSet createTranslateAnimationY(float yStart,float distance,long duration,int repeatCount,int repeatMode) {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation
                (       Animation.RELATIVE_TO_SELF,0,   //x起始位置
                        Animation.RELATIVE_TO_SELF,0,   //x终止位置
                        Animation.RELATIVE_TO_SELF,yStart,   //y起始位置
                        Animation.RELATIVE_TO_SELF,distance);   //y终止位置
        translateAnimation.setRepeatMode(repeatMode>1?Animation.REVERSE:Animation.RESTART);
        translateAnimation.setRepeatCount(repeatCount < 0?-1:repeatCount);
        translateAnimation.setDuration(duration);
        animationSet.addAnimation(translateAnimation);
        return animationSet;
    }

    public static AnimationSet CreateTestAnim() {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation
                (       Animation.RELATIVE_TO_SELF,-1.0f,   //x起始位置
                        Animation.RELATIVE_TO_SELF,0,   //x终止位置发放f
                        Animation.RELATIVE_TO_SELF,0,   //y起始位置
                        Animation.RELATIVE_TO_SELF,0);   //y终止位置
        translateAnimation.setFillAfter(false);
        translateAnimation.setDuration(2000);
        translateAnimation.setInterpolator(new LinearInterpolator());
        animationSet.addAnimation(translateAnimation);
        return animationSet;
    }
    public static AnimationSet CreateTestAnim1() {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation
                (       Animation.RELATIVE_TO_SELF,0.0f,   //x起始位置
                        Animation.RELATIVE_TO_SELF,1.0f,   //x终止位置发放f
                        Animation.RELATIVE_TO_SELF,0,   //y起始位置
                        Animation.RELATIVE_TO_SELF,0);   //y终止位置
        translateAnimation.setFillAfter(false);
        translateAnimation.setDuration(2000);
        translateAnimation.setInterpolator(new LinearInterpolator());
        animationSet.addAnimation(translateAnimation);
        return animationSet;
    }

    /**
     *  生成一个X轴平移动画
     * @param distance 平移的距离 小于0时为向左平移，大于0时向右平移
     * @param duration 动画持续时间
     * @param duration 动画重复次数 小于0时一直重复
     * @param repeatMode 1：重新开始   2：反向开始
     * @return
     */
    public static AnimationSet createTranslateAnimationX(float distance,long duration,int repeatCount,int repeatMode) {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation
                (       Animation.RELATIVE_TO_SELF,0,   //x起始位置
                        Animation.RELATIVE_TO_SELF,distance,   //x终止位置
                        Animation.RELATIVE_TO_SELF,0,   //y起始位置
                        Animation.RELATIVE_TO_SELF,0);   //y终止位置
        translateAnimation.setRepeatMode(repeatMode>1?Animation.REVERSE:Animation.RESTART);
        translateAnimation.setRepeatCount(repeatCount < 0?-1:repeatCount);
        translateAnimation.setDuration(duration);
        animationSet.addAnimation(translateAnimation);
        return animationSet;
    }

    /**
     *  生成一个Y轴平移动画指定x开始的起点
     * @param distance 平移的距离 小于0时为向左平移，大于0时向右平移
     * @param distance 平移的距离 小于0时为向左平移，大于0时向右平移
     * @param duration 动画持续时间
     * @param duration 动画重复次数 小于0时一直重复
     * @param repeatMode 1：重新开始   2：反向开始
     * @return
     */
    public static AnimationSet createTranslateAnimationX(float xStart,float distance,long duration,int repeatCount,int repeatMode) {
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = new TranslateAnimation
                (       Animation.RELATIVE_TO_SELF,xStart,   //x起始位置
                        Animation.RELATIVE_TO_SELF,distance,   //x终止位置
                        Animation.RELATIVE_TO_SELF,0,   //y起始位置
                        Animation.RELATIVE_TO_SELF,0);   //y终止位置
        translateAnimation.setRepeatMode(repeatMode>1?Animation.REVERSE:Animation.RESTART);
        translateAnimation.setRepeatCount(repeatCount < 0?-1:repeatCount);
        translateAnimation.setDuration(duration);
        animationSet.addAnimation(translateAnimation);
        return animationSet;
    }
    /**
     *
     * @param formAlpha 开始的透明度 1是透明   介于0-1之间
     * @param toAlpha   结束的透明度 0是不透明
     * @param duration  动画持续时间
     * @param duration 动画重复次数 小于0时一直重复
     * @param repeatMode 1：重新开始   2：反向开始
     * @return
     */
    public static AnimationSet cretateAlphaAnimation(float formAlpha,long toAlpha,long duration,int repeatCount,int repeatMode) {
        AnimationSet animationSet = new AnimationSet(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(formAlpha,toAlpha);        //开始的透明度，结束的透明度(透明到不透明)
        alphaAnimation.setDuration(duration);
        alphaAnimation.setRepeatMode(repeatMode>1?Animation.REVERSE:Animation.RESTART);                //Animation.REVERSE反向开始，Animation.RESTART重新开始
        alphaAnimation.setRepeatCount(repeatCount < 0?-1:repeatCount);
        animationSet.addAnimation(alphaAnimation);
        return animationSet;
    }

    /**
     *
     * @param fromDegrees 开始角度
     * @param toDegrees   结束角度
     *@param duration  动画持续时间
     * @param duration 动画重复次数 小于0时一直重复
     * @param repeatMode 1：重新开始   2：反向开始
     * @param relativeX  旋转中心X的值 0.5f表示本身的一半
     * @param relativeY  旋转中心Y的值 0.5f表示本身的一半
     * @return
     */
    public static AnimationSet createRotateAnimation(float fromDegrees,float toDegrees,long duration,int repeatCount,int repeatMode,float relativeX,float relativeY) {
        AnimationSet animationSet = new AnimationSet(true);
        //1、开始角度    2、结束角度后面    3、确定旋转中心的位置
        RotateAnimation rotateAnimation = new RotateAnimation(fromDegrees,toDegrees,Animation.RELATIVE_TO_SELF,relativeX,Animation.RELATIVE_TO_SELF,relativeY);
        rotateAnimation.setDuration(duration);
        rotateAnimation.setRepeatMode(repeatMode>1?Animation.REVERSE:Animation.RESTART);                //Animation.REVERSE反向开始，Animation.RESTART重新开始
        rotateAnimation.setRepeatCount(repeatCount);
        animationSet.addAnimation(rotateAnimation);
        return animationSet;
    }


    /*public static AnimationSet createTranslateAnimation(int type,int startlocation,float end,int repeatmode,int repeatcount,long duration){
        AnimationSet animationSet = new AnimationSet(true);
        TranslateAnimation translateAnimation = null;
        if(startlocation == ANIMATION_CENTER && type == TRANSLATE_Y) {
            translateAnimation = new TranslateAnimation
                    (       Animation.RELATIVE_TO_SELF,0.5f,   //x起始位置
                            Animation.RELATIVE_TO_SELF,0.5f,   //x终止位置
                            Animation.RELATIVE_TO_SELF,0.5f,   //y起始位置
                            Animation.RELATIVE_TO_SELF,end);   //y终止位置
        }else if(startlocation == ANIMATION_CENTER && type == TRANSLATE_X) {
            translateAnimation = new TranslateAnimation
                    (       Animation.RELATIVE_TO_SELF,0,   //x起始位置
                            Animation.RELATIVE_TO_SELF,end,   //x终止位置
                            Animation.RELATIVE_TO_SELF,0,   //y起始位置
                            Animation.RELATIVE_TO_SELF,0);   //y终止位置
        }else if(startlocation == ANIMATION_XY && type == TRANSLATE_Y) {
            translateAnimation = new TranslateAnimation
                    (       Animation.RELATIVE_TO_SELF,0,   //x起始位置
                            Animation.RELATIVE_TO_SELF,0,   //x终止位置
                            Animation.RELATIVE_TO_SELF,0,   //y起始位置
                            Animation.RELATIVE_TO_SELF,end);   //y终止位置
        }else if(startlocation == ANIMATION_XY && type == TRANSLATE_X) {
            translateAnimation = new TranslateAnimation
                    (       Animation.RELATIVE_TO_SELF,0,   //x起始位置
                            Animation.RELATIVE_TO_SELF,end,   //x终止位置
                            Animation.RELATIVE_TO_SELF,0,   //y起始位置
                            Animation.RELATIVE_TO_SELF,0);   //y终止位置
        }

        translateAnimation.setRepeatMode(repeatmode==Animation.REVERSE?Animation.REVERSE:Animation.RESTART);
        translateAnimation.setRepeatCount(repeatcount<0?-1:repeatcount);
        translateAnimation.setDuration(duration);
        animationSet.addAnimation(translateAnimation);

        return animationSet;
    }*/
    public static TranslateYAnimation createTranslateYAnim(boolean isInAnimation,boolean isPositive,long duration) {
        TranslateYAnimation  translateYAnimation = new TranslateYAnimation(isInAnimation,isPositive);
        translateYAnimation.setDuration(duration);
        translateYAnimation.setFillAfter(false);
        //new AccelerateInterpolator()
        translateYAnimation.setInterpolator(new LinearInterpolator());
        return translateYAnimation;
    }
    public static TranslateXAnimation createTranslateXAnim(boolean isInAnimation,boolean isPositive,long duration){
        TranslateXAnimation translateXAnimation = new TranslateXAnimation(isInAnimation,isPositive);
        translateXAnimation.setDuration(duration);
        translateXAnimation.setFillAfter(false);
        //new AccelerateInterpolator()
        translateXAnimation.setInterpolator(new LinearInterpolator());
        return translateXAnimation;
    }

    public static class  TranslateXAnimation extends Animation {

        private Camera mCamera;
        private int mCenterX;
        private int mCenterY;
        private boolean isInAnimation = true;
        private boolean isPositive = true;
        private int direction = 1;
        /**
         *
         * @param isInAnimation  true:入场动画   false:出场动画
         * @param isPositive     true:正向（x轴是左到右方向）  false：反向（x轴从右到左）
         */
        public TranslateXAnimation(boolean isInAnimation,boolean isPositive) {
            this.isInAnimation = isInAnimation;
            this.isPositive = isPositive;
            this.direction = isPositive?1:-1;
        }
        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCenterX = width / 2;
            mCenterY = height / 2;
            mCamera = new Camera();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float centerX = mCenterX ;
            float centerY = mCenterY ;
            Camera camera = mCamera;
            Matrix matrix = t.getMatrix();
            camera.save();
            if(isInAnimation) {
                camera.translate((interpolatedTime - 1.0f) * mCenterX * 2 * direction,0.0f,0.0f);
            }else {
                camera.translate((interpolatedTime * mCenterX * 2) * direction,0.0f,0.0f);
            }

            camera.getMatrix(matrix);
            camera.restore(); //恢复原始状态
            //设置操作中心为view的中心
            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }

    public static class  TranslateYAnimation  extends Animation {
        private Camera mCamera;
        private int mCenterX;
        private int mCenterY;
        private boolean isInAnimation = true;
        private boolean isPositive = true;
        private int direction = 1;

        /**
         * @param isInAnimation  true:入场动画   false:出场动画
         * @param isPositive     true:正向（y轴是从下到上方向）  false：反向（y轴从上到下）
         */
        public TranslateYAnimation(boolean isInAnimation,boolean isPositive) {
            this.isInAnimation = isInAnimation;
            this.isPositive = isPositive;
            this.direction = isPositive?1:-1;
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCenterX = width / 2;
            mCenterY = height / 2;
            mCamera = new Camera();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            //interpolatedTime 变化值 0-1
            float centerX = mCenterX ;
            float centerY = mCenterY ;
            Camera camera = mCamera;
            Matrix matrix = t.getMatrix();
            camera.save();
            if(isInAnimation) {
                camera.translate(0.0f, direction * mCenterY * 2 * (interpolatedTime - 1.0f), 0.0f);  //向下平移一个view的高度，因为是入场动画所以先下移动一个view的高度
            }else {
                camera.translate(0.0f, direction * mCenterY * 2 * interpolatedTime, 0.0f);  //向上平移一个view的高度，出场动画无需移动，只需要根据interpolatedTime值进行移动
            }
            camera.getMatrix(matrix);
            camera.restore(); //恢复原始状态
            //设置操作中心为view的中心
            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }


    public static  Rotate3dAnimation createAnim(float start, float end, boolean turnIn, boolean turnUp){
        final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end, turnIn, turnUp);
        rotation.setDuration(800);
        rotation.setFillAfter(false);
        rotation.setInterpolator(new AccelerateInterpolator());
        return rotation;
    }
    static class Rotate3dAnimation extends Animation {
        private final float mFromDegrees;
        private final float mToDegrees;
        private float mCenterX;
        private float mCenterY;
        private final boolean mTurnIn;
        private final boolean mTurnUp;
        private Camera mCamera;
        public Rotate3dAnimation(float fromDegrees, float toDegrees, boolean turnIn, boolean turnUp) {
            mFromDegrees = fromDegrees;
            mToDegrees = toDegrees;
            mTurnIn = turnIn;
            mTurnUp = turnUp;
        }
        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
            mCamera = new Camera();
            mCenterY = width / 2;  //中心
            mCenterX = height / 2;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final float fromDegrees = mFromDegrees;
            float degrees = fromDegrees + ((mToDegrees - fromDegrees) * interpolatedTime);
            final float centerX = mCenterX ;
            final float centerY = mCenterY ;
            final Camera camera = mCamera;
            final int derection = mTurnUp ? 1: -1;
            final Matrix matrix = t.getMatrix();
            camera.save();
            if (mTurnIn) {
                camera.translate(0.0f, derection *mCenterY * (interpolatedTime - 1.0f), 0.0f);
            } else {
                camera.translate(0.0f, derection *mCenterY * (interpolatedTime), 0.0f);
            }
            camera.rotateX(degrees);
            camera.getMatrix(matrix);
            camera.restore();
            //矩阵前乘和后乘，作用确定操作的中心点
            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
        }
    }
}
