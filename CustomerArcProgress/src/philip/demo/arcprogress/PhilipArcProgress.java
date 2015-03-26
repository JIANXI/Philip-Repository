package philip.demo.arcprogress;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PhilipArcProgress extends View {
	
    private Paint painMain;
    private Paint paintBg;
    private RectF rectF = new RectF();

    private int strokeColorMain;
    private int strokeColorBg;
    private float strokeWidthMain;
    private float strokeWidthBg;
    private int max;
    private float arcAngle;
    private int progress;

    private final int default_color_main = Color.BLUE;
    private final int default_color_bg = Color.GRAY;
    private final float default_stroke_width_main;
    private final float default_stroke_width_bg;
    private final int default_max = 100;
    private final float default_arc_angle = 360 * 0.75f;
    private final int default_progress = 0;

    public PhilipArcProgress(Context context) {
        this(context, null);
    }

    public PhilipArcProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhilipArcProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        default_stroke_width_main = dp2px(getResources(), 10);
        default_stroke_width_bg = dp2px(getResources(), 30);

        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcProgress, defStyleAttr, 0);
        initByAttributes(attributes);
        attributes.recycle();

        initPainters();
    }

    protected void initByAttributes(TypedArray attributes) {
        strokeColorMain = attributes.getColor(R.styleable.ArcProgress_arc_color_main, default_color_main);
        strokeColorBg = attributes.getColor(R.styleable.ArcProgress_arc_color_bg, default_color_bg);
        strokeWidthMain = attributes.getDimension(R.styleable.ArcProgress_arc_stroke_width_main, default_stroke_width_main);
        strokeWidthBg = attributes.getDimension(R.styleable.ArcProgress_arc_stroke_width_bg, default_stroke_width_bg);
        setMax(attributes.getInt(R.styleable.ArcProgress_arc_max, default_max));
        arcAngle = attributes.getDimension(R.styleable.ArcProgress_arc_angle, default_arc_angle);
        setProgress(attributes.getInt(R.styleable.ArcProgress_arc_progress, default_progress));     
    }

    protected void initPainters() {
        painMain = new Paint();
        painMain.setAntiAlias(false);
        painMain.setColor(strokeColorMain);
        painMain.setStrokeWidth(strokeWidthMain);
        painMain.setStyle(Paint.Style.STROKE);
        painMain.setStrokeCap(Paint.Cap.ROUND);
        
        paintBg = new Paint();
        paintBg.setAntiAlias(false);
        paintBg.setColor(strokeColorBg);
        paintBg.setStrokeWidth(strokeWidthBg);
        paintBg.setStyle(Paint.Style.STROKE);
        paintBg.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public void invalidate() {
        initPainters();
        super.invalidate();
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        if (this.progress > max) {
            this.progress = max;
        }
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	float width = strokeWidthBg > strokeWidthMain ? strokeWidthBg : strokeWidthMain;
		rectF.set(width / 2, width / 2
	    		, MeasureSpec.getSize(widthMeasureSpec) - width / 2
	    		, MeasureSpec.getSize(heightMeasureSpec) - width / 2);
        
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float startAngle = 270 - arcAngle / 2f;
        float sweepAngleMain = progress / (float) getMax() * arcAngle;
        canvas.drawArc(rectF, startAngle , arcAngle , false, paintBg);
        canvas.drawArc(rectF, startAngle, sweepAngleMain, false, painMain);
    }
    
    public float getStrokeWidthMain() {
        return strokeWidthMain;
    }

    public void setStrokeWidthMain(float strokeWidthMain) {
        this.strokeWidthMain = strokeWidthMain;
        this.invalidate();
    }
    
    public float getStrokeWidthBg() {
        return strokeWidthBg;
    }

    public void setStrokeWidthBg(float strokeWidthBg) {
        this.strokeWidthBg = strokeWidthBg;
        this.invalidate();
    }
    
    public int getStrokeColorMain() {
        return strokeColorMain;
    }

    public void setStrokeColorMain(int strokeColorMain) {
        this.strokeColorMain = strokeColorMain;
        this.invalidate();
    }
    
    public int getStrokeColorBg() {
        return strokeColorBg;
    }
    
    public void setStrokeColorBg(int strokeColorBg) {
    	this.strokeColorBg = strokeColorBg;
        this.invalidate();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        if (max > 0) {
            this.max = max;
            invalidate();
        }
    }

    public float getArcAngle() {
        return arcAngle;
    }

    public void setArcAngle(float arcAngle) {
        this.arcAngle = arcAngle;
        this.invalidate();
    }
    
    public static float dp2px(Resources resources, float dp) {
        final float scale = resources.getDisplayMetrics().density;
        return  dp * scale + 0.5f;
    }

    public static float sp2px(Resources resources, float sp){
        final float scale = resources.getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
