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
* �״�ͼ 
*/  
public class RadarView extends View {
    private int count = 8;
    private float angle = 360/count;    
    private int point_radius = 5;   //����İ뾶
    private int regionwidth = 40;   //ѡ���ֵС������
    private int valueRulingCount = 5;      //���ȷ�ֵ��
    private int radius;
    private int centerX;
    private int centerY;
    private String[] titles = {"����","�Ƹ�","����","����","��ͥ","�罻","ѧ��","����"};
    
    private Point[] pts;  //ά�ȶ˵�
    private Region[] regions;       //��ֵ�����
    private float[] regionValues;   //��ֵ����
    private Path valuePath;
    private float[] values = {8,6,8,6,6,6,4,5}; //��ά�ȷ�ֵ
    private int maxValue = 10;
    private Point[] value_pts;  //ά�ȶ˵�    
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
        /* ���û�������ɫ */
        canvas.drawColor(Color.WHITE);
        //  canvas.drawColor(0x00ffffff); ͸��
        paint.setAntiAlias(true);
        //���߿���
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
        
        //д����
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

        //�������̷�ֵ����
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
            //��valuePath��ֵ
            if (i == 0)
                valuePath.moveTo(value_pts[i].x, value_pts[i].y);
            else
                valuePath.lineTo(value_pts[i].x, value_pts[i].y);
            //��ȡ��ԲȦ
            canvas.drawCircle(value_pts[i].x, value_pts[i].y, point_radius, paint);
        }
        valuePaint.setAlpha(150);     
        canvas.drawPath(valuePath, valuePaint);
    }
}