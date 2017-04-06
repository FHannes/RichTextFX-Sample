import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import org.fxmisc.richtext.model.Codec;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static javafx.scene.text.TextAlignment.*;

public class ParagraphStyle {

    public static final ParagraphStyle EMPTY = new ParagraphStyle();

    public static final Codec<ParagraphStyle> CODEC = new Codec<ParagraphStyle>() {

        private final Codec<Optional<TextAlignment>> OPT_ALIGNMENT_CODEC =
                Codec.optionalCodec(Codec.enumCodec(TextAlignment.class));
        private final Codec<Optional<Color>> OPT_COLOR_CODEC =
                Codec.optionalCodec(Codec.COLOR_CODEC);

        @Override
        public String getName() {
            return "par-style";
        }

        @Override
        public void encode(DataOutputStream os, ParagraphStyle t) throws IOException {
            OPT_ALIGNMENT_CODEC.encode(os, t.alignment);
            OPT_COLOR_CODEC.encode(os, t.backgroundColor);
        }

        @Override
        public ParagraphStyle decode(DataInputStream is) throws IOException {
            return new ParagraphStyle(
                    OPT_ALIGNMENT_CODEC.decode(is),
                    OPT_COLOR_CODEC.decode(is));
        }

    };

    public static ParagraphStyle alignLeft() { return EMPTY.updateAlignment(LEFT); }
    public static ParagraphStyle alignCenter() { return EMPTY.updateAlignment(CENTER); }
    public static ParagraphStyle alignRight() { return EMPTY.updateAlignment(RIGHT); }
    public static ParagraphStyle alignJustify() { return EMPTY.updateAlignment(JUSTIFY); }
    public static ParagraphStyle backgroundColor(Color color) { return EMPTY.updateBackgroundColor(color); }

    final Optional<TextAlignment> alignment;
    final Optional<Color> backgroundColor;

    public ParagraphStyle() {
        this(Optional.empty(), Optional.empty());
    }

    public ParagraphStyle(Optional<TextAlignment> alignment, Optional<Color> backgroundColor) {
        this.alignment = alignment;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(alignment, backgroundColor);
    }

    @Override
    public boolean equals(Object other) {
        if(other instanceof ParagraphStyle) {
            ParagraphStyle that = (ParagraphStyle) other;
            return Objects.equals(this.alignment, that.alignment) &&
                    Objects.equals(this.backgroundColor, that.backgroundColor);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return toCss();
    }

    public String toCss() {
        StringBuilder sb = new StringBuilder();

        alignment.ifPresent(al -> {
            String cssAlignment;
            switch(al) {
                case LEFT:    cssAlignment = "left";    break;
                case CENTER:  cssAlignment = "center";  break;
                case RIGHT:   cssAlignment = "right";   break;
                case JUSTIFY: cssAlignment = "justify"; break;
                default: throw new AssertionError("unreachable code");
            }
            sb.append("-fx-text-alignment: " + cssAlignment + ";");
        });

        backgroundColor.ifPresent(color -> {
            sb.append("-fx-background-color: " + TextStyle.cssColor(color) + ";");
        });

        return sb.toString();
    }

    public ParagraphStyle updateWith(ParagraphStyle mixin) {
        return new ParagraphStyle(
                mixin.alignment.isPresent() ? mixin.alignment : alignment,
                mixin.backgroundColor.isPresent() ? mixin.backgroundColor : backgroundColor);
    }

    public ParagraphStyle updateAlignment(TextAlignment alignment) {
        return new ParagraphStyle(Optional.of(alignment), backgroundColor);
    }

    public ParagraphStyle updateBackgroundColor(Color backgroundColor) {
        return new ParagraphStyle(alignment, Optional.of(backgroundColor));
    }

}
