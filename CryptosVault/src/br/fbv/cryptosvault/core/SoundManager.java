package br.fbv.cryptosvault.core;

import java.util.ArrayList;
import java.util.Stack;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import br.fbv.cryptosvault.R;

/**
 * Classe responsável por controle de audio
 * @author  Rogerio Peixoto
 */
public class SoundManager {

    public static final int     DIGIT       = 0;
    public static final int     LOGIN_OK    = 1;
    public static final int     LOGIN_WRONG = 2;

    private static final int    MAXSTREAMS  = 1;
    /**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
    private static SoundManager instance;

    private SoundPool           mSoundPool;
    private AudioManager        mAudioManager;
    private ArrayList<Integer>  mSoundPoolMap;
    private Stack<Integer>      mSongsTransactions;

    private Context             mContext;

    private void initSounds() {
        addSound(R.raw.digit_new);
        addSound(R.raw.login_ok);
        addSound(R.raw.login_wrong);
    }

    /**
     * Construtor
     * @param ct
     */
    private SoundManager(Context ct) {
        mContext = ct;
        mSoundPoolMap = new ArrayList<Integer>();
        mSongsTransactions = new Stack<Integer>();

        mSoundPool = new SoundPool(MAXSTREAMS, AudioManager.STREAM_MUSIC, 0);

        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        
        initSounds();
    }

    public static SoundManager getInstance(Context ct) {
        if (instance == null) {
            instance = new SoundManager(ct);
        }
        return instance;
    }

    private void addSound(int soundId) {
        mSoundPoolMap.add(mSoundPool.load(mContext, soundId, 1));
    }

    public void playSound(int index) {

        float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume /= mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int playId = mSoundPool.play(mSoundPoolMap.get(index), streamVolume, streamVolume, 1, 0, 1f);

        mSongsTransactions.push(playId);
    }

    public void stopSounds() {
        while (mSongsTransactions.size() > 0) {
            mSoundPool.stop(mSongsTransactions.pop());
        }
    }

    public void cleanup() {
        mSoundPool.release();
        mSoundPool = null;
        mSoundPoolMap.clear();
        mSongsTransactions.clear();
        mAudioManager.unloadSoundEffects();
    }
}
