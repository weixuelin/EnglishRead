package jp.wasabeef.glide.transformations.internal;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;


public final class Utils {
  private Utils() { }

  public static Drawable getMaskDrawable(Context context, int maskId) {
    Drawable drawable;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      drawable = context.getDrawable(maskId);
    } else {
      drawable = context.getResources().getDrawable(maskId);
    }

    if (drawable == null) {
      throw new IllegalArgumentException("maskId is invalid");
    }

    return drawable;
  }
}
