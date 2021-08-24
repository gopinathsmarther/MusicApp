package smarther.com.musicapp.Model;

import java.io.Serializable;

public class EqualizerSeekbarModel implements Serializable {

    private short     presetPos;
    private short     lowerEqualizerBandLevel;


    public short getPresetPos() {
        return presetPos;
    }

    public void setPresetPos(short presetPos) {
        this.presetPos = presetPos;
    }

    public short getLowerEqualizerBandLevel() {
        return lowerEqualizerBandLevel;
    }

    public void setLowerEqualizerBandLevel(short lowerEqualizerBandLevel) {
        this.lowerEqualizerBandLevel = lowerEqualizerBandLevel;
    }
}

