package com.coahr.cvfan.view;


import com.coahr.cvfan.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;


public class SlipSwitch extends View implements OnTouchListener
{
    private OnSwitchListener mOnCheckedChangeListener;

    private boolean mChecked;

    public boolean flag = true;
    public boolean NowChoose = false;
    private boolean OnSlip = false;
    public float DownX = 0f, NowX = 0f;
    private Rect Btn_On, Btn_Off;

    private Bitmap bg_on, bg_off, slip_btn;

    public interface OnSwitchListener
    {
        public void onSwitched(SlipSwitch MySlipSwitchView, boolean isChecked);
    }

    public SlipSwitch(Context context)
    {
        this(context, null);
    }

    public SlipSwitch(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public SlipSwitch(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        // init desity dpi
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metric);
        setSwitchState(false);
        init();
    }

    private void init()
    {
        bg_on = BitmapFactory.decodeResource(getResources(), R.drawable.switch_on_bg);
        bg_off = BitmapFactory.decodeResource(getResources(), R.drawable.switch_off_bg);
        slip_btn = BitmapFactory.decodeResource(getResources(), R.drawable.slid_bt);
        Btn_On = new Rect(0, 0, slip_btn.getWidth(), slip_btn.getHeight());
        Btn_Off = new Rect(bg_off.getWidth() - slip_btn.getWidth(), 0, bg_off.getWidth(), slip_btn.getHeight());
        setOnTouchListener(this);
    }

    public void setSwitchState(boolean checked)
    {
        mChecked = checked;
        if (mChecked)
        {
            flag = true;
            NowChoose = true;
            NowX = bg_on.getWidth();
        }
        else
        {
            flag = false;
            NowChoose = false;
            NowX = 0;
        }
        postInvalidate();
    }

    public boolean getSwitchState()
    {
        return mChecked;
    }

    Matrix matrix = new Matrix();
    Paint paint = new Paint();
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        float x;
        {
            if (flag)
            {
                NowX = bg_on.getWidth();
                flag = false;
            }
            if (NowX < (bg_on.getWidth() / 2))
            {
                canvas.drawBitmap(bg_off, matrix, paint);
            }
            else
            {
                canvas.drawBitmap(bg_on, matrix, paint);
            }

            if (OnSlip)
            {
                if (NowX >= bg_on.getWidth())
                {
                    x = bg_on.getWidth() - slip_btn.getWidth() / 2;
                }
                else
                {
                    x = NowX - slip_btn.getWidth() / 2;
                }
            }
            else
            {
                if (NowChoose)
                {
                    x = Btn_Off.left;
                }
                else
                {
                    x = Btn_On.left;
                }
            }
            if (x < 0)
            {
                x = 0;
            }
            else if (x > bg_on.getWidth() - slip_btn.getWidth())
            {
                x = bg_on.getWidth() - slip_btn.getWidth();
            }
            canvas.drawBitmap(slip_btn, x, 0, paint);
        }
    }

    public boolean onTouch(View v, MotionEvent event)
    {
        System.out.println("**************************" + event.getAction());
        switch (event.getAction())
        {
            
            case MotionEvent.ACTION_MOVE:
                NowX = event.getX();
                break;
            case MotionEvent.ACTION_DOWN:
                if (event.getX() > bg_on.getWidth() || event.getY() > bg_on.getHeight())
                {
                    return false;
                }
                OnSlip = true;
                DownX = event.getX();
                NowX = DownX;
                break;
            case MotionEvent.ACTION_UP:
                OnSlip = false;
                boolean LastChoose = NowChoose;
                if (event.getX() >= (bg_on.getWidth() / 2))
                {
                    NowChoose = true;
                }
                else
                {
                    NowChoose = false;
                }
                setSwitchState(!mChecked);
                if (LastChoose != NowChoose)
                {
                    if (mOnCheckedChangeListener != null)
                    {
                        mOnCheckedChangeListener.onSwitched(this, getSwitchState());
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                OnSlip = false;
                boolean LastChoose_cancel = NowChoose;
                if (event.getX() >= (bg_on.getWidth() / 2))
                {
                    NowChoose = true;
                }
                else
                {
                    NowChoose = false;
                }
                setSwitchState(!mChecked);
                if (LastChoose_cancel != NowChoose)
                {
                    if (mOnCheckedChangeListener != null)
                    {
                        mOnCheckedChangeListener.onSwitched(this, getSwitchState());
                    }
                }
                break;
            default:
                break;

        }
        invalidate();
        return true;
    }

    public void setOnSwitchListener(OnSwitchListener listener)
    {
        mOnCheckedChangeListener = listener;
    }

}
