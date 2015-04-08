package com.fischer.tom.reddiator;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fischer.tom.reddiator.content.Comment;
import com.fischer.tom.reddiator.content.Comments;
import com.fischer.tom.reddiator.content.CommentsAdapter;
import com.fischer.tom.reddiator.content.Post;
import com.fischer.tom.reddiator.content.PostAdapter;
import com.fischer.tom.reddiator.content.Posts;


import com.fischer.tom.reddiator.dummy.DummyContent;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

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
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Post mPost;
    private Comments mComments;
    private CommentsAdapter mCommentAdapter;
    private View mView;
    private ListView mListView;
    private ViewGroup mHeaderView;
    private boolean mListShown;
    private View mProgressContainer;
    private View mListContainer;
    private int mLastPostIndex = -1;

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
        this.mView = inflater.inflate(R.layout.fragment_item_detail, container, false);
        this.mHeaderView = (ViewGroup)inflater.inflate(R.layout.comments_header, null, false);

        this.mListView = (ListView) this.mView.findViewById(R.id.commentsListView);
        this.mListView.addHeaderView(mHeaderView, null, false);

        this.mCommentAdapter = new CommentsAdapter(getActivity(), R.layout.comment, new ArrayList<Comment>());

        mListContainer =  this.mView.findViewById(R.id.listContainer);
        mProgressContainer = this.mView.findViewById(R.id.progressContainer);
        mListShown = true;

        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                mListView.setAdapter(mCommentAdapter);

                return null;
            }
        }.execute();

        // Show the dummy content as text in a TextView.
        if (mPost != null) {
            this.mSwipeRefreshLayout = (SwipeRefreshLayout) this.mView.findViewById(R.id.swipeRefreshLayout);
            this.mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mLastPostIndex = -1;
                    mComments.clearComments();
                    new GetCommentsOperation(true).execute(mPost.getPermalink());
                }
            });

            this.mListView.setOnScrollListener(new EndlessScrollListener() {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                new GetCommentsOperation(true).execute(mPost.getPermalink(), "more");
                }
            });

            String score = mPost.getPoints() > 9999 ?
                    Double.toString((double)Math.round(mPost.getPoints() / 100) / 10) + "K" :
                    Integer.toString(mPost.getPoints());

            ((TextView) this.mHeaderView.findViewById(R.id.scoreTextView)).setText(score);

            ((TextView) this.mHeaderView.findViewById(R.id.postTitleTextView)).setText(mPost.getTitle());
            ((TextView) this.mHeaderView.findViewById(R.id.subredditTextView)).setText("r/" + mPost.getSubreddit());

            String numComments = mPost.getNumComments() > 999 ?
                    Double.toString((double)Math.round(mPost.getNumComments() / 100) / 10) + "K" :
                    Integer.toString(mPost.getNumComments());

            ((TextView) this.mHeaderView.findViewById(R.id.numCommentsTextView)).setText(numComments + " comments");
            ((TextView) this.mHeaderView.findViewById(R.id.usernameTextView)).setText("by " + mPost.getAuthor());
            ((TextView) this.mHeaderView.findViewById(R.id.timestampTextView)).setText(mPost.calculateTimestamp(System.currentTimeMillis() / 1000L));

            try {
                new ImageLoader(new URI(mPost.getThumbnail()), ((ImageView) this.mHeaderView.findViewById(R.id.thumbnailImageView)), 50, 38).execute();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            new GetCommentsOperation(false).execute(mPost.getPermalink());
        }

        return this.mView;
    }

    public void setListShown(boolean shown, boolean animate){
        if (mListShown == shown) {
            return;
        }
        mListShown = shown;
        if (shown) {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
            }
            mProgressContainer.setVisibility(View.GONE);
            mListContainer.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_in));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.fade_out));
            }
            mProgressContainer.setVisibility(View.VISIBLE);
            mListContainer.setVisibility(View.INVISIBLE);
        }
    }
    public void setListShown(boolean shown){
        setListShown(shown, true);
    }
    public void setListShownNoAnimation(boolean shown) {
        setListShown(shown, false);
    }

    public class GetCommentsOperation extends AsyncTask<String, Void, ArrayList<Comment>> {
        public GetCommentsOperation(boolean showLoading) {
            super();
            setListShown(showLoading);
        }

        @Override
        protected ArrayList<Comment> doInBackground(String... params) {
            if (mComments == null) {
                mComments = new Comments(params[0]);
            }

            return mComments.fetchComments();
        }

        @Override
        protected void onPostExecute(ArrayList<Comment> result) {
            mCommentAdapter.updateList(result);
            mCommentAdapter.notifyDataSetChanged();
            //setListAdapter(mPostAdapter);

            RelativeLayout relativeLayout = (RelativeLayout) mView.findViewById(R.id.empty);

            if (result.size() == 0) {
                relativeLayout.setVisibility(View.VISIBLE);
            } else {
                relativeLayout.setVisibility(View.GONE);
            }

            //mListView.setSelectionFromTop(mLastPostIndex, 0);
            mSwipeRefreshLayout.setRefreshing(false);
            setListShown(true);
        }
    }
}
