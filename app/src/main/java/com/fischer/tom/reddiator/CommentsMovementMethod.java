package com.fischer.tom.reddiator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Layout;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import com.fischer.tom.reddiator.content.Subreddits;

/**
 * Created by Tom on 2015-04-12.
 */
public class CommentsMovementMethod extends LinkMovementMethod {
    private static Context movementContext;
    private static CommentsMovementMethod linkMovementMethod = new CommentsMovementMethod();

    public boolean onTouchEvent(android.widget.TextView widget, android.text.Spannable buffer, android.view.MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);

            if (link.length != 0) {
                String url = link[0].getURL();

                if (url.startsWith("http://www.reddit.com/r/") || url.startsWith("/r/")) {
                    if (url.contains("comments")) {
                        Log.d("Comment Link", url);
                        Toast.makeText(movementContext, "Comment link was pressed", Toast.LENGTH_LONG).show();
                    } else {
                        Log.d("Subreddit Link", url);
                        //Toast.makeText(movementContext, "Subreddit link was pressed", Toast.LENGTH_LONG).show();

                        String subredditURL = url.substring(url.indexOf("/r/") + 3);

                        if (subredditURL.contains("/")) {
                            subredditURL = subredditURL.substring(0, subredditURL.indexOf("/") - 1);
                        }

                        movementContext.startActivity(new Intent(movementContext, MainActivity.class).putExtra("subreddit", subredditURL));
                        //movementContext.
                    }
                } else if (url.startsWith("http://www.reddit.com")) {
                    Log.d("Front Page Link", url);
                    Toast.makeText(movementContext, "Front Page link was clicked", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("Random Link", url);
                    Toast.makeText(movementContext, "Some other link was clicked", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }

    public static MovementMethod getInstance(Context c)
    {
        movementContext = c;
        return linkMovementMethod;
    }

}
