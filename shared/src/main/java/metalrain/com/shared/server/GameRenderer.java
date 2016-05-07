package metalrain.com.shared.server;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import metalrain.com.shared.R;
import metalrain.com.shared.state.Player;
import metalrain.com.shared.state.GameState;
import metalrain.com.shared.state.Vector2;

/**
 * Created by Adam on 4/21/2016.
 */
public class GameRenderer extends View {
    ScheduledExecutorService executorService;
    Bitmap mPlayerBitmap;
    long lastSampledTime = System.currentTimeMillis();
    private ScheduledFuture<?> future;

    private GameState state;

    public void setState(GameState state) {
        this.state = state;
    }

    public GameRenderer(Context context) {
        super(context);
        init();
    }

    public GameRenderer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPlayerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.player);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        executorService = Executors.newSingleThreadScheduledExecutor();
        future = executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                runSimulation();
                postInvalidate();
            }
        },0,(int)(1/45f*1000), TimeUnit.MILLISECONDS);
    }

    private void runSimulation() {
        long deltaTime = System.currentTimeMillis() - lastSampledTime;
        double deltaTimeDouble = deltaTime/1000.0;
        state.step(deltaTimeDouble,state);

        lastSampledTime = System.currentTimeMillis();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (future != null) future.cancel(false);
        executorService.shutdown();

    }


    Paint p = new Paint();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        try {
            state.draw(canvas);

            Vector2 collisionPoint = state.getCollisionPoint();
            if (collisionPoint != null) {
                p.setColor(Color.GREEN);
                double position_x = (collisionPoint.getX() / GameState.BOARD_WIDTH)*canvas.getWidth();
                double position_y = (collisionPoint.getY() / GameState.BOARD_HEIGHT)*canvas.getHeight();
                canvas.drawCircle((float)position_x, (float)position_y, 10, p);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
