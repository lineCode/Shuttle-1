package com.simplecity.amp_library.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;

import com.afollestad.aesthetic.Aesthetic;
import com.simplecity.amp_library.R;
import com.simplecity.amp_library.playback.MusicService;

import io.reactivex.disposables.Disposable;

public class ShuffleButton extends android.support.v7.widget.AppCompatImageButton {

    @MusicService.ShuffleMode
    private int shuffleMode;

    private Disposable aestheticDisposable;

    int selectedColor = Color.WHITE;

    @NonNull Drawable shuffleOff;
    @NonNull Drawable shuffleTracks;
    @NonNull Drawable shuffleAlbums;

    public ShuffleButton(Context context) {
        this(context, null);
    }

    public ShuffleButton(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShuffleButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        shuffleOff = ContextCompat.getDrawable(context, R.drawable.ic_shuffle_24dp_scaled);
        shuffleTracks = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ic_shuffle_24dp_scaled).mutate());
        shuffleAlbums = DrawableCompat.wrap(ContextCompat.getDrawable(context, R.drawable.ic_shuffle_albums_scaled_24dp).mutate());

        setShuffleMode(MusicService.ShuffleMode.OFF);
    }

    public void setShuffleMode(@MusicService.ShuffleMode int shuffleMode) {
        if (this.shuffleMode != shuffleMode) {
            this.shuffleMode = shuffleMode;

            invalidateColors(selectedColor);

            switch (shuffleMode) {
                case MusicService.ShuffleMode.OFF:
                    setContentDescription(getResources().getString(R.string.btn_shuffle_off));
                    setImageDrawable(shuffleOff);
                    break;
                case MusicService.ShuffleMode.TRACKS:
                    setContentDescription(getResources().getString(R.string.btn_shuffle_tracks));
                    setImageDrawable(shuffleTracks);
                    break;
                case MusicService.ShuffleMode.ALBUMS:
                    setContentDescription(getResources().getString(R.string.btn_shuffle_albums));
                    setImageDrawable(shuffleAlbums);
                    break;
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (isInEditMode()) {
            return;
        }

        aestheticDisposable = Aesthetic.get(getContext()).colorAccent()
                .subscribe(colorAccent -> {
                    selectedColor = colorAccent;
                    invalidateColors(selectedColor);
                });
    }

    @Override
    protected void onDetachedFromWindow() {
        aestheticDisposable.dispose();
        super.onDetachedFromWindow();
    }

    private void invalidateColors(int selectedColor) {
        DrawableCompat.setTint(shuffleTracks, selectedColor);
        DrawableCompat.setTint(shuffleAlbums, selectedColor);
    }
}
