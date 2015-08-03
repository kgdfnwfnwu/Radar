package com.example.radar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

/** 
* User: randost 
* Date: 2014/4/23 
* 雷达图 
*/  
public class RadarView extends View {
    private int count = 8;
    private float angle = 360/count;    
    private int point_radius = 5;   //画点的半径
    private int regionwidth = 40;   //选择分值小区域宽度
    private int valueRulingCount = 5;      //画等分值线
    private int radius;
    private int centerX;
    private int centerY;
    private String[] titles = {"工作","财富","信用","娱乐","家庭","社交","学历","贡献"};
    
    private Point[] pts;  //维度端点
    private Region[] regions;       //打分点区域
    private float[] regionValues;   //打分点分数
    private Path valuePath;
    private float[] values = {8,6,8,6,6,6,4,5}; //各维度分值
    private int maxValue = 10;
    private Point[] value_pts;  //维度端点    
    private Paint paint;
    private Paint valuePaint;
    
    public float[] getValues() {
        return values;
    }
    
    public void setValues(float[] values) {
 
        this.values = values;
    }
    
    public RadarView(Context context) {
        super(context);
        init();    
    }
    
    public RadarView(Context context, AttributeSet attrs) {   
        super(context, attrs);
        init();
    }   
  
    public RadarView(Context context, AttributeSet attrs, int defStyle) {   
        super(context, attrs, defStyle);   
        init();
    }  
    
    private void init() {
        paint = new Paint();
        valuePaint = new Paint();
        pts = new Point[count];
        value_pts = new Point[count];
        valuePath = new Path();
        for(int i=0; i<count; i++) {
            pts[i] = new Point();
            value_pts[i] = new Point();
        }
        
        regionValues = new float[count*valueRulingCount*2];
        regions = new Region[count*valueRulingCount*2];   
        for(int i=0; i<regions.length; i++) {
            regions[i] = new Region();
        }

    }

  

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius = Math.min(h, w)/2 - 40;
        centerX = w/2;
        centerY = h/2;
        
        for(int i=0; i<count; i++)
        {
            pts[i].x = centerX+(int)(radius*Math.cos(Math.toRadians(angle*i)));
            pts[i].y = centerY-(int)(radius*Math.sin(Math.toRadians(angle*i)));
            
            for(int j=1; j<=valueRulingCount*2; j++)
            {
                int x = centerX + (pts[i].x-centerX)/(valueRulingCount*2)*j;
                int y = centerY + (pts[i].y-centerY)/(valueRulingCount*2)*j;
                regions[i*valueRulingCount*2+j-1].set(x-regionwidth/2, y-regionwidth/2, x+regionwidth/2, y+regionwidth/2);   
                regionValues[i*valueRulingCount*2+j-1] = j;
            }            
        }                
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }
    
   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        
        switch(event.getAction()) 
        {
        case MotionEvent.ACTION_DOWN:
            for(int i = 0; i<regions.length; i++)
            {
                if (regions[i].contains((int)x, (int)y))
                {
                    values[(int)(i/(valueRulingCount*2))] = regionValues[i];
                    break;
                }
            }
            invalidate();
            break;
        case MotionEvent.ACTION_MOVE:

            break;
        case MotionEvent.ACTION_UP:

            break;
        }
        return super.onTouchEvent(event); 
    }*/
   
   
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        return super.onKeyLongPress(keyCode, event);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        /* 设置画布的颜色 */
        canvas.drawColor(Color.WHITE);
        //  canvas.drawColor(0x00ffffff); 透明
        paint.setAntiAlias(true);
        //画边框线
        paint.setColor(Color.GRAY);      
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        for(int i=0; i<count; i++)
        {
            int end = i+1 == count? 0:i+1;

            for(int j=1; j<=valueRulingCount; j++)
            {
                canvas.drawLine(centerX+(pts[i].x-centerX)/5*j, centerY+(pts[i].y-centerY)/5*j, 
                        centerX+(pts[end].x-centerX)/5*j, centerY+(pts[end].y-centerY)/5*j,  paint);
            }
            
            canvas.drawLine(centerX, centerY, pts[i].x, pts[i].y, paint);            
        }
        
        //写文字
        paint.setTextSize(20);
        paint.setColor(Color.BLACK);
        FontMetrics fontMetrics = paint.getFontMetrics();
        float fontHegiht = -fontMetrics.ascent;
        for(int i=0; i<count; i++)
        {
            if ((angle * i == 90.0) || (angle * i == 270.0))
                paint.setTextAlign(Align.CENTER);
            else if ((angle * i < 90) || (angle * i > 270))
                paint.setTextAlign(Align.LEFT);
            else if ((angle * i > 90) || (angle * i < 270))
                paint.setTextAlign(Align.RIGHT);
            
            if (angle * i == 270.0)
                canvas.drawText(titles[i], pts[i].x, pts[i].y+fontHegiht, paint);
            else
                canvas.drawText(titles[i], pts[i].x, pts[i].y, paint);
        }        

        //画方向盘分值区域
        for(int i=0; i<count; i++)
        {
            value_pts[i].x = (int)(centerX + (pts[i].x-centerX) * values[i]/maxValue);
            value_pts[i].y = (int)(centerY + (pts[i].y-centerY) * values[i]/maxValue);
        }
        
        valuePath.reset();
        valuePaint.setAntiAlias(true);
        valuePaint.setColor(Color.RED);  
        valuePaint.setStyle(Paint.Style.FILL_AND_STROKE);        
        for(int i = 0; i< pts.length; i++)
        {
            //给valuePath赋值
            if (i == 0)
                valuePath.moveTo(value_pts[i].x, value_pts[i].y);
            else
                valuePath.lineTo(value_pts[i].x, value_pts[i].y);
            //画取分圆圈
            canvas.drawCircle(value_pts[i].x, value_pts[i].y, point_radius, paint);
        }
        valuePaint.setAlpha(150);     
        canvas.drawPath(valuePath, valuePaint);
    }
}