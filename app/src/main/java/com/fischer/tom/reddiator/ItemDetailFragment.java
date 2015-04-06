package com.fischer.tom.reddiator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fischer.tom.reddiator.content.Post;
import com.fischer.tom.reddiator.content.Posts;


import com.fischer.tom.reddiator.dummy.DummyContent;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Post mPost;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.

            mPost = Posts.DATA.get(Integer.parseInt(getArguments().getString(ARG_ITEM_ID)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mPost != null) {
            String score = mPost.getPoints() > 9999 ?
                    Double.toString((double)Math.round(mPost.getPoints() / 100) / 10) + "K" :
                    Integer.toString(mPost.getPoints());

            ((TextView) rootView.findViewById(R.id.scoreTextView)).setText(score);

            ((TextView) rootView.findViewById(R.id.postTitleTextView)).setText(mPost.getTitle());
            ((TextView) rootView.findViewById(R.id.subredditTextView)).setText("r/" + mPost.getSubreddit());

            String numComments = mPost.getNumComments() > 999 ?
                    Double.toString((double)Math.round(mPost.getNumComments() / 100) / 10) + "K" :
                    Integer.toString(mPost.getNumComments());

            ((TextView) rootView.findViewById(R.id.numCommentsTextView)).setText(numComments + " comments");
            ((TextView) rootView.findViewById(R.id.usernameTextView)).setText("by " + mPost.getAuthor());
            ((TextView) rootView.findViewById(R.id.timestampTextView)).setText(mPost.calculateTimestamp(System.currentTimeMillis() / 1000L));

            try {
                new ImageLoader(new URI(mPost.getThumbnail()), ((ImageView) rootView.findViewById(R.id.thumbnailImageView)), 50, 38).execute();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        return rootView;
    }
}
