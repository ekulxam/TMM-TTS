package survivalblock.tmm_tts.client;

import folk.sisby.kaleido.api.WrappedConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.FloatRange;

/**
 * @author afamiliarquiet
 */
public class TMMTTSConfig extends WrappedConfig {
    @Comment("If enabled, all chat messages will be routed through /speak")
    public boolean autospeak = false;

    @SuppressWarnings("unused")
    public enum SynthGender {
        neutral,
        male,
        female
    }
    public SynthGender synthesizedGender = SynthGender.neutral;
    public int synthesizedGenderVariant = 1;

    @FloatRange(min=0f, max=2f)
    public float speechRate = 1f;
    @FloatRange(min=0f, max=2f)
    public float pitch = 1f;
    @FloatRange(min=0f, max=2f)
    public float pitchRange = 1f;

    public String relativized(float f) {
        return (f >= 0 ? "+" : "-") + (100 * (f-1)) + "%";
    }

    // maybe not kaleido's forte but satisfactory
    public String relativizedSpeechRate() {
        return relativized(speechRate);
    }
    public String relativizedPitch() {
        return relativized(pitch);
    }
    public String relativizedPitchRange() {
        return relativized(pitchRange);
    }
}
