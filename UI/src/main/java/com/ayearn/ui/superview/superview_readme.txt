<!--SuperTextView-->
  <declare-styleable name="SuperTextView">
    <attr name="corner" format="dimension"/>
    <attr name="left_top_corner" format="boolean"/>
    <attr name="right_top_corner" format="boolean"/>
    <attr name="left_bottom_corner" format="boolean"/>
    <attr name="right_bottom_corner" format="boolean"/>
    <attr name="solid" format="reference|color"/>
    <attr name="stroke_width" format="dimension"/>
    <attr name="stroke_color" format="reference|color"/>
    <attr name="state_drawable" format="reference"/>
    <attr name="state_drawable_width" format="dimension"/>
    <attr name="state_drawable_height" format="dimension"/>
    <attr name="state_drawable_padding_left" format="dimension"/>
    <attr name="state_drawable_padding_top" format="dimension"/>
    <attr name="state_drawable2" format="reference"/>
    <attr name="state_drawable2_width" format="dimension"/>
    <attr name="state_drawable2_height" format="dimension"/>
    <attr name="state_drawable2_padding_left" format="dimension"/>
    <attr name="state_drawable2_padding_top" format="dimension"/>
    <attr name="isShowState" format="boolean"/>
    <attr name="drawableAsBackground" format="boolean"/>
    <attr name="isShowState2" format="boolean"/>
    <attr name="text_stroke" format="boolean"/>
    <attr name="text_stroke_color" format="reference|color"/>
    <attr name="text_stroke_width" format="dimension"/>
    <attr name="text_fill_color" format="reference|color"/>
    <attr name="autoAdjust" format="boolean"/>
    <attr name="state_drawable_mode" format="enum">
      <enum name="left" value="0"/>
      <enum name="top" value="1"/>
      <enum name="right" value="2"/>
      <enum name="bottom" value="3"/>
      <enum name="center" value="4"/>
      <enum name="fill" value="5"/>
      <enum name="leftTop" value="6"/>
      <enum name="rightTop" value="7"/>
      <enum name="leftBottom" value="8"/>
      <enum name="rightBottom" value="9"/>
    </attr>
    <attr name="state_drawable2_mode" format="enum">
      <enum name="left" value="0"/>
      <enum name="top" value="1"/>
      <enum name="right" value="2"/>
      <enum name="bottom" value="3"/>
      <enum name="center" value="4"/>
      <enum name="fill" value="5"/>
      <enum name="leftTop" value="6"/>
      <enum name="rightTop" value="7"/>
      <enum name="leftBottom" value="8"/>
      <enum name="rightBottom" value="9"/>
    </attr>
    <attr name="shaderStartColor" format="reference|color"/>
    <attr name="shaderEndColor" format="reference|color"/>
    <attr name="shaderMode" format="enum">
      <enum name="topToBottom" value="0"/>
      <enum name="bottomToTop" value="1"/>
      <enum name="leftToRight" value="2"/>
      <enum name="rightToLeft" value="3"/>
    </attr>
    <attr name="shaderEnable" format="boolean"/>
    <attr name="pressBgColor" format="reference|color"/>
    <attr name="pressTextColor" format="reference|color"/>
  </declare-styleable>

<com.coorchice.library.SuperTextView
            android:id="@+id/btn_6"
            android:layout_width="100dp"
            android:layout_height="100dp"
            android:layout_marginTop="20dp"
            android:paddingBottom="5dp"
            //文字设置
            android:text="Drawable2"
            //文字的位置
            android:gravity="bottom|center_horizontal"
            //文字的颜色
            android:textColor="#ED86AE"
            android:layout_alignParentLeft="true"
            android:layout_marginLeft="55dp"
            //设置圆角
            app:corner="50dp"
            //设置圆角出现的位置
            app:left_top_corner="true"
            app:right_top_corner="true"
            //设置边框值
            app:solid="@color/white"
            //设置状态图1的资源
            app:state_drawable="@drawable/avatar6"
            //设置状态图1的宽高
            app:state_drawable_height="20dp"
            app:state_drawable_width="20dp"
            //设置转态图1的位置
            app:state_drawable_mode="left"
            //设置状态图1的边距
            app:state_drawable_padding_left="10dp"
            //设置状态图1为背景
            app:drawableAsBackground="true"
            //设置状态图2
            app:isShowState2="true"
            //设置状态图2的资源
            app:state_drawable2="@drawable/recousers"
            //设置状态图2的位置
            app:state_drawable2_mode="rightTop"
            //设置状态图2的宽高
            app:state_drawable2_height="20dp"
            app:state_drawable2_width="20dp"
            //设置按压变色，有背景图的时候不显示可调整PressAdjust的Opportunity.BEFORE_DRAWABLE为Opportunity.BEFORE_TEXT
            app:pressBgColor="@color/black"
            app:pressTextColor="@color/white"
/>