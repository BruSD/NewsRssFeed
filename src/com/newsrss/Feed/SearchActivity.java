package com.newsrss.Feed;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.fortysevendeg.android.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.android.swipelistview.SwipeListView;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: BruSD
 * Date: 14.05.13
 * Time: 10:36
 * To change this template use File | Settings | File Templates.
 */
public class SearchActivity extends shareToSocial {

    ImageButton searchStart;
    EditText searchQueryHolder;
    SwipeListView searchListResult;
    String searchQueryId;
    String searchToGenerate;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.search_layout);
        searchQueryHolder = (EditText)findViewById(R.id.serchquerybox);
        searchStart = (ImageButton)findViewById(R.id.searchBTN);

        searchListResult =(SwipeListView)findViewById(R.id.searchListView);
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        float otstup = (float) (metrics.widthPixels *0.8) ;
        searchListResult.setSwipeMode(SwipeListView.SWIPE_MODE_RIGHT);
        searchListResult.setOffsetRight(otstup);
        searchListResult.setAnimationTime(500);
        searchListResult.setSwipeOpenOnLongPress(false);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.search_head, searchListResult, false);
        searchListResult.addHeaderView(header, null, false);
        ((TextView)findViewById(R.id.text_to_saved)).setText(R.string.save_this_search_button);

        ImageButton backBTN = (ImageButton)findViewById(R.id.BtnBack);
        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        searchStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchQueryHolder.getText().length() != 0){
                    searchQueryId = searchQueryHolder.getText().toString();
                serchArticle();
                }else {
                    Toast toast = Toast.makeText(SearchActivity.this, "Enter Search Query", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });
        View saveSearch = (View)findViewById(R.id.save_search);

        saveSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((TextView)findViewById(R.id.text_to_saved)).getText().toString() != getString(R.string.saved_search_button)){
                    try {
                        LocalDB.open(getApplicationContext());
                    } catch (SQLException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    ((TextView)findViewById(R.id.text_to_saved)).setText(R.string.saved_search_button);
                    ((TextView)findViewById(R.id.text_to_saved)).setEnabled(false);
                    LocalDB.addSearch(searchQueryHolder.getText().toString());
                    Toast toast = Toast.makeText(SearchActivity.this,"Search Query is Saved " ,Toast.LENGTH_SHORT);
                    toast.show();
                }  else {
                    Toast toast = Toast.makeText(SearchActivity.this,"Search Query already Saved " ,Toast.LENGTH_SHORT);
                    toast.show();
                }
                //:TODO Close DB
            }
        });
        Intent startDetailArticle = getIntent();
        searchQueryId = startDetailArticle.getStringExtra("searchquery");

        if (searchQueryId.length() == 1){
        MyCAdapter adapter = new MyCAdapter(this,
                new ArrayList<Map<String, ?>>(), R.layout.rss_item_layout,
                new String[] { "rssnewstitle", "rssnewsdate"},
                new int [] { R.id.rss_news_title, R.id.rss_news_date});
        adapter.setViewBinder(new CustomViewBinder());
        searchListResult.setAdapter(adapter);
            ((TextView)findViewById(R.id.text_to_saved)).setText(R.string.save_this_search_button);
        }else {
            try {
                LocalDB.open(getApplicationContext());
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            ((TextView)findViewById(R.id.text_to_saved)).setText(R.string.saved_search_button);

            searchQueryHolder.setText(searchQueryId);
            serchArticle();


        }



    }

    public void serchArticle(){

             if(!createArticleList().isEmpty()){
             MyCAdapter adapter = new MyCAdapter(this,
                     createArticleList(), R.layout.rss_item_layout,
                     new String[] { "rssnewstitle", "rssnewsdate"},
                     new int [] { R.id.rss_news_title, R.id.rss_news_date});
             adapter.setViewBinder(new CustomViewBinder());
             searchListResult.setAdapter(adapter);
             searchListResult.setVisibility(View.VISIBLE);

             } else {
                 Toast toast = Toast.makeText(SearchActivity.this, "No Result for this Query", Toast.LENGTH_LONG);
                 toast.show();
                 searchListResult.setVisibility(View.GONE);
             }



    }

    private List<Map<String, ?>> createArticleList() {
        List<Map<String, ?>> items = new ArrayList<Map<String, ?>>();
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
            ArrayList<Article> articleList = DataStorage.startSearch(searchQueryId);

            for (Article article : articleList)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("rssnewstitle", article.getTitle());
                map.put("rssnewsdate", sdf.format(article.getPubDate()));
                if (article.getNewsImage() == null) {
                    //System.out.println("NULL");
                    map.put("rssnewsimage", getResources().getDrawable(R.drawable.default_news_icon));
                }
                else {
                    //System.out.println("NOT NULL");
                    map.put("rssnewsimage", article.getNewsImage());
                }
                items.add(map);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        return items;
    }

    class CustomViewBinder implements SimpleAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Object data,String textRepresentation) {
            if((view instanceof ImageView) & (data instanceof Drawable))
            {
                ImageView iv = (ImageView) view;
                Drawable image = (Drawable) data;
                iv.setImageDrawable(image);
                return true;
            }
            return false;
        }

    }

    public class MyCAdapter extends SimpleAdapter {
        private List<? extends Map<String, ?>> data;
        private Context context;







        public MyCAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
            super(context, data, resource, from, to);
            this.context = context;
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            SimpleDateFormat sdf;
            String dateArticleV ;
            ViewHolder holder;

                    final Article currentArticle = DataStorage.getArticleList().get(position);

                    if (convertView == null) {
                        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = li.inflate(R.layout.rss_item_layout, parent, false);
                        holder = new ViewHolder();
                        holder.favBtn = (ImageButton) convertView.findViewById(R.id.fav_btn);
                        holder.shareBtn = (ImageButton)convertView.findViewById(R.id.share_btn);
                        holder.articleTitle = (TextView)convertView.findViewById(R.id.rss_news_title);
                        holder.articleDate = (TextView)convertView.findViewById(R.id.rss_news_date);


                        convertView.setTag(holder);
                    } else {
                        holder = (ViewHolder) convertView.getTag();
                    }
                    holder.articleTitle.setText(currentArticle.getTitle());
                    sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
                    dateArticleV = sdf.format(currentArticle.getPubDate());
                    holder.articleDate.setText(dateArticleV);

                    searchListResult.setSwipeListViewListener(new BaseSwipeListViewListener() {
                        @Override
                        public void  onClickFrontView (int position){
                            Intent startDetailArticl = new Intent(SearchActivity.this, DetailsArticle.class);
                            startDetailArticl.putExtra("position", position);
                            startActivity(startDetailArticl);
                        }


                    });

                    holder.favBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                LocalDB.open(getApplicationContext());
                            } catch (SQLException e) {
                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                            }
                            Toast toast = Toast.makeText(getApplicationContext(),"Add to Favorit article N " +position ,Toast.LENGTH_SHORT);
                            toast.show();
                            Article currentArticle1 = DataStorage.getArticleList().get(position);

                            LocalDB.addArticle(currentArticle1);
                        }
                    });

                    holder.shareBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog = new Dialog(context);
                            dialog.setContentView(R.layout.share_panel_in_list);
                            dialog.setTitle("Share: " +currentArticle.getTitle());

                            ImageButton faceBookShare = (ImageButton)dialog.findViewById(R.id.facebook_in_listview);
                            faceBookShare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onClickbtnConnectFB(1, position);

                                }
                            });
                            ImageButton twitterShare = (ImageButton)dialog.findViewById(R.id.twitter_in_listview);
                            twitterShare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    onTwitterClick(currentArticle.getTitle()+" "+ currentArticle.getLink().toString());
                                }
                            });
                            ImageButton linkeinShare = (ImageButton)dialog.findViewById(R.id.linkedin_in_listview);
                            linkeinShare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // createServiseToLinkedIn(1, position);

                                }
                            });
                            ImageButton mailShare =  (ImageButton)dialog.findViewById(R.id.mail_in_listview);
                            mailShare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    MailSender.send(SearchActivity.this, currentArticle);
                                }
                            });
                            Button copyURLShare = (Button)dialog.findViewById(R.id.copy_url_list);
                            copyURLShare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int sdk = android.os.Build.VERSION.SDK_INT;
                                    if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                                        android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                        clipboard.setText(currentArticle.getLink().toString());
                                    } else {
                                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText("label",currentArticle.getLink().toString());
                                        clipboard.setPrimaryClip(clip);
                                    }
                                    Toast toast = Toast.makeText(getApplicationContext(),"URL Copied ",Toast.LENGTH_SHORT);
                                    toast.show();

                                }
                            });

                            dialog.show();
                        }
                    }  );



            return convertView;
        }

        public class ViewHolder {
            ImageButton favBtn;
            ImageButton shareBtn;
            TextView articleTitle;
            TextView articleDate;

        }
    }
}
