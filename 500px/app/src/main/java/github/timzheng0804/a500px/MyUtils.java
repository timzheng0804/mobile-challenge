package github.timzheng0804.a500px;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Tim on 29/09/2017.
 */

public class MyUtils {

    public static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
}
