package com.fischer.tom.reddiator;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.v4.app.ListFragment;
import android.support.v4.util.LruCache;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import com.fischer.tom.reddiator.content.DBAdapter;
import com.fischer.tom.reddiator.content.Database;
import com.fischer.tom.reddiator.content.Post;
import com.fischer.tom.reddiator.content.PostAdapter;
import com.fischer.tom.reddiator.content.Posts;


/**
 * A list fragment representing a list of Items. This fragment
 * also supports tablet devices by allowing list items to be given an
 * 'activated' state upon selection. This helps indicate which item is
 * currently being viewed in a {@link ItemDetailFragment}.
 * <p/>
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class ItemListFragment extends ListFragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    public Posts mPosts;
    private ListView mListView;
    private int mLastPostIndex = -1;
    private boolean mListShown;
    private View mProgressContainer;
    private View mListContainer;
    private View mView;
    private String mSubreddit = "Front Page";
    private DBAdapter dbAdapter;
    private PostAdapter mPostAdapter;

    /**
     * The serialization (saved instance state) Bundle key representing the
     * activated item position. Only used on tablets.
     */
    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    /**
     * The fragment's current callback object, which is notified of list item
     * clicks.
     */
    private Callbacks mCallbacks = sPostCallbacks;

    /**
     * The current activated item position. Only used on tablets.
     */
    private int mActivatedPosition = ListView.INVALID_POSITION;

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(String id);
    }

    /**
     * A dummy implementation of the {@link Callbacks} interface that does
     * nothing. Used only when this fragment is not attached to an activity.
     */
    private static Callbacks sPostCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(String id) {
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.mSubreddit = this.getArguments().getString("subreddit");

        dbAdapter = Database.getInstance(getActivity());
        this.mPostAdapter = new PostAdapter(getActivity(), R.layout.posts, new ArrayList<Post>());

        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                setListAdapter(mPostAdapter);

                return null;
            }
        }.execute();

        this.mView = inflater.inflate(R.layout.posts_content, container, false);
        (this.mView.findViewById(R.id.internalEmpty)).setId(0x00ff0001);
        mListView = (ListView) this.mView.findViewById(android.R.id.list);
        mListContainer =  this.mView.findViewById(R.id.listContainer);
        mProgressContainer = this.mView.findViewById(R.id.progressContainer);
        mListShown = true;

        this.mSwipeRefreshLayout = (SwipeRefreshLayout) this.mView.findViewById(R.id.swipeRefreshLayout);
        this.mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLastPostIndex = -1;
                mPosts.clearPosts();
                new GetPostsOperation(true).execute(mSubreddit);
            }
        });

        //this.mListView = (ListView) this.mView.findViewById(android.R.id.list);
        this.mListView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                mLastPostIndex = mListView.getFirstVisiblePosition();
                new GetPostsOperation(true).execute(mSubreddit, "more");
            }
        });

        new GetPostsOperation(false).execute(mSubreddit);

        return this.mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Restore the previously serialized activated item position.
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState.getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Activities containing this fragment must implement its callbacks.
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }

        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Reset the active callbacks interface to the dummy implementation.
        mCallbacks = sPostCallbacks;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        Post post = Posts.DATA.get(position);
        dbAdapter.insertRow(post.getPermalink());
        mPostAdapter.notifyDataSetChanged();
        mCallbacks.onItemSelected(position + "");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mActivatedPosition != ListView.INVALID_POSITION) {
            // Serialize and persist the activated item position.
            outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        getListView().setChoiceMode(activateOnItemClick
                ? ListView.CHOICE_MODE_SINGLE
                : ListView.CHOICE_MODE_NONE);
    }

    private void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            getListView().setItemChecked(mActivatedPosition, false);
        } else {
            getListView().setItemChecked(position, true);
        }

        mActivatedPosition = position;
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

    public class GetPostsOperation extends AsyncTask<String, Void, ArrayList<Post>> {
        public GetPostsOperation(boolean showLoading) {
            super();
            setListShown(showLoading);
        }

        @Override
        protected ArrayList<Post> doInBackground(String... params) {
            if (mPosts == null) {
                mPosts = new Posts(params[0]);
            }

            if (params.length > 1) {
                return mPosts.fetchMorePosts();
            } else {
                return mPosts.fetchPosts();
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Post> result) {
            mPostAdapter.updateList(result);
            mPostAdapter.notifyDataSetChanged();
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
