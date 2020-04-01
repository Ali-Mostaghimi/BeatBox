package com.example.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox {
    private static final String TAG = "BeatBox";
    private static final String SOUND_FOLDER = "sample_sounds";
    private static final int MAX_SOUND = 5;

    private AssetManager mAsset;
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;

    public BeatBox(Context context){
        mAsset = context.getAssets();
        mSoundPool = new SoundPool(MAX_SOUND, AudioManager.STREAM_MUSIC, 0);
        loadSounds();
    }

    private void loadSounds(){
        String[] soundsNames;
        try {
            soundsNames = mAsset.list(SOUND_FOLDER);
            Log.i(TAG, "Found " +soundsNames.length + " sounds");
        } catch (IOException e) {
            Log.e(TAG, "Could not list assets", e);
            e.printStackTrace();
            soundsNames = new String[0];
        }

        for (String filename: soundsNames){
            try {
                String assetPath = SOUND_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            }catch (IOException e){
                Log.e(TAG, "Could not load sound " + filename, e);
            }
        }

    }

    private void load(Sound sound) throws IOException{
        AssetFileDescriptor afd = mAsset.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

    public void play(Sound sound){
        Integer soundId = sound.getSoundId();
        if (soundId == null){
            return;
        }
        mSoundPool.play(soundId, 1.0f, 1.0f, 1, 0, 0);
    }

    public void release(){
        mSoundPool.release();
    }

    public List<Sound> getSounds() {
        return mSounds;
    }
}
