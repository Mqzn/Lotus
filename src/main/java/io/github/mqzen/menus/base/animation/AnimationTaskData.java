package io.github.mqzen.menus.base.animation;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public final class AnimationTaskData {
    
    private long ticks, delay;
    private boolean async;
    
    private final static AnimationTaskData DEFAULT = AnimationTaskData.builder()
            .ticks(5L)
            .delay(1L)
            .async(false)
            .build();
    
    public static AnimationTaskData defaultData() {
        return DEFAULT;
    }
}
