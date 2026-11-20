package fr.bgoodes.uhc.page;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import org.jetbrains.annotations.Nullable;

public class HostPageData {

    public static final BuilderCodec<HostPageData> CODEC = BuilderCodec.builder(HostPageData.class, HostPageData::new)
            .append(new KeyedCodec<>("Tab", Codec.STRING, false),
                    (data, value) -> data.tab = value,
                    (data) -> data.tab).add()
            .append(new KeyedCodec<>("Action", Codec.STRING, false),
                    (data, value) -> data.action = value,
                    (data) -> data.action).add()
            .append(new KeyedCodec<>("Id", Codec.STRING, false),
                    (data, value) -> data.id = value,
                    (data) -> data.id).add()
            .append(new KeyedCodec<>("@Number", Codec.DOUBLE, false),
                    (data, value) -> data.number = value,
                    (data) -> data.number).add()
            .append(new KeyedCodec<>("@Bool", Codec.BOOLEAN, false),
                    (data, value) -> data.bool = value,
                    (data) -> data.bool).add()
            .append(new KeyedCodec<>("@Text", Codec.STRING, false),
                    (data, value) -> data.text = value,
                    (data) -> data.text).add()
            .build();

    private @Nullable String tab;
    private @Nullable String action;
    private @Nullable String id;
    private @Nullable Double number;
    private @Nullable Boolean bool;
    private @Nullable String text;

    public @Nullable String getTab() {
        return tab;
    }

    public @Nullable String getAction() {
        return action;
    }

    public @Nullable String getId() {
        return id;
    }

    public @Nullable Double getNumber() {
        return number;
    }

    public @Nullable Boolean getBool() {
        return bool;
    }

    public @Nullable String getText() {
        return text;
    }
}
