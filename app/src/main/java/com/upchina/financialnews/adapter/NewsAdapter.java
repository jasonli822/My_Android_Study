package com.upchina.financialnews.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.upchina.financialnews.R;
import com.upchina.financialnews.bean.News;
import com.upchina.financialnews.support.Check;
import com.upchina.financialnews.support.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.CardViewHolder> {

    private List<News> newsList;
    // universal-image-loader 图片异步加载类库
    private ImageLoader imageLoader = ImageLoader.getInstance();
    // 配置图片显示的选型
    private DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.noimage)
            .showImageOnFail(R.drawable.noimage)
            .showImageForEmptyUri(R.drawable.noimage)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .considerExifParams(true)
            .build();
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public NewsAdapter(List<News> newsList) {
        this.newsList = newsList;

        setHasStableIds(true);
    }

    public void addNewsListAndNofity(List<News> newsList) {
        if (this.newsList == null) {
            this.newsList = new ArrayList<News>();
        }
        this.newsList.addAll(newsList);
        notifyDataSetChanged();
    }

    public void updateNewsListAndNotify(List<News> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();

        View itemView = LayoutInflater.from(context).inflate(R.layout.news_list_item, parent, false);


        return new CardViewHolder(itemView, new CardViewHolder.ClickResponseListener() {

            @Override
            public void onWholeClick(int position) {
                browseOrShare(context, position, true);
            }

            @Override
            public void onOverflowClick(View v, final int position) {
                PopupMenu popupMenu = new PopupMenu(context, v);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.contextual_news_list, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_share_url:
                                browseOrShare(context, position, false);
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        final News news = newsList.get(position);
        String thumbnailUrl = Constants.Url.TOPIC_RECOMMEND_UPLOAD_IMAGE + news.getThumbnail();
        imageLoader.displayImage(thumbnailUrl, holder.thumbnailImage, displayImageOptions, animateFirstListener);


        holder.newsSubjectTextView.setText(news.getSubject());
        holder.newsIntroTextView.setText(news.getIntro());
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @Override
    public long getItemId(int position) {
        return newsList.get(position).hashCode();
    }

    private void browseOrShare(final Context context, int position, final boolean browse) {
        final News news = newsList.get(position);
        String newsDetailUrl = Constants.Url.TOPIC_RECOMMEND_DETAIL + news.getId() + Constants.Url.TOPIC_SUFFIX;
        if (browse) {
            showNewsDetail(context, newsDetailUrl);
        } else {
            String subject = news.getSubject();
            share(context, subject, newsDetailUrl);
        }
    }

    private void showNewsDetail(Context context, String url) {
        openUsingBrowser(context, url);
    }

    private void openUsingBrowser(Context context, String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        if (Check.isIntentSafe(browserIntent)) {
            context.startActivity(browserIntent);
        } else {
            Toast.makeText(context, context.getString(R.string.no_browser), Toast.LENGTH_SHORT).show();
        }
    }

    private void share(Context context, String subject, String newsUrl) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        //noinspection deprecation
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.putExtra(Intent.EXTRA_TEXT, subject + " " + newsUrl + " 分享来自优品财经网");
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_to)));
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView thumbnailImage;
        private TextView newsSubjectTextView;
        private TextView newsIntroTextView;
        private ImageView overflowImage;

        private ClickResponseListener mClickResponseListener;

        public CardViewHolder(View itemView, ClickResponseListener clickResponseListener) {
            super(itemView);

            this.mClickResponseListener = clickResponseListener;

            thumbnailImage = (ImageView) itemView.findViewById(R.id.thumbnail_image);
            newsSubjectTextView = (TextView) itemView.findViewById(R.id.news_subject);
            newsIntroTextView = (TextView) itemView.findViewById(R.id.news_intro);
            overflowImage = (ImageView) itemView.findViewById(R.id.card_share_overflow);

            itemView.setOnClickListener(this);
            overflowImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == overflowImage) {
                mClickResponseListener.onOverflowClick(v, getAdapterPosition());
            } else {
                mClickResponseListener.onWholeClick(getAdapterPosition());
            }
        }

        public interface ClickResponseListener {
            void onWholeClick(int position);

            void onOverflowClick(View v, int position);
        }
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayImages.add(imageUri);
                }
            }
        }
    }
}
