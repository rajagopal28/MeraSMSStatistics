package app.android.mera.sms.statistics.chart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class MyGraphview extends View
{
    private Paint mBagPaints=new Paint();
	private Paint mLinePaints = new Paint();
    private float[] arcValueDegree;
    private int[] COLORS={Color.rgb(204, 177, 255),Color.rgb(163, 236, 218),Color.rgb(153, 178, 255),Color.rgb(255, 141, 198)};
    RectF fillingRectangle = new RectF (50, 50, 400, 400);
    int startPoint=0;
    public MyGraphview(Context context, float[] values) {

        super(context);
        arcValueDegree=new float[values.length];
        for(int i=0;i<values.length;i++)
        {
            arcValueDegree[i]=values[i];
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        startPoint = 0;
        for (int i = 0; i < arcValueDegree.length; i++) {
        	mBagPaints.setAntiAlias(true);
    		mBagPaints.setStyle(Paint.Style.FILL);
    		mBagPaints.setColor(COLORS[i]);
    		mBagPaints.setStrokeWidth(0.0f);    		

			mLinePaints.setAntiAlias(true);
    		mLinePaints.setColor(COLORS[i]);
    		mLinePaints.setStrokeWidth(1.0f);
    		mLinePaints.setStyle(Paint.Style.STROKE);          
          
            canvas.drawArc(fillingRectangle, startPoint, arcValueDegree[i], true, mBagPaints);
            canvas.drawArc(fillingRectangle, startPoint, arcValueDegree[i], true, mLinePaints);
            startPoint += (int) arcValueDegree[i];
        }
    }

}
