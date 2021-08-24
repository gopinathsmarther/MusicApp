package smarther.com.musicapp.Utils;

import smarther.com.musicapp.Model.EqualizerModel;

public class EqualizerSettings {

        public static boolean        isEqualizerEnabled  = true;
        public static boolean        isEqualizerReloaded = true;
        public static int[]          seekbarpos          = new int[5];
        public static int            presetPos;
        public static short          reverbPreset        = -1;
        public static short          bassStrength        = -1;
        public static EqualizerModel equalizerModel;
        public static double         ratio               = 1.0;
        public static boolean        isEditing           = false;

}
