package com.example.facedetector.Helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

public class RectOverlay extends GraphicOverlay.Graphic{

	private GraphicOverlay graphicOverlay;
	private Rect rect;
	private Paint rectPaint;

	public RectOverlay(GraphicOverlay graphicOverlay , Rect rect) {

		super(graphicOverlay);
		rectPaint = new Paint();
		rectPaint.setColor(Color.RED);
		rectPaint.setStyle(Paint.Style.STROKE);
		rectPaint.setStrokeWidth(4.0f);

		this.graphicOverlay = graphicOverlay;
		this.rect = rect;
		postInvalidate();

	}

	@Override
	public void draw(Canvas canvas) {
		RectF rectF = new RectF(rect);
		rectF.left = translateX(rectF.left);
		rectF.right = translateX(rectF.right);
		rectF.top = translateY(rectF.top);
		rectF.bottom = translateY(rectF.bottom);

		canvas.drawRect(rectF , rectPaint);
	}
}
