package org.gnuton.newshub.view;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.gnuton.newshub.R;
import org.gnuton.newshub.utils.FontsProvider;

import java.io.IOException;

/**
 * Created by gnuton on 11/19/13.
 */
public class MediaPlayerView extends LinearLayout {
    private final String TAG = MediaPlayerView.class.getName();
    final LayoutInflater mLayoutInflate;
    private View mView;

    // Let's keep media source and media player singleton
    static private String mSourceUrl;
    static private Object mMediaPlayer;
    static private Boolean mSessionPlayPauseButtonEnabled;
    static private int mSessionPlayPauseButtonText;

    private Button mPlayPauseButton;
    private TextView mInfoLabel;

    public MediaPlayerView(Context context) {
        super(context);
        mLayoutInflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initialize();
    }

    public MediaPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLayoutInflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initialize();
    }
    private void initialize(){
        if (mLayoutInflate != null) {
            // Inflate into this mView
            mView = this.mLayoutInflate.inflate(R.layout.mediaplayer, this, true);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            this.setLayoutParams(lp);

            mInfoLabel = (TextView) findViewById(R.id.mediaPlayerTrackInfo);
            mPlayPauseButton = (Button)findViewById(R.id.playPauseButton);

            if (mPlayPauseButton != null && ! isInEditMode()){
                // Let's reuse the same mediaplayer object
                if (mMediaPlayer == null){
                    mMediaPlayer = new MediaPlayer();
                }
                mPlayPauseButton.setOnClickListener(new playPauseButtonClickListener());
                mPlayPauseButton.setTypeface(FontsProvider.getInstace().getTypeface("fontawesome-webfont"));
            }
        }
    }

    public void setMedia(final String url){

        MediaPlayer mp = (MediaPlayer) mMediaPlayer;
        if (mp == null)
            return;

        // Do not do anything if the mediasource is the same.
        if (mSourceUrl != null && mSourceUrl.equals(url)){
            // we need to set the previous playpause button status
            // because the actual button view has been recreated during device orientat changes
            setPlayPauseButtonStatus();
            return;
        }

        mSourceUrl = url;
        mp.stop();

        mPlayPauseButton.setText(R.string.icon_play);

        if (url == null){
            return;
        }

        Log.d(TAG, "Setting Media" + url);
        try {
            mp.reset();
            mp.setDataSource(url);
            mp.prepare();
            mInfoLabel.setText("");
            mSessionPlayPauseButtonEnabled = true;
            mSessionPlayPauseButtonText = R.string.icon_play;
        } catch (IOException e) {
            e.printStackTrace();
            mInfoLabel.setText(R.string.UnableToPlayMedia);
            mSessionPlayPauseButtonEnabled = false;
            mSessionPlayPauseButtonText = R.string.icon_bug;
        }
        setPlayPauseButtonStatus();
    }

    private void setPlayPauseButtonStatus(){
        mPlayPauseButton.setEnabled(mSessionPlayPauseButtonEnabled);
        mPlayPauseButton.setText(mSessionPlayPauseButtonText);
    }

    private class playPauseButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View view) {
            MediaPlayer mp = (MediaPlayer) mMediaPlayer;
            if (mp.isPlaying()){
                mp.pause();
                mSessionPlayPauseButtonText =R.string.icon_play;
            } else {
                mp.start();
                mSessionPlayPauseButtonText =R.string.icon_pause;
            }
            setPlayPauseButtonStatus();
        }
    }

}
