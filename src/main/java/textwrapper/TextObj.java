package textwrapper;

import javafx.scene.Node;
import org.fxmisc.richtext.model.Codec;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface TextObj<S> {

    static <S> Codec<TextObj<S>> codec(Codec<S> styleCodec) {
        return new Codec<TextObj<S>>() {
            @Override
            public String getName() {
                return "TextObj<" + styleCodec.getName() + ">";
            }

            @Override
            public void encode(DataOutputStream dataOutputStream, TextObj<S> sTextObj) throws IOException {
                if (sTextObj.getStyle() != null) {
                    String ref = sTextObj.getReference();
                    Codec.STRING_CODEC.encode(dataOutputStream, ref);
                    styleCodec.encode(dataOutputStream, sTextObj.getStyle());
                }
            }

            @Override
            public TextObj<S> decode(DataInputStream dataInputStream) throws IOException {
                String ref = Codec.STRING_CODEC.decode(dataInputStream);
                S style = styleCodec.decode(dataInputStream);
                return new TextObjInst<>(ref, style);
            }
        };
    }

    TextObj<S> setStyle(S style);

    S getStyle();

    /**
     * Reference to identify the target value.
     */
    String getReference();

    Node createNode();

}
